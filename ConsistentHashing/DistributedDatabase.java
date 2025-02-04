import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Distributed Database implementation using Consistent Hashing.
 *
 * <p>Manages a ring of a fixed size (typically a large number to avoid collisions) and maps each
 * node and data key to their hashed locations on this ring. Initially contains N nodes and no data.
 *
 * <p>Supports the following operations:
 *
 * <ul>
 *   <li>Inserting a key-value pair.
 *   <li>Retrieving the value of a key.
 *   <li>Adding a data node.
 *   <li>Deleting a data node.
 * </ul>
 *
 * <p>Upon addition or deletion of a data node, consistent hashing algorithm only shifts minimal
 * data from one adjacent node without redistributing the data in other nodes.
 */
public final class DistributedDatabase {
  private static final int RING_SIZE = 32; // Usually, some large number.
  private DataNode[] dataNodes;
  private final Set<Integer> nodeIndices;
  private int nextNodeId = 0;

  public DistributedDatabase(int dataNodeCount) {
    this.dataNodes = new DataNode[dataNodeCount];
    this.nodeIndices = new HashSet<>();
    for (int i = 0; i < dataNodeCount; i++) {
      dataNodes[i] = new DataNode(nextNodeId++, getNextAvailableNodeIndex());
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
   * Returns the value of the given key.
   *
   * <p>This first finds the hashed index of the key and then locates the next data node in the
   * ring. The value of the key is then fetched from this data node.
   */
  public String get(String key) {
    int index = HashUtil.hash31(key) % RING_SIZE;
    System.out.println("Fetching key: " + key + ", index: " + index);
    DataNode nextDataNode = getNextDataNode(index);
    return nextDataNode.get(key);
  }

  /**
   * Returns the first data node that to the right side (clockwise) of a given index. If no such
   * node is found, returns the first node (i.e. loop back to the beginning).
   */
  private DataNode getNextDataNode(int index) {
    for (DataNode node : dataNodes) {
      if (node.getIndex() > index) {
        return node;
      }
    }
    return dataNodes[0];
  }

  /**
   * Adds a new data node to the ring.
   *
   * <p>This creates a new data node with the next available index and adds it to the ring. Also,
   * moves the required data from the next node to the new node.
   */
  public void addNewDataNode() {
    DataNode newDataNode = new DataNode(nextNodeId++, getNextAvailableNodeIndex());
    System.out.println("Adding new data node at index: " + newDataNode.getIndex());
    DataNode[] newDataNodes = new DataNode[dataNodes.length + 1];
    System.arraycopy(dataNodes, 0, newDataNodes, 0, dataNodes.length);
    newDataNodes[dataNodes.length] = newDataNode;
    Arrays.sort(newDataNodes, Comparator.comparingInt(DataNode::getIndex));
    dataNodes = newDataNodes;
    moveData(getNextDataNode(newDataNode.getIndex()), newDataNode);
  }

  /**
   * Deletes a given data node from the ring.
   *
   * <p>Also, moves all of its data to the next data node in the ring.
   */
  public void deleteDataNode(int id) {
    DataNode nodeToDelete = null;
    for (DataNode node : dataNodes) {
      if (node.getId() == id) {
        nodeToDelete = node;
        break;
      }
    }
    if (nodeToDelete == null) {
      System.out.println("Node with id: " + id + " does not exist.");
      return;
    }
    System.out.println("Deleting data node at index: " + nodeToDelete.getIndex());
    nodeIndices.remove(nodeToDelete.getIndex());
    DataNode[] newDataNodes = new DataNode[dataNodes.length - 1];
    int i = 0;
    for (DataNode node : dataNodes) {
      if (node.getId() != id) {
        newDataNodes[i++] = node;
      }
    }
    dataNodes = newDataNodes;
    moveData(nodeToDelete, getNextDataNode(nodeToDelete.getIndex()));
  }

  /**
   * Moves data from one node to another.
   *
   * <p>Some keys that previously belonged to the src node may now belong to the dest node based on
   * the hashed index. So, we need to re-map the keys to their new locations. We compute the hash of
   * the key and move this key-value pair to the dest node if the next node in the ring is the dest
   * node.
   */
  private void moveData(DataNode src, DataNode dest) {
    System.out.println(
        "Moving data from node index: " + src.getIndex() + " to node index: " + dest.getIndex());
    for (String key : src.getKeys()) {
      int index = HashUtil.hash31(key) % RING_SIZE;
      DataNode nextDataNode = getNextDataNode(index);
      if (nextDataNode.getIndex() == dest.getIndex()) {
        dest.insert(key, src.get(key));
        src.delete(key);
      }
    }
  }

  public void printDataNodes() {
    System.out.println("Data Nodes:");
    for (DataNode node : dataNodes) {
      System.out.println(node);
    }
  }
}
