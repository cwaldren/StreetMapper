import java.util.Collection;


public class Map {

	private Collection<Road> roads;
	private Collection<RoadIntersection> roadIntersections;
	
	public Map(Collection<Road> rs, Collection<RoadIntersection> ris) {
		roads = rs;
		roadIntersections = ris;
	}

	public Collection<Road> getRoads() {
		return roads;
	}

	public Collection<RoadIntersection> getRoadIntersections() {
		return roadIntersections;
	}

	public boolean isEmpty() {
		return (roads == null && roadIntersections == null);
	}
	
	
}
