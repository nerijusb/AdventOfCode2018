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
        int recipeCount = Inputs.readInt("Day14");
        System.out.println("Scores of the ten recipes: "
                + new Day14_1()
                    .getScores(
                        scores -> scores.size() < recipeCount + 10));
    }

    String getScores(RecipeCallback callback) {
        List<Integer> scores = new ArrayList<>();
        int firstElvenIndex = 0;
        int firstElvenScore = 3;
        int secondElvenIndex = 1;
        int secondElvenScore = 7;

        scores.add(firstElvenScore);
        scores.add(secondElvenScore);

        while (true) {
            Collection<Integer> newRecipes = getNewRecipes(firstElvenScore, secondElvenScore);
            for (Integer newRecipe : newRecipes) {
                scores.add(newRecipe);
                if (!callback.onScoresChanged(scores)) {
                    return getLastTenScores(scores);
                }
            }

            firstElvenIndex = (firstElvenIndex + firstElvenScore + 1) % scores.size();
            firstElvenScore = scores.get(firstElvenIndex);
            secondElvenIndex = (secondElvenIndex + secondElvenScore + 1) % scores.size();
            secondElvenScore = scores.get(secondElvenIndex);
        }
    }

    private Collection<Integer> getNewRecipes(int firstElvenRecipe, int secondElvenRecipe) {
        int sum = firstElvenRecipe + secondElvenRecipe;

        return Arrays.stream(String.valueOf(sum).split(""))
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    static String getLastTenScores(List<Integer> scores) {
        StringBuilder lastTenScores = new StringBuilder();
        for (int i = scores.size() - 10; i < scores.size(); i++) {
            lastTenScores.append(scores.get(i));
        }
        return lastTenScores.toString();
    }

    interface RecipeCallback {
        /**
         * Callback invoked on every iteration when calculating scores
         *
         * @param scores current scores
         * @return boolean whether to continue or not
         */
        boolean onScoresChanged(List<Integer> scores);
    }
}