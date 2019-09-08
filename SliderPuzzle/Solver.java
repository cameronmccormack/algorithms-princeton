import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Solver {
    private List<Board> solutionBoards = new ArrayList<>();
    private boolean solved;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SolverStep> steps = new MinPQ<>(new SolverStepComparator());
        steps.insert(new SolverStep(initial, 0, null));

        MinPQ<SolverStep> stepsTwin = new MinPQ<>(new SolverStepComparator());
        stepsTwin.insert(new SolverStep(initial.twin(), 0, null));

        SolverStep step, stepTwin;
        while (!steps.min().getBoard().isGoal() && !stepsTwin.min().getBoard().isGoal()) {
            step = steps.delMin();

            for (Board neighbor : step.getBoard().neighbors()) {
                if (!inPath(step, neighbor)) {
                    steps.insert(new SolverStep(neighbor, step.getMoves() + 1, step));
                }
            }

            stepTwin = stepsTwin.delMin();
            for (Board neighbor : stepTwin.getBoard().neighbors()) {
                if (!inPath(stepTwin, neighbor)) {
                    stepsTwin.insert(new SolverStep(neighbor, stepTwin.getMoves() + 1, stepTwin));
                }
            }
        }
        
        step = steps.delMin();
        solved = step.getBoard().isGoal();

        solutionBoards.add(step.getBoard());
        while ((step = step.getPreviousStep()) != null) {
            solutionBoards.add(0, step.getBoard());
        }
    }

    private static class SolverStep {
        private int moves;
        private Board board;
        private SolverStep previousStep;

        private SolverStep(Board brd, int mov, SolverStep prev) {
            moves = mov;
            board = brd;
            previousStep = prev;
        }

        public int getMoves() {
            return moves;
        }

        public int getPriority() {
            return board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public SolverStep getPreviousStep() {
            return previousStep;
        }
    }

    private class SolutionIterator implements Iterator<Board> {
        private int index = 0;

        public boolean hasNext() {
            return index < solutionBoards.size();
        }

        public Board next() {
            return solutionBoards.get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private boolean inPath(SolverStep step, Board board) {
        SolverStep previousStep = step;
        while ((previousStep = previousStep.getPreviousStep()) != null) {
            if (previousStep.getBoard().equals(board)) {
                return true;
            }
        }
        return false;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solved;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (isSolvable()) {
            return solutionBoards.size() - 1;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Iterable<Board> iterable;
        if (isSolvable()) {
            iterable = new Iterable<Board>() {
                @Override
                public Iterator<Board> iterator() {
                    return new SolutionIterator();
                }
            };
        } else {
            iterable = null;
        }
        return iterable;
    }

    private static class SolverStepComparator implements Comparator<SolverStep> {
        public int compare(SolverStep step1, SolverStep step2) {
            return step1.getPriority() - step2.getPriority();
        }
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible.");
        } else {
            StdOut.print("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
