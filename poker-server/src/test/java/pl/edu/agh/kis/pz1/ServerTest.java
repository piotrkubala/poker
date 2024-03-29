package pl.edu.agh.kis.pz1;

import org.testng.annotations.Test;
import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Hand;

import java.util.Random;

import static org.testng.Assert.*;

/**
 * Class used for testing {@link Server} class.
 * @author Piotr Kubala
 */
public class ServerTest {
    /**
     * Tests if server can be created.
     */
    @Test
    public void testServerCreation() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        // then
        assertNotNull(server);
    }

    /**
     * Tests if server can be started correctly.
     */
    @Test
    public void testServerStart() {
        // given
        Server server = new Server(2, "localhost", 9000, 100, true);

        // when
        server.start();

        // then
        assertNotNull(server);
    }

    /**
     * Tests handleCommand method multiple times.
     */
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

    /**
     * Tests handleCommand method.
     */
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

    /**
     * Tests handleCommand method multiple times.
     */
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

    /**
     * Tests handleCommand method multiple times while game is in the betting phase.
     */
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

    /**
     * Tests writeMessageToAllClients method in debug mode.
     */
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

    /**
     * Tests writeMessageToClient method in debug mode.
     */
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

    /**
     * private method for adding players to the game in test mode.
     */
    private void addPlayers(Server server, int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            try {
                server.handleAccept(null);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    /**
     * Tests handleUsername method.
     */
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

    /**
     * Tests usernameSet method.
     */
    @Test
    public void testUsernameSetTest() {
        // given
        Server server = new Server(1, "localhost", 9000, 100, true);

        String expected = "Username set to piotrThere are enough players now\nType 'start' to start the gameYour status is ready nowThe game has started\nYou can check your cards and other stats by typing 'show'Now it is your turnPlayer: piotr\nCurrent game state: First round bets\nCurrent bet: 0\nCurrent player to make a move: piotr\n(0) Ace of Clubs\n(1) Ten of Clubs\n(2) Jack of Diamonds\n(3) Queen of Spades\n(4) King of Hearts\nSmall Blind: piotr has 100$ left, current bet: 0$\n";
        addPlayers(server, 1);

        // when
        server.handleCommands("username piotr", null);
        server.handleCommands("start", null);

        server.game.playersInJoinOrder[0].playerHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });

        server.handleCommands("show", null);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), expected, "Test output should be equal for test");
    }

    /**
     * Tests cardsChangeHandle method.
     */
    @Test
    public void testCardsChangeHandle() {
        // given
        Server server = new Server(1, "localhost", 9000, 100, true);

        addPlayers(server, 1);

        String expected = "Username set to piotrThere are enough players now\n" +
                "Type 'start' to start the gameYour status is ready nowThe game has started\n" +
                "You can check your cards and other stats by typing 'show'Now it is your turnpiotr has changed his cardsThe second round of betting has startedNow it is your turnPlayer: piotr\n" +
                "Current game state: Second round bets\n" +
                "Current bet: 0\n" +
                "Current player to make a move: piotr\n" +
                "(0) Ace of Clubs\n" +
                "(1) Jack of Clubs\n" +
                "(2) Jack of Diamonds\n" +
                "(3) Queen of Clubs\n" +
                "(4) King of Clubs\n" +
                "Small Blind: piotr has 100$ left, current bet: 0$\n" +
                "piotr called\n" +
                "The game is over\n" +
                "The winner is piotr with hand:\n" +
                "(0) Ace of Clubs\n" +
                "(1) Jack of Clubs\n" +
                "(2) Jack of Diamonds\n" +
                "(3) Queen of Clubs\n" +
                "(4) King of Clubs\n" +
                "\n" +
                "You can check game results by typing 'show'\n" +
                "Player: piotr\n" +
                "Current game state: After showdown\n" +
                "[WINNER] Player 1: piotr:\n" +
                "(0) Ace of Clubs\n" +
                "(1) Jack of Clubs\n" +
                "(2) Jack of Diamonds\n" +
                "(3) Queen of Clubs\n" +
                "(4) King of Clubs\n" +
                "\n" +
                "Small Blind: piotr has 100$ left, current bet: 0$\n";

        // when
        server.handleCommands("username piotr", null);

        server.handleCommands("start", null);

        server.game.playersInJoinOrder[0].playerHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });

        server.game.gameDeck = new Deck();
        server.game.gameDeck.setRandom(new Random(12345));

        server.game.state = Game.GameState.CARDS_CHANGING;

        server.handleCommands("change 1 2 3", null);
        server.handleCommands("show", null);
        server.handleCommands("showdown", null);
        server.handleCommands("show", null);

        // then
        assertTrue(server.testMode, "Test mode should be enabled");
        assertNotNull(server);
        assertEquals(server.testOutput.toString(), expected, "Test output should be equal for test");
    }
}
