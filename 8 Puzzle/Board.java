import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] tiles;
    private int hammingDistance;
    private int manhattanDistance;
    private Board[] adj;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) this.tiles[i][j] = tiles[i][j];

        hammingDistance = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                if (tiles[i][j] != i * n + j + 1 && (!(i == n - 1 && j == n - 1))) ++hammingDistance;

        manhattanDistance = 0;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                if (tiles[i][j] == 0) continue;
                int i_des = (tiles[i][j] - 1) / n;
                int j_des = (tiles[i][j] - 1) % n;
                manhattanDistance += Math.abs(i - i_des) + Math.abs(j - j_des);
            }
    }

    // string representation of this board
    public String toString() {
        StringBuilder result = new StringBuilder();
        int n = dimension();
        result.append(String.format("%d\n", n));
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) result.append(String.format(" %d", tiles[i][j]));
            result.append('\n');
        }
        return result.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDistance == 0 && manhattanDistance == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        return this.toString().equals(y.toString());
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new NeighborIterable();
    }

    private class NeighborIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new NeighborIterator();
        }
    }

    private class NeighborIterator implements Iterator<Board> {
        private int i;

        private NeighborIterator() {
            if (adj != null) {
                i = adj.length;
                return;
            }
            int n = dimension();
            int iBlank = 0;
            int jBlank = 0;
            for (int i = 0; i < n; ++i)
                for (int j = 0; j < n; ++j)
                    if (tiles[i][j] == 0) {
                        iBlank = i;
                        jBlank = j;
                    }
            i = 0;
            if (iBlank > 0) i++;
            if (iBlank < n - 1) i++;
            if (jBlank > 0) i++;
            if (jBlank < n - 1) i++;
            adj = new Board[i];
            i = 0;
            if (iBlank > 0) adj[i++] = swap(iBlank, jBlank, iBlank - 1, jBlank);
            if (iBlank < n - 1) adj[i++] = swap(iBlank, jBlank, iBlank + 1, jBlank);
            if (jBlank > 0) adj[i++] = swap(iBlank, jBlank, iBlank, jBlank - 1);
            if (jBlank < n - 1) adj[i++] = swap(iBlank, jBlank, iBlank, jBlank + 1);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();
            return adj[--i];
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int n = dimension();
        for (int i = 0; i < n; ++i)
            for (int j = 1; j < n; ++j)
                if (tiles[i][j] != 0 && tiles[i][j - 1] != 0) return swap(i, j, i, j - 1);
        return null;
    }

    // unit testing
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        StdOut.printf("%s\n", "String representation of this board: ");
        StdOut.printf("%s\n", initial);

        StdOut.printf("%s%s%s\n","This board is ", initial.isGoal() ? "" : "not", " the goal board.");
        StdOut.printf("%s%d\n", "Number of tiles out of place: ", initial.hamming());
        StdOut.printf("%s%d\n\n", "Sum of Manhattan distances between tiles and goal: ", initial.manhattan());

        Board twin = initial.twin();
        StdOut.printf("%s\n", "A board that is obtained by exchanging any pair of tiles: ");
        StdOut.printf("%s", twin);
        StdOut.printf("%s%s%s\n\n","This board is ", initial.equals(twin) ? "" : "not", " the same board.");

        StdOut.printf("%s\n", "Listing all neighboring boards...");
        Iterable<Board> neighbors = initial.neighbors();
        for (Board neighbor: neighbors) StdOut.printf("%s\n", neighbor);
    }

    private Board swap(int iSrc, int jSrc, int iDes, int jDes) {
        int n = dimension();
        int [][] copy = new int[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) copy[i][j] = tiles[i][j];
        int tmp = copy[iSrc][jSrc];
        copy[iSrc][jSrc] = copy[iDes][jDes];
        copy[iDes][jDes] = tmp;
        return new Board(copy);
    }
}