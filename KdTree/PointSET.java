import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateArg(p);
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validateArg(p);
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point: pointSet) point.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateArg(rect);
        List<Point2D> pointList = new ArrayList<Point2D>();
        for (Point2D point: pointSet)
            if (rect.contains(point)) pointList.add(point);
        return pointList;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validateArg(p);
        if (pointSet.isEmpty()) return null;
        Point2D result = null;
        for (Point2D point: pointSet)
            if (result == null || p.distanceTo(point) < p.distanceTo(result)) result = point;
        return result;
    }

    // unit testing of the methods
    public static void main(String[] args) {

    }

    private void validateArg(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }
}