import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * Part one of
 * https://adventofcode.com/2018/day/5
 *
 * @author Nerijus
 */
public class Day5_1 {

    private static final Set<String> REACTIVE_PAIRS = generateReactivePairs();

    public static void main(String[] args) {
        System.out.println("Remaining polymer size: " + new Day5_1().getRemainingPolymerSize());
    }

    private int getRemainingPolymerSize() {
        String polymer = Inputs.readString("Day5");
        return reactPolymer(polymer).length();
    }

    String reactPolymer(String polymer) {
        int polymerSize;
        do {
            polymerSize = polymer.length();
            for (String reactivePair : REACTIVE_PAIRS) {
                polymer = polymer.replace(reactivePair, "");
            }
        } while (polymer.length() < polymerSize);

        return polymer;
    }

    private static Set<String> generateReactivePairs() {
        Set<String> reactivePairs = new HashSet<>();
        for (char i = 'a'; i <= 'z'; i++) {
            String pair = "" + i + Character.toString(i).toUpperCase();
            String pairReverse = new StringBuilder(pair).reverse().toString();
            reactivePairs.add(pair);
            reactivePairs.add(pairReverse);
        }
        return reactivePairs;
    }
}