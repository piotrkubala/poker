package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import java.nio.channels.SelectionKey;

import static org.testng.Assert.assertNotNull;

public class PlayerTest {
    @Test
    public void testPlayerCreation() {
        // given
        Player player = new Player(null, 0, null);

        // when
        // then
        assertNotNull(player);
    }
}
