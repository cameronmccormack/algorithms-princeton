import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; // ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] chars = initChars();
        String str = BinaryStdIn.readString();
        char[] inputChars = str.toCharArray();
        for (char c : inputChars) {
            char temp;
            int i = 0;
            while (c != chars[i]) {
                temp = chars[i];
                chars[i+1] = temp;
                i++;
            }
            chars[i+1] = temp;
            BinaryStdOut.write(i, 8);
            chars[0] = (char) i;
        }
        BinaryStdOut.close();
    }

    // creates an array with the ASCII characters 0 to 255 in sequence
    private static char[] initChars() {
        char[] chars = new char[R];
        for (int i = 0; i < R; i++) {
            chars[i] = (char) i;
        }
        return chars;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = initChars();
        while (!BinaryStdIn.isEmpty()) {
            char i = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[i], 8);
            char index = chars[i];
            while (i > 0) {
                chars[i] = chars[--i];
            }
            chars[0] = index;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-" apply move-to-front encoding
    // if args[0] is "+" apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        if (args[0].equals("+")) {
            decode();
        } else if (args[0].equals("-")) {
            encode();
        } else {
            throw new IllegalArgumentException();
        }
    }
}
