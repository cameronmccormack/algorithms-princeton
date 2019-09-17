import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int length;
    private int[] index;
    private ST<String, Integer> circulations;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        length = s.length();
        circulations = new ST<String, Integer>();
        circulations.put(s, 0);
        for (int i = 1; i < length; i++) {
            circulations.put(s.substring(i, length) + s.substring(0, i), i);
        }
        index = new int[length];
        int i = 0;
        for (String key : circulations.keys()) {
            index[i++] = circulations.get(key);
        }
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) {
            throw new IndexOutOfBoundsException();
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
        StdOut.println(" (should be 1032645).");
    }
}
