import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int n = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String text = StdIn.readString();
            ++n;
            //Reservoir sampling
            if (rq.size() < k) {
                rq.enqueue(text);
            } else {
                int index = StdRandom.uniform(n) + 1;
                if (index <= k) {
                    rq.dequeue();
                    rq.enqueue(text);
                }
            }
        }
        for (int i = 0; i < k; ++i) StdOut.println(rq.dequeue());
    }
}