import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part two of
 * https://adventofcode.com/2018/day/14
 *
 * @author Nerijus
 */
public class Day14_2 extends Day14_1 {

    public static void main(String[] args) {
        String input = Inputs.readString("Day14");
        List<Integer> digits = Arrays.stream(input.split(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());

        System.out.println("Recipe count until input is found:");
        new Day14_2().getScores(scores -> {
            if (scores.size() < digits.size()) {
                // not enough scores yet, continue
                return true;
            }

            if (!endsWith(scores, digits)) {
                // continue search
                return true;
            }

            // print the count before the match
            System.out.println(scores.size() - digits.size());
            return false;
        });
    }

    private static boolean endsWith(List<Integer> scores, List<Integer> digits) {
        for (int i = digits.size() - 1, iter = 0; i >= 0; i--, iter++) {
            if (!digits.get(i).equals(scores.get(scores.size()-1-iter))) {
                return false;
            }
        }

        return true;
    }
}