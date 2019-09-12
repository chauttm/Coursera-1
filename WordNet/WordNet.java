import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class WordNet {
    private Map<String, Integer> nounMap;
    private String[] synsets;
    private Digraph G;
    private SAP sap;
    private List<List<Integer> > synsetOf;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        validateNull(synsets);
        validateNull(hypernyms);
        In in = new In(synsets);
        nounMap = new LinkedHashMap<String, Integer>();
        int synsetNum = 0;
        int nounNum = 0;
        List<String> synsetList = new ArrayList<String>();
        while (!in.isEmpty()) {
            String text = in.readLine();
            String synset = text.split(",")[1];
            synsetList.add(synset);
            synsetNum++;
            String[] nouns = synset.split(" ");
            for (int i = 0; i < nouns.length; ++i)
                if (!nounMap.containsKey(nouns[i])) nounMap.put(nouns[i], nounNum++);
        }

        G = new Digraph(synsetNum);
        in = new In(hypernyms);
        while (!in.isEmpty()) {
            String text = in.readLine();
            String[] hypernymList = text.split(",");
            int v = Integer.parseInt(hypernymList[0]);
            for (int i = 1; i < hypernymList.length; ++i) {
                int w = Integer.parseInt(hypernymList[i]);
                G.addEdge(v, w);
            }
        }
        validateRootedDAG();
        sap = new SAP(G);

        synsetOf = new ArrayList<List<Integer> >();
        for (int i = 0; i < nounNum; ++i) synsetOf.add(new ArrayList<Integer>());
        this.synsets = synsetList.toArray(new String[synsetNum]);
        for (int i = 0; i < synsetNum; ++i) {
            String[] nouns = this.synsets[i].split(" ");
            for (int j = 0; j < nouns.length; ++j) {
                int nounId = nounMap.get(nouns[j]);
                synsetOf.get(nounId).add(i);
            }
        }

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        validateNull(word);
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateWordNetNoun(nounA);
        validateWordNetNoun(nounB);
        int nounAId = nounMap.get(nounA);
        int nounBId = nounMap.get(nounB);
        return sap.length(synsetOf.get(nounAId), synsetOf.get(nounBId));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateWordNetNoun(nounA);
        validateWordNetNoun(nounB);
        int nounAId = nounMap.get(nounA);
        int nounBId = nounMap.get(nounB);
        return synsets[sap.ancestor(synsetOf.get(nounAId), synsetOf.get(nounBId))];
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }

    private void validateNull(String s) {
        if (s == null) throw new IllegalArgumentException("Null string");
    }

    private void validateWordNetNoun(String s) {
        if (!isNoun(s)) throw new IllegalArgumentException("Not a WordNet noun");
    }

    private void validateRootedDAG() {
        boolean foundedRoot = false;
        for (int i = 0; i < G.V(); ++i)
            if (G.outdegree(i) == 0) {
                if (!foundedRoot) foundedRoot = true;
                else throw new IllegalArgumentException("Input does not correspond to a rooted DAG");
            }
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle()) throw new IllegalArgumentException("Input does not correspond to a rooted DAG");
    }
}