/**
 * Consistent Hashing implementation.
 *
 * <p>Manages a ring of a fixed size (typically a large number to avoid collisions) and maps each
 * node and data key to their hashed locations on this ring.
 *
 * <p>Initially contains N nodes and no data. Supports addition and deletion of nodes. This shifts
 * minimal data from the adjacent node without recomputing and redistributing all the data in other
 * nodes.
 */
public final class ConsistentHashing {
  private static final int RING_SIZE = 32; // Some large number.
  private final DataNode[] dataNodes;

  public ConsistentHashing(int noOfDataNodes) {
    this.dataNodes = new DataNode[noOfDataNodes];
    for (int i = 0; i < noOfDataNodes; i++) {
      dataNodes[i] = new DataNode(i, HashUtil.hashSimple(Integer.toString(i)) % RING_SIZE);
    }
  }

  public void printNodes() {
    for (DataNode node : dataNodes) {
      System.out.println(node);
    }
  }

  public static void main(String[] args) {
    ConsistentHashing consistentHashing = new ConsistentHashing(5);
    consistentHashing.printNodes();
  }
}
