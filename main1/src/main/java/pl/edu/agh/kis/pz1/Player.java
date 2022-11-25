package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Hand;

import java.nio.channels.SelectionKey;

public class Player {
    private SelectionKey key;

    private String name;

    private int money;
    private int bet = 0;

    private boolean folded = false;

    private Hand playerHand;

    private boolean isReady;

    private Game game;
    private static int readyPlayers = 0;

    public Player(SelectionKey key_, int money_, Game game_) {
        key = key_;
        money = money_;
        game = game_;
    }

    private void setReady() {
        game.nextPlayerIsReady(this);
        isReady = true;
    }

    public void setName(String name_) {
        name = name_;

        setReady();
    }

    public String getUsername() {
        return name;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public int getMoney() {
        return money;
    }

    public int getBet() {
        return bet;
    }

    public int setReady(boolean isReady_) {
        isReady = isReady_;

        if (isReady) {
            readyPlayers++;
        } else {
            readyPlayers--;
        }

        return readyPlayers;
    }
}
