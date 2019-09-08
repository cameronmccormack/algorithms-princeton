import java.util.LinkedList;
import java.util.Arrays;

public class Board {
    private int n;
    private int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        
        n = tiles.length;

        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        board = copy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(n + "\n");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str.append(board[i][j] + " ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int wrong = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!tileInPlace(i, j)) {
                    wrong++;
                }
            }
        }
        return wrong;
    }

    // check if a tile is in place
    private boolean tileInPlace(int row, int col) {
        int tile = board[row][col];
        return (tile == 0 || tile == (row * n + col + 1));
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += manhattanDistance(i, j);
            }
        }
        return sum;
    }

    // calculate the minimum number of moves to put a piece in place
    private int manhattanDistance(int row, int col) {
        int tile = board[row][col];
        if (tile == 0) {
            return 0;
        } else {
            int target_row = (tile - 1) / n;
            int target_col = (tile - 1) % n;
            return Math.abs(row - target_row) + Math.abs(col - target_col);
        }
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!tileInPlace(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.deepEquals(this.board, that.board);
    }

    private int[][] swap(int row1, int col1, int row2, int col2) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = board[i][j];
            }
        }
        int temp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;
        return copy;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighbors = new LinkedList<Board>();

        int spaceRow = 0;
        int spaceCol = 0;

        // find space
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    spaceRow = i;
                    spaceCol = j;
                }
            }
        }

        if (spaceRow > 0) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow-1, spaceCol)));
        }
        if (spaceRow < n - 1) { 
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow+1, spaceCol)));
        }
        if (spaceCol > 0) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol-1)));
        }
        if (spaceCol < n - 1) {
            neighbors.add(new Board(swap(spaceRow, spaceCol, spaceRow, spaceCol+1)));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        for (int i = 0; i < n; i++) {
            if ((board[i][0] != 0) && (board[i][1] != 0)) {
                return new Board(swap(i, 0, i, 1));
                
            }
        }
        throw new RuntimeException();
    }

    // quick test
    public static void main(String[] args) {
    
    }
}
