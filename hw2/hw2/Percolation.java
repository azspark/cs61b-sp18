package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int numOpen;
    private WeightedQuickUnionUF uf;
    private boolean[] openRecord;
    private int n;
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.n = N;
        uf = new WeightedQuickUnionUF(N * N);
        openRecord = new boolean[N * N];
        // union the first row
        for (int i = 1; i < N; i++) {
            uf.union(0, i);
        }
        //union the last row
        for (int i = N * N - N; i < N * N - 1; i++) {
            uf.union(i, N * N - 1);
        }
        numOpen = 0;
    }

    /**
     * Open the site (row, col) if it is not open already
     * @param row
     * @param col
     */
    public void open(int row, int col) {

        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int curIndex = row * n + col;
        if (!openRecord[curIndex]) {
            openRecord[curIndex] = true;
        } else {
            return;
        }
        if (row > 0) {
            int upIndex = curIndex - n;
            if (openRecord[upIndex]) {
                uf.union(upIndex, curIndex);
            }
        }
        if (row < n - 1) {
            int downIndex = curIndex + n;
            if (openRecord[downIndex]) {
                uf.union(downIndex, curIndex);
            }
        }
        if (col > 0) {
            int leftIndex = curIndex - 1;
            if (openRecord[leftIndex]) {
                uf.union(leftIndex, curIndex);
            }
        }
        if (col < n - 1) {
            int rightIndex = curIndex + 1;
            if (openRecord[rightIndex]) {
                uf.union(rightIndex, curIndex);
            }
        }
        numOpen += 1;
    }

    /**
     * Is the site (row, col) open?
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return openRecord[row * n + col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return isOpen(row, col) && uf.find(0) == uf.find(row * n + col);
    }

    public int numberOfOpenSites() {
        return numOpen;
    }

    public boolean percolates() {
        return uf.find(0) == uf.find(n * n - 1);
    }

    public static void main(String[] args) {

    }
}
