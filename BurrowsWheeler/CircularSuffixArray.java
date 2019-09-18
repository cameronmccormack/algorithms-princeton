import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int length;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        length = s.length();
        ST<String, Integer> circulations = new ST<String, Integer>();
        circulations.put(s, 0);
        for (int i = 1; i < length; i++) {
            String current = s.substring(i, length) + s.substring(0, i);
            // if string is already a key, add an a to the end to make it the next
            // sequential position. as all keys are of equal length, there is no risk of
            // this resulting in a collision with another unique key
            while (circulations.get(current) != null) {
                current = current + "a";
            }
            circulations.put(current, i);
        }
        index = new int[length];
        int i = 0;
        if (length > 0) {
            for (String key : circulations.keys()) {
                index[i++] = circulations.get(key);
            }
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length || length == 0) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    // unit test
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("BADCFGE");
        StdOut.println("Input string is BADCFGE.");
        StdOut.printf("Length is %d (should be 7).\n", csa.length());
        StdOut.print("Index positions of CSA are: ");
        for (int i = 0; i < csa.length(); i++) {
            StdOut.print(csa.index(i));
        }
        StdOut.println(" (should be 1032645).\n");

        CircularSuffixArray csa2 = new CircularSuffixArray("COUSCOUS");
        StdOut.println("Input string is COUSCOUS.");
        StdOut.printf("Length is %d (should be 8).\n", csa2.length());
        StdOut.print("Index positions of CSA are: ");
        for (int i = 0; i < csa2.length(); i++) {
            StdOut.print(csa2.index(i));
        }
        StdOut.println(" (should be 04153726).");
    }
}
