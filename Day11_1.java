import java.util.HashMap;
import java.util.Map;

/**
 * Part one of
 * https://adventofcode.com/2018/day/11
 *
 * @author Nerijus
 */
public class Day11_1 {

    public static void main(String[] args) {
        System.out.println("Coordinates for largest total power: " + new Day11_1().findCoordinates());
    }

    int[][] getPowerLevels() {
        int gridSerialNumber = Inputs.readInt("Day11");
        int[][] powerValues = new int[300][300];
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                powerValues[x][y] = getPowerLevel(x + 1, y + 1, gridSerialNumber);
            }
        }

        return powerValues;
    }

    private String findCoordinates() {
        int[][] grid = getPowerLevels();
        return get3x3PowerLevels(grid).entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Max value not found"))
                .getKey();
    }

    private Map<String, Integer> get3x3PowerLevels(int[][] grid) {
        Map<String, Integer> totalPowers = new HashMap<>();
        for (int x = 0; x < 297; x++) {
            for (int y = 0; y < 297; y++) {
                totalPowers.put((x + 1) + "," + (y + 1), sum3x3(grid, x, y));
            }
        }
        return totalPowers;
    }

    private int sum3x3(int[][] grid, int startingX, int startingY) {
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sum += grid[startingX + i][startingY + j];
            }
        }
        return sum;
    }

    private int getPowerLevel(int x, int y, int gridSerialNumber) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel = powerLevel + gridSerialNumber;
        powerLevel = powerLevel * rackId;
        if (powerLevel < 100) {
            return -5;
        } else {
            powerLevel = Integer.valueOf(
                    new StringBuilder("" + powerLevel)
                            .reverse()
                            .toString()
                            .substring(2, 3));
            return powerLevel - 5;
        }
    }
}