package pl.edu.agh.kis.pz1;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class PokerClientTest {
    @Test
    public void shouldCreatePokerClientObject(){
        // given
        PokerClient pokerClient = new PokerClient("localhost", 10101);
        // when
        // then
        assertNotNull("Main method called on class Main2.", pokerClient);
    }
}
