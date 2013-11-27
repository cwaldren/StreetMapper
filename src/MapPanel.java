import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;


@SuppressWarnings("serial")
public class MapPanel extends JPanel {
	
	List<PointPair> pairs;
	List<RoadIntersection> intersections;
	
	public MapPanel(String fileName) throws IOException, InterruptedException, ExecutionException {
		super(new BorderLayout());

		ParserWorker parser = new ParserWorker(fileName);
		MapView view = new MapView();
		MapController controller = new MapController(parser, view);
		controller.control();
		this.add(view, BorderLayout.CENTER);
	}

	
	public void setPoints(ArrayList<PointPair> pairs) {
		this.pairs = pairs;

	}
	
	public void setIntersections(Collection<RoadIntersection> rIntersections) {
		this.intersections = new ArrayList<RoadIntersection>(rIntersections);
		
	}



}
