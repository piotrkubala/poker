package pl.edu.agh.kis.pz1;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

/**
 * Game PokerClient test class
 */
public class PokerClientTest {
    /**
     * Test for the construction of Main and the
     * main method being called
     */
    @Test
    public void shouldCreatePokerClientObject(){
        // given
        PokerClient pokerClient = new PokerClient("localhost", 10101);
        // when
        // then
        assertNotNull("Main method called on class Main2.", pokerClient);
    }
}
