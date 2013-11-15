import java.awt.BorderLayout;
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
public class MapPanel extends JPanel {
	/**
	 * 
	 */
	
	
	private RoadIntersection snapPoint;
	
	List<PointPair> pairs;
	List<RoadIntersection> intersections;
	
	public MapPanel() {
		super(new BorderLayout());

//		
//		pairs = new ArrayList<PointPair>(10000);
//		intersections = new ArrayList<RoadIntersection>(10000);
		//loadBackgroundImage();
//		addMouseMotionListener(new MouseHandler());
		MapParser parser = new MapParser("monroe-county.tab");
		MapView view = new MapView(parser);
		MapController controller = new MapController(parser, view);
	
		this.add(view, BorderLayout.CENTER);
		this.add(controller, BorderLayout.SOUTH);
	

	}
	
//	public void paintComponent(Graphics g) {
//		
////		int width = getWidth();
//		int height = getHeight();
//		super.paintComponent(g);
//	
//		Graphics2D g2 = (Graphics2D) g;
//		
//		AffineTransform t = new AffineTransform();//.getTranslateInstance(-145,-40);
//		t.translate(0, 0);
//		t.scale(.8, 1);
//		g2.drawImage(bg, t, null);
//		g2.setColor(Color.white);
//		for (PointPair p : pairs) {
//			Point2D p1 = new Point2D.Double(p.x1, p.y1);
//			Point2D p2 = new Point2D.Double(p.x2, p.y2);
//			g2.draw(new Line2D.Double(p1, p2));
//			
//		}
////		RoadIntersection closest = null;
////		double distance = Integer.MAX_VALUE;
////
////        for (RoadIntersection r : intersections) {
////        	
////        	double d = Math.pow(Math.abs(mouseY - r.getY()), 2) + Math.pow(Math.abs(mouseX - r.getX()), 2);
////        	if (d < distance) {
////        		distance = d;
////        		closest = r;
////        	}
////        }
//     
//	
//		g2.drawString("Mouse: ("+mouseX+", "+mouseY+")", 0, height-19);
//		g2.drawString("*Image not necessarily to scale", 0, height-5);
//		
//
//		g2.setColor(Color.RED);
////		if (closest != null) {
////			g2.fillOval((int)closest.getX()-5, (int)closest.getY()-5, 10, 10);
////		}
//		
//	}

	
	public void setPoints(ArrayList<PointPair> pairs) {
		this.pairs = pairs;

	}
	
	public void setIntersections(Collection<RoadIntersection> rIntersections) {
		this.intersections = new ArrayList<RoadIntersection>(rIntersections);
		
	}

//	public void draw(Collection<Road> roads, Hashtable<Road, IntersectionPair> roadTable, Collection<RoadIntersection> rIntersections) {
//		ArrayList<PointPair> pairs = new ArrayList<PointPair>(10000);
//		for (Road r : roads) {
//			IntersectionPair p = roadTable.get(r);
//			
//			double x1 = p.getA().x;
//			double y1 = p.getA().y;
//			double x2 = p.getB().x;
//			double y2 = p.getB().y;
//			PointPair pp = new PointPair(x1, y1, x2, y2);
//			pairs.add(pp);
//			
//		}
//		map.setPoints(pairs);
//		
//		map.setIntersections(rIntersections);
//		map.repaint();
//		
//		
//		
//	}
	
	

}
