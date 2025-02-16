public final class DataStructures {
  public static void main(String[] args) {
    testVector();
  }

  private static void testVector() {
    Vector<String> vector = new Vector<>();
    print("Initially: ", vector);

    vector.add("one");
    vector.add("two");
    print("After adding two elements: ", vector);

    System.out.println("Element at index 1: " + vector.get(1));

    vector.add("three");
    vector.add("four");
    vector.add("five");
    print("After adding three more elements: ", vector);

    try {
      System.out.println("Element at index 5: " + vector.get(5));
    } catch (RuntimeException e) {
      print("Exception: ", e.getMessage());
    }

    vector.remove();
    vector.remove();
    vector.remove();
    print("After removing three elements: ", vector);

    vector.remove();
    vector.remove();
    print("After removing all elements: ", vector);

    try {
      vector.remove();
    } catch (RuntimeException e) {
      print("Exception: ", e.getMessage());
    }
  }

  private static void print(String s, Object o) {
    System.out.println(s + o);
  }
}
