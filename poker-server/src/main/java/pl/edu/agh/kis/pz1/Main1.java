package pl.edu.agh.kis.pz1;

import java.util.logging.Logger;

/**
 * Klasa Main serwera gry w pokera
 * @author Piotr Kubala, schemat klas: Paweł Skrzyński
 */
public class Main1 {
    private static Logger logger = Logger.getLogger(Main1.class.getName());

    public static void main(String[] args) {
        if (args.length < 3) {
            logger.severe("Not enough arguments");
            logger.info("Usage: java -jar server.jar <number of players> <port> <ante>");
            System.exit(1);
        }


        try {
            int playersNumber, portNumber, ante;

            playersNumber = Integer.parseInt(args[0]);
            portNumber = Integer.parseInt(args[1]);
            ante = Integer.parseInt(args[2]);

            while (true) {
                Server gameServer = new Server(playersNumber, portNumber, ante);
                gameServer.start();
            }
        } catch (NumberFormatException e) {
            logger.severe("Wrong arguments");
            logger.info("Usage: java -jar server.jar <number of players> <port>");
            System.exit(1);
        }
    }
}
