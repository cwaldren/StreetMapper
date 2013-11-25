import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MapGraph  {
	private Map map;
	private List<RoadIntersection> vertices;
	private List<Road> edges;
	private HashMap<String, IntersectionPair> roads;
	private HashMap<String, RoadIntersection> intersections;
	
	public MapGraph(Map map, HashMap<String, IntersectionPair> roads, HashMap<String, RoadIntersection> intersections) {
		this.map = map;
		this.vertices = map.getRoadIntersections();
		this.edges = new ArrayList(map.getRoads());
		this.roads = roads;
		this.intersections = intersections;
		calculateAdjacencies();
	}
	
	
	
	

	private void calculateAdjacencies() {
		
		for (Road r : edges) {
			String aId = r.getIntersectionIdA();
			String bId = r.getIntersectionIdB();
			RoadIntersection a = intersections.get(aId);
			RoadIntersection b = intersections.get(bId);
			
			a.addNeighbor(b);
			b.addNeighbor(a);
			
		}	
		
	}



	
	
	public List<RoadIntersection> getVertices() {
		return vertices;
	}
	

}
