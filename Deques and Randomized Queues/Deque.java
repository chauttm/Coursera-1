import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        Item item;
        Node prev, next;
    }

    private Node first, last;
    private int numItems;

    // construct an empty deque
    public Deque() {
        first = last = null;
        numItems = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return numItems;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        first.next = oldFirst;
        if (isEmpty()) last = first; else oldFirst.prev = first;
        ++numItems;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateItem(item);
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        last.next = null;
        if (isEmpty()) first = last; else oldLast.next = last;
        ++numItems;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmpty();
        Item item = first.item;
        first = first.next;
        --numItems;
        if (isEmpty()) last = null; else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkEmpty();
        Item item = last.item;
        last = last.prev;
        --numItems;
        if (isEmpty()) first = null; else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("Already at the end of the queue");
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        StringBuilder expected, actual;

        Deque<Double> deque = new Deque<Double>();
        Double lastNum = StdRandom.uniform() * 100;
        StdOut.printf("%s%.2f%s\n\n", "Adding ", lastNum, " to the rear of the deque...");
        deque.addLast(lastNum);

        Double firstNum = StdRandom.uniform() * 100;
        StdOut.printf("%s%.2f%s\n\n", "Adding ", firstNum, " to the front of the deque...");
        deque.addFirst(firstNum);

        expected = new StringBuilder();
        expected.append(String.format("%.2f %.2f", firstNum, lastNum));
        actual = new StringBuilder();
        Iterator<Double> iterator = deque.iterator();
        while (iterator.hasNext()) actual.append(String.format("%.2f ", iterator.next()));
        actual.deleteCharAt(actual.length() - 1);
        StdOut.printf("%s\n", "Listing all elements in the deque using interator: ");
        StdOut.printf("%s%s\n", "Expected: ", expected.toString());
        StdOut.printf("%s%s\n", "Actual: ", actual.toString());
        StdOut.printf("%s\n\n", expected.compareTo(actual) == 0 ? "PASSED" : "FAILED");

        StdOut.printf("%s%.2f%s\n", "Removing ", firstNum, " from the deque: ");
        Double popNum = deque.removeFirst();
        StdOut.printf("%s%.2f\n", "Expected: ", firstNum);
        StdOut.printf("%s%.2f\n", "Actual: ", popNum);
        StdOut.printf("%s\n\n", firstNum.compareTo(popNum) == 0 ? "PASSED" : "FAILED");

        StdOut.printf("%s\n", "Checking size of the deque: ");
        StdOut.printf("%s\n", "Expected: 1");
        StdOut.printf("%s%d\n", "Actual: ", deque.size());
        StdOut.printf("%s\n\n", deque.size() == 1 ? "PASSED" : "FAILED");

        StdOut.printf("%s%.2f%s\n", "Removing ", lastNum, " from the deque: ");
        popNum = deque.removeLast();
        StdOut.printf("%s%.2f\n", "Expected: ", lastNum);
        StdOut.printf("%s%.2f\n", "Actual: ", popNum);
        StdOut.printf("%s\n\n", lastNum.compareTo(popNum) == 0 ? "PASSED" : "FAILED");

        StdOut.printf("%s\n", "Checking if deque is empty: ");
        StdOut.printf("%s\n", "Expected: true");
        StdOut.printf("%s%s\n", "Actual: ", deque.isEmpty() ? "true" : "false");
        StdOut.printf("%s\n\n", deque.isEmpty() == true ? "PASSED" : "FAILED");
    }

    private void validateItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Null item");
    }

    private void checkEmpty() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
    }
}