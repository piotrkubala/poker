package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import java.nio.channels.SelectionKey;

import static org.testng.Assert.*;

/**
 * Player test class
 */
public class PlayerTest {
    /**
     * Test for the construction of Player
     */
    @Test
    public void testPlayerCreation() {
        // given
        Player player = new Player(null, 0, null);

        // when
        // then
        assertNotNull(player);
    }

    /**
     * player test
     */
    @Test
    public void testResetPlayerForNextRound() {
        // given
        Player player = new Player(null, 0, null);

        // when
        player.resetPlayerForNextRound();

        // then
        assertNotNull(player);
        assertEquals(player.bet, 0, "Player should be folded");
        assertFalse(player.isStartReady, "Player should be starting");
        assertFalse(player.isSmallBlind, "Player should not be small blind");
        assertFalse(player.isBigBlind, "Player should not be big blind");
        assertTrue(player.isPlaying, "Player should be playing");
        assertFalse(player.wereCardsChanged, "Player should be small blind");
        assertFalse(player.isWinner, "Player should be big blind");
        assertEquals(player.handValue, -1, "Player should be winner");
        assertFalse(player.nextRoundReady, "Player should be start ready");
    }

    /**
     * player test
     */
    @Test
    public void testGetNextRoundReady() {
        // given
        Player player = new Player(null, 0, null);

        // when
        boolean result = player.getNextRoundReady();

        // then
        assertNotNull(player);
        assertFalse(result, "Player should not be start ready");
    }

    /**
     * player test
     */
    @Test
    public void testSetNextRoundReady() {
        // given
        Player player = new Player(null, 0, null);
        player.testMode = true;

        // when
        player.setNextRoundReady(true);

        // then
        assertNotNull(player);
        assertTrue(player.nextRoundReady, "Player should be start ready");
    }

    /**
     * player test
     */
    @Test
    public void testGetKey() {
        // given
        Player player = new Player(null, 0, null);
        player.testMode = true;

        // when
        SelectionKey result = player.getKey();

        // then
        assertNull(result);
    }

    /**
     * player test
     */
    @Test
    public void testGetName() {
        // given
        Player player = new Player(null, 0, null);
        player.testMode = true;

        // when
        SelectionKey result = player.getKey();

        // then
        assertNull(result);
    }

    /**
     * player test
     */
    @Test
    public void testSetName() {
        // given
        Player player = new Player(null, 0, null);
        player.testMode = true;

        // when
        player.setName("test");

        // then
        assertEquals(player.name, "test");
        assertFalse(player.isStartReady, "Player should not be starting");
    }

    /**
     * player test
     */
    @Test
    public void testGetMoney() {
        // given
        Player player = new Player(null, 0, null);
        player.testMode = true;

        // when
        int result = player.getMoney();

        // then
        assertEquals(result, 0);
    }
}
