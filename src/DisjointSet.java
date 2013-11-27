/**
*Casey Waldren
*cwaldren@u.rochester.edu
*TAs Ciaran Downey & Yang Yu
*Street Mapper
*
*DisjointSet adapted from
*https://github.com/abreen/Kruskal/blob/master/Kruskal.java 
*/
import java.util.ArrayList;
import java.util.List;

public class DisjointSet {
  private int nodeCount = 0;
  private int setCount = 0;

  ArrayList<Node> rootNodes;

  public int find(Node n) {
    Node current = n;

    while (current.parent != null)
      current = current.parent;

    Node root = current;

    current = n;
    while (current != root) {
      Node temp = current.parent;
      current.parent = root;
      current = temp;
    }

    return root.index;
  }


  public void union(Node i, Node j) {
    int indexI = find(i);
    int indexJ = find(j);

    if (indexI == indexJ) return;

    Node a = this.rootNodes.get(indexI);
    Node b = this.rootNodes.get(indexJ);

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

  public void makeSets(List<RoadIntersection> vertices) {
    for (RoadIntersection v : vertices)
      makeSet(v);
  }


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