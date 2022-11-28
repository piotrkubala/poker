package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class ServerTest {
    @Test
    public void testServerCreation() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        // then
        assertNotNull(server);
    }

    @Test
    public void testServerStart() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.start();

        // then
        assertNotNull(server);
    }

    @Test
    public void testServerHandleCommands() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.handleCommands("raise 10", null);

        // then
        assertNotNull(server);
    }
}
