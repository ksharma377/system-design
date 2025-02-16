/**
 * An implementation of a dynamic array that expands and shrinks as needed.
 *
 * <p>Supports the following operations:
 *
 * <ul>
 *   <li>Adding an element at the end.
 *   <li>Removing the last element.
 *   <li>Getting the value at an index.
 * </ul>
 */
public final class Vector {
  private static final double ADD_LOAD_FACTOR_THRESHOLD = 0.5;
  private static final double REMOVE_LOAD_FACTOR_THRESHOLD = 0.125;

  private int capacity; // Capacity of the vector.
  private int count; // Number of elements in the vector.
  private int[] array;

  public Vector() {
    this.capacity = 4; // Initial capacity.
    this.count = 0; // No elements to begin with.
    this.array = new int[this.capacity];
  }

  /** Adds an element to the end of the vector. */
  public void add(int value) {
    array[count++] = value;
    double loadFactor = (double) count / capacity;
    if (loadFactor >= ADD_LOAD_FACTOR_THRESHOLD) {
      resize(capacity * 2);
    }
  }

  /**
   * Removes and returns the last element of the vector.
   *
   * <p>If the vector is empty, throws a {@code RuntimeException}.
   */
  public int remove() throws RuntimeException {
    if (count == 0) {
      throw new RuntimeException("Vector is Empty");
    }
    int value = array[--count];
    double loadFactor = (double) count / capacity;
    if (loadFactor <= REMOVE_LOAD_FACTOR_THRESHOLD) {
      resize(capacity / 2);
    }
    return value;
  }

  /**
   * Returns the value at an index.
   *
   * <p>If the index is out of bounds, throws a {@code RuntimeException}.
   */
  public int get(int index) throws RuntimeException {
    if (index < 0 || index >= count) {
      throw new RuntimeException("Index Out of Bounds");
    }
    return array[index];
  }

  /** Returns the number of elements in the vector. */
  public int size() {
    return count;
  }

  private void resize(int newCapacity) {
    if (newCapacity < 4) {
      // Don't shrink below capacity 4.
      return;
    }
    int[] newArray = new int[newCapacity];
    for (int i = 0; i < count; i++) {
      newArray[i] = array[i];
    }
    array = newArray;
    capacity = newCapacity;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Vector: [");
    for (int i = 0; i < count; i++) {
      sb.append(array[i]);
      if (i < count - 1) {
        sb.append(", ");
      }
    }
    sb.append("]");
    sb.append(", Size: " + count);
    sb.append(", Capacity: " + capacity);
    return sb.toString();
  }
}
