import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout; 
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
	List<PointPair> pairs;
	
	public DrawPanel() {
		super();
		setBackground(Color.WHITE);
		pairs = new ArrayList<PointPair>(10000);
	}
	
	public void paintComponent(Graphics g) {
		 int width = getWidth();           
	     int height = getHeight();          
	     super.paintComponent(g); 
	     Graphics2D g2 = (Graphics2D) g;
	     
	     for (PointPair p : pairs) {
	    	 g2.draw(new Line2D.Double(p.x1, p.y1, p.x2, p.y2));
	     }
	     
	}

	public void setPoints(ArrayList<PointPair> pairs) {
		this.pairs = pairs;

	}
	
}
