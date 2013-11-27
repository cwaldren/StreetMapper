/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class MapGraph  {
	private List<RoadIntersection> vertices;
	private List<Road> edges;

	private HashMap<String, IntersectionPair> ipairs;
	private HashMap<String, RoadIntersection> intersections;
	
	public MapGraph(Map map, HashMap<String, IntersectionPair> roads, HashMap<String, RoadIntersection> intersections) {
		this.vertices = map.getRoadIntersections();
		this.edges = new ArrayList<Road>(map.getRoads());
		this.intersections = intersections;
		ipairs = roads;
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
	
	public Collection<IntersectionPair> getEdges() {
		return ipairs.values();
	}
	

}
