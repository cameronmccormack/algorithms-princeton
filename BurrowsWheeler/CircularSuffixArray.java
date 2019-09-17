import edu.princeton.cs.algs4.ST;

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
        return 1;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        return 1;
    }

    // unit test
    public static void main(String[] args) {

    }
}
