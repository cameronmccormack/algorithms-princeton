import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private int numOpenSites;
    private final WeightedQuickUnionUF uf, uf2;
    private boolean[] blocked;

    public Percolation(int num) {
        // check whether the parameter is valid
        if (num <= 0) {
            throw new IllegalArgumentException();
        }

        // create n-by-n grid, with all sites blocked
        n = num;
        uf = new WeightedQuickUnionUF(n*n+2); // 2 extra virtual sites (check percolation status)
        uf2 = new WeightedQuickUnionUF(n*n+1); // 1 extra virtual site (check full cells)
        blocked = new boolean[n*n+2]; // 2 extra virtual sites
        for (int i = 1; i <= n*n; ++i) {
            blocked[i] = true;
        }

        // initial number of open sites is zero
        numOpenSites = 0;
    }

    // return index of array site
    private int index(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
        return (row-1)*n + col;
    }
    
    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int id = index(row, col);
        if (!isOpen(row, col)) {
            numOpenSites++;
            blocked[id] = false;
            // build connections for real sites
            if (row < n && isOpen(row+1, col)) {
                int neighbour = index(row+1, col);
                uf.union(id, neighbour);
                uf2.union(id, neighbour);
            }
            if (row > 1 && isOpen(row-1, col)) {
                int neighbour = index(row-1, col);
                uf.union(id, neighbour);
                uf2.union(id, neighbour);
            }
            if (col < n && isOpen(row, col+1)) {
                int neighbour = index(row, col+1);
                uf.union(id, neighbour);
                uf2.union(id, neighbour);
            }
            if (col > 1 && isOpen(row, col-1)) {
                int neighbour = index(row, col-1);
                uf.union(id, neighbour);
                uf2.union(id, neighbour);
            }

            // build connections for virtual sites
            if (row == 1) {
                uf.union(0, id);
                uf2.union(0, id);
            }
            if (row == n) {
                // do not union uf2 to avoid backwash
                uf.union(n*n+1, id);
            }
        }
    }

    // check if site is open
    public boolean isOpen(int row, int col) {
        return !blocked[index(row, col)];
    }

    // check if site is full
    public boolean isFull(int row, int col) {
        return uf2.connected(0, index(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // check if the system percolates
    public boolean percolates() {
        return uf.connected(0, n*n+1);
    }
}
