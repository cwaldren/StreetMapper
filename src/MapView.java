import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import javax.swing.JProgressBar;


@SuppressWarnings("serial")
public class MapView extends JPanel {
	private MapParser parser;
	private Image bg;
	private JLabel mouseInfo;
	private double mouseX, mouseY;
	private JProgressBar progressBar;
	private JLabel progressLabel;
	
	public MapView(MapParser parser) {
		this.parser = parser;
		parser.addObserver(new ParserObserver());
		loadBackgroundImage();
		this.addMouseMotionListener(new MouseHandler());
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		mouseInfo = new JLabel("Mouse: (0.00, 0.00)");
		springLayout.putConstraint(SpringLayout.WEST, mouseInfo, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, mouseInfo, -10, SpringLayout.SOUTH, this);
		mouseInfo.setForeground(Color.GREEN);
	
		
		this.add(mouseInfo);
		
		try {
			progressBar = new JProgressBar(0, parser.count());
		} catch (IOException e) {
			//failed to parse map
			e.printStackTrace();
		}
		springLayout.putConstraint(SpringLayout.SOUTH, progressBar, -10, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, progressBar, -10, SpringLayout.EAST, this);
		add(progressBar);
		
		progressLabel = new JLabel("Loading");
		progressLabel.setForeground(Color.GREEN);
		springLayout.putConstraint(SpringLayout.SOUTH, progressLabel, 0, SpringLayout.SOUTH, mouseInfo);
		springLayout.putConstraint(SpringLayout.EAST, progressLabel, -6, SpringLayout.WEST, progressBar);
		add(progressLabel);
		
		parser.parse();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform t = new AffineTransform();
		t.translate(0, 0);
		t.scale(.8, 1);
		g2.drawImage(bg, t, null);
	}
	
	
	private class ParserObserver implements Observer {

		@Override
		public void update(Observable o, Object arg) {
			int i = (Integer) arg;
			progressBar.setValue(i);
			
		}
		
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
		}
	}
}
