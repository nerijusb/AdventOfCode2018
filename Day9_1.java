import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Part one of
 * https://adventofcode.com/2018/day/9
 *
 * @author Nerijus
 */
public class Day9_1 {

    static final int NUM_OF_PLAYERS = 413;
    static final int LAST_MARBLE_WORTH = 71082;

    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        System.out.println("Winning Elf's score: "
                + new Day9_1().getHighScore(NUM_OF_PLAYERS, LAST_MARBLE_WORTH));
        System.out.println("Calculated in: " + stopwatch.stop());
    }

    Long getHighScore(int playerCount, int lastMarble) {
        System.out.println(String.format("Players: %d, lastMarble: %s", playerCount, lastMarble));

        ElementCircle<Player> allPlayers = getPlayers(playerCount);

        play(allPlayers, lastMarble);

        return allPlayers.getElements()
                .stream()
                .mapToLong(e -> e.score)
                .max()
                .orElseThrow(() -> new IllegalStateException("No player with max score"));
    }

    private ElementCircle<Player> getPlayers(int players) {
        ElementCircle<Player> allPlayers = new ElementCircle<>();
        IntStream.range(1, players + 1).forEach(i -> allPlayers.add(new Player(i)));
        return allPlayers;
    }

    private void play(ElementCircle<Player> allPlayers, int lastMarble) {
        ElementCircle<Integer> allMarbles = new ElementCircle<>();
        allMarbles.insert(0);

        Player currentPlayer = allPlayers.current();
        int currentMarble = 1;
        while (currentMarble <= lastMarble) {
            if (currentMarble % 23 == 0) {
                System.out.println("Adding points, marble: " + currentMarble);
                currentPlayer.addPoints(currentMarble);
                Integer removed = allMarbles.goBack(7).remove();
                currentPlayer.addPoints(removed);
            } else {
                allMarbles.goForward(2);
                allMarbles.insert(currentMarble);
            }
            currentPlayer = allPlayers.next();
            currentMarble = currentMarble + 1;
        }
    }

    class Player {
        int id;
        Long score = 0L;

        Player(int id) {
            this.id = id;
        }

        void addPoints(int points) {
            this.score = this.score + points;
        }

        @Override
        public String toString() {
            return String.format("Elf #%d, score %d", id, score);
        }
    }

    static class ElementCircle<T> {
        private int currentIndex = 0;
        private List<T> elements = new ArrayList<>();

        void add(T e) {
            elements.add(e);

        }
        void insert(T e) {
            elements.add(currentIndex, e);
        }

        List<T> getElements() {
            return elements;
        }

        T current() {
            if (isEmpty()) {
                throw new IllegalStateException("No elements");
            }
            return elements.get(currentIndex);
        }

        T remove() {
            T current = current();
            elements.remove(current);
            return current;
        }

        T previous() {
            goBack();
            return current();
        }

        T next() {
            goForward();
            return current();
        }

        ElementCircle<T> goBack() {
            if (isEmpty()) {
                return this;
            }
            if (currentIndex == 0) {
                currentIndex = elements.size() - 1;
            } else {
                currentIndex = currentIndex - 1;
            }
            return this;
        }

        ElementCircle<T> goBack(int times) {
            for (int i = 0; i < times; i++) {
                goBack();
            }
            return this;
        }

        ElementCircle<T> goForward() {
            if (isEmpty()) {
                return this;
            }
            if (currentIndex == elements.size() - 1) {
                currentIndex = 0;
            } else {
                currentIndex = currentIndex + 1;
            }
            return this;
        }

        ElementCircle<T> goForward(int times) {
            for (int i = 0; i < times; i++) {
                goForward();
            }
            return this;
        }

        private boolean isEmpty() {
            return elements.size() == 0;
        }

        @Override
        public String toString() {
            return elements.stream().map(Object::toString).collect(Collectors.joining(" "));
        }
    }

    static class Stopwatch {
        private long startTime;

        void start() {
            startTime = new Date().getTime();
        }

        String stop() {
            return (new Date().getTime() - startTime) + "ms";
        }
    }
}