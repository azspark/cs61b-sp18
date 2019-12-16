package byog.Core;

public class Position {
    public int x;
    public int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position shift(int shiftX, int shiftY) {
        return new Position(x + shiftX, y + shiftY);
    }
}