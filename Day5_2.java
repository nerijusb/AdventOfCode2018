/**
 * Part two of
 * https://adventofcode.com/2018/day/5
 *
 * @author Nerijus
 */
public class Day5_2 extends Day5_1 {

    public static void main(String[] args) {
        System.out.println("Shortest possible reacted polymer size: " + new Day5_2().getShortestPossiblePolymerSize());
    }

    private int getShortestPossiblePolymerSize() {
        String initialPolymer = Inputs.readString("Day5");
        int shortestLength = initialPolymer.length();

        for (char i = 'a'; i <= 'z'; i++) {
            String shortenedPolymer = removeUnit(initialPolymer, i);
            int length = reactPolymer(shortenedPolymer).length();
            if (length < shortestLength) {
                shortestLength = length;
            }
        }

        return shortestLength;
    }

    private String removeUnit(String polymer, char unit) {
        return polymer
                .replace(Character.toString(unit), "")
                .replace(Character.toString(unit).toUpperCase(), "");
    }
}