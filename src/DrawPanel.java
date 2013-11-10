import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.BorderLayout; 

public class DrawPanel extends JPanel {
	
	public DrawPanel() {
		super();
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g) {
		 int width = getWidth();           
	     int height = getHeight();          
	     super.paintComponent(g); 
	}
	
}
