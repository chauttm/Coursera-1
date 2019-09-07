import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class KdTree {
    private class Node {
        Point2D point;
        RectHV rect;
        Node left, right;

        Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    private Node root;
    private int size;
    private Point2D nearestPoint;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private int compare(Point2D p1, Point2D p2, boolean level) {
        if (p1.compareTo(p2) == 0) return 0;
        if (level) {
            if (p1.y() < p2.y()) return -1; else return +1;
        } else {
            if (p1.x() < p2.x()) return -1; else return +1;
        }
    }

    private Node insertNode(Node h, boolean level, Point2D p, double xmin, double ymin, double xmax, double ymax) {
        if (h == null) {
            ++size;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        int cmp = compare(p, h.point, level);
        if (cmp < 0) {
            if (level) h.left = insertNode(h.left, !level, p, xmin, ymin, xmax, h.point.y());
            else h.left = insertNode(h.left, !level, p, xmin, ymin, h.point.x(), ymax);
        }
        else if (cmp > 0) {
            if (level) h.right = insertNode(h.right, !level, p, xmin, h.point.y(), xmax, ymax);
            else h.right = insertNode(h.right, !level, p, h.point.x(), ymin, xmax, ymax);
        }
        return h;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validateArg(p);
        root = insertNode(root, false, p, 0, 0, 1, 1);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validateArg(p);
        boolean level = true;
        Node h = root;
        while (h != null) {
            level = !level;
            int cmp = compare(p, h.point, level);
            if (cmp == 0) return true;
            if (cmp < 0) h = h.left; else h = h.right;
        }
        return false;
    }

    private void drawNode(Node h) {
        if (h == null) return;
        h.point.draw();
        drawNode(h.left);
        drawNode(h.right);
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root);
    }

    private void rangeSearch(Node h, RectHV rect, List<Point2D> out) {
        if (h == null) return;
        if (!rect.intersects(h.rect)) return;
        if (rect.contains(h.point)) out.add(h.point);
        rangeSearch(h.left, rect, out);
        rangeSearch(h.right, rect, out);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateArg(rect);
        List<Point2D> pointList = new ArrayList<Point2D>();
        rangeSearch(root, rect, pointList);
        return pointList;
    }

    private void nearestSearch(Node h, boolean level, Point2D p) {
        if (h == null) return;
        if (p.distanceTo(nearestPoint) <= h.rect.distanceTo(p)) return;
        if (p.distanceTo(nearestPoint) > p.distanceTo(h.point)) nearestPoint = h.point;
        int cmp = compare(p, h.point, level);
        if (cmp < 0) {
            nearestSearch(h.left, !level, p);
            nearestSearch(h.right, !level, p);
        } else {
            nearestSearch(h.right, !level, p);
            nearestSearch(h.left, !level, p);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validateArg(p);
        if (isEmpty()) return null;
        nearestPoint = root.point;
        nearestSearch(root, false, p);
        return nearestPoint;
    }

    // unit testing of the methods
    public static void main(String[] args) {

    }

    private void validateArg(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }
}