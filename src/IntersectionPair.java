
public class IntersectionPair {
	private RoadIntersection a;
	private RoadIntersection b;
	private double distance;
	
	public IntersectionPair(RoadIntersection lhs, RoadIntersection rhs) {
		a = lhs;
		b = rhs;
	//	distance = Math.sqrt(Math.pow(a.x - b.x, 2)+ Math.pow(a.y - b.y, 2));
	}

	public RoadIntersection getA() {
		return a;
	}
	
	public double getLength() {
		return distance;
	}

	public RoadIntersection getB() {
		return b;
	}
}
