package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class GameTest {
    @Test
    public void testGameCreation() {
        // given
        Game game = new Game(0, 0);

        // when
        // then
        assertNotNull(game);
    }
}
