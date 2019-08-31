import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;
    private int n;
    private int first;
    private int last;

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[1];
        n = 0;
        first = 0;
        last = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }

    // resize the queue
    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[i];
        }
        q = temp;
        first = 0;
        last = n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (n == q.length) {
            resize(2 * q.length);
        }
        if (last == q.length) {
            last = 0;
        }
        q[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = StdRandom.uniform(n);
        Item item = q[index];

        q[index] = q[n-1];
        q[--n] = null;
        if (n > 0 && n == q.length/4) {
            resize(q.length/2);
        }
        return item;
    }

    // return a random item (but do no remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return q[StdRandom.uniform(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // class for iterator
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        private int[] perm = new int[n];

        public ArrayIterator() {
            for (int j = 0; j < n; ++j) {
                perm[j] = j;
            }
            StdRandom.shuffle(perm);
        }

        public boolean hasNext() {
            return i < n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return q[perm[i++]];
        }
    }

    // prints queue status (used for testing)
    private void status() {
        System.out.format("Length: %d | n: %d | first: %d | last: %d\n", q.length, n, first, last);
    }

    // testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.status();
        rq.enqueue(4);
        rq.status();
        rq.enqueue(30);
        rq.status();
        rq.enqueue(31);
        rq.status();
        rq.enqueue(9);
        rq.status();
        rq.dequeue();
        rq.status();
    }
}
