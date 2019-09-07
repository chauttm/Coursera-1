import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int numItems;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        numItems = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return numItems == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numItems;
    }

    // add the item
    public void enqueue(Item item) {
        validateItem(item);
        if (numItems == items.length) resize(2 * items.length);
        items[numItems++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        checkEmpty();
        int randIndex = StdRandom.uniform(numItems);
        Item item = items[randIndex];
        items[randIndex] = items[--numItems];
        items[numItems] = null;
        if (numItems > 0 && numItems == items.length / 4) resize(items.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkEmpty();
        return items[StdRandom.uniform(numItems)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        private int i;
        private int[] indices;

        private ReverseArrayIterator() {
            i = numItems;
            indices = new int[numItems];
            for (int k = 0; k < numItems; ++k) indices[k] = k;
            StdRandom.shuffle(indices);
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[indices[--i]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        StringBuilder expected, actual;

        RandomizedQueue<Double> rq = new RandomizedQueue<>();
        Double num1 = StdRandom.uniform() * 100;
        StdOut.printf("%s%.2f%s\n\n", "Adding ", num1, " to the queue...");
        rq.enqueue(num1);

        StdOut.printf("%s\n", "Getting a random number from the queue: ");
        Double sampleNum = rq.sample();
        StdOut.printf("%s%.2f\n", "Expected: ", num1);
        StdOut.printf("%s%.2f\n", "Actual: ", sampleNum);
        StdOut.printf("%s\n\n", num1.compareTo(sampleNum) == 0 ? "PASSED" : "FAILED");

        Double num2 = StdRandom.uniform() * 100;
        StdOut.printf("%s%.2f%s\n\n", "Adding ", num2, " to the queue...");
        rq.enqueue(num2);

        Double num3 = StdRandom.uniform() * 100;
        StdOut.printf("%s%.2f%s\n\n", "Adding ", num3, " to the queue...");
        rq.enqueue(num3);

        StdOut.printf("%s\n", "Checking size of the queue: ");
        StdOut.printf("%s\n", "Expected: 3");
        StdOut.printf("%s%d\n", "Actual: ", rq.size());
        StdOut.printf("%s\n\n", rq.size() == 3 ? "PASSED" : "FAILED");

        expected = new StringBuilder();
        expected.append(String.format("%.2f %.2f %.2f", num1, num2, num3));
        actual = new StringBuilder();
        Iterator<Double> iterator = rq.iterator();
        boolean okIterator = true;
        while (iterator.hasNext()) {
            String popNum = String.format("%.2f", iterator.next());
            okIterator = okIterator & (expected.indexOf(popNum) != -1);
            actual.append(popNum + " ");
        }
        actual.deleteCharAt(actual.length() - 1);
        StdOut.printf("%s\n", "Listing all elements in the queue using interator: ");
        StdOut.printf("%s%s\n", "Expected: ", expected.toString());
        StdOut.printf("%s%s\n", "Actual: ", actual.toString());
        StdOut.printf("%s\n\n", okIterator ? "PASSED" : "FAILED");

        StdOut.printf("%s%.2f%s\n\n", "Removing ", rq.dequeue(), " from the queue...");

        StdOut.printf("%s%.2f%s\n\n", "Removing ", rq.dequeue(), " from the queue...");

        StdOut.printf("%s%.2f%s\n\n", "Removing ", rq.dequeue(), " from the queue...");

        StdOut.printf("%s\n", "Checking if queue is empty: ");
        StdOut.printf("%s\n", "Expected: true");
        StdOut.printf("%s%s\n", "Actual: ", rq.isEmpty() ? "true" : "false");
        StdOut.printf("%s\n\n", rq.isEmpty() == true ? "PASSED" : "FAILED");
    }

    private void validateItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Null item");
    }

    private void checkEmpty() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < numItems; ++i) copy[i] = items[i];
        items = copy;
    }
}