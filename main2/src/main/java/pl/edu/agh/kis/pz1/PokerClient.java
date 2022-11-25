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

            byte[] bytes = new byte[1024];

            while(true) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                String command = scanner.nextLine();

                if (command.equals("check")) {
                    clientSocket.configureBlocking(false);

                    if (clientSocket.read(buffer) > 0) {
                        System.out.println((new String(buffer.array())).trim());
                    }

                    clientSocket.configureBlocking(true);

                    continue;
                }

                if (command.length() == 0) {
                    continue;
                }

                buffer.put(command.getBytes(), 0, command.length());
                buffer.flip();

                clientSocket.write(buffer);

                if (command.equals("exit")) {
                    logger.info("Exit command");
                    break;
                }

                ByteBuffer newBuffer = ByteBuffer.allocate(1024);
                if (clientSocket.read(newBuffer) > 0) {
                    System.out.println((new String(newBuffer.array())).trim());
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
