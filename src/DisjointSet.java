import java.util.ArrayList;
import java.util.List;

public class DisjointSet {
  private int nodeCount = 0;
  private int setCount = 0;

  ArrayList<Node> rootNodes;

  /*
   * Returns the index of the set that n is currently in.
   * The index of the root node of each set uniquely identifies the set.
   * This is used to determine whether two elements are in the
   * same set.
   */
  public int find(Node n) {
    Node current = n;

    /* Ride the pointer up to the root node */
    while (current.parent != null)
      current = current.parent;

    Node root = current;

    /*
     * Ride the pointer up to the root node again, but make each node below
     * a direct child of the root. This alters the tree such that future
     * calls will reach the root more quickly.
     */
    current = n;
    while (current != root) {
      Node temp = current.parent;
      current.parent = root;
      current = temp;
    }

    return root.index;
  }


  /*
   * Combines the sets containing nodes i and j.
   */
  public void union(Node i, Node j) {
    int indexI = find(i);
    int indexJ = find(j);

    /* Are these nodes already part of the same set? */
    if (indexI == indexJ) return;

    /* Get the root nodes of each set (this will run in constant time) */
    Node a = this.rootNodes.get(indexI);
    Node b = this.rootNodes.get(indexJ);

    /* Attach the smaller tree to the root of the larger tree */
    if (a.rank < b.rank) {
      a.parent = b;
    } else if (a.rank > b.rank) {
      b.parent = a;
    } else {
      b.parent = a;
      a.rank++;
    }
    
    this.setCount--;
  }


  /*
   * Takes a list of n vertices and creates n disjoint singleton sets.
   */
  public void makeSets(List<RoadIntersection> vertices) {
    for (RoadIntersection v : vertices)
      makeSet(v);
  }


  /*
   * Creates a singleton set containing one vertex.
   */
  public void makeSet(RoadIntersection vertex) {
    Node n = new Node(0, rootNodes.size(), null);
    vertex.setNode(n);
    this.rootNodes.add(n);
    this.setCount++;
    this.nodeCount++;
  }


  public DisjointSet(List<RoadIntersection> vertices) {
    this.rootNodes = new ArrayList<Node>(vertices.size());
    makeSets(vertices);
  }
 
}