package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ServerTest {
    @Test
    public void testServerCreation() {
        // given
        Server server = new Server(2, "localhost", 9000, 100);

        // when
        // then
        assertNotNull(server);
    }
}
