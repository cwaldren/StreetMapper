
public class Node {
	int rank; // the approximate count of nodes below this node
	int index; // a unique index for each node in the tree
	Node parent;

	public Node(int r, int i, Node p) {
		this.rank = r;
		this.index = i;
		this.parent = p;
	}
}