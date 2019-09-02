import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node previous;
        private Node next;
    }

    private Node first;
    private Node last;
    private int n;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        n = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the deque
    public int size() {
        return n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            newNode.previous = null;
            newNode.next = null;
            first = newNode;
            last = newNode;
        } else {
            newNode.previous = null;
            newNode.next = first;
            first.previous = newNode;
            first = newNode;
        }
        n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node newNode = new Node();
        newNode.item = item;

        if (isEmpty()) {
            newNode.previous = null;
            newNode.next = null;
            first = newNode;
            last = newNode;
        } else {
            newNode.previous = last;
            newNode.next = null;
            last.next = newNode;
            last = newNode;
        }
        n++;
    }

    // remove and return the item from the front 
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        Item item = first.item;
        first = first.next;
        if (first == null) {
            last = null;
        } else {
            first.previous = null;
        }
        n--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }

        Item item = last.item;
        last = last.previous;
        if (last == null) {
            first = null;
        } else {
            last.next = null;
        }
        n--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // iterator class over items from front to back
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // quick test
    public static void main(String[] args) {
        Deque<String> queue = new Deque<String>();
        System.out.println(queue.isEmpty());
        System.out.println(queue.size());
        queue.addLast("b");
        queue.addFirst("a");
        queue.addLast("c");
        queue.addLast("d");
        queue.addFirst("e");
        queue.removeFirst();
        queue.removeFirst();
        queue.removeLast();
        System.out.println(queue.isEmpty());
        System.out.println(queue.size());

        for (String s: queue) {
            System.out.println(s);
        }
    }
}
