import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] ratio;
    private int trials, numSites;
    private double conf = 1.96; // 95 % confidence level
    private double sd, av, sqrtTrials;

    public PercolationStats(int n, int trls) {
        if (n <= 0 || trls <= 0) {
            throw new IllegalArgumentException();
        }

        trials = trls;
        sqrtTrials = Math.sqrt(trials);
        ratio = new double[trials];
        numSites = n*n;

        // perform trials on n x n grid
        for (int t = 0; t < trials; t++) {
            Percolation p = new Percolation(n);
            int[] perm = StdRandom.permutation(numSites);
            int i = 0; // counting index for permutation
            while (!p.percolates()) {
                int index = perm[i++];
                int row = index/n + 1;
                int col = index % n + 1;
                // open new site
                p.open(row, col);
            }
            ratio[t] = p.numberOfOpenSites() * 1.0 / numSites;
        }
        sd = StdStats.stddev(ratio);
        av = StdStats.mean(ratio);
    }

    public double mean() {
        // sample mean of percolation threshold
        return av;
    }

    public double stddev() {
        return sd;
    }

    public double confidenceLo() {
        // lower bound of 95% confidence interval
        return av - conf * sd / sqrtTrials;
    }

    public double confidenceHi() {
        // upper bound of 95% confidence interval
        return av + conf * sd / sqrtTrials;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int testCase = Integer.parseInt(args[1]);
        PercolationStats tester = new PercolationStats(n, testCase);
        System.out.format("mean                    = %f\n", tester.mean());
        System.out.format("stddev                  = %f\n", tester.stddev());
        System.out.format("95%% confidence interval = %f to %f\n", tester.confidenceLo(), tester.confidenceHi());
    }
}
