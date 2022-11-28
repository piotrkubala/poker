package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

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
    public void testServerHandleMultipleCommands() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        addPlayers(server, 1);

        // when
        server.handleCommands("raise 10", null);
        server.handleCommands("call", null);
        server.handleCommands("fold", null);
        server.handleCommands("showdown", null);
        server.handleCommands("exit", null);
        server.handleCommands("help help", null);
        server.handleCommands("help", null);
        server.handleCommands("help help", null);
        server.handleCommands("help raise", null);
        server.handleCommands("raise i", null);
        server.handleCommands("raise -10", null);
        server.handleCommands("raise 0", null);
        server.handleCommands("change 1 2 3", null);
        server.handleCommands("change 1 2 3 4", null);
        server.handleCommands("start 1 2 3 4", null);
        server.handleCommands("start", null);
        server.handleCommands("username piotr", null);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "You cannot raise now, the game is not in betting phaseUnknown commandYou cannot fold now, the game is not in betting phaseYou cannot showdown now, the game is not in betting phaseUnknown commandhelp - shows available commandsAvailable commands: help, username, exit, showdown, raise, fold, change, nextround, showhelp - shows available commandsraise <amount> - raises the current betYou cannot raise now, the game is not in betting phaseYou cannot raise now, the game is not in betting phaseYou cannot raise now, the game is not in betting phaseYou cannot change cards nowYou cannot change cards nowThere are not enough players (need 2 more players)There are not enough players (need 2 more players)Username set to piotr", "Server should handle multiple commands");
    }

    @Test
    public void testServerHandleCommands() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.handleCommands("call", null);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "Unknown command", "Test output should be equal for call");
    }

    @Test void testServerHandleMultipleCommands2() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.handleCommands("help help", null);
        server.handleCommands("help call", null);
        server.handleCommands("help start", null);
        server.handleCommands("help start", null);
        server.handleCommands("help start", null);
        server.handleCommands("help raise", null);
        server.handleCommands("raise 10", null);
        server.handleCommands("change -", null);


        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "help - shows available commandsshowdown - end the game and the show resultsUnknown commandUnknown commandUnknown commandraise <amount> - raises the current betYou cannot raise now, the game is not in betting phaseYou cannot change cards now", "Test output should be equal for help help");
    }

    @Test
    public void testHandleMultipleCommandsWhileInBettingPhase() {
        // given
        Server server = new Server(4, "localhost", 9000, 100, true);

        addPlayers(server, 4);
        server.game.state = Game.GameState.FIRST_ROUND_BETS;

        // when
        server.handleCommands("call", null);
        server.handleCommands("help help", null);
        server.handleCommands("help call", null);
        server.handleCommands("help start", null);
        server.handleCommands("help raise", null);
        server.handleCommands("raise 10", null);
        server.handleCommands("change -", null);
        server.handleCommands("raise i", null);
        server.handleCommands("raise -10", null);
        server.handleCommands("raise 0", null);
        server.handleCommands("change 1 2 3", null);
        server.handleCommands("change 1 2 3 4", null);
        server.handleCommands("start 1 2 3 4", null);
        server.handleCommands("start", null);
        server.handleCommands("username piotr", null);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "Unknown commandhelp - shows available commandsshowdown - end the game and the show resultsUnknown commandraise <amount> - raises the current betIt is not your turn, you cannot raise nowYou cannot change cards nowIt is not your turn, you cannot raise nowIt is not your turn, you cannot raise nowIt is not your turn, you cannot raise nowYou cannot change cards nowYou cannot change cards nowThe game has already startedThe game has already startedCannot change username now", "Test output should be equal for call");
    }

    @Test
    public void testWriteMessageToAllClients() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.writeMessageToAllClients("testowa wiadomosc");

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "testowa wiadomosc", "Test output should be equal for test");
    }

    @Test
    public void testWriteMessageToClient() {
        // given
        Server server = new Server(4, "localhost", 9000, 100, true);

        // when
        server.writeMessageToAllClients("testowa wiadomosc 2");

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "testowa wiadomosc 2", "Test output should be equal for test");
    }

    private void addPlayers(Server server, int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                server.handleAccept(null);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    @Test
    public void testHandleUsernameCommand() {
        // given
        Server server = new Server(4, "localhost", 9000, 100, true);

        addPlayers(server, 4);

        // when
        server.handleUsernameCommand(new String[] {"username", "piotr"}, null, Game.GameState.WAITING_FOR_PLAYERS);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), "Username set to piotr", "Test output should be equal for test");
    }
}
