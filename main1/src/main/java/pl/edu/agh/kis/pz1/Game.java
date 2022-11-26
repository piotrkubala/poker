package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Deck;
import pl.edu.agh.kis.pz1.util.Hand;

import java.util.*;

public class Game {
    public enum GameState{
        WAITING_FOR_PLAYERS("Waiting for players"),
        WAITING_FOR_READY("Waiting for ready"),
        FIRST_ROUND_BETS("First round bets"),
        SECOND_ROUND_BETS("Second round bets"),
        AFTER_SHOWDOWN("After showdown"),
        NEW_GAME_PREPARATION("New game preparation"),
        FINISHED("Finished");

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
    private int currentGamePool = 0;
    private int currentBet = 0;

    // the 0th player is small blind, the 1st is big blind; all in order
    private Player[] playersByNumber;
    private Vector<Integer> playersOrder;

    private Deck gameDeck;

    private int ante;
    private int playersNumber;
    private int readyPlayers = 0;

    private int readyPlayersForStart = 0;

    private int stillPlayingPlayers = 4;

    public Game(int playersNumber_, int ante_) {
        playersNumber = playersNumber_;
        ante = ante_;

        playersOrder = new Vector<Integer>(playersNumber);
        playersByNumber = new Player[playersNumber];

        for (int i = 0; i < playersNumber; i++) {
            playersOrder.add(i);
        }

        Collections.shuffle(playersOrder);
    }

    public void nextPlayerIsReady(Player player) {
        int index = playersOrder.elementAt(readyPlayers);

        if (index == 0) {
            player.setSmallBlind(true);
        } else if (index == 1) {
            player.setBigBlind(true);
        }
        playersByNumber[index] = player;

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
        return state == GameState.AFTER_SHOWDOWN || state == GameState.FIRST_ROUND_BETS || state == GameState.SECOND_ROUND_BETS;
    }

    public String getPlayersMoneyAndBetAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playersNumber; i++) {
            Player player = playersByNumber[i];
            if (playersByNumber[i] != null) {
                if (player.isSmallBlind()) {
                    sb.append("Small Blind: ");
                } else if (player.isBigBlind()) {
                    sb.append("Big Blind:   ");
                } else {
                    sb.append("             ");
                }

                sb.append(player.getUsername() + " has " + player.getMoney() + "$ left" + " current bet: " + player.getBet() + "$\n");
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
        this.currentBet = currentBet;
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
}
