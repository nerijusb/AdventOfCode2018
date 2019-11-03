import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Part one of
 * https://adventofcode.com/2018/day/13
 *
 * @author Nerijus
 */
public class Day13_1 {

    public static void main(String[] args) {
        System.out.println("First crash location: "
                + new Day13_1().findCrashLocation());
    }

    private String findCrashLocation() {
        RailNetwork railNetwork = readRailNetwork();
        System.out.println("Initial network:");
        railNetwork.print();

        try {
            int tick = 0;
            while (true) {
                System.out.println("Tick: " + ++tick);
                railNetwork.tick();
                railNetwork.print();
            }
        } catch (CartCrashException e) {
            RailNode crashLocation = e.location;
            return crashLocation.y + "," + crashLocation.x;
        }
    }

    /**
     * Reads rail network from file and forms it's structure. It's a two step process:
     * first - reads all nodes leaving some 'unknown', second - analyzes all 'unknown' nodes
     * and determines their types (based on their neighbors)
     *
     * @return rail network as array of rail nodes
     */
    private RailNetwork readRailNetwork() {
        RailNetwork railNetwork = readInitialNetwork();

        while (!railNetwork.isComplete()) {
            analyze(railNetwork);
        }

        return railNetwork;
    }

    private void analyze(RailNetwork railNetwork) {
        for (RailNode[] row : railNetwork.get()) {
            for (RailNode node : row) {
                analyzeNode(node, railNetwork);
            }
        }
    }

    private RailNetwork readInitialNetwork() {
        List<String> rows = Inputs.readStrings("Day13");
        int maxRowLength = rows.stream().mapToInt(r -> r.toCharArray().length).max().getAsInt();
        RailNode[][] nodes = new RailNode[rows.size()][maxRowLength];
        for (int x = 0; x < rows.size(); x++) {
            char[] rowParts = rows.get(x).toCharArray();
            for (int y = 0; y < maxRowLength; y++) {
                if (y >= rowParts.length) {
                    nodes[x][y] = buildNode(x, y, ' ');
                } else {
                    nodes[x][y] = buildNode(x, y, rowParts[y]);
                }
            }
        }
        return new RailNetwork(nodes);
    }

    private void analyzeNode(RailNode node, RailNetwork railNetwork) {
        if (!node.isUnknown()) {
            return;
        }

        if (node.cart != null) {
            if (node.cart.direction == Direction.LEFT || node.cart.direction == Direction.RIGHT) {
                node.type = RailNodeType.HORIZONTAL;
            } else {
                node.type = RailNodeType.VERTICAL;
            }
        } else if (node.source == '/') {
            RailNode nodeDown = railNetwork.get(node.x + 1, node.y);
            RailNode nodeUp = railNetwork.get(node.x - 1, node.y);
            if (nodeDown.is(RailNodeType.VERTICAL, RailNodeType.INTERSECTION) || nodeUp.is(RailNodeType.EMPTY)) {
                node.type = RailNodeType.BEND_TOP_LEFT;
            } else {
                node.type = RailNodeType.BEND_BOTTOM_RIGHT;
            }
        } else if (node.source == '\\') {
            RailNode nodeUp = railNetwork.get(node.x - 1, node.y);
            if (nodeUp.is(RailNodeType.VERTICAL, RailNodeType.INTERSECTION)) {
                node.type = RailNodeType.BEND_BOTTOM_LEFT;
            } else {
                node.type = RailNodeType.BEND_TOP_RIGHT;
            }
        }
    }

    private RailNode buildNode(int x, int y, char rail) {
        RailNode node = new RailNode(rail, x, y);
        switch (rail) {
            case ' ':
                node.type = RailNodeType.EMPTY;
                break;
            case '-':
                node.type = RailNodeType.HORIZONTAL;
                break;
            case '|':
                node.type = RailNodeType.VERTICAL;
                break;
            case '+':
                node.type = RailNodeType.INTERSECTION;
                break;
            case '/':
                // could be top left or bottom right, needs context
                break;
            case '\\':
                // could be bottom left or top right, needs context
                break;
            case '>':
                // with cart, needs context
                node.cart = new Cart(Direction.RIGHT);
                break;
            case '<':
                // with cart, needs context
                node.cart = new Cart(Direction.LEFT);
                break;
            case 'v':
                // with cart, needs context
                node.cart = new Cart(Direction.DOWN);
                break;
            case '^':
                // with cart, needs context
                node.cart = new Cart(Direction.UP);
                break;
            default:
                throw new IllegalStateException("Unexpected rail node: " + rail);
        }
        return node;
    }

    enum Direction {
        UP("^"),
        DOWN("v"),
        LEFT("<"),
        RIGHT(">");

        private String arrow;

        Direction(String arrow) {
            this.arrow = arrow;
        }

        Direction turn(Turn turn) {
            switch (turn) {
                case LEFT:
                    switch (this) {
                        case UP:
                            return Direction.LEFT;
                        case DOWN:
                            return Direction.RIGHT;
                        case LEFT:
                            return Direction.DOWN;
                        case RIGHT:
                            return Direction.UP;
                    }
                case STRAIGHT:
                    // direction does not change
                    return this;
                case RIGHT:
                    switch (this) {
                        case UP:
                            return Direction.RIGHT;
                        case DOWN:
                            return Direction.LEFT;
                        case LEFT:
                            return Direction.UP;
                        case RIGHT:
                            return Direction.DOWN;
                    }
                default:
                    throw new IllegalStateException("Unknown turn: " + turn);
            }
        }

        @Override
        public String toString() {
            return arrow;
        }
    }

    enum Turn {
        LEFT,
        STRAIGHT,
        RIGHT;

        Turn next() {
            int nextTurn = this.ordinal() + 1;
            if (nextTurn == 3) {
                nextTurn = 0;
            }
            return values()[nextTurn];
        }
    }

    static class Cart {
        private Direction direction;
        private Turn nextTurn = Turn.LEFT;

        Cart(Direction direction) {
            this.direction = direction;
        }
    }

    public enum RailNodeType {
        UNKNOWN("?", cart -> cart.direction),
        EMPTY(" ", cart -> cart.direction),
        INTERSECTION("+", cart -> {
            Direction nextDirection = cart.direction.turn(cart.nextTurn);
            // update next turn
            cart.nextTurn = cart.nextTurn.next();
            return nextDirection;
        }),
        BEND_TOP_LEFT("l", cart -> cart.direction == Direction.UP ? Direction.RIGHT : Direction.DOWN),
        BEND_TOP_RIGHT("r", cart -> cart.direction == Direction.UP ? Direction.LEFT : Direction.DOWN),
        BEND_BOTTOM_LEFT("\\", cart -> cart.direction == Direction.DOWN ? Direction.RIGHT : Direction.UP),
        BEND_BOTTOM_RIGHT("/", cart ->  cart.direction == Direction.DOWN ? Direction.LEFT : Direction.UP),
        VERTICAL("|", cart -> cart.direction),
        HORIZONTAL("-", cart -> cart.direction);

        private String display;
        private Function<Cart, Direction> directionChange;

        RailNodeType(String display, Function<Cart, Direction> directionChange) {
            this.display = display;
            this.directionChange = directionChange;
        }

        @Override
        public String toString() {
            return display;
        }
    }

    static class RailNode {
        RailNodeType type = RailNodeType.UNKNOWN;
        char source;
        int x;
        int y;
        Cart cart; // cart on rail, null if rail is empty

        RailNode(char source, int x, int y) {
            this.source = source;
            this.x = x;
            this.y = y;
        }

        static RailNode empty(int x, int y) {
            RailNode empty = new RailNode(' ', x, y);
            empty.type = RailNodeType.EMPTY;
            return empty;
        }

        boolean is(RailNodeType... types) {
            for (RailNodeType typeToCheck : types) {
                if (type == typeToCheck) {
                    return true;
                }
            }
            return false;
        }

        boolean isUnknown() {
            return type == RailNodeType.UNKNOWN;
        }

        Direction determineDirectionChange(Cart cart) {
            return type.directionChange.apply(cart);
        }

        @Override
        public String toString() {
            if (isUnknown()) {
                return "?";
            }
            return cart != null? cart.direction.arrow : "" + type.display;
        }
    }

    /**
     * Rail network represented as a grid of individual nodes
     */
    static class RailNetwork {
        private RailNode[][] nodes;

        RailNetwork(RailNode[][] nodes) {
            this.nodes = nodes;
        }

        RailNode[][] get() {
            return nodes;
        }

        RailNode get(int x, int y) {
            if (x < 0 || x >= nodes.length) {
                return RailNode.empty(x, y);
            }
            if (y < 0 || y >= nodes[0].length) {
                return RailNode.empty(x, y);
            }
            return nodes[x][y];
        }

        RailNode get(int x, int y, Direction direction) {
            switch (direction) {
                case UP:
                    return nodes[x - 1][y];
                case DOWN:
                    return nodes[x + 1][y];
                case LEFT:
                    return nodes[x][y - 1];
                case RIGHT:
                    return nodes[x][y + 1];
                default:
                    throw new IllegalStateException("Unknown direction: " + direction);
            }
        }

        boolean isComplete() {
            for (RailNode[] row : nodes) {
                for (RailNode n : row) {
                    if (n.isUnknown()) {
                        return false;
                    }
                }
            }
            return true;
        }

        void print() {
            for (RailNode[] row : nodes) {
                for (RailNode n : row) {
                    System.out.print(n != null? n.toString() : ' ');
                }
                System.out.println();
            }
        }

        void tick() {
            List<RailNode> nodesToMove = new ArrayList<>();
            for (RailNode[] row : nodes) {
                for (RailNode node : row) {
                    if (node.cart != null) {
                        nodesToMove.add(node);
                    }
                }
            }
            nodesToMove.forEach(this::move);
        }

        private void move(RailNode currentNode) {
            Cart cart = currentNode.cart;
            RailNode nextNode = get(currentNode.x, currentNode.y, cart.direction);
            if (nextNode.is(RailNodeType.EMPTY, RailNodeType.UNKNOWN)) {
                throw new IllegalStateException("Wrong next node");
            }
            if (nextNode.cart != null) {
                // cart is already in the next node, this is what we are looking for
                throw new CartCrashException(nextNode);
            }

            if (!nextNode.is(RailNodeType.VERTICAL, RailNodeType.HORIZONTAL)) {
                // only horizontal and vertical movement does not change direction
                cart.direction = nextNode.determineDirectionChange(cart);
            }

            currentNode.cart = null;
            nextNode.cart = cart;
        }
    }

    static class CartCrashException extends RuntimeException {
        private RailNode location;

        CartCrashException(RailNode location) {
            this.location = location;
        }
    }
}