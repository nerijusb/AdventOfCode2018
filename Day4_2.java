import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

/**
 * Part two of
 * https://adventofcode.com/2018/day/4
 *
 * @author Nerijus
 */
public class Day4_2 extends Day4_1 {

    public static void main(String[] args) {
        System.out.println("Guard result (id x minute): " + new Day4_2().getGuardResult());
    }

    private int getGuardResult() {
        List<Day4_1.SleepyTime> naps = getAllNaps();

        Map<Integer, Map.Entry<Integer, Integer>> mostMinuteByGuard = naps.stream()
                .collect(groupingBy(SleepyTime::getGuardId, toList()))
                .entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> getMostFrequentMinute(e.getValue())));

        Map.Entry<Integer, Map.Entry<Integer, Integer>> toFind = mostMinuteByGuard.entrySet().iterator().next();
        for (Map.Entry<Integer, Map.Entry<Integer, Integer>> entry : mostMinuteByGuard.entrySet()) {
            Integer count = entry.getValue().getValue();
            if (count > toFind.getValue().getValue()) {
                toFind = entry;
            }
        }

        Integer guardId = toFind.getKey();
        System.out.println("Guard id: #" + guardId);

        Integer mostSleptMinute = toFind.getValue().getKey();
        System.out.println("Most slept minute: " + mostSleptMinute);

        return guardId * mostSleptMinute;
    }

    private Map.Entry<Integer, Integer> getMostFrequentMinute(List<SleepyTime> naps) {
        return naps
                .stream()
                .flatMap(SleepyTime::getMinutes)
                .collect(toMap(Function.identity(), v -> 1, Integer::sum))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Minute not found"));
    }
}