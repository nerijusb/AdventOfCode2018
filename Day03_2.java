import java.util.List;

/**
 * Part two of
 * https://adventofcode.com/2018/day/3
 *
 * @author Nerijus
 */
public class Day03_2 extends Day03_1 {

    public static void main(String[] args) {
        System.out.println("Doesn't overlap: #" + new Day03_2().findNonOverlappingClaimId());
    }

    private String findNonOverlappingClaimId() {
        List<Claim> claims = getClaims();
        String[][] fullArea = createAndFill(claims);
        return claims.stream()
                .filter(c -> count(fullArea, c.id) == c.getSize())
                .map(c -> c.id)
                .findFirst()
                .orElse("(not found)");
    }

    private String[][] createAndFill(List<Claim> claims) {
        String[][] fullArea = createFullArea(claims);
        claims.forEach(claim -> fill(fullArea, claim));
        return fullArea;
    }

    private void fill(String[][] fullArea, Claim claim) {
        for (int i = claim.top; i < claim.top + claim.height; i++) {
            for (int j = claim.left; j < claim.left + claim.width; j++) {
                fullArea[i][j] = fullArea[i][j] == null ? claim.id : OVERLAP;
            }
        }
    }
}