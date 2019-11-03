/**
 * Part two of
 * https://adventofcode.com/2018/day/13
 *
 * @author Nerijus
 */
public class Day13_2 extends Day13_1 {

    public static void main(String[] args) {
        System.out.println("Location of the last cart: "
                + new Day13_2().findLastCartLocation());
    }

    private String findLastCartLocation() {
        RailNetwork railNetwork = readRailNetwork();
        CartCrashCallback cartCrashCallback = (currentNode, nextNode, network) -> {
            // remove crashed carts
            currentNode.cart = null;
            nextNode.cart = null;
        };
        int tick = 0;
        while (railNetwork.cartCount() > 1) {
            System.out.println("Tick: " + ++tick);
            System.out.println("Cart count: " + railNetwork.cartCount());
            railNetwork.tick(cartCrashCallback);
            //railNetwork.print();
        }

        System.out.println("Cart count: " + railNetwork.cartCount());
        RailNode railNode = railNetwork.getNodesWithCarts().get(0);
        return railNode.y + "," + railNode.x;
    }
}