
public class IntersectionPair implements Comparable<IntersectionPair> {
	private RoadIntersection a;
	private RoadIntersection b;
	private String roadId;
	private double distance;
	
	public IntersectionPair(RoadIntersection lhs, RoadIntersection rhs) {
		a = lhs;
		b = rhs;
	   distance = Math.sqrt(Math.pow(a.x - b.x, 2)+ Math.pow(a.y - b.y, 2));
	}
	
	public IntersectionPair(RoadIntersection lhs, RoadIntersection rhs, String id) {
		a = lhs;
		b = rhs;
	   distance = Math.sqrt(Math.pow(a.x - b.x, 2)+ Math.pow(a.y - b.y, 2));
	   roadId = id;
	}
 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntersectionPair other = (IntersectionPair) obj;
		if (a.equals(other.getA()) && b.equals(other.getB()))
			return true;
		if (a.equals(other.getB()) && b.equals(other.getA()))
			return true;
		else
			return false;
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
	
	public String getId() {
		return roadId;
	}

	@Override
	public int compareTo(IntersectionPair o) {
		return Double.compare(distance, o.distance);
	}
}
