package byow.lab13;

import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryGameTest {

    @Test
    public void testGenerateRandomString() {
        MemoryGame game = new MemoryGame(30, 80, 12312);
        System.out.println(game.generateRandomString(10));
        int len1 = game.generateRandomString(10).length();
        int len2 = 10;
        assertEquals(len1, len2);
    }

    @Test
    public void testDrawFrame() {
        MemoryGame game = new MemoryGame(30, 80, 12312);
        String word1 = game.generateRandomString(10);
        game.drawFrame(word1);
    }
}
