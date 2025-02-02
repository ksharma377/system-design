import java.util.HashMap;
import java.util.Map;

/**
 * A data node in the consistent hashing ring.
 *
 * <p>Here we store simple key values as data. The data is mapped to the node based on the hash of
 * their keys.
 *
 * <p>Each node is identified by an id and has a location index in the ring.
 */
public final class DataNode {
  private int id;
  private int index;
  private Map<String, String> data;

  DataNode(int id, int index) {
    this.id = id;
    this.index = index;
    this.data = new HashMap<>();
  }

  public int getId() {
    return id;
  }

  public int getIndex() {
    return index;
  }

  public String get(String key) {
    return data.get(key);
  }

  public void add(String key, String value) {
    data.put(key, value);
  }

  public Map<String, String> getAllData() {
    return data;
  }

  @Override
  public String toString() {
    return "[Node: " + id + ", Index: " + index + ", Data: " + data + "]";
  }
}
