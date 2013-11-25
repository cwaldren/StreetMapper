import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MapGraph  {
	private List<Vertex> vertices;
	private List<Road> roadsList;
	HashMap<String, IntersectionPair> roads;
	HashMap<String, RoadIntersection> intersections;
	
	public MapGraph(HashMap<String, IntersectionPair> roadsHashMap,
			HashMap<String, RoadIntersection> intersectionsHashMap, Collection<Road> roads) {
		this.roads = roadsHashMap;
		this.roadsList = new ArrayList<Road>(roads);
		intersections = intersectionsHashMap;
		vertices = new ArrayList<Vertex>();
	
		
		//setVertices(roads);
		calculateAdjacencies();
	}
	
	
	
	

	private void calculateAdjacencies() {
		//HashSet<Vertex> hs = new HashSet<Vertex>();
		//Collections.sort(vertices);
		for (IntersectionPair r : roads.values()) {
			RoadIntersection a = r.getA();
			RoadIntersection b = r.getB();
			Vertex av = new Vertex(a.getX(), a.getY());
			Vertex bv = new Vertex(b.getX(), b.getY());
			
			if (!vertices.contains(av)) {
				vertices.add(av);
			} else {
				vertices.get(vertices.indexOf(av)).neighbors.add(bv);
			}
			if (!vertices.contains(bv)) {
				vertices.add(bv);
			} else {
				vertices.get(vertices.indexOf(bv)).neighbors.add(av);
			}
		}
	}



	private void setVertices(List<RoadIntersection> intersections) {
		for (RoadIntersection r : intersections) {
			vertices.add(new Vertex(r.getX(), r.getY()));
		}
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}
	

}
