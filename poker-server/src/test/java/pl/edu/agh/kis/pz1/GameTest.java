package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Game test class
 */
public class GameTest {
    /**
     * Test for the construction of Game and the
     */
    @Test
    public void testGameCreation() {
        // given
        Game game = new Game(0);

        // when
        // then
        assertNotNull(game);
    }
}
