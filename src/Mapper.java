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
public class Mapper implements Runnable {
	
	public static void main(String[] args) {

//	    SwingUtilities.invokeLater(new Runnable() 
//	    {
//	        public void run() 
//	        {
//	        	new Mapper();
//	        }
//	    });
		
		
		EventQueue.invokeLater(new Mapper());
		    
	}
	
	
	
	@Override
	public void run() {
		initUI();
		
	}
	
	private void initUI() {
		JFrame frame = new JFrame("Rochester Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new MapPanel());
		frame.setResizable(false);
		frame.setSize(650,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
