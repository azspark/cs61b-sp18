package byog.Core;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class TestRoom {

    @Test
    public void testSampleCommonSequence() {
        Random RANDOM = new Random(23);
        for (int i = 0; i < 20; i++) {
            int a = Room.sampleCommonSequence(RANDOM, 1, 5, 2, 9);
            assertTrue(a >= 2 && a <= 5);
        }

        for (int i = 0; i < 20; i++) {
            int a = Room.sampleCommonSequence(RANDOM, 1, 5, -10, 100);
            assertTrue(a >= 1 && a <= 5);
        }

        for (int i = 0; i < 20; i++) {
            int a = Room.sampleCommonSequence(RANDOM, 1, 5, 1, 3);
            assertTrue(a >= 1 && a <= 3);
        }

        for (int i = 0; i < 20; i++) {
            int a = Room.sampleCommonSequence(RANDOM, 1, 5, 2, 4);
            assertTrue(a >= 2 && a <= 4);
        }

        for (int i = 0; i < 20; i++) {
            int a = Room.sampleCommonSequence(RANDOM, 1, 1, 0, 4);
            assertTrue(a == 1);
        }
    }
}
