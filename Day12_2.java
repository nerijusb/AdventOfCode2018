import java.math.BigDecimal;

/**
 * Part two of
 * https://adventofcode.com/2018/day/12
 *
 * @author Nerijus
 */
public class Day12_2 extends Day12_1 {

    /**
     * From the results from previous puzzle, it can be seen that on #158 iteration it starts to form a clean pattern
     * and sum increases by +86 continuously. From this we can calculate what value we will have on 50B-th iteration
     *
     * 156: sum: 15589 .......###.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.#...#.#......
     * 157: sum: 15795 ......##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.#...#.#......
     * 158: sum: 16002 .....#.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.#...#.#......
     * 159: sum: 16088 ......#.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.#...#.#......
     * 160: sum: 16174 .......#.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.##.#...#.#......
     */
    public static void main(String[] args) {
        System.out.println("Sum of numbers of all pots with plant after 50B iterations: "
                + new Day12_2().calculate(50000000000L));
    }

    private BigDecimal calculate(long iteration) {
        // n >= 158, (n - 158) * 86 + 16002
        return new BigDecimal(iteration)
                .subtract(new BigDecimal(158))
                .multiply(new BigDecimal(86))
                .add(new BigDecimal(16002));
    }

}