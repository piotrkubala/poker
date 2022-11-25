package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.logging.Logger;

public class PokerClient {
    private static Logger logger = Logger.getLogger(PokerClient.class.getName());
    private static Scanner scanner = new Scanner(System.in);

    private final String serverAddress;
    private final int portNumber;

    public PokerClient(String serverAddress_, int portNumber_) {
        serverAddress = serverAddress_;
        portNumber = portNumber_;
    }

    public void clientLoop() {
        logger.info("Starting poker client");

        try {
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress(serverAddress, portNumber));
            clientSocket.configureBlocking(false);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            byte[] bytes = new byte[1024];

            while(true) {
                buffer.clear();

                String command = scanner.nextLine();

                if (command.equals("check")) {
                    logger.info("Check command");
                    if (clientSocket.read(buffer) > 0) {
                        logger.info("Received message: " + new String(buffer.array()));
                    }

                    continue;
                }
                buffer.put(command.getBytes(), 0, command.length());
                buffer.flip();

                clientSocket.write(buffer);

                if (command.equals("exit")) {
                    logger.info("Exit command");
                    break;
                }

                buffer.clear();
                if (clientSocket.read(buffer) > 0) {
                    logger.info("Received message: " + new String(buffer.array()));
                }
            }

            clientSocket.close();

            logger.info("Client connection closed");
        } catch (IOException e) {
            logger.severe("Cannot connect to server");
            System.exit(1);
        }
    }
}
