import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel {
	/**
	 * 
	 */
	private Image bg;
	private double mouseX, mouseY;
	private RoadIntersection snapPoint;
	
	List<PointPair> pairs;
	List<RoadIntersection> intersections;
	public DrawPanel() {
		super();
		setBackground(Color.GREEN);
		
		pairs = new ArrayList<PointPair>(10000);
		intersections = new ArrayList<RoadIntersection>(10000);
		loadBackgroundImage();
		addMouseMotionListener(new MouseHandler());
	}
	
	public void paintComponent(Graphics g) {
		
		int width = getWidth();
		int height = getHeight();
		super.paintComponent(g);
	
		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform t = new AffineTransform();//.getTranslateInstance(-145,-40);
		t.translate(0, 0);
		t.scale(.8, 1);
		g2.drawImage(bg, t, null);
		g2.setColor(Color.white);
		for (PointPair p : pairs) {
			Point2D p1 = new Point2D.Double(p.x1, p.y1);
			Point2D p2 = new Point2D.Double(p.x2, p.y2);
			g2.draw(new Line2D.Double(p1, p2));
			
		}
//		RoadIntersection closest = null;
//		double distance = Integer.MAX_VALUE;
//
//        for (RoadIntersection r : intersections) {
//        	
//        	double d = Math.pow(Math.abs(mouseY - r.getY()), 2) + Math.pow(Math.abs(mouseX - r.getX()), 2);
//        	if (d < distance) {
//        		distance = d;
//        		closest = r;
//        	}
//        }
     
	
		g2.drawString("Mouse: ("+mouseX+", "+mouseY+")", 0, height-19);
		g2.drawString("*Image not necessarily to scale", 0, height-5);
		

		g2.setColor(Color.RED);
//		if (closest != null) {
//			g2.fillOval((int)closest.getX()-5, (int)closest.getY()-5, 10, 10);
//		}
		
	}

	private void loadBackgroundImage() {
		try {
			bg = ImageIO.read(new File("src/roch.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setPoints(ArrayList<PointPair> pairs) {
		this.pairs = pairs;

	}
	
	public void setIntersections(Collection<RoadIntersection> rIntersections) {
		this.intersections = new ArrayList<RoadIntersection>(rIntersections);
		
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
           
        
             DrawPanel.this.repaint();
         }
     }

}
