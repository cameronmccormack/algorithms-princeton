import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.Queue;

public class BoggleSolver {
    private TST<Boolean> dict;
    private int rows;
    private int cols;
    private BoggleBoard boggle;
    private Queue<String> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A though Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new TST<Boolean>();
        for (String word : dictionary) {
            dict.put(word, true);
        }
    }

    // Returns the set of all valid words in the given Boggle board as an Iterable
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        rows = board.rows();
        cols = board.cols();
        boggle = board;
        words = new Queue<String>();
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                boolean[][] visited = new boolean[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        visited[i][j] = false;
                    }
                }
                StringBuilder build = new StringBuilder();
                appendWords(x, y, build, visited);
            }
        }
        return words;
    }

    private void appendWords(int x, int y, StringBuilder build, boolean[][] visited) {
        // add current letter to string and mark position as visited
        build.append(boggle.getLetter(x, y));
        visited[x][y] = true;

        // if the new string is in the dictionary and >= 3 characters, add to words list
        String current = build.toString();
        if (contains(current) && current.length() >= 3) {
            words.enqueue(current);
        }

        // if there are no words with the current string as a prefix, stop searching from this
        // path
        if (dict.keysWithPrefix(current) == null) {
            return;
        }

        // check the position to the left
        if (x+1 < rows && !visited[x+1][y]) {
            appendWords(x+1, y, build, visited);
        }

        // check the position to the right
        if (!visited[x-1][y] && x-1 >= 0) {
            appendWords(x-1, y, build, visited);
        }

        // check the position below
        if (!visited[x][y+1] && y+1 < cols) {
            appendWords(x, y+1, build, visited);
        }

        // check the position above
        if (!visited[x][y-1] && y-1 >= 0) {
            appendWords(x, y-1, build, visited);
        }

        // check the position left and down
        if (!visited[x+1][y+1] && x+1 < rows && y+1 < cols) {
            appendWords(x+1, y+1, build, visited);
        }

        // check the position left and up
        if (!visited[x+1][y-1] && x+1 < rows && y-1 >= 0) {
            appendWords(x+1, y-1, build, visited);
        }
        
        // check the position right and down
        if (!visited[x-1][y+1] && x-1 >= 0 && y+1 < cols) {
            appendWords(x-1, y+1, build, visited);
        }

        // check the position right and up
        if (!visited[x-1][y-1] && x-1 >= 0 && y-1 >= 0) {
            appendWords(x-1, y-1, build, visited);
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word only contains the uppercase letters A though Z.)
    public int scoreOf(String word) {
        if (!contains(word) || word.length() < 3) {
            return 0;
        } else if (word.length() < 5) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    private boolean contains(String word) {
        if (dict.get(word) == null) {
            return false;
        } else {
            return true;
        }
    }

    // Test client that takes the filename of a dictionary and the filename of a Boggle board as
    // command line argument and prints out all valid words for the given board using the given
    // dictionary
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
