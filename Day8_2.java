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
        return ((NodeEx)readTree()).getNodeValue();
    }

    @Override
    protected Node createNode() {
        return new NodeEx();
    }

    class NodeEx extends Node {
        Integer getNodeValue() {
            if (children.isEmpty()) {
                return metadata.stream().mapToInt(i -> i).sum();
            } else {
                return metadata
                        .stream()
                        .mapToInt(i -> {
                            if (i == 0) {
                                return 0;
                            }
                            if (i > childCount) {
                                return 0;
                            }
                            return ((NodeEx) children.get(i - 1)).getNodeValue();
                        }).sum();
            }
        }
    }
}