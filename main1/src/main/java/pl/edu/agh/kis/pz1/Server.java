package pl.edu.agh.kis.pz1;

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
    private static Logger logger = Logger.getLogger(Server.class.getName());

    private Map<SelectionKey, Player> clients = new HashMap<SelectionKey, Player>();
    private Set<String> chosenNames = new HashSet<String>();

    Selector selector;

    private int playersNumber;
    private int portNumber;
    private int ante;

    private Game game;

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

    private void handleCommands(String command, SelectionKey key) {
        String[] commandParts = command.split(" ");

        if (commandParts.length == 0) {
            return;
        }

        Game.GameState gameState = game.getState();

        boolean isCurrentPlayerMakingAMove = game.isMakingMove(clients.get(key));

        switch (commandParts[0]) {
            case "help":
                if (commandParts.length == 1) {
                    writeMessageToClient(key, "Available commands: help, username, exit, check, call, raise, fold, show");
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
                        case "check":
                            writeMessageToClient(key, "check - checks if server has sent something");
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
                        case "allin":
                            writeMessageToClient(key, "show - shows your current hand and money");
                            break;
                        default:
                            writeMessageToClient(key, "Unknown command");
                            break;
                    }
                }
                break;
            case "username":
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
                break;
            case "start":
                if (gameState != Game.GameState.WAITING_FOR_PLAYERS && gameState != Game.GameState.WAITING_FOR_READY) {
                    writeMessageToClient(key, "The game has already started");
                } else if (gameState == Game.GameState.WAITING_FOR_READY) {
                    writeMessageToClient(key, "Your status is ready now");
                    if (clients.get(key).setReady(true) == playersNumber) {
                        game.start();

                        writeMessageToAllClients("The game has started\nYou can check your cards and other stats by typing \'show\'");
                    }
                } else {
                    writeMessageToClient(key, "There are not enough players (need " + (playersNumber - game.getReadyPlayersCount()) + " more players)");
                }
                break;
            case "call":
                if (!isCurrentPlayerMakingAMove) {
                    writeMessageToClient(key, "It is not your turn, you cannot call now");
                    break;
                }
                break;
            case "raise":
                if (!isCurrentPlayerMakingAMove) {
                    writeMessageToClient(key, "It is not your turn, you cannot raise now");
                    break;
                }
                break;
            case "fold":
                if (!isCurrentPlayerMakingAMove) {
                    writeMessageToClient(key, "It is not your turn, you cannot fold now");
                    break;
                }
                break;
            case "show":
                StringBuilder sb = new StringBuilder();

                if (clients.get(key).getUsername() != null) {
                    sb.append("Player: " + clients.get(key).getUsername() + "\n");
                } else {
                    sb.append("Player: UNKNOWN - SET USERNAME \n");
                }
                if (game.canShowPlayersHand() && clients.get(key).getPlayerHand() != null) {
                    sb.append(clients.get(key).getPlayerHand().toString());
                } else {
                    sb.append("You cannot show your hand now\n");
                }

                sb.append(game.getPlayersMoneyAndBetAsString());
                writeMessageToClient(key, sb.toString());
                break;
            default:
                writeMessageToClient(key, "Unknown command");
                break;
        }
    }

    private void handleAccept(ServerSocketChannel mySocket, SelectionKey key) throws IOException {
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
                logger.info("Connection closed");
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

            while (true) {
                selector.select();
                logger.info("Selecting");

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    if (key.isAcceptable()) {
                        // zaakceptuj połączenie
                        handleAccept(socket, key);
                    } else if (key.isReadable()) {
                        // odczytaj przesłane dane
                        handleRead(key);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            logger.severe("IO error");
            System.exit(1);
        }
    }
}
