package pl.edu.agh.kis.pz1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PokerClient {
    private static Logger logger = Logger.getLogger(PokerClient.class.getName());

    class CustomRecordFormatter extends Formatter {
        @Override
        public String format(final LogRecord r) {
            StringBuilder sb = new StringBuilder();
            sb.append(formatMessage(r)).append(System.getProperty("line.separator"));
            if (null != r.getThrown()) {
                sb.append("Throwable occurred: ");
                Throwable t = r.getThrown();
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                    t.printStackTrace(pw);
                    sb.append(sw.toString());
                } catch (Exception ex) {
                    // ignore all exceptions here
                }
            }
            return sb.toString();
        }
    }

    static class CheckServerAnswers extends Thread {
        private SocketChannel socketChannel;

        public CheckServerAnswers(SocketChannel socketChannelArg) {
            socketChannel = socketChannelArg;
        }

        @Override
        public void run() {
            ByteBuffer buffer;
            try {
                socketChannel.configureBlocking(true);

                while (true) {
                    buffer = ByteBuffer.allocate(1024);

                    if (socketChannel.read(buffer) > 0) {
                        String message = (new String(buffer.array())).trim();
                        logger.info(message);
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

    private static Scanner scanner = new Scanner(System.in);

    private final String serverAddress;
    private final int portNumber;

    public PokerClient(String serverAddressParam, int portNumberParam) {
        serverAddress = serverAddressParam;
        portNumber = portNumberParam;

        logger.setUseParentHandlers(false);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new CustomRecordFormatter());
        logger.addHandler(consoleHandler);
    }

    public void clientLoop() {
        logger.info("Starting poker client");

        try {
            SocketChannel clientSocket = SocketChannel.open(new InetSocketAddress(serverAddress, portNumber));

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

            logger.info("Client connection closed");
        } catch (IOException e) {
            logger.severe("Cannot connect to server");
            System.exit(1);
        }
    }
}
