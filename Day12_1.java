import java.util.ArrayList;
import java.util.List;

/**
 * Part one of
 * https://adventofcode.com/2018/day/12
 *
 * @author Nerijus
 */
public class Day12_1 {

    private static final int GENERATIONS = 20;
    private static final String EXTRA_START = ".....";

    public static void main(String[] args) {
        System.out.println("Sum of numbers of all pots with plant: " + new Day12_1().getSumOfPlantPotNumbers(GENERATIONS));
    }

    private int getSumOfPlantPotNumbers(int generations) {
        List<String> input = Inputs.readStrings("Day12");

        String state = readInitialState(input.get(0));
        List<Rule> rules = readRules(input);

        System.out.println(String.format("%3d: sum: %5d ", 0, sumPotNumbersWithPlants(state)) + state);
        for (int i = 1; i <= generations; i++) {
            StringBuilder newState = new StringBuilder("..");
            char[] pots = state.toCharArray();
            for (int j = 2; j < pots.length - 2; j++) {
                newState.append(
                        applyRule("" + pots[j - 2] + pots[j - 1] + pots[j] + pots[j + 1] + pots[j + 2], rules));
            }
            state = newState.toString() + "...";
            System.out.println(String.format("%3d: sum: %5d ", i, sumPotNumbersWithPlants(state)) + state);
        }

        return sumPotNumbersWithPlants(state);
    }

    private String applyRule(String potCombination, List<Rule> rules) {
        for (Rule rule : rules) {
            if (rule.isApplicable(potCombination)) {
                return rule.getOutcome();
            }
        }
        return ".";
    }

    private int sumPotNumbersWithPlants(String state) {
        int index = EXTRA_START.length() * -1;
        int sum = 0;
        char[] pots = state.toCharArray();
        for (int i = 0; i < pots.length; i++) {
            if (Character.valueOf('#').equals(pots[i])) {
                sum += index;
            }
            index++;
        }
        return sum;
    }

    private String readInitialState(String line) {
        return EXTRA_START + line.replace("initial state: ", "") + "...";
    }

    private List<Rule> readRules(List<String> input) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 2; i < input.size(); i++) {
            rules.add(new Rule(input.get(i)));
        }
        return rules;
    }

    static class Rule {

        private String rule;
        private String outcome;

        Rule(String source) {
            String[] parts = source.split(" => ");
            this.rule = parts[0];
            this.outcome = parts[1];
        }

        boolean isApplicable(String pots) {
            return pots.equals(rule);
        }

        String getOutcome() {
            return outcome;
        }

        @Override
        public String toString() {
            return "Rule{" +
                    "rule=" + rule +
                    ", outcome=" + outcome +
                    '}';
        }
    }
}