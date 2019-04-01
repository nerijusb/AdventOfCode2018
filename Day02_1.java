import java.util.*;

/**
 * Part one of
 * https://adventofcode.com/2018/day/2
 *
 * @author Nerijus
 */
public class Day02_1 {

    public static void main(String[] args) {
        System.out.println("Checksum: " + calculateChecksum(Inputs.readStrings("Day02")));
    }

    private static int calculateChecksum(List<String> boxIds) {
        return countWithTwoChars(boxIds) * countWithThreeChars(boxIds);
    }

    private static int countWithTwoChars(List<String> boxIds) {
        return boxIds.stream().mapToInt(boxId -> hasSameChars(boxId, 2)? 1 : 0).sum();
    }

    private static int countWithThreeChars(List<String> boxIds) {
        return boxIds.stream().mapToInt(boxId -> hasSameChars(boxId, 3)? 1 : 0).sum();
    }

    private static boolean hasSameChars(String boxId, int charCount) {
        Map<Character, Integer> counts = new HashMap<>();
        for (int i = 0; i < boxId.length(); i++) {
            char letter = boxId.charAt(i);
            counts.putIfAbsent(letter, 0);
            counts.put(letter, counts.get(letter)+1);
        }

        return counts.values().stream().anyMatch(c -> c == charCount);
    }
}