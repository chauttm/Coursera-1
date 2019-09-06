import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private  int n, trials;
    private double threshold, sharpness;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        double[] x = new double[trials];
        int[] blocksites = new int[n * n];
        for (int i = 0; i < n * n; ++i) blocksites[i] = i;

        Percolation percolation;
        for (int i = 0; i < trials; ++i) {
            StdRandom.shuffle(blocksites);
            percolation = new Percolation(n);
            for (int j = 0; j < n * n; ++j) {
                int row = blocksites[j] / n + 1;
                int col = blocksites[j] % n + 1;
                percolation.open(row, col);
                if (percolation.percolates()) break;
            }
            x[i] = (double)percolation.numberOfOpenSites() / (n * n);
        }
        threshold = StdStats.mean(x);
        sharpness = StdStats.stddev(x);
    }

    // sample mean of percolation threshold
    public double mean() {
        return threshold;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return sharpness;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return threshold - 1.96 * sharpness / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return threshold + 1.96 * sharpness / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, T);
        StdOut.printf("%s%f\n", "mean                    = ", ps.mean());
        StdOut.printf("%s%f\n", "stddev                  = ", ps.stddev());
        StdOut.printf("%s%f%s%f%s\n", "95% confidence interval = [", ps.confidenceLo(), ", ", ps.confidenceHi(), "]");
    }
}
