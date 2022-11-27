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

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final Map<SelectionKey, Player> clients = new HashMap<>();
    private final Set<String> chosenNames = new HashSet<>();

    Selector selector;

    private final int playersNumber;
    private final int portNumber;
    private final int ante;

    private final Game game;

    private boolean stopServer = false;

    public Server(int playersNumber_, int port_, int ante_) {
        playersNumber = playersNumber_;
        portNumber = port_;
        ante = ante_;
        game = new Game(playersNumber, ante);

        try {
            selector = Selector.open();
        } catch (IOException e) {
            logger.severe("Cannot open selector");
            System.exit(1);
        }
    }

    private void writeMessageToClient(SelectionKey key, String message) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes());
        buffer.flip();

        try {
            ((SocketChannel) key.channel()).write(buffer);
        } catch (IOException e) {
            logger.severe("Cannot write to client");
            System.exit(-1);
        }
    }

    private void writeMessageToAllClients(String message) {
        for (SelectionKey key : clients.keySet()) {
            writeMessageToClient(key, message);
        }
    }

    private void handleHelpCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (commandParts.length == 1) {
            writeMessageToClient(key, "Available commands: help, username, exit, call, raise, fold, change, show");
        } else {
            switch (commandParts[1]) {
                case "help":
                    writeMessageToClient(key, "help - shows available commands");
                    break;
                case "username":
                    writeMessageToClient(key, "username - sets username");
                    break;
                case "exit":
                    writeMessageToClient(key, "exit - exits the game");
                    break;
                case "call":
                    writeMessageToClient(key, "call - calls the current bet");
                    break;
                case "raise":
                    writeMessageToClient(key, "raise <amount> - raises the current bet");
                    break;
                case "fold":
                    writeMessageToClient(key, "fold - folds the current hand");
                    break;
                case "change":
                    writeMessageToClient(key, "change <card number 1> [<card number 2>] ... [<card number 5>] - changes cards");
                    break;
                case "show":
                    writeMessageToClient(key, "show - shows your current hand and money");
                    break;
                default:
                    writeMessageToClient(key, "Unknown command");
                    break;
            }
        }
    }

    private void handleUsernameCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
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

                writeMessageToAllClients("There are enough players now\nType \'start\' to start the game");
            }
        }
    }

    private void handleStartCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.WAITING_FOR_PLAYERS && gameState != Game.GameState.WAITING_FOR_READY) {
            writeMessageToClient(key, "The game has already started");
        } else if (gameState == Game.GameState.WAITING_FOR_READY) {
            writeMessageToClient(key, "Your status is ready now");
            if (clients.get(key).setStartReady(true) == playersNumber) {
                game.start();

                writeMessageToAllClients("The game has started\nYou can check your cards and other stats by typing 'show'");
                writeMessageToClient(game.getCurrentPlayer().getKey(), "Now it is your turn");
            }
        } else {
            writeMessageToClient(key, "There are not enough players (need " + (playersNumber - game.getReadyPlayersCount()) + " more players)");
        }
    }

    private void handleCallCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.FIRST_ROUND_BETS && gameState != Game.GameState.SECOND_ROUND_BETS) {
            writeMessageToClient(key, "You cannot call now, the game is not in betting phase");
            return;
        }
        if (!isCurrentPlayerMakingAMove) {
            writeMessageToClient(key, "It is not your turn, you cannot call now");
            return;
        }

        if (commandParts.length != 1) {
            writeMessageToClient(key, "usage: call");
            return;
        }

        List<Player> winners = game.showDownAndDivideMoneyFromPool();

        if (winners == null) {
            writeMessageToClient(key, "You cannot call now, the game is not in betting phase");
            return;
        }

        StringBuilder sb = new StringBuilder();

        sb.append(player.getUsername() + " called\n");
        sb.append("The game is over\n");

        if (winners.size() == 1) {
            sb.append("The winner is ");
            sb.append(winners.get(0).getUsername());
            sb.append(" with hand:\n");
            sb.append(winners.get(0).getPlayerHand().toString());
            sb.append("\n");
        } else {
            sb.append("There is a draw between ");
            for (int i = 0; i < winners.size(); i++) {
                sb.append(winners.get(i).getUsername());
                sb.append(" with hand:\n");
                sb.append(winners.get(i).getPlayerHand().toString());
            }
            sb.append("\n");
        }

        sb.append("You can check game results by typing 'show'\n");

        writeMessageToAllClients(sb.toString());
    }

    private void handleRaiseCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
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
        if (player.isBigBlind() && player.getBet() == 0) {
            minRaise = game.getCurrentBet() * 2;

            if (minRaise > player.getMoney()) {
                minRaise = player.getMoney();
            }
        }

        if (player.isSmallBlind() && player.getBet() == 0) {
            minRaise = 1;
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
            writeMessageToClient(game.getCurrentPlayer().getKey(), "Now it is your turn");
        } catch (NumberFormatException e) {
            writeMessageToClient(key, "usage: raise <amount>");
        }
    }

    private void handleFoldCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
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
        writeMessageToClient(game.getCurrentPlayer().getKey(), "Now it is your turn");
    }

    private void handleChangeCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
        if (gameState != Game.GameState.CARDS_CHANGING) {
            writeMessageToClient(key, "You cannot change cards now");
            return;
        }
        if (player.wereCardsChanged()) {
            writeMessageToClient(key, "You have already changed your cards");
            return;
        }

        if (commandParts.length < 2 || commandParts.length > 6) {
            writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]");
            return;
        }

        try {
            int[] cardsToChange = new int[commandParts.length - 1];
            for (int i = 1; i < commandParts.length; i++) {
                cardsToChange[i - 1] = Integer.parseInt(commandParts[i]);
                if (cardsToChange[i - 1] < 0 || cardsToChange[i - 1] > 4) {
                    writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]");
                    return;
                }
            }

            Card[] newCards = game.getCardsFromDeck(cardsToChange.length);
            Card[] oldPlayerCards = player.getPlayerHand().getCards();

            for (int i = 0; i < cardsToChange.length; i++) {
                oldPlayerCards[cardsToChange[i]] = newCards[i];
            }

            player.getPlayerHand().setCards(oldPlayerCards);
            player.setCardsChanged(true);

            writeMessageToAllClients(player.getUsername() + " has changed his cards");

            if (game.isChangingCardsEnded()) {
                game.beginSecondRound();
                writeMessageToAllClients("The second round of betting has started");
                writeMessageToClient(game.getCurrentPlayer().getKey(), "Now it is your turn");
            } else {
                writeMessageToAllClients("\nWaiting for other players to change their cards (" + (playersNumber - game.getPlayersWhoChangedCardsCount()) + " more players)");
            }
        } catch (NumberFormatException e) {
            writeMessageToClient(key, "usage: change <card1> [<card2>] ... [<card5>]");
        }
    }

    private void handleShowCommand(Player player, String[] commandParts, SelectionKey key, Game.GameState gameState, boolean isCurrentPlayerMakingAMove) {
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

        sb.append(game.getPlayersMoneyAndBetAsString());
        writeMessageToClient(key, sb.toString());
    }

    private void handleCommands(String command, SelectionKey key) {
        String[] commandParts = command.split(" ");

        if (commandParts.length == 0) {
            return;
        }

        Game.GameState gameState = game.getState();

        boolean isCurrentPlayerMakingAMove = game.isMakingMove(clients.get(key));
        Player player = clients.get(key);

        switch (commandParts[0]) {
            case "help":
                handleHelpCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "username":
                handleUsernameCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "start":
                handleStartCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "call":
                handleCallCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "raise":
                handleRaiseCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "fold":
                handleFoldCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "change":
                handleChangeCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            case "show":
                handleShowCommand(player, commandParts, key, gameState, isCurrentPlayerMakingAMove);
                break;
            default:
                writeMessageToClient(key, "Unknown command");
                break;
        }

        checkForFirstRoundEnd(gameState);
    }

    private void checkForFirstRoundEnd(Game.GameState gameState) {
        if (gameState == Game.GameState.FIRST_ROUND_BETS && game.isBettingEnded()) {
            game.beginChangingCards();
            writeMessageToAllClients("The first round of betting has ended\nYou can now change your cards");
        }
    }

    private void handleAccept(ServerSocketChannel mySocket) throws IOException {
        logger.info("Accepting connection");

        SocketChannel clientSocket = mySocket.accept();
        clientSocket.configureBlocking(false);

        SelectionKey clientKey = clientSocket.register(selector, SelectionKey.OP_READ);

        clients.put(clientKey, new Player(clientKey, ante, game));
    }

    private void handleRead(SelectionKey key) throws IOException {
        logger.info("Reading data");

        SocketChannel client = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        client.read(buffer);

        String data = new String(buffer.array()).trim();

        logger.info("Received data length: " + data.length());

        if (data.length() > 0) {
            logger.info("Received message: " + data);
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

    public void start() {
        try {
            ServerSocketChannel socket = ServerSocketChannel.open();
            ServerSocket serverSocket = socket.socket();
            serverSocket.bind(new InetSocketAddress("localhost", portNumber));
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
            selector.close();
        } catch (IOException e) {
            logger.severe("Error: " + e.getMessage());
            logger.severe("IO error");
            System.exit(1);
        }
    }
}
