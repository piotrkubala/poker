package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Server {
    private static Logger logger = Logger.getLogger(Server.class.getName());

    private Map<SelectionKey, Player> clients = new HashMap<SelectionKey, Player>();

    Selector selector;

    private int playersNumber;
    private int portNumber;
    private int ante;

    private Game game;

    public Server(int playersNumber_, int port_, int ante_) {
        playersNumber = playersNumber_;
        portNumber = port_;
        ante = ante_;

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
        }
    }

    private void handleCommands(String command, SelectionKey key) {
        String[] commandParts = command.split(" ");

        logger.info("Received command: " + commandParts.toString());

        if (commandParts.length == 0) {
            return;
        }

        Game.GameState gameState = game.getState();

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
                    break;
                }

                if (commandParts.length != 2) {
                    writeMessageToClient(key, "usage: username <username>");
                    return;
                }

                if (clients.get(key).getUsername() != null) {
                    writeMessageToClient(key, "You have already set your username");
                    return;
                }
                clients.get(key).setName(commandParts[1]);
                break;
            case "start":
                if (gameState != Game.GameState.WAITING_FOR_PLAYERS) {
                    writeMessageToClient(key, "The game has already started");
                } else if (game.isReady()) {
                    writeMessageToClient(key, "Game is ready");
                } else {
                    writeMessageToClient(key, "There are not enough players (need " + (playersNumber - game.getReadyPlayers()) + " more players)");
                }
                break;
            case "call":
                break;
            case "raise":
                break;
            case "fold":
                break;
            case "show":
                if (game.canShowPlayersHand()) {
                    writeMessageToClient(key, "Your hand: " + clients.get(key).getPlayerHand().toString());
                } else {
                    writeMessageToClient(key, "You cannot show your hand now");
                }
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
                client.close();
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
