import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data node in the consistent hashing ring.
 *
 * <p>Here, we store simple key-value pairs as data. Whether the data will be stored on this node or
 * not depends on the index of the hashed key in the consistent hashing ring.
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

  public void insert(String key, String value) {
    data.put(key, value);
  }

  public void delete(String key) {
    data.remove(key);
  }

  public List<String> getKeys() {
    return new ArrayList<>(data.keySet());
  }

  @Override
  public String toString() {
    return "[Node: " + id + ", Index: " + index + ", Data: " + data + "]";
  }
}
