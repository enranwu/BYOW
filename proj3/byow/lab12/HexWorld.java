package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /** Draw a row of tiles to the board, anchored at a given position. */
    public static void drawRow(TETile[][] tiles, Position p, TETile tile, int length) {
        for (int dx = 0; dx < length; dx++) {
            tiles[p.x + dx][p.y] = tile;
        }
    }

    /** Helper method for addHex. */
    public static void addHexHelper(TETile[][] tiles, Position p, TETile tile, int b, int t) {
        // Draw this row
        Position startOfRow = p.shift(b, 0);
        drawRow(tiles, startOfRow, tile, t);

        // Draw remaining row recursively
        if (b > 0) {
            Position nextP = p.shift(0, -1);
            addHexHelper(tiles, nextP, tile,b - 1, t + 2);
        }

        // Draw this row's reflection
        Position startOfReflecRow = startOfRow.shift(0, -(2*b + 1));
        drawRow(tiles, startOfReflecRow, tile, t);
    }

    /** Adds a hexagon to the world at position p of size SIZE. */
    public static void addHex(TETile[][] tiles, Position p, TETile t, int size) {
        if (size < 2) {
            return;
        }

        addHexHelper(tiles, p, t, size - 1, size);
    }

    /** Adds a column of N hexagons, each of whose tile are chosen randomly
     * to the world at position P. Each of the hexagons are of size SIZE.
     */
    public static void addHexColumn(TETile[][] tiles, Position p, int size, int n) {
        if (n < 1) {
            return ;
        }

        // Draw this hexagon
        addHex(tiles, p, randomTile(), size);

        // Draw n - 1 hexagons below it
        if (n > 1) {
            Position botNeighbor = getBotNeighbor(p, size);
            addHexColumn(tiles, botNeighbor, size, n - 1);
        }
    }

    /**
     * Fills the given 2D array of tiles with blank tiles.
     * @param tiles
     */
    public static void fillBoardWithNothing(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Picks a RANDOM tile. */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.SAND;
            case 4: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    /** Gets the position of the top right neighbor of a hexagon at Position P.
     * N is the size of the hexagons we are tessellating.
     */
    public static Position getTopRightNeighbor(Position p, int n) {
        return p.shift(2*n - 1, n);
    }

    /** Gets the position of the bottom right neighbor of a hexagon at Position P.
     * N is the size of the hexagons we are tessellating.
     */
    public static Position getBotRightNeighbor(Position p, int n) {
        return p.shift(2*n - 1, -n);
    }

    /** Gets the position of the bottom neighbor of a hexagon at Position P.
     * N is the size of the hexagons we are tessellating.
     */
    public static Position getBotNeighbor(Position p, int n) {
        return p.shift(0, -2*n);
    }


    // Private helper class to deal with position
    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shift(int dx, int dy) {
            return new Position(this.x + dx, this.y + dy);
        }
    }

    /** Draws the hexagonal world. */
    public static void drawWorld(TETile[][] tiles, Position p, int hexSize, int tessSize) {

        // Draw the first hexagon
        addHexColumn(tiles, p, hexSize, tessSize);

        // Expand up and to the right
        for (int i = 1; i < tessSize; i++) {
            p = getTopRightNeighbor(p, hexSize);
            addHexColumn(tiles, p, hexSize, tessSize + i);
        }

        // Expand down and to the right
        for (int i = tessSize - 2; i >= 0; i--) {
            p = getBotRightNeighbor(p, hexSize);
            addHexColumn(tiles, p, hexSize, tessSize + i);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillBoardWithNothing(world);
        Position anchor = new Position(12, 34); // Picked arbitrarily
        drawWorld(world, anchor, 3, 4);

        ter.renderFrame(world);
    }
}
