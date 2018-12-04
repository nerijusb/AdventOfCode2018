import java.util.ArrayList;
import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2018/day/2
 *
 * @author Nerijus
 */
public class Day2_2 {

    public static void main(String[] args) {
        System.out.println("Common letters: " + findCommonLetters(Inputs.readStrings("Day2")));
    }

    private static String findCommonLetters(List<String> boxIds) {
        for (String boxId : boxIds) {
            List<String> toCheckAgainst = new ArrayList<>(boxIds);
            toCheckAgainst.remove(boxId);
            for (String toCompareWith : toCheckAgainst) {
                if (differsByOne(boxId, toCompareWith)) {
                    return extractResult(boxId, toCompareWith);
                }
            }
        }
        return "not found";
    }

    private static boolean differsByOne(String boxId, String toCompareWith) {
        byte diffs = 0;
        for (int i = 0; i < boxId.length(); i++) {
            if (boxId.charAt(i) != toCompareWith.charAt(i)) {
                diffs++;
            }
        }
        return diffs == 1;
    }

    private static String extractResult(String boxId, String toCompareWith) {
        System.out.println("First : " + boxId);
        System.out.println("Second: " + toCompareWith);

        String result = "";
        for (int i = 0; i < boxId.length(); i++) {
            if (boxId.charAt(i) == toCompareWith.charAt(i)) {
                result = result + boxId.charAt(i);
            }
        }
        return result;
    }
}