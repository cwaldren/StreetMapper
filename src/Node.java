/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*
*Part of the DisjointSet implementation by
*https://github.com/abreen/Kruskal/blob/master/Kruskal.java
*/
public class Node {
	int rank; 
	int index; 
	Node parent;

	public Node(int r, int i, Node p) {
		this.rank = r;
		this.index = i;
		this.parent = p;
	}
}