import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;

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

	private float alpha;
	private double mouseX, mouseY;

	private RoadIntersection pointA;
	private RoadIntersection pointB;
	private RoadIntersection closestPoint;
	private Timer timer;
	private MapGraph mapGraph;

	public MapView(ParserWorker parser) throws IOException {
		this.parser = parser;
		addMouseMotionListener(new MouseHandler());
		addMouseListener(new MouseHandler());
		loadBackgroundImage();
		setupComponents();
		timer = new Timer(20, this);
		alpha = 1f;

	}

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

	private RoadIntersection getClosestPoint() {
		RoadIntersection closestPoint = null;
		double closestDistance = Integer.MAX_VALUE;
		for (RoadIntersection s : snapPoints) {
			double distance = Math.sqrt(Math.pow(mouseX - s.x, 2)
					+ Math.pow(mouseY - s.y, 2));
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
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		paintBackground(g2);
		
		g2.setColor(Color.WHITE);
		//Antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//Setup fade in
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				alpha));

		//If there's roads, draw them
		if (roads != null) {
			for (Line2D r : roads) {
				g2.draw(r);
			}
			closestPoint = getClosestPoint();
		}

		final int offset = 4;
		if (closestPoint != null) {
			g2.setColor(Color.RED);
			Ellipse2D.Double circle = new Ellipse2D.Double((int) closestPoint.x
					- offset, (int) closestPoint.y - offset, 10, 10);
			g2.fill(circle);
		}

		if (pointA != null) {
			g2.setColor(Color.BLUE);
			Ellipse2D.Double circle = new Ellipse2D.Double((int) pointA.x
					- offset, (int) pointA.y - offset, 10, 10);
			g2.fill(circle);
		}

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
			pointA = closestPoint;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			update(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
			update(e);
		}

		private void update(MouseEvent e) {
			mouseInfo.setText("Mouse: (" + mouseX + ", " + mouseY + ")");
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
	
	private void dijkstra(MapGraph g, RoadIntersection s) {
		List<Vertex> vertices = g.getVertices();
		Vertex source = new Vertex(s.getX(), s.getY());
		source = vertices.get(vertices.indexOf(source));
		
		source.distance = 0;
		
		PriorityQueue<Vertex> q = new PriorityQueue<Vertex>();
		q.add(source);
		
		while (!q.isEmpty()) {
			Vertex u = q.poll();
		}
	}

	public void setGraph(MapGraph graph) {
		this.mapGraph = graph;
		
	}

}
