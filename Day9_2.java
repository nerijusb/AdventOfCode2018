/**
 * Part two of
 * https://adventofcode.com/2018/day/9
 *
 * @author Nerijus
 */
public class Day9_2 extends Day9_1 {

    public static void main(String[] args) {
        System.out.println("Winning Elf's score: "
                + new Day9_2().getHighScore(NUM_OF_PLAYERS, LAST_MARBLE_WORTH * 100));
    }
}