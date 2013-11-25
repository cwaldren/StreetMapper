import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable {
	public double x, y;
	public List<Vertex> neighbors;
	public double distance;
	public boolean visited;
	public Vertex previous;
	public String id;

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
		this.distance = Integer.MAX_VALUE;
		this.visited = false;
		this.previous = null;
		neighbors = new ArrayList<Vertex>();
	}
	
	public Vertex(double x, double y, String s) {
		this.x = x;
		this.y = y;
		id = s;
		this.distance = Integer.MAX_VALUE;
		this.visited = false;
		this.previous = null;
		neighbors = new ArrayList<Vertex>();
	}

	@Override
	public int compareTo(Object arg0) {
		Vertex other = (Vertex) arg0;
		if (other.distance > distance)
			return 1;
		if (other.distance < distance) 
			return -1;
		return 0;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}