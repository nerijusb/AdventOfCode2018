import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Part one of
 * https://adventofcode.com/2018/day/7
 *
 * @author Nerijus
 */
public class Day7_1 {

    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("Step (\\w) must be finished before step (\\w) can begin\\.");

    public static void main(String[] args) {
        System.out.println("Step order: " + new Day7_1().getStepOrder());
    }

    private String getStepOrder() {
        // building dependency map
        Map<Character, List<Character>> dependencyMap = getDependencyMap();

        List<Character> doneParts = new ArrayList<>();
        List<Character> nextCandidates = getNextCandidates(dependencyMap, doneParts);
        while (!nextCandidates.isEmpty()) {
            doneParts.add(nextCandidates.get(0));
            nextCandidates = getNextCandidates(dependencyMap, doneParts);
        }

        return doneParts.stream().map(Object::toString).collect(Collectors.joining());
    }

    Map<Character, List<Character>> getDependencyMap() {
        Map<Character, List<Character>> dependencyMap = new HashMap<>();
        getInstructions().forEach(i -> {
            dependencyMap.computeIfAbsent(i.to, k -> new ArrayList<>()).add(i.from);
            dependencyMap.computeIfAbsent(i.from, k -> new ArrayList<>());
        });
        return dependencyMap;
    }

    List<Character> getNextCandidates(Map<Character, List<Character>> dependencyMap, List<Character> doneParts) {
        return dependencyMap.entrySet()
                .stream()
                // next could be either without dependencies or one will all dependencies already done
                .filter(e -> e.getValue().isEmpty() || doneParts.containsAll(e.getValue()))
                .map(Map.Entry::getKey)
                // filter already done parts
                .filter(ch -> !doneParts.contains(ch))
                .sorted()
                .collect(toList());
    }


    private List<StepInstruction> getInstructions() {
        return Inputs.readStrings("Day7")
                .stream()
                .map(StepInstruction::new)
                .collect(toList());
    }

    static class StepInstruction {
        char from;
        char to;

        StepInstruction(String from) {
            Matcher matcher = INSTRUCTION_PATTERN.matcher(from);
            matcher.find();
            this.from = matcher.group(1).charAt(0);
            this.to = matcher.group(2).charAt(0);
        }

        @Override
        public String toString() {
            return from + " -> " + to;
        }
    }
}