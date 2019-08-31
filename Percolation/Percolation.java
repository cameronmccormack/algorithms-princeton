import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private int n, numOpenSites;
	private WeightedQuickUnionUF uf;
	private boolean[] blocked;

	public Percolation(int num) {
		// check whether the parameter is valid
		if (num <= 0) {
			throw new IllegalArgumentException();
		}

		// create n-by-n grid, with all sites blocked
		n = num;
		uf = new WeightedQuickUnionUF(n*n+2);
		blocked = new boolean[n*n+2]; // 2 extra virtual sites

		for (int i = 1; i <= n*n; ++i) {
			blocked[i] = true;
		}

		// initial number of open sites is zero
		numOpenSites = 0;
	}

	private int index(int row, int col) {
		// return index of array site
		if (row <= 0 || row > n || col <= 0 || col > n) {
			throw new IllegalArgumentException();
		}
		return (row-1)*n + col;
	}

	public void open(int row, int col) {
		// open site (row, col) if it is not open already
		int id = index(row, col);
		if (!isOpen(row, col)) {
			numOpenSites++;
			blocked[id] = false;
			// build connections for real sites
			if (row < n && isOpen(row+1, col)) {
				int neighbour = index(row+1, col);
				uf.union(id, neighbour);
			}
			if (row > 1 && isOpen(row-1, col)) {
				int neighbour = index(row-1, col);
				uf.union(id, neighbour);
			}
			if (col < n && isOpen(row, col+1)) {
				int neighbour = index(row, col+1);
				uf.union(id, neighbour);
			}
			if (col > 1 && isOpen(row, col-1)) {
				int neighbour = index(row, col-1);
				uf.union(id, neighbour);
			}

			// build connections for virtual sites
			if (row == 1) {
				uf.union(0, id);
			}
			if (row == n) {
				uf.union(n*n+1, id);
			}
		}
	}

	public boolean isOpen(int row, int col) {
		// check if site is open
		return !blocked[index(row, col)];
	}

	public boolean isFull(int row, int col) {
		// check if site is full
		return uf.connected(0, index(row, col));
	}

	public int numberOfOpenSites() {
		// number of open sites
		return numOpenSites;
	}

	public boolean percolates() {
		// check if the system percolates
		return uf.connected(0, n*n+1);
	}
}
