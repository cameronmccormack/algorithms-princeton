import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;

public class BaseballElimination {
    private static final double ERR = 0.0000000001;
    private final int n;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final  int[][] against;
    private final boolean[] eliminated;
    private final ST<String, Integer> tags;
    private final List<SET<String>> certificate;

    // create a baseball division from given filename
    public BaseballElimination(String filename) {
        In in = new In(filename);
        n = in.readInt();

        teams = new String[n];
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];
        eliminated = new boolean[n];
        tags = new ST<String, Integer>();
        certificate = new ArrayList<SET<String>>();

        for (int i = 0; i < n; i++) {
            teams[i] = in.readString();
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }

            tags.put(teams[i], i);
            certificate.add(new SET<String>());
        }

        eliminationCheck();

        // set non-eliminated teams certificate to null
        for (int i = 0; i < n; i++) {
            if (!eliminated[i]) {
                certificate.set(i, null);
            }
        }
    }

    // check for elimination and adjust certificates accordingly
    private void eliminationCheck() {
        // initialize all eliminated statuses as false
        for (int i = 0; i < n; i++) {
            eliminated[i] = false;
        }

        // quick check if number of fixtures is less than number of wins needed
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (wins[i] + remaining[i] < wins[j]) {
                    eliminated[i] = true;
                    certificate.get(i).add(teams[j]);
                }
            }
        }

        // full check, skipping teams elimiated by the quick check
        int fixtureVertices = (n-1) * (n-2) / 2;
        int totalVertices = fixtureVertices + n + 1;
        for (int i = 0; i < n; i++) {
            if (eliminated[i] == true) {
                continue;
            }
            
            int currentVertex = fixtureVertices + 1;
            ST<Integer, Integer> vertexTags = new ST<Integer, Integer>();
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                vertexTags.put(j, currentVertex);
                currentVertex++;
            }

            FlowNetwork G = new FlowNetwork(totalVertices);

            // add edges to/from fixture vertices
            currentVertex = 1;
            for (int j = 0; j < n - 1; j++) {
                if (j == i) { 
                    continue;
                }
                for (int k = j + 1; k < n; k++) {
                    if (k == i) {
                        continue;
                    }
                    FlowEdge e;
                    
                    // edge from source to fixture
                    e = new FlowEdge(0, currentVertex, against[j][k]);
                    G.addEdge(e);

                    // edges between fixtures and teams
                    e = new FlowEdge(currentVertex, vertexTags.get(j), Double.POSITIVE_INFINITY);
                    G.addEdge(e);
                    e = new FlowEdge(currentVertex, vertexTags.get(k), Double.POSITIVE_INFINITY);
                    G.addEdge(e);

                    currentVertex++;
                }
            }

            // add edges from team vertices
            for (int j = 0; j < n; j++) {
                if (j == i) {
                    continue;
                }
                FlowEdge e = new FlowEdge(vertexTags.get(j), totalVertices - 1,
                                          wins[i] + remaining[i] - wins[j]);
                G.addEdge(e);
            }

            // find certificate of elimination
            FordFulkerson ff = new FordFulkerson(G, 0, totalVertices - 1);
            for (FlowEdge e : G.adj(0)) {
                if (Math.pow(e.flow() - e.capacity(), 2) > ERR) {
                    eliminated[i] = true;
                    for (int j = 0; j < n; j++) {
                        if (j == i) {
                            continue;
                        }
                        if (ff.inCut(vertexTags.get(j))) {
                            certificate.get(i).add(teams[j]);
                        }
                    }
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        return tags.keys();
    }

    // number of wins for given team
    public int wins(String team) {
        Integer teamID = tags.get(team);
        if (teamID == null) {
            throw new IllegalArgumentException();
        }
        return wins[teamID];
    }

    // number of losses for given team
    public int losses(String team) {
        Integer teamID = tags.get(team);
        if (teamID == null) {
            throw new IllegalArgumentException();
        }
        return losses[teamID];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        Integer teamID = tags.get(team);
        if (teamID == null) {
            throw new IllegalArgumentException();
        }
        return remaining[teamID];
    }

    // number of remaining games between team 1 and team 2
    public int against(String team1, String team2) {
        Integer teamID1 = tags.get(team1);
        Integer teamID2 = tags.get(team2);
        if (teamID1 == null || teamID2 == null) {
            throw new IllegalArgumentException();
        }
        return against[teamID1][teamID2];
    }

    // is a given team eliminated
    public boolean isEliminated(String team) {
        Integer teamID = tags.get(team);
        if (teamID == null) {
            throw new IllegalArgumentException();
        }
        return eliminated[teamID];
    }

    // subset R of teams that eliminated given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        Integer teamID = tags.get(team);
        if (teamID == null) {
            throw new IllegalArgumentException();
        }
        return certificate.get(teamID);
    }

    // main function to list teams and elimination status
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
