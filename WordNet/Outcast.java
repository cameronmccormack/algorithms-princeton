import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns.length == 0) {
            throw new java.lang.IllegalArgumentException();
        }
        int maxDistance = 0;
        String maxNoun = nouns[0];
        for (String testNoun : nouns) {
            int sumDistance = 0;
            for (String compareNoun : nouns) {
                sumDistance += net.distance(testNoun, compareNoun);
            }
            if (sumDistance > maxDistance){
                maxDistance = sumDistance;
                maxNoun = testNoun;
            }
        }
        return maxNoun;
    }

    // quick test
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}