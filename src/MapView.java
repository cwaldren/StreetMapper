import javax.swing.*;
import java.awt.*;

public class MapView extends JFrame {
	DrawPanel map;
	
	private static final long serialVersionUID = -7081517290750627650L;

	public MapView() {
		initUI();
	}
	
	private void initUI() {
		JFrame frame = new JFrame("Rochester Map");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		map = new DrawPanel();
		frame.add(map);
		frame.setSize(800,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
