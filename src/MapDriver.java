import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class MapDriver {
	static MapView mapView;
	static Hashtable<Road, IntersectionPair> roads;
	static Hashtable<String, RoadIntersection> intersections;
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mapView = new MapView();

			}
		});

		SwingWorker<Map, Void> parseWorker = new SwingWorker<Map, Void>() {

			@Override
			protected Map doInBackground() throws Exception {

				MapParser mp = new MapParser("monroe-county.tab");

				if (mp.parse())
					return new Map(mp.getRoads(), mp.getRoadIntersections());
				else {
					return new Map(null, null);
				}
			}

			protected void done() {

				Map map;
				try {
					map = get();
					if (!map.isEmpty()) {
						MapEngine engine = new MapEngine(map);
						mapView.setStatus("Map parsed.");
						roads = new Hashtable<Road, IntersectionPair>();
						intersections = new Hashtable<String, RoadIntersection>();
						for (RoadIntersection ri : map.getRoadIntersections()) {
							intersections.put(ri.getId(), ri);
						}
						for (Road r : map.getRoads()) {
							roads.put(r, new IntersectionPair(
										intersections.get(r.getIntersectionIdA()),
										intersections.get(r.getIntersectionIdB())));
						}
						mapView.draw(map.getRoads(), roads, map.getRoadIntersections());
				
						
						
					} else {
						mapView.setStatus("Couldn't parse file");
					}

				} catch (InterruptedException e) {

				} catch (ExecutionException e) {

				}
				
			}
		};

		parseWorker.execute();

	}
}
