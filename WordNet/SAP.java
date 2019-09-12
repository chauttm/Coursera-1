import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class SAP {
    private Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        validateNull(G);
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> listV = new ArrayList<Integer>();
        listV.add(v);
        List<Integer> listW = new ArrayList<Integer>();
        listW.add(w);
        return BFS(listV, listW, true);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> listV = new ArrayList<Integer>();
        listV.add(v);
        List<Integer> listW = new ArrayList<Integer>();
        listW.add(w);
        return BFS(listV, listW, false);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return BFS(v, w, true);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return BFS(v, w, false);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void validateNull(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    private void validateVertex(Integer v) {
        validateNull(v);
        if (v < 0 || v >= G.V()) throw new IllegalArgumentException();
    }

    private void validateIterable(Iterable<Integer> v) {
        validateNull(v);
        Iterator<Integer> iterator = v.iterator();
        while (iterator.hasNext()) validateVertex(iterator.next());
    }

    private int BFS(Iterable<Integer> v, Iterable<Integer> w, boolean queryLength) {
        validateIterable(v);
        validateIterable(w);
        int[] f = new int[G.V()];
        for (int i = 0; i < G.V(); ++i) f[i] = -1;
        Queue<Integer> que = new LinkedList<Integer>();
        for (Integer u: v) {
            que.add(u);
            f[u] = 0;
        }
        while (!que.isEmpty()) {
            Integer u = que.poll();
            for (Integer adj: G.adj(u))
                if (f[adj] < 0) {
                    que.add(adj);
                    f[adj] = f[u] + 1;
                }
        }

        int length = Integer.MAX_VALUE;
        int ancestor = -1;
        int[] g = new int[G.V()];
        for (int i = 0; i < G.V(); ++i) g[i] = -1;
        que = new LinkedList<Integer>();
        for (Integer u: w) {
            que.add(u);
            g[u] = 0;
        }
        while (!que.isEmpty()) {
            Integer u = que.poll();
            if (f[u] >= 0)  {
                if (f[u] + g[u] < length) {
                    length = f[u] + g[u];
                    ancestor = u;
                }
            }
            for (Integer adj: G.adj(u))
                if (g[adj] < 0) {
                    que.add(adj);
                    g[adj] = g[u] + 1;
                }
        }
        if (ancestor < 0) return -1;
        if (queryLength) return length; else return ancestor;
    }
}