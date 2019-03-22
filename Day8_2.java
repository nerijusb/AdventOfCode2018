/**
 * Part two of
 * https://adventofcode.com/2018/day/8
 *
 * @author Nerijus
 */
public class Day8_2 extends Day8_1 {

    public static void main(String[] args) {
        System.out.println("Value of root node: " + new Day8_2().getValueOfRootNode());
    }

    private Integer getValueOfRootNode() {
        return readTree().getNodeValue();
    }
}