import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MapView extends JPanel implements ActionListener {
	private ParserWorker parser;
	private Image bg;

	private JLabel mouseInfo;
	private JProgressBar progressBar;
	private JLabel progressLabel;

	private List<Line2D> roads;
	private List<RoadIntersection> snapPoints;
	private List<Line2D> shortestPath;
	private List<RoadIntersection> adjPoints;
	private float alpha;
	private double mouseX, mouseY;

	private RoadIntersection pointA;
	private RoadIntersection pointB;
	private RoadIntersection closestPoint;
	private Timer timer;
	private Timer dTimer;
	private MapGraph mapGraph;
	private boolean ready;
	private enum DrawModes {
		RENDER_ADJ, RENDER_AS_POINTS, RENDER_AS_LINES, RENDER_FANCY, RENDER_SHORTEST_PATH
	}
	
	private EnumSet<DrawModes> flags;
	
	public MapView(ParserWorker parser) throws IOException {
		this.parser = parser;
		addMouseMotionListener(new MouseHandler());
		addMouseListener(new MouseHandler());
		loadBackgroundImage();
		setupComponents();
		timer = new Timer(20, this);
		ready = false;
		//dTimer = new Timer(100, dijkstraCalculator);
		alpha = 1f;

	}
//	ActionListener dijkstraCalculator = new ActionListener() {
//	      public void actionPerformed(ActionEvent evt) {
//	    	  if (pointA != null) {
//	    		  new DijkstraWorker(closestPoint).execute();
//	    	  }
//	      }
//	  };


	private void setupComponents() throws IOException {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);

		/* Mouse Info */
		mouseInfo = new JLabel("Mouse: (0.00, 0.00)");
		mouseInfo.setForeground(Color.GREEN);
		springLayout.putConstraint(SpringLayout.WEST, mouseInfo, 10,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, mouseInfo, -10,
				SpringLayout.SOUTH, this);
		this.add(mouseInfo);
		mouseInfo.setVisible(false);

		/* Progress Bar */
		progressBar = new JProgressBar(0, 100);
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, -10,
				SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -10,
				SpringLayout.EAST, this);
		add(progressBar);

		/* Progress Label */
		progressLabel = new JLabel("Parsing..");
		progressLabel.setForeground(Color.GREEN);
		springLayout.putConstraint(SpringLayout.SOUTH, progressLabel, 0,
				SpringLayout.SOUTH, mouseInfo);
		springLayout.putConstraint(SpringLayout.EAST, progressLabel, -6,
				SpringLayout.WEST, progressBar);
		add(progressLabel);
	}

	private double distanceFormula(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2)
				+ Math.pow(y1 - y2, 2));
	}
	
	private RoadIntersection getClosestPoint() {
		RoadIntersection closestPoint = null;
		double closestDistance = Integer.MAX_VALUE;
		for (RoadIntersection s : snapPoints) {
			double distance = distanceFormula(mouseX, mouseY, s.x, s.y);
			if (distance < closestDistance) {
				closestDistance = distance;
				closestPoint = s;
			}
		}
		return closestPoint;
	}

	private void paintBackground(Graphics2D g2) {
		AffineTransform t = new AffineTransform();
		t.translate(0, 0);
		t.scale(.8, 1);
		g2.drawImage(bg, t, null);
	}

	public void paintComponent(Graphics g) {
		flags = EnumSet.of(
				DrawModes.RENDER_FANCY, 
				DrawModes.RENDER_AS_LINES,
				DrawModes.RENDER_SHORTEST_PATH
		);
		
		super.paintComponent(g);
	
		//Setup the context and draw the bg picture
		Graphics2D g2 = (Graphics2D) g;
		paintBackground(g2);
		
		//Font in case we want to display some stuff
		Font font = new Font("Arial", Font.PLAIN, 12);
		g2.setFont(font);
		g2.setColor(Color.WHITE);
	
		//Antialiasing
		if (flags.contains(DrawModes.RENDER_FANCY))
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Setup map fade in
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		
		//Offset so stuff lines up properly
		final int offset = 4;
		
		if (flags.contains(DrawModes.RENDER_AS_LINES)) {
			if (roads != null) {
				g2.setStroke(new BasicStroke(1));
				for (Line2D r : roads) {
					g2.draw(r);
				}
			}
		}
			
		if (flags.contains(DrawModes.RENDER_AS_POINTS)) {
			if (snapPoints != null) {
				for (RoadIntersection r : snapPoints) {
					g2.setColor(Color.RED);
					Ellipse2D.Double circle = new Ellipse2D.Double((int) r.x
							, (int) r.y , 1, 1);
					g2.fill(circle);
				}
			}
		}
		
		if (flags.contains(DrawModes.RENDER_SHORTEST_PATH)) {
			if (shortestPath != null) {
				g2.setColor(Color.YELLOW);
				g2.setStroke(new BasicStroke(3));
				for (Line2D r : shortestPath) {
					g2.draw(r);
				}
			}
		}

		
		if (flags.contains(DrawModes.RENDER_ADJ)) {
			if (adjPoints != null) {
				StringBuilder sb = new StringBuilder();
				for (RoadIntersection r : adjPoints) {
					g2.setColor(Color.GREEN);
					sb.append(r.getId() + ", ");
					Ellipse2D.Double circle = new Ellipse2D.Double((int) r.x
							- offset, (int)r.y - offset, 3, 3);
					g2.fill(circle);
				}
				mouseInfo.setText(sb.toString());
			}
		}

		
		if (closestPoint != null) {
			g2.setColor(Color.RED);
			Ellipse2D.Double circle = new Ellipse2D.Double((int) closestPoint.x
					- offset, (int) closestPoint.y - offset, 10, 10);
			g2.fill(circle);
		}
		
		if (pointA != null) {
			int size = flags.contains(DrawModes.RENDER_ADJ) ? 5 : 10;
			g2.setColor(Color.YELLOW);
			g2.drawString("A", (int)pointA.x+10, (int)pointA.y);
			
			Ellipse2D.Double circle = new Ellipse2D.Double((int) pointA.x
					- offset, (int) pointA.y - offset, size, size);
			g2.fill(circle);
		}
		
		if (pointB != null) {
			int size = flags.contains(DrawModes.RENDER_ADJ) ? 5 : 10;
			g2.setColor(Color.YELLOW);
			g2.drawString("B", (int)pointB.x+10, (int)pointB.y);
			Ellipse2D.Double circle = new Ellipse2D.Double((int) pointB.x
					- offset, (int) pointB.y - offset, size, size);
			g2.fill(circle);
		}
		
		
		//End drawing

	}

	public void setProgress(String s) {
		progressLabel.setText(s);
	}

	public void updateProgressBar(int i) {
		if (i == 100) {
			timer.start();
			alpha = .01f;
		}
		progressBar.setValue(i);

	}

	private void loadBackgroundImage() {
		try {
			bg = ImageIO.read(new File("src/roch.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setRoads(List<Line2D> roads) {
		this.roads = roads;
		progressBar.setVisible(false);
		progressLabel.setVisible(false);
		mouseInfo.setVisible(true);

	}

	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (pointA == null) {
				pointA = closestPoint;
			} else
			if (pointA != null && pointB == null) {
				pointB = closestPoint;
				new DijkstraWorker().execute();
			} else 
			if (pointA != null && pointB != null) {
				shortestPath = null;
				pointB = null;
				pointA = closestPoint;
			}
			
		   adjPoints = closestPoint.getNeighbors();
			//dTimer.start();
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			update(e);
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			if (snapPoints != null)
				closestPoint = getClosestPoint();
			update(e);
		}

		private void update(MouseEvent e) {
			//mouseInfo.setText("Mouse: (" + mouseX + ", " + mouseY + ")");
			mouseInfo.repaint();
			MapView.this.repaint();

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		alpha += 0.03f;
		if (alpha >= 1) {
			alpha = 1;
			timer.stop();
		}
		repaint();
	}

	public void setPoints(List<RoadIntersection> snapPoints) {
		this.snapPoints = snapPoints;

	}
	
	
	private class DijkstraWorker extends SwingWorker<Double, Void> {
		
		public DijkstraWorker() {
			
		}
		@Override
		protected Double doInBackground() throws Exception {
			
			List<RoadIntersection> vertices = mapGraph.getVertices();
			RoadIntersection source = vertices.get(vertices.indexOf(pointA));
			RoadIntersection dest = vertices.get(vertices.indexOf(pointB));
			for (RoadIntersection r : vertices) {
				r.distance = Integer.MAX_VALUE;
				r.visited = false;
				r.previous = null;
			}
			//source = vertices.get(vertices.indexOf(source));
			
			source.distance = 0;
			
			PriorityQueue<RoadIntersection> q = new PriorityQueue<RoadIntersection>();
			q.add(source);
			
			while (!q.isEmpty()) {
				RoadIntersection u = q.poll();
				u.visited = true;
				
				for (RoadIntersection v : u.getNeighbors()) {
					double alt = u.distance + distanceFormula(u.x, u.y, v.x, v.y);
					if (alt < v.distance && !v.visited) {
						v.distance = alt;
						v.previous = u;
						q.add(v);
					}
				}
			}
			shortestPath = new ArrayList<Line2D>();
			List<RoadIntersection> path = new ArrayList<RoadIntersection>();
			for (RoadIntersection r = dest; r != null; r = r.previous)
	            path.add(r);
			for (int i = 0; i < path.size()-1; i++) {
				RoadIntersection a = path.get(i);
				RoadIntersection b = path.get(i+1);
				Line2D line = new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());
				shortestPath.add(line);
			}

			return dest.distance;
		}
		
		protected void done() {
			try {
				double distance = get();
				mouseInfo.setText("A to B distance: "+distance);
				
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public void setGraph(MapGraph graph) {
		this.mapGraph = graph;
		
	}

}
