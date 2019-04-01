import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/**
 * Part one of
 * https://adventofcode.com/2018/day/3
 *
 * @author Nerijus
 */
public class Day03_1 {

    private static final Pattern CLAIM_PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");

    private static final String FILLED = "#";
    static final String OVERLAP = "X";

    public static void main(String[] args) {
        System.out.println("Overlapping area: " + new Day03_1().findOverlappingArea());
    }

    List<Claim> getClaims() {
        return Inputs.readStrings("Day03")
                .stream()
                .map(Claim::new)
                .collect(toList());
    }

    String[][] createFullArea(List<Claim> claims) {
        int maxHeight = findMaxHeight(claims);
        int maxWidth = findMaxWidth(claims);
        System.out.println("Area size " + maxWidth + "x" + maxHeight);
        return new String[maxHeight][maxWidth];
    }

    long count(String[][] fullArea, String toCount) {
        return Arrays.stream(fullArea)
                .flatMap(Arrays::stream)
                .filter(toCount::equals)
                .count();
    }

    private long findOverlappingArea() {
        List<Claim> claims = getClaims();
        String[][] fullArea = createFullArea(claims);
        claims.forEach(claim -> fill(fullArea, claim));
        print(fullArea);
        return count(fullArea, OVERLAP);
    }

    private void fill(String[][] fullArea, Claim claim) {
        for (int i = claim.top; i < claim.top + claim.height; i++) {
            for (int j = claim.left; j < claim.left + claim.width; j++) {
                fullArea[i][j] = fullArea[i][j] == null ? FILLED : OVERLAP;
            }
        }
    }

    private int findMaxHeight(List<Claim> claims) {
        return claims.stream()
                .mapToInt(claim -> claim.top + claim.height)
                .max()
                .orElseThrow(() -> new IllegalStateException("Could not determine max height"));
    }

    private int findMaxWidth(List<Claim> claims) {
        return claims.stream()
                .mapToInt(claim -> claim.left + claim.width)
                .max()
                .orElseThrow(() -> new IllegalStateException("Could not determine max width"));
    }

    private void print(String[][] fullArea) {
        for (String[] row : fullArea) {
            System.out.println();
            for (String cell : row) {
                System.out.print(cell == null ? '.' : cell);
            }
        }
    }

    public static class Claim {
        String id;
        int left;
        int top;
        int width;
        int height;

        Claim(String claimString) {
            Matcher matcher = CLAIM_PATTERN.matcher(claimString);
            if (!matcher.find()) {
                throw new IllegalStateException("Input does not match expected pattern: " + claimString);
            }

            this.id = matcher.group(1);
            this.left = Integer.valueOf(matcher.group(2));
            this.top = Integer.valueOf(matcher.group(3));
            this.width = Integer.valueOf(matcher.group(4));
            this.height = Integer.valueOf(matcher.group(5));
        }

        int getSize() {
            return width * height;
        }

        @Override
        public String toString() {
            return "#" + id + " @ " + left + "," + top + ": " + width + "x" + height;
        }
    }
}