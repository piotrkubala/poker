package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.poker.common.BadProgramArgumentException;

import java.util.logging.Logger;

/**
 * Klasa Main serwera gry w pokera
 * @author Piotr Kubala, schemat klas: Paweł Skrzyński
 */
public class Main1 {
    private static final Logger logger = Logger.getLogger(Main1.class.getName());

    private static boolean stopServerInNextRound = false;

    private static void stopSignalHandler() {
        stopServerInNextRound = true;
        logger.info("Server will be stopped in next round");
    }

    public static void main(String[] args) {
        try {
            if (args.length < 3) {
                throw new BadProgramArgumentException("Not enough arguments");
            }
        } catch (BadProgramArgumentException e) {
            logger.severe(e.getMessage());
            logger.info("Usage: java -jar server.jar <number of players> <address> <port> <money per player>");
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopSignalHandler()));

        try {
            int playersNumber, portNumber, playersMoney;

            playersNumber = Integer.parseInt(args[0]);
            portNumber = Integer.parseInt(args[2]);
            playersMoney = Integer.parseInt(args[3]);

            while (!stopServerInNextRound) {
                Server gameServer = new Server(playersNumber, args[1], portNumber, playersMoney, false);
                gameServer.start();
            }
        } catch (NumberFormatException e) {
            logger.severe("Wrong arguments");
            logger.info("Usage: java -jar server.jar <number of players> <port>");
            System.exit(1);
        }
    }
}
