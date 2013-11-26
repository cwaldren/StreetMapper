
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class MapView extends JPanel implements ActionListener, ItemListener{
	private Image bg;
	
	private List<Line2D> roads;
	private List<RoadIntersection> snapPoints;
	private List<Line2D> shortestPath;
	private List<Line2D> spanningPath;
	private List<RoadIntersection> adjPoints;
	private float alpha;
	private double mouseX, mouseY;

	private RoadIntersection pointA;
	private RoadIntersection pointB;
	private RoadIntersection closestPoint;
	private Timer timer;
	private MapGraph mapGraph;
	private enum DrawModes {
		RENDER_ADJ, RENDER_AS_POINTS, RENDER_AS_LINES, RENDER_FANCY, RENDER_SHORTEST_PATH, RENDER_SPANNING_TREE
	}
	
	private EnumSet<DrawModes> flags;
	private SpringLayout springLayout;
	

	private JLabel info;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private JCheckBox showMstCheckbox;
	private JPanel settingsPanel;
	private JRadioButton drawPoints;
	private JRadioButton drawLines;
	private JButton enterIntersectionsButton;
	
	//fair game hashing to last things
	//12/05 quiz test exa,?
	public MapView() throws IOException {
		addMouseMotionListener(new MouseHandler());
		addMouseListener(new MouseHandler());
		loadBackgroundImage();
		setupComponents();
		timer = new Timer(20, this);
		
		
		flags = EnumSet.of(
				DrawModes.RENDER_FANCY, 
				DrawModes.RENDER_AS_LINES,
				DrawModes.RENDER_SHORTEST_PATH
		);
		
		alpha = 1f;
	}


	private void setupComponents() throws IOException {
		springLayout = new SpringLayout();
		setLayout(springLayout);

		
		
		/* Settings Panel */
		settingsPanel = new JPanel();
		settingsPanel.setOpaque(false);
		settingsPanel.setBackground(Color.GREEN);
		springLayout.putConstraint(SpringLayout.NORTH, settingsPanel, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, settingsPanel, 520, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, settingsPanel, 100, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST,settingsPanel, 0, SpringLayout.EAST, this);
		add(settingsPanel);
		
		/* Show MST Checkbox */
		showMstCheckbox = new JCheckBox("Show MST");
		showMstCheckbox.setOpaque(false);
		showMstCheckbox.setEnabled(false);
		showMstCheckbox.setVisible(false);
		springLayout.putConstraint(SpringLayout.NORTH,showMstCheckbox, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, showMstCheckbox, 0, SpringLayout.EAST, this);
		showMstCheckbox.addItemListener(this);
		settingsPanel.add(showMstCheckbox);
		
		/* Radio Button for drawing as Points */
		drawPoints = new JRadioButton("Draw Points");
		settingsPanel.add(drawPoints);
		drawPoints.setOpaque(false);
		drawPoints.setVisible(false);
		drawPoints.setActionCommand("drawPoints");
		drawPoints.addActionListener(this);
		
		/* Radio Button for drawing as Lines */
		drawLines = new JRadioButton("Draw Roads");
		settingsPanel.add(drawLines);
		drawLines.setSelected(true);
		drawLines.setOpaque(false);
		springLayout.putConstraint(SpringLayout.NORTH, drawPoints, 6, SpringLayout.SOUTH, drawLines);
		drawLines.setVisible(false);
		drawLines.setActionCommand("drawLines");
		drawLines.addActionListener(this);
		
		
		/* Button Group for radios */
		ButtonGroup radios = new ButtonGroup();
		radios.add(drawPoints);
		radios.add(drawLines);
		
		
		springLayout.putConstraint(SpringLayout.WEST, drawPoints, 0, SpringLayout.WEST, showMstCheckbox);
		springLayout.putConstraint(SpringLayout.NORTH, drawLines, 13, SpringLayout.SOUTH, showMstCheckbox);
		springLayout.putConstraint(SpringLayout.WEST, drawLines, 0, SpringLayout.WEST, showMstCheckbox);
		
		
		
		/* Mouse Info */
		info = new JLabel("Info: ");
		info.setBackground(Color.WHITE);
		info.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.WEST, info, 5,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, info, -2,
				SpringLayout.SOUTH, this);
		this.add(info);
		info.setVisible(false);

		/* Progress Bar */
		progressBar = new JProgressBar(0, 100);
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, -10,
				SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -10,
				SpringLayout.EAST, this);
		add(progressBar);

		/* Progress Label */
		progressLabel = new JLabel("Parsing..");
		springLayout.putConstraint(SpringLayout.NORTH, progressLabel, 0, SpringLayout.NORTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, progressLabel, -6, SpringLayout.WEST, progressBar);
		progressLabel.setForeground(Color.GREEN);
		add(progressLabel);
		
		/* Direct Intersection Entry Button */
		enterIntersectionsButton = new JButton("Enter Intersections");
		springLayout.putConstraint(SpringLayout.SOUTH, enterIntersectionsButton, 25, SpringLayout.NORTH, progressBar);
		springLayout.putConstraint(SpringLayout.EAST, enterIntersectionsButton, 0, SpringLayout.EAST, settingsPanel);
		enterIntersectionsButton.setVisible(false);
		enterIntersectionsButton.addActionListener(this);
		add(enterIntersectionsButton);
	
		
	}

	
	


	private void paintBackground(Graphics2D g2) {
		AffineTransform t = new AffineTransform();
		t.translate(0, 0);
		t.scale(.84, 1);
		g2.drawImage(bg, t, null);
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
	
		//Setup the context and draw the bg picture
		Graphics2D g2 = (Graphics2D) g;
		
		paintBackground(g2);
		
		//Font in case we want to display some stuff
		Font font = new Font("Arial", Font.PLAIN, 20);
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
							, (int) r.y , 1.5, 1.5);
					g2.fill(circle);
				}
			}
		}
		if (flags.contains(DrawModes.RENDER_SPANNING_TREE)) {
			if (spanningPath != null) {
				g2.setColor(Color.GREEN);
				g2.setStroke(new BasicStroke(3));
				for (Line2D r : spanningPath) {
					g2.draw(r);
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
				info.setText(sb.toString());
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

	

	private void loadBackgroundImage() {
		try {
			bg = ImageIO.read(new File("src/roch.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				repaint();
			} else 
			if (pointA != null && pointB != null) {
				shortestPath = null;
				pointB = null;
				pointA = closestPoint;
			}
			
		   adjPoints = closestPoint.getNeighbors();
		
			
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
			if (snapPoints != null)
				if (pointB == null)
					info.setText("Info: hovering over intersection ID "+closestPoint.getId());
			info.repaint();
			MapView.this.repaint();

		}
	}
	
	private class KruskalWorker extends SwingWorker<List<IntersectionPair>, Void> {
	
		@Override
		protected List<IntersectionPair> doInBackground() throws Exception {
	
			
			List<IntersectionPair> roads = new ArrayList<IntersectionPair>(mapGraph.getEdges());
			DisjointSet ds = new DisjointSet(snapPoints);
			List<IntersectionPair> spanningTree = new ArrayList<IntersectionPair>();
			Collections.sort(roads);
			
			for (IntersectionPair p : roads) {
				RoadIntersection u = p.getA();
				RoadIntersection v = p.getB();
				if (ds.find(u.getNode()) != ds.find(v.getNode())) {
					spanningTree.add(p);
					ds.union(u.getNode(), v.getNode());
				}
	
			}
			return spanningTree;
		}
		
		protected void done() {
			try {
				List<IntersectionPair> result = get();
				spanningPath = new ArrayList<Line2D>();
				for (IntersectionPair p : result) {
					Line2D line = new Line2D.Double(p.getA().x, p.getA().y, p.getB().x, p.getB().y);
					spanningPath.add(line);
				}
				showMstCheckbox.setEnabled(true);
				
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	

	private class DijkstraWorker extends SwingWorker<Double, Void> {
		
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
			printPath(path);
			return dest.distance;
		}
		private void printPath(List<RoadIntersection> path) {
			Collections.sort(path);
			System.out.println("Printing as vertices");
			printPathAsVertices(path, 0);
			System.out.println("\nPrinting as roads");
			printPathAsRoads(path);
		}
		private void printPathAsVertices(List<RoadIntersection> path, int index) {
			String suffix = (index == path.size() - 1 ? "." : " to ");
			System.out.print(path.get(index).getId() + suffix);
			if (index < path.size() - 1)
				printPathAsVertices(path, index + 1);
			
		}
		
		private void printPathAsRoads(List<RoadIntersection> path) {
			List<String> roads = new ArrayList<String>();
			List<IntersectionPair> ipairs = new ArrayList<IntersectionPair>(mapGraph.getEdges()); 
			for (int i = 0; i < path.size()-1; i++) {
				RoadIntersection a = path.get(i);
				RoadIntersection b = path.get(i+1);
				IntersectionPair ip = new IntersectionPair(a, b);
				IntersectionPair temp = ipairs.get(ipairs.indexOf(ip));
				roads.add(temp.getId());
			}
			
			for (int i = 0; i < roads.size(); i++) {
				String suffix = (i == roads.size() - 1 ? "." : " to ");
				System.out.print(roads.get(i) + suffix);
			}
			
		}

		protected void done() {
			try {
				double distance = get();
				
				String dist = (distance == Integer.MAX_VALUE ? "unreachable" : Double.toString(distance));
				info.setText("A (" + pointA.getId() + ") to B (" + pointB.getId() + ") distance: " + dist);
				repaint();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
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
		
		if (e.getSource() == drawLines || e.getSource() == drawPoints) {
			if (e.getActionCommand().equals("drawLines")) {
				flags.add(DrawModes.RENDER_AS_LINES);
				flags.remove(DrawModes.RENDER_AS_POINTS);
				repaint();
			}
			
			if (e.getActionCommand().equals("drawPoints")) {
				flags.add(DrawModes.RENDER_AS_POINTS);
				flags.remove(DrawModes.RENDER_AS_LINES);
				repaint();
			}
		}
		
		if (e.getSource() == enterIntersectionsButton) {
			String aId = JOptionPane.showInputDialog("Enter an ID", "i212602056");
			String bId = JOptionPane.showInputDialog("Enter another ID", "i212602355");
			pointA = new RoadIntersection(aId);
			pointB = new RoadIntersection(bId);
			
			if (snapPoints.contains(pointA) && snapPoints.contains(pointB)) {
				pointA = snapPoints.get(snapPoints.indexOf(pointA));
				pointB = snapPoints.get(snapPoints.indexOf(pointB));
				new DijkstraWorker().execute();
				
				repaint();
				
			} else {
				pointA = null;
				pointB = null;
				shortestPath = null;
				JOptionPane.showMessageDialog(this, "Your inputed intersection IDs were not found.");
			}
		}
	}
	
	
	public void itemStateChanged(ItemEvent e) {
	    if (e.getStateChange() == ItemEvent.SELECTED) {
	    	flags.add(DrawModes.RENDER_SPANNING_TREE);
	    	repaint();
	    } else
	    if (e.getStateChange() == ItemEvent.DESELECTED) {
	    	flags.remove(DrawModes.RENDER_SPANNING_TREE);
	    	repaint();
	    }
	}

	public void setPoints(List<RoadIntersection> snapPoints) {
		this.snapPoints = snapPoints;
	}
	
	public void setRoads(List<Line2D> roads) {
		this.roads = roads;
		progressBar.setVisible(false);
		progressLabel.setVisible(false);
		
		info.setVisible(true);
		showMstCheckbox.setVisible(true);
		drawLines.setVisible(true);
		drawPoints.setVisible(true);
		enterIntersectionsButton.setVisible(true);
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
	
	private double distanceFormula(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2)
				+ Math.pow(y1 - y2, 2));
	}
	
	public void setGraph(MapGraph graph) {
		this.mapGraph = graph;
		KruskalWorker k = new KruskalWorker();
		k.execute();
	}
}
