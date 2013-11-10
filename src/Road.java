
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
	
}
