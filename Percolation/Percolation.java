import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private int numSites;
    private int top;
    private int bottom;
    private int numOpenSites;
    private boolean[][] isOpen;
    private WeightedQuickUnionUF wquufPercolate;
    private WeightedQuickUnionUF wquufFull;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("N must be > 0");
        size = n;
        numSites = n * n;
        top = numSites;
        bottom = numSites + 1;
        numOpenSites = 0;
        isOpen = new boolean [n][n];
        wquufPercolate = new WeightedQuickUnionUF(bottom + 1);
        wquufFull = new WeightedQuickUnionUF(top + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateSite(row, col);
        int row0 = row - 1;
        int col0 = col - 1;
        if (isOpen(row, col)) return; else isOpen[row0][col0] = true;
        ++numOpenSites;
        int currentCode = encode(row0, col0);
        int[] dx = {-1, 0, 0,+1};
        int[] dy = { 0,-1,+1, 0};
        for (int k = 0; k < 4; ++k) {
            int adj_row0 = row0 + dx[k];
            int adj_col0 = col0 + dy[k];
            if (isOnGrid(adj_row0 + 1, adj_col0 + 1) && isOpen(adj_row0 + 1, adj_col0 + 1)) {
                int adjCode = encode(adj_row0, adj_col0);
                if (!wquufPercolate.connected(currentCode, adjCode)) wquufPercolate.union(currentCode, adjCode);
                if (!wquufFull.connected(currentCode, adjCode)) wquufFull.union(currentCode, adjCode);
            }
        }
        if (row == 1) wquufFull.union(currentCode, top);
        if (row == 1) wquufPercolate.union(currentCode, top);
        if (row == size) wquufPercolate.union(currentCode, bottom);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateSite(row, col);
        return isOpen[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateSite(row, col);
        return isOpen(row, col) && wquufFull.connected(top, encode(row - 1, col - 1));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquufPercolate.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
    }

    private boolean isOnGrid(int row, int col) {
        return (1 <= row && row <= size && 1 <= col && col <= size);
    }

    private void validateSite(int row, int col) {
        if (!isOnGrid(row, col)) throw new IllegalArgumentException("Out of bound");
    }

    private int encode(int row0, int col0) {
        return row0 * size + col0;
    }
}