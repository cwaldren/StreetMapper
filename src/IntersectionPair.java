
public class IntersectionPair {
	private RoadIntersection a;
	private RoadIntersection b;
	
	public IntersectionPair(RoadIntersection lhs, RoadIntersection rhs) {
		a = lhs;
		b = rhs;
	}

	public RoadIntersection getA() {
		return a;
	}

	public RoadIntersection getB() {
		return b;
	}
}
