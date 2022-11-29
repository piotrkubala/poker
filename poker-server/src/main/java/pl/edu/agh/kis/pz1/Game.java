package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Hand;

import java.util.*;

/**
 * Server class modelling the game of poker on the server
 * @author Piotr Kubala
 */
public class Game {
    /**
     * enum representing possible game states
     */
    public enum GameState{
        WAITING_FOR_PLAYERS("Waiting for players"),
        WAITING_FOR_READY("Waiting for ready"),
        FIRST_ROUND_BETS("First round bets"),
        CARDS_CHANGING("Cards changing"),
        SECOND_ROUND_BETS("Second round bets"),
        AFTER_SHOWDOWN("After showdown");

        private final String name;

        GameState(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    GameState state = GameState.WAITING_FOR_PLAYERS;

    int currentPlayerIndex = 0;
    int turnsSinceLastBetIncrease = 0;
    int currentGamePool = 0;
    int currentBet = 0;

    // the 0th player is small blind, the 1st is big blind; all in order
    final Player[] playersByNumber;
    final Player[] playersInJoinOrder;
    final ArrayList<Integer> playersOrder;

    Deck gameDeck;

    private final int playersNumber;
    private int readyPlayers = 0;

    private int readyPlayersForStart = 0;

    private int stillPlayingPlayers;

    private int playersWhoChangedCards = 0;

    private int playersWhoAreReadyForNextRound = 0;

    /**
     * constructor for the Game class
     * @param playersNumberArg number of players in the game necessary to start
     */
    public Game(int playersNumberArg) {
        playersNumber = playersNumberArg;
        stillPlayingPlayers = playersNumber;

        playersOrder = new ArrayList<>(playersNumber);
        playersByNumber = new Player[playersNumber];
        playersInJoinOrder = new Player[playersNumber];

        for (int i = 0; i < playersNumber; i++) {
            playersOrder.add(i);
        }

        Collections.shuffle(playersOrder);
    }

    /**
     * method used to count number of ready players ("start" command)
     * @param player player to be added to the ready players counter
     */
    public void nextPlayerIsReady(Player player) {
        int index = playersOrder.get(readyPlayers);

        if (index == 0) {
            player.setSmallBlind(true);
        } else if (index == 1) {
            player.setBigBlind(true);
        }
        playersByNumber[index] = player;
        playersInJoinOrder[readyPlayers] = player;

        readyPlayers++;
    }

    /**
     * method used to count number of players ready to start the game ("ready" command)
     * @return number of ready players
     */
    public int getReadyPlayersCount() {
        return readyPlayers;
    }

    /**
     * method used to check if there is enough players to start the game
     * @return true if there is enough players, false otherwise
     */
    public boolean isReady() {
        return readyPlayers == playersNumber;
    }

    /**
     * getter for the game state
     * @return current game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * checks if player can show his/her cards
     * @return true if player can show his/her cards, false otherwise
     */
    public boolean canShowPlayersHand() {
        return state == GameState.AFTER_SHOWDOWN || state == GameState.FIRST_ROUND_BETS || state == GameState.SECOND_ROUND_BETS || state == GameState.CARDS_CHANGING;
    }

    /**
     * returns string representation of the game state
     * @return string representation of the game state useful for the client
     */
    public String getPlayersMoneyAndBetAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playersNumber; i++) {
            Player player = playersByNumber[i];
            if (playersByNumber[i] != null) {
                if (!player.isPlaying()) {
                    sb.append("Has ended:   ");
                } else if (player.isSmallBlind()) {
                    sb.append("Small Blind: ");
                } else if (player.isBigBlind()) {
                    sb.append("Big Blind:   ");
                } else {
                    sb.append("             ");
                }

                sb.append(player.getUsername());
                sb.append(" has " );
                sb.append(player.getMoney());
                sb.append("$ left, current bet: ");
                sb.append(player.getBet());
                sb.append("$\n");
            }
        }
        return sb.toString();
    }

    /**
     * get current player
     * @return current player
     */
    public Player getCurrentPlayer() {
        return playersByNumber[currentPlayerIndex];
    }

    /**
     * change currentPlayerIndex to the next player
     */
    public void nextPlayerMove() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % playersNumber;
        } while (!playersByNumber[currentPlayerIndex].isPlaying());
    }

    /**
     * method used to check if player is making a move now
     * @param player player to be checked
     * @return true if player is making a move now, false otherwise
     */
    public boolean isMakingMove(Player player) {
        return player == getCurrentPlayer() && (state == GameState.FIRST_ROUND_BETS || state == GameState.SECOND_ROUND_BETS);
    }

    /**
     * call after all players have joined the game
     */
    public void allPlayersJoined() {
        if (state == GameState.WAITING_FOR_PLAYERS) {
            state = GameState.WAITING_FOR_READY;
        }
    }

    public void start() {
        if (state == GameState.WAITING_FOR_READY) {
            state = GameState.FIRST_ROUND_BETS;
            gameDeck = new Deck();
            gameDeck.shuffle();

            for (int i = 0; i < playersNumber; i++) {
                playersByNumber[i].giveHand(new Hand(gameDeck.getCards(Hand.HAND_SIZE)));
            }

            currentPlayerIndex = 0;
        }
    }

    /**
     * gen number cards from the deck
     * @param number number of cards to be returned
     * @return array of cards
     */
    public Card[] getCardsFromDeck(int number) {
        return gameDeck.getCards(number);
    }

    /**
     * method should be called before beginning of change cards phase
     */
    public void beginChangingCards() {
        if (state == GameState.FIRST_ROUND_BETS) {
            state = GameState.CARDS_CHANGING;
            turnsSinceLastBetIncrease = 0;
        }
    }

    /**
     * add money to the game pool
     * @param money money to be added
     */
    public void addMoneyToPool(int money) {
        currentGamePool += money;
    }

    /**
     * return small blind player
     * @return small blind player
     */
    public Player getSmallBlind() {
        return playersByNumber[0];
    }

    /**
     * return big blind player
     * @return big blind player
     */
    public Player getBigBlind() {
        return playersByNumber[1];
    }

    /**
     * increase number of ready players
     */
    public void increaseReadyPlayersForStart() {
        readyPlayersForStart++;
    }

    /**
     * decrease number of ready players
     */
    public void decreaseReadyPlayersForStart() {
        readyPlayersForStart--;
    }

    /**
     * get number of ready players
     * @return number of ready players
     */
    public int getReadyPlayersForStart() {
        return readyPlayersForStart;
    }

    /**
     * get current bet
     * @return current bet
     */
    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * set current bet
     * @param currentBet current bet
     */
    public void setCurrentBet(int currentBet) {
        if (currentBet > this.currentBet) {
            this.currentBet = currentBet;
            turnsSinceLastBetIncrease = 0;
        } else {
            turnsSinceLastBetIncrease++;
        }
    }

    /**
     * increase stil playing players count
     */
    public void increaseStillPlayingPlayers() {
        stillPlayingPlayers++;
    }

    /**
     * decrease stil playing players count
     */
    public void decreaseStillPlayingPlayers() {
        stillPlayingPlayers--;
    }

    /**
     * get stil playing players count
     * @return stil playing players count
     */
    public int getStillPlayingPlayers() {
        return stillPlayingPlayers;
    }

    /**
     * check if game is finished
     * @return true if game is finished, false otherwise
     */
    public boolean isGameFinished() {
        return stillPlayingPlayers == 1;
    }

    /**
     * if game is finished for players
     * @return true if game is finished for players, false otherwise
     */
    public boolean isGameFinishedForPlayer(Player player) {
        return stillPlayingPlayers == 1 && player.isPlaying();
    }

    /**
     * check if betting phase ended
     * @return true if betting phase ended, false otherwise
     */
    public boolean isBettingEnded() {
        return turnsSinceLastBetIncrease >= stillPlayingPlayers;
    }

    /**
     * increase number of players who have changed cards
     */
    public void increasePlayersWhoChangedCards() {
        playersWhoChangedCards++;
    }

    /**
     * decrease number of players who have changed cards
     */
    public void decreasePlayersWhoChangedCards() {
        playersWhoChangedCards--;
    }

    /**
     * check if changing cards phase ended
     * @return true if changing cards phase ended, false otherwise
     */
    public boolean isChangingCardsEnded() {
        return playersWhoChangedCards >= stillPlayingPlayers;
    }

    /**
     * get number of players who have changed cards
     * @return number of players who have changed cards
     */
    public int getPlayersWhoChangedCardsCount() {
        return playersWhoChangedCards;
    }

    /**
     * begin second betting phase
     */
    public void beginSecondRound() {
        currentPlayerIndex = 0;
        state = GameState.SECOND_ROUND_BETS;
    }

    /**
     * get winners
     * @return winners list
     */
    private List<Player> getWinners() {
        final double eps = 1.0-12;
        Player[] players = new Player[stillPlayingPlayers];

        int index = 0;
        for (int i = 0; i < playersNumber; i++) {
            Player player = playersByNumber[i];
            if (player.isPlaying()) {
                players[index++] = player;

                player.calculateHandValue();
            }
        }

        Arrays.sort(players, Collections.reverseOrder());

        double firstPlayerValue = players[0].getHandValue();
        List<Player> winners = new ArrayList<>();
        winners.add(players[0]);

        for (int i = 1; i < stillPlayingPlayers; i++) {
            if (Math.abs(players[i].getHandValue() - firstPlayerValue) < eps) {
                winners.add(players[i]);
            } else {
                break;
            }
        }

        return winners;
    }

    /**
     * get players index closer to big blind in clockwise order in array playersByNumber
     * @return players index closer to big blind in clockwise order in array playersByNumber
     */
    private int getClosestWinnerIndexToBigBlind(List<Player> winners) {
        if (winners.size() == 1) {
            return 0;
        }

        if (winners.get(0).isSmallBlind()) {
            return 1;
        }
        return 0;
    }

    /**
     * set money for winners
     * @param winners winners list
     */
    private void setWinnersMoney(List<Player> winners) {
        int moneyForWinner = currentGamePool / winners.size();
        int moneyForWinnerRemainder = currentGamePool % winners.size();

        winners.get(getClosestWinnerIndexToBigBlind(winners)).addMoney(moneyForWinnerRemainder);

        for (Player player : winners) {
            player.addMoney(moneyForWinner);
            player.setWinner(true);
        }

        for (Player player : playersByNumber) {
            player.setBet(0);
        }
    }

    /**
     * showdown and divide money between winners
     * @return winners list
     */
    public List<Player> showDownAndDivideMoneyFromPool() {
        if (state != GameState.FIRST_ROUND_BETS && state != GameState.SECOND_ROUND_BETS) {
            return new ArrayList<>();
        }

        state = GameState.AFTER_SHOWDOWN;

        List<Player> winners = getWinners();

        setWinnersMoney(winners);

        return winners;
    }

    /**
     * gets playersByNumber array
     * @return playersByNumber array
     */
    public Player[] getPlayersByNumber() {
        return playersByNumber;
    }

    /**
     * reset all parameters necessary for new game
     */
    public void startNewRound() {
        state = GameState.WAITING_FOR_READY;
        currentGamePool = 0;
        currentBet = 0;
        turnsSinceLastBetIncrease = 0;
        playersWhoChangedCards = 0;
        stillPlayingPlayers = playersNumber;
        readyPlayersForStart = 0;
        currentPlayerIndex = 0;
        gameDeck = null;
        playersWhoAreReadyForNextRound = 0;

        Collections.shuffle(playersOrder);
        for (int i = 0; i < playersNumber; i++) {
            int index = playersOrder.get(i);
            playersByNumber[index] = playersInJoinOrder[i];
        }

        for (Player player : playersByNumber) {
            player.resetPlayerForNextRound();
        }

        playersByNumber[0].setSmallBlind(true);
        playersByNumber[1].setBigBlind(true);
    }

    /**
     * increase number of players who are ready for next round
     */
    public void increasePlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound++;
    }

    /**
     * decrease number of players who are ready for next round
     */
    public void decreasePlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound--;
    }

    /**
     * checks if next round can be started
     * @return true if next round can be started, false otherwise
     */
    public boolean isNextRoundReady() {
        return playersWhoAreReadyForNextRound == stillPlayingPlayers;
    }

    /**
     * reset players who are ready for next round counter to 0
     */
    public void resetPlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound = 0;
    }
}
