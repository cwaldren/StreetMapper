import java.awt.geom.Point2D;
import java.util.HashMap;


public class MapEngine {
	private Map map;
	private HashMap<Point2D, RoadIntersection> pointMap;
	
	public MapEngine(Map m) {
		map = m;
		
	}
	
	private void generatePointMap() {
		RoadIntersection closest = null;
		for (int x = 0; x < 640; x++) {
			for (int y = 0; y < 600; y++) {
				  double distance = Integer.MAX_VALUE;
				  for (RoadIntersection r : map.getRoadIntersections()) {
					  double d = Math.pow(Math.abs(y - r.getY()), 2) + Math.pow(Math.abs(x - r.getX()), 2);
			          if (d < distance) {
			        		distance = d;
			        		closest = r;
			        	}
			        }
				  Point2D p = new Point2D.Double(x, y);
				  pointMap.put(p, closest);
			}
		}
	}
	
	private RoadIntersection getNearestIntersection(double x , double y) {
		return pointMap.get(new Point2D.Double(x, y));
	}
}