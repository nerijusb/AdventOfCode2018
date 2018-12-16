import java.util.ArrayList;
import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2018/day/6
 *
 * @author Nerijus
 */
public class Day6_2 extends Day6_1 {

    public static void main(String[] args) {
        System.out.println("Size of the region containing all locations which have a total distance to all destinations of less than 10000: "
                + new Day6_2().getAreaSizeWithDistanceToAllDestinationsLessThan(10000));
    }

    private long getAreaSizeWithDistanceToAllDestinationsLessThan(int target) {
        List<Integer> combinedDistances = new ArrayList<>();
        FieldData field = getFieldData();
        for (int i = 0; i < field.width; i++) {
            for (int j = 0; j < field.height; j++) {
                combinedDistances.add(getCombinedDistance(i, j, field.destinations));
            }
        }

        return combinedDistances
                .stream()
                .filter(distance -> distance < target)
                .count();
    }

    private Integer getCombinedDistance(int x, int y, List<DestinationPoint> destinations) {
        return destinations
                .stream()
                .mapToInt(destinationPoint -> destinationPoint.distanceTo(x, y))
                .sum();
    }
}