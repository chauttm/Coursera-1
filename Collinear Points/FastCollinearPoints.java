import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> list;
    private int numSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        Point[] slopes;
        for (int i = 0; i < n; ++i) {
            slopes = sortedPoints.clone();
            Point root = slopes[i];
            Arrays.sort(slopes, sortedPoints[i].slopeOrder());
            int l = 0;
            while (l < n) {
                double sl = sortedPoints[i].slopeTo(slopes[l]);
                int r = l;
                boolean ok = (sortedPoints[i].compareTo(slopes[r]) < 0);
                while (r + 1 < n && sl == sortedPoints[i].slopeTo(slopes[r + 1])) {
                    ++r;
                    if (sortedPoints[i].compareTo(slopes[r]) > 0) ok = false;
                }
                if (ok && r - l + 1 >= 3) {
                    ++numSegments;
                    list.add(new LineSegment(root, slopes[r]));
                }
                l = r + 1;
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