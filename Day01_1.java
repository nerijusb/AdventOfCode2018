import java.util.Arrays;

/**
 * Part one of
 * https://adventofcode.com/2018/day/1
 *
 * @author Nerijus
 */
public class Day01_1 {

    public static void main(String[] args) {
        System.out.println("Calculated frequency: " + calculateFrequency(Inputs.readString("Day01")));
    }

    private static int calculateFrequency(String input) {
        return Arrays.stream(input.split("\r\n"))
                .mapToInt(Integer::valueOf)
                .sum();
    }
}