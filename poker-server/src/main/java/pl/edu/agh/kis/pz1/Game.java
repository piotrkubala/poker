package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Hand;

import java.util.*;

public class Game {
    public enum GameState{
        WAITING_FOR_PLAYERS("Waiting for players"),
        WAITING_FOR_READY("Waiting for ready"),
        FIRST_ROUND_BETS("First round bets"),
        CARDS_CHANGING("Cards changing"),
        SECOND_ROUND_BETS("Second round bets"),
        AFTER_SHOWDOWN("After showdown");

        private String name;

        GameState(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }

    private GameState state = GameState.WAITING_FOR_PLAYERS;

    private int currentPlayerIndex = 0;
    private int turn = 0;
    private int turnsSinceLastBetIncrease = 0;
    private int currentGamePool = 0;
    private int currentBet = 0;

    // the 0th player is small blind, the 1st is big blind; all in order
    private Player[] playersByNumber;
    private Player[] playersInJoinOrder;
    private ArrayList<Integer> playersOrder;

    private Deck gameDeck;

    private int ante;
    private int playersNumber;
    private int readyPlayers = 0;

    private int readyPlayersForStart = 0;

    private int stillPlayingPlayers;

    private int playersWhoChangedCards = 0;

    private int playersWhoAreReadyForNextRound = 0;

    public Game(int playersNumber_, int ante_) {
        playersNumber = playersNumber_;
        ante = ante_;
        stillPlayingPlayers = playersNumber;

        playersOrder = new ArrayList<Integer>(playersNumber);
        playersByNumber = new Player[playersNumber];
        playersInJoinOrder = new Player[playersNumber];

        for (int i = 0; i < playersNumber; i++) {
            playersOrder.add(i);
        }

        Collections.shuffle(playersOrder);
    }

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

    public int getReadyPlayersCount() {
        return readyPlayers;
    }

    public boolean isReady() {
        return readyPlayers == playersNumber;
    }

    public GameState getState() {
        return state;
    }

    public boolean canShowPlayersHand() {
        return state == GameState.AFTER_SHOWDOWN || state == GameState.FIRST_ROUND_BETS || state == GameState.SECOND_ROUND_BETS || state == GameState.CARDS_CHANGING;
    }

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

                sb.append(player.getUsername() + " has " + player.getMoney() + "$ left, current bet: " + player.getBet() + "$\n");
            }
        }
        return sb.toString();
    }

    public Player getCurrentPlayer() {
        return playersByNumber[currentPlayerIndex];
    }

    public void nextPlayerMove() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % playersNumber;
        } while (!playersByNumber[currentPlayerIndex].isPlaying());
    }

    public boolean isMakingMove(Player player) {
        return player == getCurrentPlayer() && (state == GameState.FIRST_ROUND_BETS || state == GameState.SECOND_ROUND_BETS);
    }

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

    public Card[] getCardsFromDeck(int number) {
        return gameDeck.getCards(number);
    }

    public void beginChangingCards() {
        if (state == GameState.FIRST_ROUND_BETS) {
            state = GameState.CARDS_CHANGING;
            turnsSinceLastBetIncrease = 0;
        }
    }

    public void addMoneyToPool(int money) {
        currentGamePool += money;
    }

    public Player getSmallBlind() {
        return playersByNumber[0];
    }

    public Player getBigBlind() {
        return playersByNumber[1];
    }

    public void increaseReadyPlayersForStart() {
        readyPlayersForStart++;
    }

    public void decreaseReadyPlayersForStart() {
        readyPlayersForStart--;
    }

    public int getReadyPlayersForStart() {
        return readyPlayersForStart;
    }

    public int getCurrentBet() {
        return currentBet;
    }

    public void setCurrentBet(int currentBet) {
        if (currentBet > this.currentBet) {
            this.currentBet = currentBet;
            turnsSinceLastBetIncrease = 0;
        } else {
            turnsSinceLastBetIncrease++;
        }
    }

    public void increaseStillPlayingPlayers() {
        stillPlayingPlayers++;
    }

    public void decreaseStillPlayingPlayers() {
        stillPlayingPlayers--;
    }

    public int getStillPlayingPlayers() {
        return stillPlayingPlayers;
    }

    public boolean isGameFinished() {
        return stillPlayingPlayers == 1;
    }

    public boolean isGameFinishedForPlayer(Player player) {
        return stillPlayingPlayers == 1 && player.isPlaying();
    }

    public boolean isBettingEnded() {
        return turnsSinceLastBetIncrease >= stillPlayingPlayers;
    }

    public void increasePlayersWhoChangedCards() {
        playersWhoChangedCards++;
    }

    public void decreasePlayersWhoChangedCards() {
        playersWhoChangedCards--;
    }

    public boolean isChangingCardsEnded() {
        return playersWhoChangedCards >= stillPlayingPlayers;
    }

    public int getPlayersWhoChangedCardsCount() {
        return playersWhoChangedCards;
    }

    public void beginSecondRound() {
        currentPlayerIndex = 0;
        state = GameState.SECOND_ROUND_BETS;
    }

    private List<Player> getWinners() {
        final double eps = 1.0-12;
        Player[] players = new Player[stillPlayingPlayers];

        for (int i = 0, index = 0; i < playersNumber; i++) {
            Player player = playersByNumber[i];
            if (player.isPlaying()) {
                players[index++] = player;

                player.calculateHandValue();
            }
        }

        Arrays.sort(players, Collections.reverseOrder());

        double firstPlayerValue = players[0].getHandValue();
        List<Player> winners = new ArrayList<Player>();
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

    private int getClosestWinnerIndexToBigBlind(List<Player> winners) {
        if (winners.size() == 1) {
            return 0;
        }

        if (winners.get(0).isSmallBlind()) {
            return 1;
        }
        return 0;
    }

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

    public List<Player> showDownAndDivideMoneyFromPool() {
        if (state != GameState.FIRST_ROUND_BETS && state != GameState.SECOND_ROUND_BETS) {
            return null;
        }

        state = GameState.AFTER_SHOWDOWN;

        List<Player> winners = getWinners();

        setWinnersMoney(winners);

        return winners;
    }

    public Player[] getPlayersByNumber() {
        return playersByNumber;
    }

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

    public void increasePlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound++;
    }

    public void decreasePlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound--;
    }

    public boolean isNextRoundReady() {
        return playersWhoAreReadyForNextRound == stillPlayingPlayers;
    }

    public void resetPlayersWhoAreReadyForNextRoundCount() {
        playersWhoAreReadyForNextRound = 0;
    }
}
