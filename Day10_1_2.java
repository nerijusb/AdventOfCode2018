import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Part one and two of
 * https://adventofcode.com/2018/day/10
 *
 * @author Nerijus
 */
public class Day10_1_2 {

    private static final Pattern POINT_PATTERN = Pattern.compile(
            "position=<( +)?(?<positionX>(-)?\\d+), ( +)?(?<positionY>(-)?\\d+)> velocity=<( +)?(?<velocityX>(-)?\\d+), ( +)?(?<velocityY>(-)?\\d+)>");

    public static void main(String[] args) {
        List<Point> movingPoints = Inputs.readStrings("Day10")
                .stream()
                .map(Point::new)
                .collect(Collectors.toList());

        // Lets assume that all these points move closer to each other in order to form a message.
        // This would mean that points are slowly collapsing, until some point where they start drifting
        // away. The moment they are closest to each other we should see that message. Or somewhere
        // close around that time.

        // looking for smallest frame size
        Frame frame = new Frame(movingPoints);
        Long currentAreaSize = frame.getArea();
        Long nextAreaSize = frame.forward().getArea();
        int second = 1;
        while (nextAreaSize < currentAreaSize) {
            currentAreaSize = nextAreaSize;
            nextAreaSize = frame.forward().getArea();
            second++;
        }

        // found smallest frame (one before current), print it
        render(frame.backward());
        System.out.println(String.format("After %d seconds", second - 1));
    }

    private static void render(Frame frame) {
        int longestSide = Math.max(frame.getHeight() + 1, frame.getWidth() + 1);
        String[][] image = new String[longestSide][longestSide];

        // these will be the top left corner (0,0)
        int minX = frame.getMinX();
        int maxY = frame.getMaxY();

        // fill with values
        frame.points.forEach(p -> image[p.x - minX][Math.abs(p.y - maxY)] = "#");

        // print whole image
        for (int i = 0; i < longestSide; i++) {
            for (int j = 0; j < longestSide; j++) {
                System.out.print(image[i][j] == null? ' ': '#');
            }
            System.out.print("\n");
        }
    }

    /**
     *      neg |
     *          |
     *          |
     * ---------|----------> (x)
     * neg      |        pos
     *          |
     *      pos v (y)
     *
     */
    static class Point {
        private Integer x;
        private Integer y;
        private Integer velocityX;
        private Integer velocityY;

        Point(String info) {
            Matcher matcher = POINT_PATTERN.matcher(info);
            if (!matcher.matches()) {
                throw new IllegalStateException("Could not match input: " + info);
            }

            this.x = Integer.valueOf(matcher.group("positionX"));
            this.y = Integer.valueOf(matcher.group("positionY"));
            this.velocityX = Integer.valueOf(matcher.group("velocityX"));
            this.velocityY = Integer.valueOf(matcher.group("velocityY"));
        }

        void forward() {
            this.x = this.x + velocityX;
            this.y = this.y + velocityY;
        }

        void backward() {
            this.x = this.x - velocityX;
            this.y = this.y - velocityY;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x + ", y=" + y +
                    ", velocityX=" + velocityX +
                    ", velocityY=" + velocityY +
                    '}';
        }
    }

    static class Frame {
        List<Point> points;

        Frame(List<Point> points) {
            this.points = points;
        }

        long getArea() {
            return (long) getWidth() * (long) getHeight();
        }

        private int getWidth() {
            return getMaxX() - getMinX();
        }

        private int getHeight() {
            return getMaxY() - getMinY();
        }

        private int getMaxX() {
            return points.stream().mapToInt(p -> p.x).max().orElseThrow(() -> new IllegalStateException("Max X not found"));
        }

        private int getMinX() {
            return points.stream().mapToInt(p -> p.x).min().orElseThrow(() -> new IllegalStateException("Min X not found"));
        }

        private int getMaxY() {
            return points.stream().mapToInt(p -> p.y).max().orElseThrow(() -> new IllegalStateException("Max Y not found"));
        }

        private int getMinY() {
            return points.stream().mapToInt(p -> p.y).min().orElseThrow(() -> new IllegalStateException("Min Y not found"));
        }

        Frame forward() {
            points.forEach(Point::forward);
            return this;
        }

        Frame backward() {
            points.forEach(Point::backward);
            return this;
        }
    }
}