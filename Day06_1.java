import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2018/day/6
 *
 * @author Nerijus
 */
public class Day06_1 {

    public static void main(String[] args) {
        System.out.println("Largest area size: " + new Day06_1().getLargestAreaSize());
    }

    private int getLargestAreaSize() {
        FieldData field = getFieldData();
        return field.pointsByDestination.entrySet()
                .stream()
                .filter(e -> !isInfinite(e.getKey(), e.getValue(), field))
                .map(e -> e.getValue().size())
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Could not find biggest area"));
    }

    FieldData getFieldData() {
        FieldData data = new FieldData();

        data.destinations = getDestinationPoints();
        System.out.printf("Destination points: %d%n", data.destinations.size());

        data.width = maxX(data.destinations);
        data.height = maxY(data.destinations);
        System.out.printf("Area size %d x %d%n", data.width, data.height);

        // group all points in area by the closest destination
        data.pointsByDestination = new HashMap<>();
        for (int i = 0; i < data.width; i++) {
            for (int j = 0; j < data.height; j++) {
                DestinationPoint closestDestination = getClosestDestinationPoint(data.destinations, i, j);
                if (closestDestination != null) {
                    data.pointsByDestination.computeIfAbsent(closestDestination, key -> new ArrayList<>()).add(new Point(i, j));
                }
            }
        }
        return data;
    }

    private boolean isInfinite(DestinationPoint destination, List<Point> points, FieldData field) {
        // lets check if area has point on the area edge
        // which would indicate that area is potentially infinite
        boolean isInfinite = points.stream().anyMatch(
                point -> point.x == 0
                        || point.y == 0
                        || point.x == field.width
                        || point.y == field.height);

        System.out.printf("Destination #%d has %d close points. Is infinite? %s%n",
                destination.id, points.size(), isInfinite ? "Yes" : "No");
        return isInfinite;
    }

    private DestinationPoint getClosestDestinationPoint(List<DestinationPoint> points, int x, int y) {
        // calculating distances to all points
        Map<DestinationPoint, Integer> distances = new HashMap<>();
        for (DestinationPoint point : points) {
            distances.put(point, point.distanceTo(x, y));
        }

        Map.Entry<DestinationPoint, Integer> closestDestination = distances.entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(() -> new IllegalStateException("Could not find min distance"));

        // if more then one point has the same min distance, then there is none
        if (distances.values()
                .stream()
                .filter(d -> closestDestination.getValue().equals(d))
                .count() > 1) {
            return null;
        }

        // only one point has the this min distance
        return closestDestination.getKey();
    }

    private int maxX(List<DestinationPoint> points) {
        return points
                .stream()
                .map(point -> point.x)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Could not find max X"));
    }

    private int maxY(List<DestinationPoint> points) {
        return points
                .stream()
                .map(point -> point.y)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("Could not find max Y"));
    }

    private List<DestinationPoint> getDestinationPoints() {
        Supplier<Integer> idGenerator = new Supplier<Integer>() {
            int currentId = 0;

            @Override
            public Integer get() {
                currentId = currentId + 1;
                return currentId;
            }
        };

        return Inputs.readStrings("Day06")
                .stream()
                .map(source -> new DestinationPoint(idGenerator, source))
                .collect(Collectors.toList());
    }

    /**
     * Holds the information about the field
     */
    class FieldData {
        // size
        int width, height;
        // all destinations
        List<DestinationPoint> destinations;
        // points by closest destination
        Map<DestinationPoint, List<Point>> pointsByDestination;
    }

    class DestinationPoint extends Point {
        int id;

        DestinationPoint(Supplier<Integer> idGenerator, String source) {
            super(Integer.valueOf(source.split(", ")[0]),
                    Integer.valueOf(source.split(", ")[1]));
            this.id = idGenerator.get();
        }

        @Override
        public String toString() {
            return "#" + id + "(" + x + "," + y + ")";
        }
    }

    class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int distanceTo(int x, int y) {
            return Math.abs(this.x - x) + Math.abs(this.y - y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }
}