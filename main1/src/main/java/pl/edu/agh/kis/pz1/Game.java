package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Deck;

import java.nio.channels.SelectionKey;
import java.util.*;

public class Game {
    public enum GameState{
        WAITING_FOR_PLAYERS,
        WAITING_FOR_READY,
        FIRST_ROUND_BETS,
        SECOND_ROUND_BETS,
        AFTER_SHOWDOWN,
        NEW_GAME_PREPARATION,
        FINISHED
    }

    private GameState state = GameState.WAITING_FOR_PLAYERS;

    // the 0th player is small blind, the 1st is big blind; all in order
    private Player[] playersByNumber;
    private Vector<Integer> playersOrder;

    private Deck gameDeck;
    private int playersNumber;
    private int readyPlayers = 0;

    public void Game(int playersNumber_) {
        playersNumber = playersNumber_;
        playersOrder = new Vector<Integer>(playersNumber);
        playersByNumber = new Player[playersNumber];

        for (int i = 0; i < playersNumber; i++) {
            playersOrder.set(i, i);
        }

        Collections.shuffle(playersOrder);
    }

    public void nextPlayerIsReady(Player player) {
        playersByNumber[playersOrder.elementAt(readyPlayers)] = player;

        readyPlayers++;
    }

    public int getReadyPlayers() {
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
}
