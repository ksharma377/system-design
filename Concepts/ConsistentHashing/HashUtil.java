/** A util class that exposes hash functions to compute hashes of strings. */
public final class HashUtil {
  // A large prime number that fits in a 32-bit integer and also allows safe doubling without any
  // overflow.
  private static final int MOD = 1_000_000_007;

  public static int hashSimple(String key) {
    return key.hashCode();
  }

  public static int hash17(String key) {
    int hash = 0;
    for (int i = 0; i < key.length(); i++) {
      hash = ((int) ((1L * 17 * hash) % MOD) + key.charAt(i)) % MOD;
    }
    return hash;
  }

  public static int hash31(String key) {
    int hash = 0;
    for (int i = 0; i < key.length(); i++) {
      hash = ((int) ((1L * 31 * hash) % MOD) + key.charAt(i)) % MOD;
    }
    return hash;
  }
}
