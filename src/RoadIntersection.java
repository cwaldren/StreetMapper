/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
import java.util.ArrayList;
import java.util.List;


public class RoadIntersection implements Comparable<RoadIntersection> {
	private List<RoadIntersection> neighbors;
	public RoadIntersection previous;
	private Node n;
	private String id;
	public double x;
	public double y;
	public double distance;
	public boolean visited;
	
	public void setNode(Node n) {this.n = n;}
	
	public Node getNode() {return this.n;}
	
	public void addNeighbor(RoadIntersection r) {
		neighbors.add(r);
	}
	
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
		neighbors = new ArrayList<RoadIntersection>();
	}
	
	public RoadIntersection(String isId) {
		id = isId;
		neighbors = new ArrayList<RoadIntersection>();
	}
	
	public List<RoadIntersection> getNeighbors() {
		return neighbors;
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
		
		return true;
	}

	@Override
	//This right here...I originally had it return 1 for -1 and -1 for 1. It screwed up everything. I was stumped for hours.
	public int compareTo(RoadIntersection arg0) {
		RoadIntersection other = arg0;
		if (other.distance > distance)
			return -1;
		if (other.distance < distance) 
			return 1;
		return 0;
	}

}
