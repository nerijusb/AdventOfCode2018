import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Part two of
 * https://adventofcode.com/2018/day/7
 *
 * @author Nerijus
 */
public class Day07_2 extends Day07_1 {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        System.out.println("Assembly time in seconds: " + new Day07_2().getAssemblyTime());
    }

    private int getAssemblyTime() {
        int timer = 0;

        Map<Character, List<Character>> dependencyMap = getDependencyMap();

        List<Character> doneParts = new ArrayList<>();
        List<PartInProgress> partsInProgress = new ArrayList<>();

        while (true)
        {
            doWork(partsInProgress);
            collectDoneParts(partsInProgress, doneParts);
            occupyWorkers(partsInProgress, getNextCandidates(dependencyMap, doneParts));
            if (partsInProgress.isEmpty()) {
                // all done
                break;
            }
            timer = timer + 1;
        }

        return timer;
    }

    private void doWork(List<PartInProgress> partsInProgress) {
        partsInProgress.forEach(PartInProgress::tick);
    }

    private void collectDoneParts(List<PartInProgress> partsInProgress, List<Character> doneParts) {
        partsInProgress.forEach(p -> {
            if (p.isDone()) {
                // mark as done
                doneParts.add(p.letter);
            }
        });
        // remove from in progress
        partsInProgress.removeIf(p -> doneParts.contains(p.letter));
    }

    private void occupyWorkers(List<PartInProgress> partsInProgress, List<Character> candidates) {
        for (Character candidate : candidates) {
            if (partsInProgress.size() == 5) {
                // all workers occupied
                return;
            }
            if (partsInProgress.stream().noneMatch(p -> p.letter == candidate)) {
                // not started yet
                partsInProgress.add(new PartInProgress(candidate));
            }
        }
    }

    class PartInProgress {
        char letter;
        int timeRemaining;

        PartInProgress(char letter) {
            this.letter = letter;
            this.timeRemaining = 60 + LETTERS.indexOf(letter) + 1;
        }

        void tick() {
            timeRemaining = timeRemaining - 1;
        }

        boolean isDone() {
            return timeRemaining == 0;
        }
    }
}