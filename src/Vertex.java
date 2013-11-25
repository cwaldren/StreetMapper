import java.util.List;

public class Vertex implements Comparable {
	private double x, y;
	public List<Vertex> neighbors;
	public double distance;
	public boolean visited;
	public Vertex previous;

	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
		this.distance = Integer.MAX_VALUE;
		this.visited = false;
		this.previous = null;
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
	
	
}