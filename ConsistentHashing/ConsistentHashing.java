/** Consistent Hashing implementation in a Distributed Database. */
public final class ConsistentHashing {
  public static void main(String[] args) {
    DistributedDatabase db = new DistributedDatabase(/* dataNodeCount= */ 4);
    db.printDataNodes();

    // Insert some key-value pairs.
    db.insert("cat", "white");
    db.insert("parrot", "green");
    db.insert("dog", "brown");
    db.insert("elephant", "gray");
    db.insert("giraffe", "yellow");
    db.insert("tiger", "orange");
    db.printDataNodes();

    // Fetch some keys.
    System.out.println("Value of key 'cat': " + db.get("cat"));
    System.out.println("Value of key 'elephant': " + db.get("elephant"));

    // Add a new data node.
    db.addNewDataNode();
    db.printDataNodes();

    // Fetch some keys.
    System.out.println("Value of key 'dog': " + db.get("dog"));
    System.out.println("Value of key 'tiger': " + db.get("tiger"));

    // Delete a data node.
    db.deleteDataNode(/* id= */ 2);
    db.printDataNodes();

    // Fetch some keys.
    System.out.println("Value of key 'parrot': " + db.get("parrot"));
    System.out.println("Value of key 'giraffe': " + db.get("giraffe"));
  }
}
