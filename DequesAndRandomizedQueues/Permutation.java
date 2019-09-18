import edu.princeton.cs.algs4.StdIn;

// reads a sequence of strings and prints k of them at random
public class Permutation {
    public static void main(String[] args) {
        
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }

        for (int i = 0; i < k; ++i) {
            System.out.println(q.dequeue());
        }
    }
}
