
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
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoadIntersection other = (RoadIntersection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

//	@Override
//	public int compareTo(Object arg0) {
//		RoadIntersection other = (RoadIntersection) arg0;
//		if (other.getX() > this.getX())
//			return 1;
//		if (other.getX() < this.getX())
//			return -1;
//		if (other.getX() == this.getX()) {
//			if (other.getY() > this.getY())
//				return 1;
//			if (other.getY() < this.getY())
//				return -1;
//			return 0;
//		}
//		return 0;
//	}
	
	
}
