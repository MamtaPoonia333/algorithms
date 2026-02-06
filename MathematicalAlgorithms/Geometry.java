import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Geometry {
    private Geometry() {
    }

    public static class Point {
        public final double x;
        public final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static double cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    public static List<Point> convexHull(List<Point> pts) {
        if (pts.size() <= 1) {
            return new ArrayList<>(pts);
        }
        List<Point> points = new ArrayList<>(pts);
        points.sort(Comparator.comparingDouble((Point p) -> p.x).thenComparingDouble(p -> p.y));
        List<Point> lower = new ArrayList<>();
        for (Point p : points) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.get(lower.size() - 1), p) <= 0) {
                lower.remove(lower.size() - 1);
            }
            lower.add(p);
        }
        List<Point> upper = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            Point p = points.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.get(upper.size() - 1), p) <= 0) {
                upper.remove(upper.size() - 1);
            }
            upper.add(p);
        }
        lower.remove(lower.size() - 1);
        upper.remove(upper.size() - 1);
        lower.addAll(upper);
        return lower;
    }

    public static boolean segmentsIntersect(Point a, Point b, Point c, Point d) {
        double d1 = cross(a, b, c);
        double d2 = cross(a, b, d);
        double d3 = cross(c, d, a);
        double d4 = cross(c, d, b);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        }
        return onSegment(a, b, c) || onSegment(a, b, d) || onSegment(c, d, a) || onSegment(c, d, b);
    }

    private static boolean onSegment(Point a, Point b, Point p) {
        return Math.abs(cross(a, b, p)) < 1e-9
            && Math.min(a.x, b.x) - 1e-9 <= p.x && p.x <= Math.max(a.x, b.x) + 1e-9
            && Math.min(a.y, b.y) - 1e-9 <= p.y && p.y <= Math.max(a.y, b.y) + 1e-9;
    }

    public static boolean pointInPolygon(Point p, List<Point> poly) {
        boolean inside = false;
        for (int i = 0, j = poly.size() - 1; i < poly.size(); j = i++) {
            Point a = poly.get(i);
            Point b = poly.get(j);
            boolean intersect = ((a.y > p.y) != (b.y > p.y))
                && (p.x < (b.x - a.x) * (p.y - a.y) / (b.y - a.y + 0.0) + a.x);
            if (intersect) {
                inside = !inside;
            }
        }
        return inside;
    }

    public static double closestPair(List<Point> pts) {
        List<Point> points = new ArrayList<>(pts);
        points.sort(Comparator.comparingDouble(p -> p.x));
        return Math.sqrt(closestPairRec(points, 0, points.size()));
    }

    private static double closestPairRec(List<Point> pts, int l, int r) {
        if (r - l <= 3) {
            double best = Double.POSITIVE_INFINITY;
            for (int i = l; i < r; i++) {
                for (int j = i + 1; j < r; j++) {
                    best = Math.min(best, dist2(pts.get(i), pts.get(j)));
                }
            }
            pts.subList(l, r).sort(Comparator.comparingDouble(p -> p.y));
            return best;
        }
        int m = (l + r) >>> 1;
        double midX = pts.get(m).x;
        double d = Math.min(closestPairRec(pts, l, m), closestPairRec(pts, m, r));
        List<Point> merged = new ArrayList<>();
        int i = l;
        int j = m;
        while (i < m && j < r) {
            if (pts.get(i).y <= pts.get(j).y) {
                merged.add(pts.get(i++));
            } else {
                merged.add(pts.get(j++));
            }
        }
        while (i < m) {
            merged.add(pts.get(i++));
        }
        while (j < r) {
            merged.add(pts.get(j++));
        }
        for (int k = 0; k < merged.size(); k++) {
            pts.set(l + k, merged.get(k));
        }
        List<Point> strip = new ArrayList<>();
        for (int k = l; k < r; k++) {
            if ((pts.get(k).x - midX) * (pts.get(k).x - midX) < d) {
                strip.add(pts.get(k));
            }
        }
        for (int a = 0; a < strip.size(); a++) {
            for (int b = a + 1; b < strip.size() && (strip.get(b).y - strip.get(a).y) * (strip.get(b).y - strip.get(a).y) < d; b++) {
                d = Math.min(d, dist2(strip.get(a), strip.get(b)));
            }
        }
        return d;
    }

    private static double dist2(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    public static double convexPolygonDiameter(List<Point> hull) {
        int n = hull.size();
        if (n < 2) {
            return 0.0;
        }
        int j = 1;
        double best = 0;
        for (int i = 0; i < n; i++) {
            int ni = (i + 1) % n;
            while (area(hull.get(i), hull.get(ni), hull.get((j + 1) % n)) > area(hull.get(i), hull.get(ni), hull.get(j))) {
                j = (j + 1) % n;
            }
            best = Math.max(best, Math.sqrt(dist2(hull.get(i), hull.get(j))));
        }
        return best;
    }

    private static double area(Point a, Point b, Point c) {
        return Math.abs(cross(a, b, c));
    }

    public static boolean sweepLineAnyIntersection(List<Segment> segments) {
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < segments.size(); i++) {
            Segment s = segments.get(i);
            if (s.left.x > s.right.x) {
                Point tmp = s.left;
                s.left = s.right;
                s.right = tmp;
            }
            events.add(new Event(s.left.x, true, i));
            events.add(new Event(s.right.x, false, i));
        }
        events.sort(Comparator.comparingDouble((Event e) -> e.x).thenComparing(e -> !e.isStart));
        SweepComparator comp = new SweepComparator(segments);
        TreeSet<Integer> set = new TreeSet<>(comp);
        for (Event e : events) {
            comp.currentX = e.x;
            int id = e.segmentId;
            if (e.isStart) {
                set.add(id);
                Integer prev = set.lower(id);
                Integer next = set.higher(id);
                if (prev != null && intersects(segments.get(id), segments.get(prev))) {
                    return true;
                }
                if (next != null && intersects(segments.get(id), segments.get(next))) {
                    return true;
                }
            } else {
                Integer prev = set.lower(id);
                Integer next = set.higher(id);
                if (prev != null && next != null && intersects(segments.get(prev), segments.get(next))) {
                    return true;
                }
                set.remove(id);
            }
        }
        return false;
    }

    public static class Segment {
        public Point left;
        public Point right;

        public Segment(Point a, Point b) {
            if (a.x <= b.x) {
                this.left = a;
                this.right = b;
            } else {
                this.left = b;
                this.right = a;
            }
        }

        public double yAt(double x) {
            if (Math.abs(left.x - right.x) < 1e-9) {
                return Math.min(left.y, right.y);
            }
            double t = (x - left.x) / (right.x - left.x);
            return left.y + t * (right.y - left.y);
        }
    }

    private static boolean intersects(Segment s1, Segment s2) {
        return segmentsIntersect(s1.left, s1.right, s2.left, s2.right);
    }

    private static class Event {
        final double x;
        final boolean isStart;
        final int segmentId;

        Event(double x, boolean isStart, int segmentId) {
            this.x = x;
            this.isStart = isStart;
            this.segmentId = segmentId;
        }
    }

    private static class SweepComparator implements Comparator<Integer> {
        private final List<Segment> segments;
        double currentX;

        SweepComparator(List<Segment> segments) {
            this.segments = segments;
        }

        @Override
        public int compare(Integer a, Integer b) {
            if (a.equals(b)) {
                return 0;
            }
            Segment s1 = segments.get(a);
            Segment s2 = segments.get(b);
            double y1 = s1.yAt(currentX);
            double y2 = s2.yAt(currentX);
            if (Math.abs(y1 - y2) > 1e-9) {
                return y1 < y2 ? -1 : 1;
            }
            return Integer.compare(a, b);
        }
    }
}
