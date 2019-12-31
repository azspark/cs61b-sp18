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

    public static void drawRoomInnerSpace(TETile[][] world, Position p, int width, int height) {
        drawRectangleSpace(world, p.shift(1, 1), width - 2, height - 2);
    }

    /**
     * Draw a Hallway which only support straight hallway and hallway with one turn
     *
     * Because given start and end position are actually from Room floor edge,
     * draw Vertical or Horizontal will firstly shift one unit to the wall.
     * @param world
     * @param start
     * @param end
     */
    public static void drawStraightHallway(TETile[][] world, Position start, Position end) {
        if (start.x == end.x) {
            drawVerticalStraightHallway(world, start, end);
        } else if (start.y == end.y) {
            drawHorizontalStraightHallway(world, start, end);
        } else {
            throw new Error("Input Positions are not in one line!");
        }
    }

    public static void drawLShapeHallway(TETile [][] world, Position start, Position end, int direction) {
        // This function is not written well
        if (direction == 0 || direction == 2) {
            int upDownAdd = (direction == 0) ? 2: -2;
            Position middle = new Position(start.x, end.y + upDownAdd);
            drawVerticalStraightHallway(world, start, middle);
            int leftRightShift = (end.x - start.x > 0) ? -1: 1;
            drawHorizontalStraightHallway(world, new Position(start.x + leftRightShift, end.y), end);
            //By drawing two straight hallway, one path floor TEile will be blocked which should be dealt with manually
            world[start.x][end.y + ((direction == 0) ? -1: 1)] = Tileset.FLOOR;
        } else {
            int leftRightAdd = (direction == 1) ? 2: -2;
            Position middle = new Position(end.x + leftRightAdd, start.y);
            drawHorizontalStraightHallway(world, start, middle);
            int upDownShift = (end.y - start.y > 0) ? -1: 1;
            drawVerticalStraightHallway(world, new Position(end.x, start.y + upDownShift), end);
            //By drawing two straight hallway, one path floor TEile will be blocked which should be dealt with manually
            world[end.x + ((direction == 1) ? -1: 1)][start.y] = Tileset.FLOOR;
        }
    }

    public static void drawHorizontalStraightHallway(TETile[][] world, Position start, Position end) {
        Position leftPosition;
        Position rightPosition;
        if (start.x < end.x) {
            leftPosition = start.shift(1, 0);  //Actual start position is from floor edge
            rightPosition = end.shift(-1, 0);
        } else {
            leftPosition = end.shift(1, 0);
            rightPosition = start.shift(-1, 0);
        }
        int length = rightPosition.x - leftPosition.x + 1;
        drawHorizontalWall(world, leftPosition.shift(0, 1), length);
        drawHorizontalFloor(world, leftPosition, length);
        drawHorizontalWall(world, leftPosition.shift(0, -1), length);
    }

    public static void drawVerticalStraightHallway(TETile[][] world, Position start, Position end) {
        Position downPosition;
        Position upPosition;

        if (start.y < end.y) {
            downPosition = start.shift(0, 1);
            upPosition = end.shift(0, -1);
        } else {
            downPosition = end.shift(0, 1);
            upPosition = start.shift(0, -1);
        }
        int length = upPosition.y - downPosition.y + 1;
        drawVerticalWall(world, downPosition.shift(-1, 0), length);
        drawVerticalFloor(world, downPosition, length);
        drawVerticalWall(world, downPosition.shift(1, 0), length);
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

    public static void drawVerticalFloor(TETile[][] world, Position p, int length) {
        drawVerticalLine(world, p, length, Tileset.FLOOR);
    }
}
