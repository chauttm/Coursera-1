import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BruteCollinearPoints {
    private List<LineSegment> list;
    private int numSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null point(s)");
        int n = points.length;
        for (int i = 0; i < n; ++i)
            if (points[i] == null) throw new IllegalArgumentException("Null point(s)");
        Point[] sortedPoints = points.clone();
        Arrays.sort(sortedPoints);
        for (int i = 1; i < n; ++i)
            if (sortedPoints[i - 1].compareTo(sortedPoints[i]) == 0) throw new IllegalArgumentException("Repeated point(s)");

        numSegments = 0;
        list = new ArrayList<LineSegment>();
        for (int p1 = 0; p1 < n; ++p1)
            for (int p2 = p1 + 1; p2 < n; ++p2) {
                double s12 = sortedPoints[p1].slopeTo(sortedPoints[p2]);
                for (int p3 = p2 + 1; p3 < n; ++p3)
                    if (s12 == sortedPoints[p1].slopeTo(sortedPoints[p3]))
                        for (int p4 = p3 + 1; p4 < n; ++p4)
                            if (s12 == sortedPoints[p1].slopeTo(sortedPoints[p4])) {
                                ++numSegments;
                                list.add(new LineSegment(sortedPoints[p1], sortedPoints[p4]));
                            }
            }
    }

    // the number of line segments
    public int numberOfSegments() {
        return numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return list.toArray(new LineSegment[list.size()]);
    }
}