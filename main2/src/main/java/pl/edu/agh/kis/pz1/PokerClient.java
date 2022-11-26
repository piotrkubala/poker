package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.logging.Logger;

public class PokerClient {
    static class CheckServerAnswers extends Thread {
        private SocketChannel socketChannel;
        private ByteBuffer buffer;

        public CheckServerAnswers(SocketChannel socketChannel_) {
            socketChannel = socketChannel_;
        }

        @Override
        public void run() {
            try {
                socketChannel.configureBlocking(true);

                while (true) {
                    buffer = ByteBuffer.allocate(1024);

                    if (socketChannel.read(buffer) > 0) {
                        String message = (new String(buffer.array())).trim();
                        System.out.println(message);
                        buffer.clear();

                        if (message.equals("exit")) {
                            return;
                        }
                    }
                }

            } catch (Exception e) {
                logger.severe("Cannot read from server");
                System.exit(-1);
            }
        }
    }

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

            CheckServerAnswers checkServerAnswers = new CheckServerAnswers(clientSocket);
            checkServerAnswers.start();

            while(true) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                String command = scanner.nextLine();

                if (!checkServerAnswers.isAlive()) {
                    break;
                }

                if (command.length() == 0) {
                    continue;
                }

                buffer.put(command.getBytes(), 0, command.length());
                buffer.flip();

                clientSocket.write(buffer);
            }

            clientSocket.close();

            System.out.println("Client connection closed");
        } catch (IOException e) {
            logger.severe("Cannot connect to server");
            System.exit(1);
        }
    }
}
