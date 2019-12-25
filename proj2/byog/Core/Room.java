package byog.Core;
import byog.TileEngine.TETile;
import java.util.Vector;
import java.util.ArrayList;

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
    private static final int minSize = 3;

    private ArrayList<Position> upFloorEdge;
    private ArrayList<Position> downFloorEdge;
    private ArrayList<Position> leftFloorEdge;
    private ArrayList<Position> rightFloorEdge;


    Room (Random RANDOM, int worldWidth, int worldHeight) {
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.RANDOM = RANDOM;
        maxSize = (int) (Math.min(worldHeight, worldWidth) / 2.3);
        randomGeneratePosition();
        randomGenerateSize();
        initFloorEdge();
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

    private void initFloorEdge() {
        upFloorEdge = new ArrayList<>();
        downFloorEdge = new ArrayList<>();
        leftFloorEdge = new ArrayList<>();
        rightFloorEdge = new ArrayList<>();
        //up, down floor edge
        int downFloorEdgeY = position.y + 1;
        int upFloorEdgeY = position.y + height - 2;
        for (int x = position.x + 1; x <= position.x + width - 2; x++) {
            upFloorEdge.add(new Position(x, upFloorEdgeY));
            downFloorEdge.add(new Position(x, downFloorEdgeY));
        }
        //left, right floor edge
        int leftFloorEdgeX = position.x + 1;
        int rightFloorEdgeX = position.x + width - 2;
        for (int y = position.y + 1; y <= position.y + height - 2; y++) {
            leftFloorEdge.add(new Position(leftFloorEdgeX, y));
            rightFloorEdge.add(new Position(rightFloorEdgeX, y));
        }
    }

    /** Connect to the other room */
    public void connect(TETile[][] world, Room room) {
        //If can't connect with straight line connect with one turn
        if (connectRoomStraight(world, room) == -1) {
            connectRoomWithOneTurn(world, room);
        }
    }

    private int connectRoomStraight(TETile[][] world, Room room) {
        // If two rooms are up and down and can connect with straight line
        int connectedX = sampleCommonSequence(RANDOM, position.x + 1, position.x + width - 2,
                room.position.x + 1, room.position.x + room.width - 2);
        Position from;
        Position to;
        if (connectedX != -1) {
            if (position.y > room.position.y) {
                from = new Position(connectedX, downFloorEdge.get(0).y);
                to = new Position(connectedX, room.upFloorEdge.get(0).y);
            } else {
                from = new Position(connectedX, upFloorEdge.get(0).y);
                to = new Position(connectedX, room.downFloorEdge.get(0).y);
            }
            Draw.drawStraightHallway(world, from, to);
            return 1;
        } else {
            // If two rooms are left and right and can connect with straight line
            int connectedY = sampleCommonSequence(RANDOM, position.y + 1,
                    position.y + height - 2, room.position.y + 1, room.position.y + room.height - 2);
            if (connectedY != -1) {
                if (position.x > room.position.x) {
                    from = new Position(leftFloorEdge.get(0).x, connectedY);
                    to = new Position(room.rightFloorEdge.get(0).x, connectedY);
                } else {
                    from = new Position(rightFloorEdge.get(0).x, connectedY);
                    to = new Position(room.leftFloorEdge.get(0).x, connectedY);
                }
                Draw.drawStraightHallway(world, from, to);
                return 1;
            } else {
                return -1;
            }
        }

    }

    private void connectRoomWithOneTurn(TETile[][] world, Room room) {
        double p = RandomUtils.uniform(RANDOM);
        ArrayList<Position> fromEdge;
        ArrayList<Position> toEdge;
        int direction; //0,1,2,3 are corresponding to up, right, down, left
        if (p > 0.5) {  // use up or down side to connect room's left or right side
            if (position.y < room.position.y) {
                fromEdge = upFloorEdge;
                direction = 0;
            } else {
                fromEdge = downFloorEdge;
                direction = 2;
            }
            if (position.x < room.position.x) {
                toEdge = room.leftFloorEdge;
            } else {
                toEdge = room.rightFloorEdge;
            }
        } else {  // use left or right side to connect room's up or down side
            if (position.x < room.position.x) {
                fromEdge = rightFloorEdge;
                direction = 1;
            } else {
                fromEdge = leftFloorEdge;
                direction = 3;
            }
            if (position.y < room.position.y) {
                toEdge = room.downFloorEdge;
            } else {
                toEdge = room.upFloorEdge;
            }
        }
        randomConnectEdges(RANDOM, world, fromEdge, toEdge, direction);
    }

    /** Find common sequence of s1 and s2. If have, sample one and return, otherwise return -1 */
    public static int sampleCommonSequence(Random RANDOM, int s1Start, int s1End, int s2Start, int s2End) {
        if (s1Start <= s2End && s1End >= s2Start) {
            if (s1Start < s2Start) {
                return RandomUtils.uniform(RANDOM, s2Start, Math.min(s1End, s2End) + 1);
            } else {
                return RandomUtils.uniform(RANDOM, s1Start, Math.min(s1End, s2End) + 1);
            }
        }
        return -1;
    }

    private void randomConnectEdges(Random RANDOM, TETile[][] world, ArrayList<Position> side1, ArrayList<Position> side2, int direction) {
        Position start = randomChoicePosition(RANDOM, side1);
        Position end = randomChoicePosition(RANDOM, side2);
        Draw.drawLShapeHallway(world, start, end, direction);
    }

    private Position randomChoicePosition(Random RANDOM, ArrayList<Position> side) {
        int sideLen = side.size();
        return side.get(RandomUtils.uniform(RANDOM, sideLen));
    }

}
