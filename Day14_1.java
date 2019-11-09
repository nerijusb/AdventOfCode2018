import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Part one of
 * https://adventofcode.com/2018/day/14
 *
 * @author Nerijus
 */
public class Day14_1 {

    public static void main(String[] args) {
        System.out.println("Scores of the ten recipes: "
                + new Day14_1().getLastTenScoresAfter(
                        Inputs.readInt("Day14")));
    }

    private String getLastTenScoresAfter(int recipeCount) {
        List<Integer> scores = new ArrayList<>();
        int firstElvenIndex = 0;
        int firstElvenScore = 3;
        int secondElvenIndex = 1;
        int secondElvenScore = 7;

        scores.add(firstElvenScore);
        scores.add(secondElvenScore);

        while (scores.size() < recipeCount + 10) {
            scores.addAll(getNewRecipes(firstElvenScore, secondElvenScore));

            firstElvenIndex = (firstElvenIndex + firstElvenScore + 1) % scores.size();
            firstElvenScore = scores.get(firstElvenIndex);
            secondElvenIndex = (secondElvenIndex + secondElvenScore + 1) % scores.size();
            secondElvenScore = scores.get(secondElvenIndex);
        }

        return getLastTenScores(scores);
    }

    private String getLastTenScores(List<Integer> scores) {
        StringBuilder lastTenScores = new StringBuilder();
        for (int i = scores.size() - 10; i < scores.size(); i++) {
            lastTenScores.append(scores.get(i));
        }
        return lastTenScores.toString();
    }

    private Collection<Integer> getNewRecipes(int firstElvenRecipe, int secondElvenRecipe) {
        int sum = firstElvenRecipe + secondElvenRecipe;

        return Arrays.stream(String.valueOf(sum).split(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}