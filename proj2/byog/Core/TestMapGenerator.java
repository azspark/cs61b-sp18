package byog.Core;

import byog.TileEngine.TERenderer;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMapGenerator {

    @Test
    public void testParseStringSeed() {
        String s1 = "N123S";
        long seed1 = 123;
        assertEquals(seed1, MapGenerator.parseStringSeed(s1));

        String s2 = "N123SS13";
        long seed2 = 123;
        assertEquals(seed2, MapGenerator.parseStringSeed(s2));

        String s3 = "123";
        long seed3 = 123;
        assertEquals(seed3, MapGenerator.parseStringSeed(s3));
    }

    public static void main(String[] args) {
        int testWidth = 80;
        int testHeight = 30;
        MapGenerator mg = new MapGenerator("N7S", testWidth, testHeight);
        TERenderer ter = new TERenderer();
        ter.initialize(testWidth, testHeight);
        ter.renderFrame(mg.getWorld());
    }
}
