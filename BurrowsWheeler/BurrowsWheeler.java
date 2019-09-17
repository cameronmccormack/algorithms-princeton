import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {
    private static final int BITS = 8; // ASCII

    // apply Burrows-Wheeler transform
    // reading from standard input and writing to standard output
    public static void transform() {
        String str = BinaryStdIn.readString();
        CircularSuffixArray sfxs = new CircularSuffixArray(str);
        int first = 0;
        char[] chars = new char[str.length()];
        for (int i = 0; i < str.length(); i++) {
            int offset = sfxs.index(i);
            if (offset == 0) {
                first = i;
            }
            chars[i] = getChar(str, offset);
        }
        BinaryStdOut.write(first);
        for (char c : chars) {
            BinaryStdOut.write(c, BITS);
        }
        BinaryStdOut.close();
    }

    private static char getChar(String s, int offset) {
        if (offset > 0) {
            return s.charAt(offset - 1);
        } else {
            return s.charAt(s.length() - 1);
        }
    }

    // apply Burrow-Wheeler inverse transform
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String str = BinaryStdIn.readString();
        char[] chars = str.toCharArray();
        int[] next = new int[chars.length];
        ST<Character, Queue<Integer>> positions = new ST<Character, Queue<Integer>>();
        for (int i = 0; i < chars.length; i++) {
            if (!positions.contains(chars[i])) {
                positions.put(chars[i], new Queue<Integer>());
            }
            positions.get(chars[i]).enqueue(i);
        }
        for (int i = 0; i < chars.length; i++) {
            next[i] = positions.get(chars[i]).dequeue();
        }
        int position = first;
        for (int i = 0; i < chars.length; i++) {
            BinaryStdOut.write(chars[position]);
            position = next[position];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
