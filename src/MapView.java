import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.awt.MouseInfo;

@SuppressWarnings("serial")
public class MapView extends JFrame {
	DrawPanel map;
	JLabel status;
	

	public MapView() {
		initUI();
		
	}
	
	public void hideLoadingLabel() {
		status.setVisible(false);
	}
	public void setStatus(String s) {
		status.setText(s);
		
	}
	
	
	public void draw(Collection<Road> roads, Hashtable<Road, IntersectionPair> roadTable, Collection<RoadIntersection> rIntersections) {
		ArrayList<PointPair> pairs = new ArrayList<PointPair>(10000);
		for (Road r : roads) {
			IntersectionPair p = roadTable.get(r);
			
			double x1 = p.getA().x;
			double y1 = p.getA().y;
			double x2 = p.getB().x;
			double y2 = p.getB().y;
			PointPair pp = new PointPair(x1, y1, x2, y2);
			pairs.add(pp);
			
		}
		map.setPoints(pairs);
		
		map.setIntersections(rIntersections);
		map.repaint();
		
		
		
	}
	private void initUI() {
		JFrame frame = new JFrame("Rochester Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		map = new DrawPanel();
		frame.getContentPane().add(map);

		status = new JLabel("Loading and parsing map file...");
		map.add(status);
		
		frame.setResizable(false);
		frame.setSize(650,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	

	
	
}
