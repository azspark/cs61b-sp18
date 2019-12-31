package byog.Core;
import java.util.Random;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.Vector;

public class MapGenerator {
    private Random RANDOM;
    private int width;
    private int height;
    private TETile[][] world;
    private Vector<Room> worldRooms;
    private static final int ROOM_GENERATE_TIMES = 90;

    MapGenerator(String seed, int width, int height) {
        this.width = width;
        this.height = height;
        initWorld();
        RANDOM = new Random(parseStringSeed(seed));
        worldRooms = new Vector<>();
        generateRooms();
        connectRooms();
    }

    public static long parseStringSeed(String seed) {
        seed = seed.toLowerCase();
        seed = seed.substring(seed.indexOf('n') + 1); // will be fine if 'n' not in string
        if (seed.indexOf('s') != -1) {
            seed = seed.substring(0, seed.indexOf('s'));
        }
        return Long.parseLong(seed);
    }

    private void initWorld() {
        world = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void generateRooms() {
        for (int i = 0; i < ROOM_GENERATE_TIMES; i++) {
            Room candidateRoom = new Room(RANDOM, width, height);
            if (candidateRoom.canPutInWorld(worldRooms)) {
                worldRooms.add(candidateRoom);
                candidateRoom.drawOnWorld(world);
            }
        }
    }

    private void connectRooms() {
        MST mst = new MST(worldRooms);
        int[] connectTo = mst.getEdgeTo();
        for (int i = 1; i < connectTo.length; i++) {
            worldRooms.get(i).connect(world, worldRooms.get(connectTo[i]));
        }

        for (int i = 0; i < worldRooms.size(); i++) {
            worldRooms.get(i).drawRoomInnerSpaceOnWorld(world);
        }
    }

    public TETile[][] getWorld() {
        return world;
    }

}
