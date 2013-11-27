/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*/
public class Road {
	private String id;
	private String intersectionIdA;
	private String intersectionIdB;
	
	public Road(String roadId, String idA, String idB) {
		id = roadId;
		intersectionIdA = idA;
		intersectionIdB = idB;
		
	}

	public String getId() {
		return id;
	}

	public String getIntersectionIdA() {
		return intersectionIdA;
	}

	public String getIntersectionIdB() {
		return intersectionIdB;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((intersectionIdA == null) ? 0 : intersectionIdA.hashCode());
		result = prime * result
				+ ((intersectionIdB == null) ? 0 : intersectionIdB.hashCode());
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
		Road other = (Road) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (intersectionIdA == null) {
			if (other.intersectionIdA != null)
				return false;
		} else if (!intersectionIdA.equals(other.intersectionIdA))
			return false;
		if (intersectionIdB == null) {
			if (other.intersectionIdB != null)
				return false;
		} else if (!intersectionIdB.equals(other.intersectionIdB))
			return false;
		return true;
	}
	
}
