package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Draw {

    public static void drawRoom(TETile[][] world, Position p, int width, int height) {
        //First draw the inner space of world
        drawRectangleSpace(world, p.shift(1, 1), width - 2, height - 2);
        //Then draw horizontal walls
        drawHorizontalWall(world, p, width);
        drawHorizontalWall(world, p.shift(0, height - 1), width);
        //Finally draw vertical walls
        drawVerticalWall(world, p.shift(0, 1), height - 2);
        drawVerticalWall(world, p.shift(width - 1, 1), height - 2);
    }

    public static void drawSquareSpace(TETile[][] world, Position p, int size) {
        drawRectangleSpace(world, p, size, size);
    }

    public static void drawRectangleSpace(TETile[][] world, Position p, int width, int height) {
        for (int i = 0; i < height; i++) {
            drawHorizontalFloor(world, p.shift(0, i), width);
        }
    }

    public static void drawHorizontalLine(TETile[][] world, Position p, int length, TETile ts) {
        for (int i = 0; i < length; i++) {
            world[p.x + i][p.y] = ts;
        }
    }

    public static void drawVerticalLine(TETile[][] world, Position p, int length, TETile ts) {
        for (int j = 0; j < length; j++) {
            world[p.x][p.y + j] = ts;
        }
    }

    public static void drawHorizontalWall(TETile[][] world, Position p, int length) {
        drawHorizontalLine(world, p, length, Tileset.WALL);
    }

    public static void drawVerticalWall(TETile[][] world, Position p, int length) {
        drawVerticalLine(world, p, length, Tileset.WALL);
    }

    public static void drawHorizontalFloor(TETile[][] world, Position p, int length) {
        drawHorizontalLine(world, p, length, Tileset.FLOOR);
    }
}
