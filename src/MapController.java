/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
import java.awt.geom.Line2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapController extends JPanel implements PropertyChangeListener {
	private ParserWorker parser;
	private MapView view;
	private Map map;

	private HashMap<String, IntersectionPair> roads;
	private HashMap<String, RoadIntersection> intersections;

	public MapController(ParserWorker parser, MapView view) {
		this.parser = parser;
		this.view = view;
		roads = new HashMap<String, IntersectionPair>();
		intersections = new HashMap<String, RoadIntersection>();
		parser.addPropertyChangeListener(this);

	}

	//When given control, start the chain of events by executing the parser
	public void control() {
		parser.execute();
	}

	
	private void processForView() throws InterruptedException, ExecutionException {
		map = parser.get();
		
		for (RoadIntersection r : map.getRoadIntersections()) {
			intersections.put(r.getId(), r);
		}

		for (Road r : map.getRoads()) {
			RoadIntersection r1 = intersections.get(r.getIntersectionIdA());
			RoadIntersection r2 = intersections.get(r.getIntersectionIdB());
			roads.put(r.getId(), new IntersectionPair(r1, r2, r.getId()));
		}

		List<Line2D> viewRoads = new ArrayList<Line2D>();
		List<RoadIntersection> snapPoints = new ArrayList<RoadIntersection>();
		
		for (Road r : map.getRoads()) {
			IntersectionPair ip = roads.get(r.getId());
			double x1 = ip.getA().x;
			double y1 = ip.getA().y;
			double x2 = ip.getB().x;
			double y2 = ip.getB().y;

			Line2D road = new Line2D.Double(x1, y1, x2, y2);
			viewRoads.add(road);
			
			snapPoints.add(ip.getA());
			snapPoints.add(ip.getB());
			
		}
		view.setPoints(snapPoints);
		view.setRoads(viewRoads);
		
		
		MapGraph graph = new MapGraph(map, roads, intersections);
		
		view.setGraph(graph);
	}

	//Whenever a progress event is sent out, update the view's bar, and if it is done, then process everything
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			view.updateProgressBar((int) evt.getNewValue());
			if ((int) evt.getNewValue() == 100)
				try {
					processForView();
					
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
		}

	}

}
