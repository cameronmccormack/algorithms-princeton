import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class SAP {
    private Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.IllegalArgumentException();
        }
        digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || w < 0 || v > digraph.V() - 1 || w > digraph.V() - 1) {
            throw new java.lang.IllegalArgumentException();
        }
        return calcSAP(v, w, "path");
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || w < 0 || v > digraph.V() - 1 || w > digraph.V() - 1) {
            throw new java.lang.IllegalArgumentException();
        }
        return calcSAP(v, w, "ancestor");
    }

    private int calcSAP(int v, int w, String mode) {
        if (mode != "path" && mode != "ancestor") {
            throw new java.lang.IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, w);
        int minLen = Integer.MAX_VALUE;
        int minAnc = -1;
        Boolean[] marked = new Boolean[digraph.V()];
        int[] distTo = new int[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            marked[i] = false;
            distTo[i] = Integer.MAX_VALUE;
        }
        Queue<Integer> q = new Queue<Integer>();
        marked[v] = true;
        distTo[v] = 0;
        q.enqueue(v);
        while (!q.isEmpty()) {
            int currentVertex = q.dequeue();
            if (minLen < distTo[currentVertex]) break;
            if (bfs.hasPathTo(currentVertex) && (bfs.distTo(currentVertex) + distTo[currentVertex] < minLen)) {
                minLen = bfs.distTo(currentVertex) + distTo[currentVertex];
                minAnc = currentVertex;
            }
            for (int nextVertex: digraph.adj(currentVertex)) {
                if (!marked[nextVertex]) {
                    distTo[nextVertex] = distTo[currentVertex] + 1;
                    marked[nextVertex] = true;
                    q.enqueue(nextVertex);
                }
            }
        }

        if (mode == "path") {
            if (minLen < Integer.MAX_VALUE) return minLen;
            else return -1;
        } else {
            return minAnc;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (Integer vertex: v) {
            if (vertex == null || vertex < 0 || vertex > digraph.V()) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        for (Integer vertex: w) {
            if (vertex == null || vertex < 0 || vertex > digraph.V()) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        return calcMultiSAP(v, w, "path");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.IllegalArgumentException();
        }
        for (Integer vertex: v) {
            if (vertex == null || vertex < 0 || vertex > digraph.V()) {
                throw new java.lang.IllegalArgumentException ();
            }
        }
        for (Integer vertex: w) {
            if (vertex == null || vertex < 0 || vertex > digraph.V()) {
                throw new java.lang.IllegalArgumentException ();
            }
        }
        return calcMultiSAP(v, w, "ancestor");
    }

    private int calcMultiSAP(Iterable<Integer> v, Iterable<Integer> w, String mode) {
        if (mode != "path" && mode != "ancestor") {
            throw new java.lang.IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, w);
        int minLen = Integer.MAX_VALUE;
        int minAnc = -1;
        Boolean[] marked = new Boolean[digraph.V()];
        int[] distTo = new int[digraph.V()];
        for (int i = 0; i < digraph.V(); i++) {
            marked[i] = false;
            distTo[i] = Integer.MAX_VALUE;
        }
        Queue<Integer> q = new Queue<Integer>();
        for (int vertex: v) {
            marked[vertex] = true;
            distTo[vertex] = 0;
            q.enqueue(vertex);
        }
        while (!q.isEmpty()) {
            int currentVertex = q.dequeue();
            if (minLen < distTo[currentVertex]) break;
            if (bfs.hasPathTo(currentVertex) && (bfs.distTo(currentVertex) + distTo[currentVertex] < minLen)) {
                minLen = bfs.distTo(currentVertex) + distTo[currentVertex];
                minAnc = currentVertex;
            }
            for (int next: digraph.adj(currentVertex)) {
                if (!marked[next]) {
                    distTo[next] = distTo[currentVertex] + 1;
                    marked[next] = true;
                    q.enqueue(next);
                }
            }
        }

        if (mode == "path") {
            if (minLen < Integer.MAX_VALUE) return minLen;
            else return -1;
        } else {
            return minAnc;
        }
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
}
