import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Queue;

public class BurrowsWheeler {
    private static final int R = 256; // ASCII
    private static final int BITS = 8; // 8 bit output

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

    // apply Burrows-Wheeler inverse transform
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        // read first position and array of characters
        int firstPosition = BinaryStdIn.readInt();
        String str = BinaryStdIn.readString();
        char[] chars = str.toCharArray();

        // array to store the next array or chars
        int[] next = new int[chars.length];
        // array to store the 1st character of the sorted suffixes
        char[] first = new char[chars.length];
        // array to store the total count for each character
        int[] count = new int[R+1];

        // key-index counting, storing values in next array
        for (int i = 0; i < chars.length; i++) {
            count[chars[i]+1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i+1] += count[i];
        }
        for (int i = 0; i < chars.length; i++) {
            next[count[chars[i]]] = i;
            first[count[chars[i]]++] = chars[i];
        }

        // write out to standard output
        int j = firstPosition;
        for (int i = 0; i < chars.length; i++) {
            BinaryStdOut.write(first[j]);
            j = next[j];
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
