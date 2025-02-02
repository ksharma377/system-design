import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Consistent Hashing implementation.
 *
 * <p>Manages a ring of a fixed size (typically a large number to avoid collisions) and maps each
 * node and data key to their hashed locations on this ring. Initially contains N nodes and no data.
 *
 * <p>Supports the following operations:
 *
 * <ul>
 *   <li>Adding a node.
 *   <li>Deleting a node.
 *   <li>Putting a key-value pair.
 *   <li>Retrieving the value of a key.
 * </ul>
 *
 * <p>Upon addition or deletion of a node, consistent hashing algorithm only shifts minimal data
 * from one adjacent node without redistributing the data in other nodes.
 */
public final class ConsistentHashing {
  private static final int RING_SIZE = 32; // Usually, some large number.
  private final DataNode[] dataNodes;
  private final Set<Integer> nodeIndices;

  public ConsistentHashing(int dataNodeCount) {
    this.dataNodes = new DataNode[dataNodeCount];
    this.nodeIndices = new HashSet<>();
    for (int i = 0; i < dataNodeCount; i++) {
      dataNodes[i] = new DataNode(i, getNextAvailableNodeIndex());
    }
    Arrays.sort(dataNodes, Comparator.comparingInt(DataNode::getIndex));
  }

  /**
   * Returns a random available index in the range [0, RING_SIZE).
   *
   * <p>This should ideally be implemeted as a hash of node id. However, for simplicity, we are
   * using a random number generator.
   */
  private int getNextAvailableNodeIndex() {
    Random random = new Random();
    while (true) {
      int index = random.nextInt(RING_SIZE);
      if (!nodeIndices.contains(index)) {
        nodeIndices.add(index);
        return index;
      }
    }
  }

  /**
   * Inserts the given key-value pair into the appropriate data node.
   *
   * <p>This first finds the hashed index of the key and then locates the next data node in the
   * ring. The key-value pair is then inserted into this data node.
   */
  public void insert(String key, String value) {
    int index = HashUtil.hash31(key) % RING_SIZE;
    System.out.println("Inserting key: " + key + ", value: " + value + ", index: " + index);
    DataNode nextDataNode = getNextDataNode(index);
    nextDataNode.insert(key, value);
  }

  /**
   * Returns the data node that is either at or to the right side (clockwise) of a given index. If
   * no such node is found, returns the first node (i.e. loop back to the beginning).
   */
  private DataNode getNextDataNode(int index) {
    for (DataNode node : dataNodes) {
      if (node.getIndex() >= index) {
        return node;
      }
    }
    return dataNodes[0];
  }

  public void printDataNodes() {
    System.out.println("Data Nodes:");
    for (DataNode node : dataNodes) {
      System.out.println(node);
    }
  }

  public static void main(String[] args) {
    ConsistentHashing consistentHashing = new ConsistentHashing(/* dataNodeCount= */ 4);
    consistentHashing.printDataNodes();

    // Insert some key-value pairs.
    consistentHashing.insert("cat", "white");
    consistentHashing.printDataNodes();
    consistentHashing.insert("parrot", "green");
    consistentHashing.printDataNodes();
    consistentHashing.insert("dog", "brown");
    consistentHashing.printDataNodes();
    consistentHashing.insert("elephant", "gray");
    consistentHashing.printDataNodes();
  }
}
