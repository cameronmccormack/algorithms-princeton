import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;

public class CircularSuffixArray {
    private int length;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        length = s.length();
        RedBlackBST<String, Queue<Integer>> circulations = new RedBlackBST<String, Queue<Integer>>();
        circulations.put(s, new Queue<Integer>());
        circulations.get(s).enqueue(0);
        for (int i = 1; i < length; i++) {
            String current = s.substring(i, length) + s.substring(0, i);
            if (!circulations.contains(current)) {
                circulations.put(current, new Queue<Integer>());
            }
            circulations.get(current).enqueue(i);
        }
        index = new int[length];
        int i = 0;
        if (length > 0) {
            for (String key : circulations.keys()) {
                while (!circulations.get(key).isEmpty()) {
                    index[i++] = circulations.get(key).dequeue();
                }
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
            StdOut.printf("%d ", csa.index(i));
        }
        StdOut.println("\n(should be                  1 0 3 2 6 4 5).\n");

        CircularSuffixArray csa2 = new CircularSuffixArray("COUSCOUS");
        StdOut.println("Input string is COUSCOUS.");
        StdOut.printf("Length is %d (should be 8).\n", csa2.length());
        StdOut.print("Index positions of CSA are: ");
        for (int i = 0; i < csa2.length(); i++) {
            StdOut.printf("%d ", csa2.index(i));
        }
        StdOut.println("\n(should be:                 0 4 1 5 3 7 2 6).\n");

        CircularSuffixArray csa3 = new CircularSuffixArray("aaaaaaaaaaaa");
        StdOut.println("Input string is aaaaaaaaaaaa.");
        StdOut.printf("Length is %d (should be 8).\n", csa3.length());
        StdOut.print("Index positions of CSA are: ");
        for (int i = 0; i < csa3.length(); i++) {
            StdOut.printf("%d ", csa3.index(i));
        }
        StdOut.println("\n(should be:                 0 1 2 3 4 5 6 7 8 9 10 11).");
    }
}
