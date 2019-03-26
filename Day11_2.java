import java.util.HashMap;
import java.util.Map;

/**
 * Part two of
 * https://adventofcode.com/2018/day/11
 *
 * @author Nerijus
 */
public class Day11_2 extends Day11_1 {

    public static void main(String[] args) {
        System.out.println("Coordinates for largest total power: " + new Day11_2().findCoordinates());
    }

    private String findCoordinates() {
        int[][] grid = getPowerLevels();

        Map<String, Integer> totalPowers = new HashMap<>();
        for (int size = 1; size <= 300; size++) {
            System.out.println(String.format("Calculating %d size", size));
            for (int x = 0; x < (300 - size); x++) {
                for (int y = 0; y < (300 - size); y++) {
                    totalPowers.put((x + 1) + "," + (y + 1) + "," + size, sum(grid, x, y, size));
                }
            }
        }
        return totalPowers.entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Max value not found"))
                .getKey();
    }

    private int sum(int[][] grid, int startingX, int startingY, int size) {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += grid[startingX + i][startingY + j];
            }
        }
        return sum;
    }
}