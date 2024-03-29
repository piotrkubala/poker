package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Logger;

/**
 * Main server class on the server side.
 * @author Piotr Kubala
 */
public class Server {
    static final Logger logger = Logger.getLogger(Server.class.getName());
    /**
     * Now it is your turn constant String
     */
    public static final String NOW_IT_IS_YOUR_TURN = "Now it is your turn";

    final Map<SelectionKey, Player> clients = new HashMap<>();
    final Set<String> chosenNames = new HashSet<>();

    Selector selector;

    final String serverAddress;
    final int playersNumber;
    final int portNumber;
    final int moneyPerPlayerAtBeginning;

    final Game game;

    boolean stopServer = false;

    final boolean testMode;
    StringBuilder testOutput = new StringBuilder();

    /**
     * Creates a new server with the given parameters.
     * @param playersNumberArg number of players in the game
     * @param serverAddressArg server address
     * @param portArg port number to listen on
     * @param moneyPerPlayerAtBeginningArg money per player at the beginning of the game
     * @param testModeArg if true, server will not wait for players to connect
     */
    public Server(int playersNumberArg, String serverAddressArg, int portArg, int moneyPerPlayerAtBeginningArg, boolean testModeArg) {
        playersNumber = playersNumberArg;
        portNumber = portArg;
        serverAddress = serverAddressArg;
        moneyPerPlayerAtBeginning = moneyPerPlayerAtBeginningArg;
        game = new Game(playersNumber);

        testMode = testModeArg;

        if (testMode) {
            logger.info("Test mode enabled");
            return;
        }

        try {
            selector = Selector.open();
        } catch (IOException e) {
            logger.severe("Cannot open selector");
            System.exit(1);
        }
    }

    /**
     * writes message to the client specified by the key
     * @param key key of the client socket
     * @param message message to be sent
     */
    void writeMessageToClient(SelectionKey key, String message) {
        if (testMode) {
            testOutput.append(message);
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes());
        buffer.flip();

        // do not close this channel!!!
        try {
            ((SocketChannel) key.channel()).write(buffer);
        } catch (IOException e) {
            logger.severe("Cannot write to client");
            System.exit(-1);
        }
    }

    /**
     * writes message to all clients
     * @param message message to be sent
     */
    void writeMessageToAllClients(String message) {
        if (testMode) {
            testOutput.append(message);
            return;
        }

        for (SelectionKey key : clients.keySet()) {
            writeMessageToClient(key, message);
        }
    }

    /**
     * ends game round, finds winners
     * @param player player who ended the round
     * @param key key of the player socket
     */
    void endGameRound(Player player, SelectionKey key) {
        List<Player> winners = game.showDownAndDivideMoneyFromPool();

        if (winners == null) {
            writeMessageToClient(key, "You cannot call now, the game is not in betting phase");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(player.getUsername());
        sb.append(" called\n");
        sb.append("The game is over\n");

        if (winners.size() == 1) {
            sb.append("The winner is ");
            sb.append(winners.get(0).getUsername());
            sb.append(" with hand:\n");
            sb.append(winners.get(0).getPlayerHand().toString());
            sb.append("\n");
        } else {
            sb.append("There is a draw between ");
            for (Player winner : winners) {
                sb.append(winner.getUsername());
                sb.append(" with hand:\n");
                sb.append(winner.getPlayerHand().toString());
            }
            sb.append("\n");
        }

        sb.append("You can check game results by typing 'show'\n");

        writeMessageToAllClients(sb.toString());
    }

    /**
     * handles help command
     * @param commandParts sent command parts
     * @param key key of the player socket
     */
    void handleHelpCommand(String[] commandParts, SelectionKey key) {
        String helpInfo;

        if (commandParts.length == 1) {
            helpInfo = "Available commands: help, username, exit, showdown, raise, fold, change, nextround, show";
        } else {
            switch (commandParts[1]) {
                case "help":
                    helpInfo = "help - shows available commands";
                    break;
                case "username":
                    helpInfo = "username - sets username";
                    break;
                case "exit":
                    helpInfo = "exit - exits the game";
                    break;
                case "call":
                    helpInfo = "showdown - end the game and the show results";
                    break;
                case "raise":
                    helpInfo = "raise <amount> - raises the current bet";
                    break;
                case "fold":
                    helpInfo = "fold - folds the current hand";
                    break;
                case "change":
                    helpInfo = "change <card number 1> [<card number 2>] ... [<card number 5>] - changes cards";
                    break;
                case "nextround":
                    helpInfo = "nextround - starts next round";
                    break;
                case "show":
                    helpInfo = "show - shows your current hand and money";
                    break;
                default:
                    helpInfo = "Unknown command";
                    break;
            }
        }

        writeMessageToClient(key, helpInfo);
    }

    /**
     * handles username command
     * @param commandParts sent command parts
     * @param key key of the player socket
     * @param gameState current game state
     */
    void handleUsernameCommand(String[] commandParts, SelectionKey key, Game.GameState gameState) {
        if (gameState != Game.GameState.WAITING_FOR_PLAYERS) {
            writeMessageToClient(key, "Cannot change username now");
        } else if (commandParts.length != 2) {
            writeMessageToClient(key, "usage: username <username>");
        } else if (clients.get(key).getUsername() != null) {
            writeMessageToClient(key, "You have already set your username");
        } else if (chosenNames.contains(commandParts[1])) {
            writeMessageToClient(key, "This username is already taken");
        } else {
            clients.get(key).setName(commandParts[1]);
            chosenNames.add(commandParts[1]);
            writeMessageToClient(key, "Username set to " + commandParts[1]);

            if (game.isReady()) {
                game.allPlayersJoined();

                writeMessageToAllClients("There are enough players now\nType 'start' to start the game");
            }
        }
    }

    /**
     * handles start command
     * @param key key of the player socket
     * @param gameState current game state
     */
    void handleStartCommand(SelectionKey key, Game.GameState gameState) {
        if (gameState != Game.GameState.WAITING_FOR_PLAYERS && gameState != Game.GameState.WAITING_FOR_READY) {
            writeMessageToClient(key, "The game has already started");
        } else if (gameState == Game.GameState.WAITING_FOR_READY) {
            writeMessageToClient(key, "Your status is ready now");
            if (clients.get(key).setStartReady(true) == playersNumber) {
                game.start();

                writeMessageToAllClients("The game has started\nYou can check your cards and other stats by typing 'show'");
                writeMessageToClient(game.getCurrentPlayer().getKey(), NOW_IT_IS_YOUR_TURN);
            }
        } else {
            writeMessageToClient(key, "There are not enough players (need " + (playersNumber - game.getReadyPlayersCount()) + " more players)");
        }
    }

    /**
     * handles showdown command
     * @param player player who sent the command
     * @param commandParts sent command parts
     * @param key key of the player socket
     * @param gameState current game state
     * @param isCurrentPlayerMakingAMove is current player making a move
     */
    void handleShowdownCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.FIRST_ROUND_BETS && gameState != Game.GameState.SECOND_ROUND_BETS) {
            writeMessageToClient(key, "You cannot showdown now, the game is not in betting phase");
            return;
        }
        if (!isCurrentPlayerMakingAMove) {
            writeMessageToClient(key, "It is not your turn, you cannot showdown now");
            return;
        }

        if (commandParts.length != 1) {
            writeMessageToClient(key, "usage: showdown");
            return;
        }

        endGameRound(player, key);
    }

    void executeRaiseCommand(Player player, String[] commandParts, SelectionKey key, int minRaise, int maxRaise) {
        try {
            int raiseAmount = Integer.parseInt(commandParts[1]);

            if (raiseAmount < minRaise || raiseAmount > maxRaise) {
                writeMessageToClient(key, "You cannot bet this amount");
                return;
            }

            player.raiseBet(raiseAmount);
            game.nextPlayerMove();
            writeMessageToClient(key, "You have raised the bet to " + raiseAmount);
            writeMessageToAllClients(player.getUsername() + " has raised the bet to " + raiseAmount);
            writeMessageToClient(game.getCurrentPlayer().getKey(), NOW_IT_IS_YOUR_TURN);
        } catch (NumberFormatException e) {
            writeMessageToClient(key, "usage: raise <amount>");
        }
    }

    /**
     * handles raise command
     * @param player player who sent the command
     * @param commandParts sent command parts
     * @param key key of the player socket
     * @param gameState current game state
     * @param isCurrentPlayerMakingAMove is current player making a move
     */
    void handleRaiseCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.FIRST_ROUND_BETS && gameState != Game.GameState.SECOND_ROUND_BETS) {
            writeMessageToClient(key, "You cannot raise now, the game is not in betting phase");
            return;
        }
        if (!isCurrentPlayerMakingAMove) {
            writeMessageToClient(key, "It is not your turn, you cannot raise now");
            return;
        }

        int minRaise = game.getCurrentBet();
        int maxRaise = player.getMoney() + player.getBet();

        if (gameState == Game.GameState.FIRST_ROUND_BETS) {
            if (player.isBigBlind() && player.getBet() == 0) {
                minRaise = game.getCurrentBet() * 2;

                if (minRaise > player.getMoney()) {
                    minRaise = player.getMoney();
                }
            }

            if (player.isSmallBlind() && player.getBet() == 0) {
                minRaise = 1;
            }
        }

        if (maxRaise < minRaise) {
            writeMessageToClient(key, "You cannot raise, you have already bet all your money");
            return;
        }

        if (commandParts.length != 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("usage: raise <amount>\n");

            sb.append("You can raise from ");
            sb.append(minRaise);
            sb.append(" to ");
            sb.append(maxRaise);

            writeMessageToClient(key, sb.toString());
            return;
        }

        executeRaiseCommand(player, commandParts, key, minRaise, maxRaise);
    }

    /**
     * handles fold command
     * @param player player who sent the command
     * @param key key of the player socket
     * @param gameState current game state
     * @param isCurrentPlayerMakingAMove is current player making a move
     */
    void handleFoldCommand(Player player, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.FIRST_ROUND_BETS && gameState != Game.GameState.SECOND_ROUND_BETS) {
            writeMessageToClient(key, "You cannot fold now, the game is not in betting phase");
            return;
        }
        if (!isCurrentPlayerMakingAMove) {
            writeMessageToClient(key, "It is not your turn, you cannot fold now");
            return;
        }

        player.setPlaying(false);
        writeMessageToClient(key, "You have folded");
        game.nextPlayerMove();
        writeMessageToAllClients(player.getUsername() + " has folded");
        writeMessageToClient(game.getCurrentPlayer().getKey(), NOW_IT_IS_YOUR_TURN);
    }

    boolean checkIfShouldExitFromChangeCommandHandler(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState) {
        if (gameState != Game.GameState.CARDS_CHANGING) {
            writeMessageToClient(key, "You cannot change cards now");
            return true;
        }
        if (player.wereCardsChanged()) {
            writeMessageToClient(key, "You have already changed your cards");
            return true;
        }

        if (commandParts.length < 2 || commandParts.length > 6) {
            writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]\nchange - to change 0 cards");
            return true;
        }

        return false;
    }

    /**
     * handles change command
     * @param player player who sent the command
     * @param commandParts sent command parts
     * @param key key of the player socket
     * @param gameState current game state
     */
    void handleChangeCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState) {
        if (checkIfShouldExitFromChangeCommandHandler(player, commandParts, key, gameState)) {
            return;
        }

        try {
            if (!commandParts[1].equals("-")) {
                int[] cardsToChange = new int[commandParts.length - 1];
                for (int i = 1; i < commandParts.length; i++) {
                    cardsToChange[i - 1] = Integer.parseInt(commandParts[i]);
                    if (cardsToChange[i - 1] < 0 || cardsToChange[i - 1] > 4) {
                        writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]\nchange - to change 0 cards");
                        return;
                    }
                }

                Card[] newCards = game.getCardsFromDeck(cardsToChange.length);
                Card[] oldPlayerCards = player.getPlayerHand().getCards();

                for (int i = 0; i < cardsToChange.length; i++) {
                    oldPlayerCards[cardsToChange[i]] = newCards[i];
                }

                player.getPlayerHand().setCards(oldPlayerCards);
            }
            player.setCardsChanged(true);

            writeMessageToAllClients(player.getUsername() + " has changed his cards");

            if (game.isChangingCardsEnded()) {
                game.beginSecondRound();
                writeMessageToAllClients("The second round of betting has started");
                writeMessageToClient(game.getCurrentPlayer().getKey(), NOW_IT_IS_YOUR_TURN);
            } else {
                writeMessageToAllClients("\nWaiting for other players to change their cards (" + (playersNumber - game.getPlayersWhoChangedCardsCount()) + " more players)");
            }
        } catch (NumberFormatException e) {
            writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]");
        }
    }

    /**
     * shows player's hand, is used by show command
     * @param key key of the player socket
     * @param gameState current game state
     */
    StringBuilder showCommandPlayerHand(SelectionKey key, Game.GameState gameState) {
        StringBuilder sb = new StringBuilder();

        if (game.canShowPlayersHand() && clients.get(key).getPlayerHand() != null) {
            if (gameState == Game.GameState.AFTER_SHOWDOWN) {
                Player[] players = game.getPlayersByNumber();
                for (int i = 0; i < playersNumber; i++) {
                    if (players[i].isWinner()) {
                        sb.append("[WINNER] ");
                    }
                    sb.append("Player ");
                    sb.append(i + 1);
                    sb.append(": ");
                    sb.append(players[i].getUsername());
                    sb.append(":\n");
                    sb.append(players[i].getPlayerHand().toString());
                    sb.append("\n");
                }
            } else {
                sb.append(clients.get(key).getPlayerHand().toString());
            }
        } else {
            sb.append("You cannot show your hand now\n");
        }

        return sb;
    }

    /**
     * handles show command
     * @param key key of the player socket
     * @param gameState current game state
     */
    void handleShowCommand(SelectionKey key, Game.GameState gameState) {
        StringBuilder sb = new StringBuilder();

        if (clients.get(key).getUsername() != null) {
            sb.append("Player: ");
            sb.append(clients.get(key).getUsername());
            sb.append("\n");
        } else {
            sb.append("Player: UNKNOWN - SET USERNAME \n");
        }
        sb.append("Current game state: ");
        sb.append(gameState.getName());
        sb.append("\n");
        if (gameState == Game.GameState.FIRST_ROUND_BETS || gameState == Game.GameState.SECOND_ROUND_BETS) {
            sb.append("Current bet: ");
            sb.append(game.getCurrentBet());
            sb.append("\n");
            sb.append("Current player to make a move: ");
            sb.append(game.getCurrentPlayer().getUsername());
            sb.append("\n");
        }
        sb.append(showCommandPlayerHand(key, gameState));

        sb.append(game.getPlayersMoneyAndBetAsString());
        writeMessageToClient(key, sb.toString());
    }

    /**
     * handles next round command
     * @param player player who sent the command
     * @param key key of the player socket
     * @param gameState current game state
     */
    void handleNextRoundCommand(Player player, SelectionKey key, Game.GameState gameState) {
        if (gameState != Game.GameState.AFTER_SHOWDOWN) {
            writeMessageToClient(key, "You cannot start a new round now");
            return;
        }
        if (player.getNextRoundReady()) {
            writeMessageToClient(key, "You have already confirmed that you are ready for the next round");
            return;
        } else {
            player.setNextRoundReady(true);
            writeMessageToClient(key, "You have confirmed that you are ready for the next round");
        }

        if (game.isNextRoundReady()) {
            game.startNewRound();
            writeMessageToAllClients("A new round has started");
            writeMessageToClient(game.getCurrentPlayer().getKey(), NOW_IT_IS_YOUR_TURN);
        }
    }

    /**
     * mother method for handling commands
     * @param command command sent by the player
     * @param key key of the player socket
     */
    void handleCommands(String command, SelectionKey key) {
        String[] commandParts = command.split(" ");

        if (commandParts.length == 0) {
            return;
        }

        Game.GameState gameState = game.getState();

        boolean isCurrentPlayerMakingAMove = game.isMakingMove(clients.get(key));
        Player player = clients.get(key);

        switch (commandParts[0]) {
            case "help":
                handleHelpCommand(commandParts, key);
                break;
            case "username":
                handleUsernameCommand(commandParts, key, gameState);
                break;
            case "start":
                handleStartCommand(key, gameState);
                break;
            case "showdown":
                handleShowdownCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "raise":
                handleRaiseCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "fold":
                handleFoldCommand(player, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "change":
                handleChangeCommand(player, commandParts, key, gameState);
                break;
            case "show":
                handleShowCommand(key, gameState);
                break;
            case "nextround":
                handleNextRoundCommand(player, key, gameState);
                break;
            default:
                writeMessageToClient(key, "Unknown command");
                break;
        }

        checkForFirstRoundEnd(gameState, player, key);
    }

    /**
     * check for first round end
     * @param gameState current game state
     * @param player player who sent the command
     * @param key key of the player socket
     */
    void checkForFirstRoundEnd(Game.GameState gameState, Player player, SelectionKey key) {
        if (gameState == Game.GameState.FIRST_ROUND_BETS && game.isBettingEnded()) {
            game.beginChangingCards();
            writeMessageToAllClients("The first round of betting has ended\nYou can now change your cards");
        } else if (gameState == Game.GameState.SECOND_ROUND_BETS && game.isBettingEnded()) {
            endGameRound(player, key);
        }
    }

    /**
     * check if new player can join the game, if so, add him to the game
     * @param mySocket socket of the new potential player
     */
    void handleAccept(ServerSocketChannel mySocket) throws IOException {
        if (testMode) {
            clients.put(null, new Player(null, moneyPerPlayerAtBeginning, game));

            return;
        }

        if (clients.size() >= playersNumber) {
            mySocket.accept().close();
            return;
        }

        logger.info("Accepting connection");

        SocketChannel clientSocket = mySocket.accept();
        clientSocket.configureBlocking(false);

        SelectionKey clientKey = clientSocket.register(selector, SelectionKey.OP_READ);

        clients.put(clientKey, new Player(clientKey, moneyPerPlayerAtBeginning, game));
    }

    /**
     * reads bytes from the socket and converts them to a string, calls handleCommands
     * @param key key of the player socket
     */
    void handleRead(SelectionKey key) throws IOException {
        if (testMode) {
            return;
        }

        logger.info("Reading data");

        SocketChannel client = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer);

        String data = new String(buffer.array()).trim();

        if (data.length() > 0) {
            logger.info("Received message:");
            logger.info(data);
            if (data.equals("exit")) {
                writeMessageToAllClients("exit");

                for (SelectionKey clientKey : clients.keySet()) {
                    clientKey.channel().close();
                }

                logger.info("Connection closed, restarting server");
                stopServer = true;
            } else {
                handleCommands(data, key);
            }
        }
    }

    /**
     * main loop of the server
     */
    public void start() {
        if (testMode) {
            return;
        }

        try {
            ServerSocketChannel socket = ServerSocketChannel.open();
            ServerSocket serverSocket = socket.socket();
            serverSocket.bind(new InetSocketAddress(serverAddress, portNumber));
            socket.configureBlocking(false);
            int ops = socket.validOps();
            socket.register(selector, ops, null);

            logger.info("Server started");

            while (!stopServer) {
                selector.select();
                logger.info("Selecting");

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        // zaakceptuj połączenie
                        handleAccept(socket);
                    } else if (key.isReadable()) {
                        // odczytaj przesłane dane
                        handleRead(key);
                    }
                    iterator.remove();
                }
            }

            serverSocket.close();
            socket.close();
            selector.close();
        } catch (IOException e) {
            logger.severe("Error: " + e.getMessage());
            logger.severe("IO error");
            System.exit(1);
        }
    }
}
