import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Part one of
 * https://adventofcode.com/2018/day/8
 *
 * @author Nerijus
 */
public class Day8_1 {

    public static void main(String[] args) {
        System.out.println("Sum of all metadata entries: " + new Day8_1().getSumOfMetadataEntries());
    }

    Node readTree() {
        return readNode(new LinkedList<>(Inputs.readIntsLine("Day8")));
    }

    private Integer getSumOfMetadataEntries() {
        return readTree()
                .getAllMetadata()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    private Node readNode(LinkedList<Integer> input) {
        Node node = createNode();
        node.childCount = input.pop();
        node.metadataCount = input.pop();
        for (int i = 0; i < node.childCount; i++) {
            node.children.add(readNode(input));
        }
        for (int i = 0; i < node.metadataCount; i++) {
            node.metadata.add(input.pop());
        }
        return node;
    }

    protected Node createNode() {
        return new Node();
    }

    static class Node {
        int childCount;
        int metadataCount;

        List<Integer> metadata = new ArrayList<>();
        List<Node> children = new ArrayList<>();


        List<Integer> getAllMetadata() {
            List<Integer> allMetadata = new ArrayList<>();
            collectMetadata(allMetadata);
            return allMetadata;
        }

        void collectMetadata(List<Integer> collectTo) {
            collectTo.addAll(metadata);
            children.forEach(c -> c.collectMetadata(collectTo));
        }
    }
}