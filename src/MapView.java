import javax.swing.*;
import java.awt.*;

public class MapView extends JFrame {
	DrawPanel map;
	JLabel status;
	private static final long serialVersionUID = -7081517290750627650L;

	public MapView() {
		initUI();
	}
	
	public void hideLoadingLabel() {
		status.setVisible(false);
	}
	public void setStatus(String s) {
		status.setText(s);
		status.repaint();
	}
	private void initUI() {
		JFrame frame = new JFrame("Rochester Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		map = new DrawPanel();
		frame.getContentPane().add(map);
		
		status = new JLabel("Loading and parsing map file...");
		map.add(status);
		
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
