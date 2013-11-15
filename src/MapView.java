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


@SuppressWarnings("serial")
public class MapView extends JPanel {
	private MapParser parser;
	private Image bg;
	private JLabel mouseInfo;
	private double mouseX, mouseY;
	
	
	public MapView(MapParser parser) {
		this.parser = parser;
		parser.addObserver(new ParserObserver());
		loadBackgroundImage();
		this.addMouseMotionListener(new MouseHandler());
		
		mouseInfo = new JLabel("Mouse: ");
		
		this.add(mouseInfo);
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
			System.out.println(i);
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
