import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class Map {

	private Collection<Road> roads;
	private List<RoadIntersection> roadIntersections;
	
	public Map(Collection<Road> rs, Collection<RoadIntersection> ris) {
		roads = rs;
		roadIntersections = (List<RoadIntersection>) ris;
	}

	public Collection<Road> getRoads() {
		return roads;
	}

	public List<RoadIntersection> getRoadIntersections() {
		return roadIntersections;
	}

	public boolean isEmpty() {
		return (roads == null && roadIntersections == null);
	}
	
	
}
