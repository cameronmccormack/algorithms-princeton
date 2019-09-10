import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private Digraph digraph;
    private ST<String, Bag<Integer>> wordIndex;
    private ST<Integer, String> idIndex;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);

        wordIndex = new ST<>();
        idIndex = new ST<>();

        while (inSynsets.hasNextLine()) {
            String[] splitSynsets = inSynsets.readLine().split(",");

            // create dictionary of words and bags of connected ids
            for (String noun : splitSynsets[1].split(" ")) {
                Bag<Integer> id = new Bag<Integer>();
                if (wordIndex.contains(noun)) {
                    id = wordIndex.get(noun);
                }
                id.add(Integer.parseInt(splitSynsets[0]));
                wordIndex.put(noun, id);
            }

            // create dictionary of ids and synsets
            idIndex.put(Integer.parseInt(splitSynsets[0]), splitSynsets[1]);
        }

        // create a digraph that contains all hypernym links
        digraph = new Digraph(idIndex.size());
        while (inHypernyms.hasNextLine()) {
            String[] splitHypernyms = inHypernyms.readLine().split(",");
            int source = Integer.parseInt(splitHypernyms[0]);
            for (int i = 1; i < splitHypernyms.length; i++) {
                digraph.addEdge(source, Integer.parseInt(splitHypernyms[i]));
            }
        }

        // check no cycles
        if ((new DirectedCycle(digraph).hasCycle())) {
            throw new IllegalArgumentException();
        }

        // exactly one vertex should have zero outdegree as root
        int root = 0;
        for (int i = 0; i < wordIndex.size(); i++) {
            if (digraph.outdegree(i) == 0) {
                root++;
            }
        }
        if (root != 1) {
            throw new IllegalArgumentException();
        }

        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordIndex;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return wordIndex.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Bag<Integer> idA = wordIndex.get(nounA);
        Bag<Integer> idB = wordIndex.get(nounB);
        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Bag<Integer> idA = wordIndex.get(nounA);
        Bag<Integer> idB = wordIndex.get(nounB);
        int synset = sap.ancestor(idA, idB);
        if (synset == -1) {
            return "None";
        } else {
            return idIndex.get(synset);
        }
    }

    // quick test
    public static void main(String[] args) {
        // testing is completed in SAP.java
    }
}
