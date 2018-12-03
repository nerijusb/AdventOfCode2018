import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

/**
 * Helps reading inputs from files
 *
 * @author Nerijus
 */
public final class Inputs {
    public static String read(String fileName) {
        try (InputStream input = Day1_1.class.getResourceAsStream("inputs/" + fileName)) {
            return new Scanner(input, "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static List<Integer> readInts(String fileName) {
        return Arrays.stream(read(fileName).split("\r\n"))
                .map(Integer::valueOf)
                .collect(toList());
    }
}
