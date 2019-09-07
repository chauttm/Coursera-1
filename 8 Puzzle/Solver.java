import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        Board board;
        int moves;
        SearchNode prev;
        int priority;

        SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            priority = board.manhattan() + moves;
        }

        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

    private int numMoves;
    private SearchNode trace;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Null board");
        MinPQ<SearchNode> minpq = new MinPQ<SearchNode>();
        minpq.insert(new SearchNode(initial, 0, null));

        MinPQ<SearchNode> minpqTwin = new MinPQ<SearchNode>();
        minpqTwin.insert(new SearchNode(initial.twin(), 0, null));
        numMoves = -1;

        while (true) {
            SearchNode currentNode = minpq.delMin();
            if (currentNode.board.isGoal()) {
                numMoves = currentNode.moves;
                while (currentNode != null) {
                    trace = new SearchNode(currentNode.board, currentNode.moves, trace);
                    currentNode = currentNode.prev;
                }
                break;
            }
            SearchNode currentNodeTwin = minpqTwin.delMin();
            if (currentNodeTwin.board.isGoal()) {
                break;
            }
            Iterable<Board> neighbors = currentNode.board.neighbors();
            for (Board neighbor: neighbors) {
                SearchNode neighborNode = new SearchNode(neighbor, currentNode.moves + 1, currentNode);
                if (currentNode.prev != null && neighborNode.board.equals(currentNode.prev.board)) continue;
                minpq.insert(neighborNode);
            }
            neighbors = currentNodeTwin.board.neighbors();
            for (Board neighbor: neighbors) {
                SearchNode neighborNode = new SearchNode(neighbor, currentNodeTwin.moves + 1, currentNodeTwin);
                if (currentNodeTwin.prev != null && neighborNode.board.equals(currentNodeTwin.prev.board)) continue;
                minpqTwin.insert(neighborNode);
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return moves() >= 0;
    }

    // min number of moves to solve initial board
    public int moves() {
        return numMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null; else return new SolverIterable();
    }

    private class SolverIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new SolverIterator();
        }
    }

    private class SolverIterator implements Iterator<Board> {
        private SearchNode current = trace;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Board next() {
            if (!hasNext()) throw new NoSuchElementException();
            Board board = current.board;
            current = current.prev;
            return board;
        }
    }

    // test client
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("Unsolvable puzzle");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}