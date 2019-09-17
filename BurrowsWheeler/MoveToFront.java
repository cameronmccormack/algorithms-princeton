import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {
    private static final int R = 256; // ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        // create an array with the ASCII characters in sequence
        char[] chars = new char[R];
        for (int i = 0; i < R; i++) {
            chars[i] = (char) i;
        }

        // read charaters from standard input and count how many positions into the array
        // the character is found
        char c;
        while (!BinaryStdIn.isEmpty()) {
            c = BinaryStdIn.readChar();
            int i = 0;
            while (chars[i] != c) {
                i++;
            }
            BinaryStdOut.write((char) i);
            // move all characters preceding searched character across one position and
            // put searched character at the start
            while (i > 0) {
                chars[i] = chars[--i];
            }
            chars[0] = c;
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // creates an array with the ASCII characters 0 to 255 in sequence
 //   private static char[] initChars() {
   //     return chars;
   // }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++) {
            chars[i] = i;
        }
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
