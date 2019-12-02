package hw2;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double confiLow;
    private double confiHigh;
    private double[] percolationValues;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        percolationValues = new double[T];
        for (int i = 0; i < T; i++) {
            percolationValues[i] = onePerlocationExp(pf.make(N), N);
        }
        mean = StdStats.mean(percolationValues);
        stddev = StdStats.stddev(percolationValues);
        confiHigh = mean + (1.96 * stddev) / Math.sqrt(T);
        confiLow = mean - (1.96 * stddev) / Math.sqrt(T);
    }
    public double mean() {
        return mean;
    }
    public double stddev() {
        return stddev;
    }
    public double confidenceLow() {
        return confiLow;
    }
    public double confidenceHigh() {
        return confiHigh;
    }

    private double onePerlocationExp(Percolation pl, int N) {
        double count = 0;
        while (!pl.percolates()) {
            count += 1;
            int randomRow = StdRandom.uniform(N);
            int randomCol = StdRandom.uniform(N);
            while (pl.isOpen(randomRow, randomCol)) {
                randomRow = StdRandom.uniform(N);
                randomCol = StdRandom.uniform(N);
            }
            pl.open(randomRow, randomCol);
        }
        return count / (N * N);
    }
}
