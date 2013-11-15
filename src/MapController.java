import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapController extends JPanel {
	private MapParser parser;
	private MapView view;
	private JLabel info;

	
	public MapController(MapParser parser, MapView view) {
		this.parser = parser;
		this.view = view;
		info = new JLabel("xxx intersections");
		this.add(info);
	}

}
