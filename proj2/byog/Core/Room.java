package byog.Core;
import byog.TileEngine.TETile;
import java.util.Vector;

import java.util.Random;

public class Room {
    public int x;
    public int y;
    public int height;
    public int width;
    public int worldWidth;
    public int worldHeight;
    public Position position; // left bottom position
    private Random RANDOM;
    private int maxSize;
    private static final int minSize = 4;


    Room (Random RANDOM, int worldWidth, int worldHeight) {
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.RANDOM = RANDOM;
        maxSize = (int) (Math.min(worldHeight, worldWidth) / 2.3);
        randomGeneratePosition();
        randomGenerateSize();
    }

    public void drawOnWorld(TETile[][] world) {
        Draw.drawRoom(world, position, width, height);
    }

    public boolean canPutInWorld(Vector<Room> rooms) {
        for (Room room: rooms) {
            if (isOverlap(room)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlap(Room room) {
        boolean xOverlap = position.x > room.position.x - width && position.x < room.position.x + room.width;
        boolean yOverlap = position.y > room.position.y - height && position.y < room.position.y + room.height;
        return xOverlap && yOverlap;
    }

    private void randomGeneratePosition() {
        int randomX = RandomUtils.uniform(RANDOM, 0, worldWidth - minSize);
        int randomY = RandomUtils.uniform(RANDOM, 0, worldHeight - minSize);
        position = new Position(randomX, randomY);
    }

    private void randomGenerateSize() {
        height = RandomUtils.uniform(RANDOM,  minSize, Math.min(maxSize, worldHeight - position.y) + 1);
        width = RandomUtils.uniform(RANDOM,  minSize, Math.min(maxSize, worldWidth - position.x) + 1);
    }
}
