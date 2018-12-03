import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Part two of
 * https://adventofcode.com/2018/day/1
 *
 * @author Nerijus
 */
public class Day1_2 {

    public static void main(String[] args) {
        System.out.println("First reached twice: " + findReachedTwice(Inputs.readInts("Day1")));
    }

    private static int findReachedTwice(List<Integer> input) {
        Integer frequency = 0; // initial
        Set<Integer> seenFrequencies = new HashSet<>();
        seenFrequencies.add(frequency);

        System.out.println("Frequency: " + frequency);
        LinkedList<Integer> frequencyChanges = new LinkedList<>(input);
        do {
            Integer nextChange = frequencyChanges.pop();
            System.out.println("(" + nextChange + ")");

            frequency = frequency + nextChange;
            System.out.println("Next frequency: " + frequency);

            if (seenFrequencies.contains(frequency)) {
                return frequency; // found it
            }
            seenFrequencies.add(frequency);
            frequencyChanges.addLast(nextChange); // making sure it loops
        } while (true);
    }
}