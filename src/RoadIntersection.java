
public class RoadIntersection {
	private String id;
	double x;
	double y;
	
	public String getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public RoadIntersection(String isId, String isX, String isY) {
		id = isId;
		x = Double.parseDouble(isX);
		y = Double.parseDouble(isY);
	}
	
	
}
