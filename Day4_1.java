import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Part one of
 * https://adventofcode.com/2018/day/4
 *
 * @author Nerijus
 */
public class Day4_1 {

    public static void main(String[] args) {
        System.out.println("Guard result (id x minute): " + new Day4_1().getGuardResult());
    }

    private int getGuardResult() {
        List<SleepyTime> naps = getAllNaps();

        int guardId = getIdOfMostSleepyGuard(naps);
        System.out.println("Most sleepy guard: #" + guardId);

        int minute = getMinuteGuardWasAsleepTheMost(guardId, naps);
        System.out.println("Most sleepy minute for him: " + minute);

        return guardId * minute;
    }

    List<SleepyTime> getAllNaps() {
        List<Record> records = getRecords();
        Collections.sort(records); // chronological order
        return findSleepIntervals(new LinkedList<>(records));
    }

    private List<SleepyTime> findSleepIntervals(LinkedList<Record> allRecords) {
        List<SleepyTime> naps = new ArrayList<>();
        while (!allRecords.isEmpty())
        {
            // first record is shift start
            Record shiftStart = allRecords.pop();
            if (allRecords.peek() != null && allRecords.peek().isShiftStart()) {
                // guard did not sleep the entire shift
                continue;
            }

            // we can have multiple naps during the shift so we loop some more
            do {
                Record sleepStart = allRecords.pop();
                Record sleepEnd = allRecords.pop();

                SleepyTime sleep = new SleepyTime(shiftStart.getGuardId());
                sleep.minuteFrom = sleepStart.dateAndTime.getMinute();
                sleep.minuteTo = sleepEnd.dateAndTime.getMinute();
                naps.add(sleep);
            } while (allRecords.peek() != null && !allRecords.peek().isShiftStart());

        }
        return naps;
    }

    private int getIdOfMostSleepyGuard(List<SleepyTime> naps) {
        return naps.stream().collect(
                groupingBy(SleepyTime::getGuardId, toList()))
                .entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> e.getValue().stream().mapToInt(SleepyTime::getDuration).sum()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Most sleepy guard not found"))
                .getKey();
    }

    private int getMinuteGuardWasAsleepTheMost(int guardId, List<SleepyTime> naps) {
        return naps.stream()
                .filter(s -> s.guardId == guardId)
                .flatMap(SleepyTime::getMinutes)
                .collect(toMap(Function.identity(), v -> 1, Integer::sum))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow(() -> new IllegalStateException("Minute not found"))
                .getKey();
    }

    private List<Record> getRecords() {
        return Inputs.readStrings("Day4")
                .stream()
                .map(Record::new)
                .collect(toList());
    }


    enum RecordType { SHIFT_START, GUARD_AWAKE, GUARD_SLEEPS }

    public static class Record implements Comparable<Record> {

        String fullRecord;
        LocalDateTime dateAndTime;
        RecordType type;
        String message;

        Record(String recordString) {
            this.fullRecord = recordString;

            String[] recordParts = recordString.split("] ");
            String timestamp = recordParts[0].replace("[", "");

            this.dateAndTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            this.message = recordParts[1];
            this.type = message.startsWith("Guard #")?
                    RecordType.SHIFT_START : message.startsWith("wakes up")?
                    RecordType.GUARD_AWAKE : RecordType.GUARD_SLEEPS;
        }

        boolean isShiftStart() {
            return RecordType.SHIFT_START == type;
        }

        int getGuardId() {
            if (!isShiftStart()) {
                throw new IllegalStateException("Wrong type of record");
            }

            return Integer.valueOf(message
                    .replace("Guard #", "")
                    .replace(" begins shift", ""));
        }

        @Override
        public int compareTo(Record o) {
            return dateAndTime.compareTo(o.dateAndTime);
        }
    }

    static class SleepyTime {
        int guardId;
        int minuteFrom;
        int minuteTo;

        SleepyTime(int guardId) {
            this.guardId = guardId;
        }

        Stream<Integer> getMinutes() {
            return IntStream.range(minuteFrom, minuteTo).boxed();
        }

        int getGuardId() {
            return guardId;
        }

        int getDuration() {
            return minuteTo - minuteFrom;
        }
    }
}