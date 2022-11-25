package pl.edu.agh.kis.pz1;

import java.util.logging.Logger;

/**
 *Klasa Main klienta gry w pokera
 * @author Piotr Kubala, schemat klas: Paweł Skrzyński
 */
public class Main2 {
    private static Logger logger = Logger.getLogger(Main2.class.getName());

    public static void main(String[] args) {
        if (args.length < 2) {
            logger.severe("Not enough arguments");
            logger.info("Usage: java -jar client.jar <server address> <port>");
            System.exit(1);
        }

        try {
            String serverAddress = args[0];
            int portNumber = Integer.parseInt(args[1]);
            PokerClient pokerClient = new PokerClient(serverAddress, portNumber);
            pokerClient.clientLoop();
        } catch (NumberFormatException e) {
            logger.severe("Wrong arguments");
            logger.info("Usage: java -jar client.jar <server address> <port>");
            System.exit(1);
        }
    }
}
