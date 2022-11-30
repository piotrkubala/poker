package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Hand;

import java.nio.channels.SelectionKey;

/**
 * Model of a player in the poker game on the server side.
 * @author Piotr Kubala
 */
public class Player implements Comparable<Player> {
    final SelectionKey key;

    String name;

    int money;
    int bet = 0;

    Hand playerHand;

    boolean isStartReady = false;

    boolean isSmallBlind = false;
    boolean isBigBlind = false;

    boolean isPlaying = true;

    boolean wereCardsChanged = false;

    boolean isWinner = false;

    double handValue = -1;

    boolean nextRoundReady = false;

    final Game game;

    boolean testMode = false;

    /**
     * Creates a new player with the given selecetion key, name and money.
     * @param keyArg selection key of the player socket
     * @param moneyArg money of the player
     * @param gameArg game the player is playing in
     */
    public Player(SelectionKey keyArg, int moneyArg, Game gameArg) {
        key = keyArg;
        money = moneyArg;
        game = gameArg;
    }

    /**
     * resets player for the next round
     */
    public void resetPlayerForNextRound() {
        bet = 0;
        isStartReady = false;
        isSmallBlind = false;
        isBigBlind = false;
        isPlaying = true;
        wereCardsChanged = false;
        isWinner = false;
        handValue = -1;
        nextRoundReady = false;
    }

    /**
     * get player's nextRoundReady
     * @return player's nextRoundReady
     */
    public boolean getNextRoundReady() {
        return nextRoundReady;
    }

    /**
     * set player's nextRoundReady
     * @param nextRoundReadyArg player's nextRoundReady
     */
    public void setNextRoundReady(boolean nextRoundReadyArg) {
        if (nextRoundReady == nextRoundReadyArg) {
            return;
        }

        nextRoundReady = nextRoundReadyArg;

        if (testMode) {
            return;
        }

        if (nextRoundReady) {
            game.increasePlayersWhoAreReadyForNextRoundCount();
        } else {
            game.decreasePlayersWhoAreReadyForNextRoundCount();
        }
    }

    /**
     * get player's selection key
     * @return player's selection key
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * set player's ready for start
     */
    private void setReady() {
        if (testMode){
            return;
        }
        game.nextPlayerIsReady(this);
    }

    /**
     * set player's name
     * @param nameArg player's name
     */
    public void setName(String nameArg) {
        name = nameArg;

        setReady();
    }

    /**
     * get player's name
     * @return player's name
     */
    public String getUsername() {
        return name;
    }

    /**
     * get player's hand
     * @return player's hand
     */
    public Hand getPlayerHand() {
        return playerHand;
    }

    /**
     * set player's money
     * @return player's money
     */
    public int getMoney() {
        return money;
    }

    /**
     * set player's bet
     * @return player's bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * set that player is ready for start
     * @param isReadyArg is player ready for start
     * @return number of players who are ready for start
     */
    public int setStartReady(boolean isReadyArg) {
        if (isReadyArg == isStartReady) {
            return game.getReadyPlayersForStart();
        }

        isStartReady = isReadyArg;

        if (isStartReady) {
            game.increaseReadyPlayersForStart();
        } else {
            game.decreaseReadyPlayersForStart();
        }

        return game.getReadyPlayersForStart();
    }

    /**
     * set player's hand
     * @param hand player's hand
     */
    public void giveHand(Hand hand) {
        playerHand = hand;
    }

    /**
     * set small blind
     * @param isSmallBlindArg is player small blind
     */
    public void setSmallBlind(boolean isSmallBlindArg) {
        isSmallBlind = isSmallBlindArg;
    }

    /**
     * set big blind
     * @param isBigBlindArg is player big blind
     */
    public void setBigBlind(boolean isBigBlindArg) {
        isBigBlind = isBigBlindArg;
    }

    /**
     * check if player is small blind
     * @return is player small blind
     */
    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    /**
     * check if player is big blind
     * @return is player big blind
     */
    public boolean isBigBlind() {
        return isBigBlind;
    }

    /**
     * raise player's bet to the given value
     * @param amount amount to raise
     */
    public void raiseBet(int amount) {
        money -= amount - bet;

        game.addMoneyToPool(amount - bet);
        game.setCurrentBet(amount);

        bet = amount;
    }

    /**
     * set player is playing
     * @param isPlayingArg is player playing
     */
    public void setPlaying(boolean isPlayingArg) {
        if (isPlayingArg == isPlaying) {
            return;
        }

        isPlaying = isPlayingArg;

        if (isPlaying) {
            game.increaseStillPlayingPlayers();
        } else {
            game.decreaseStillPlayingPlayers();
        }
    }

    /**
     * check if player is playing curretly
     * @return is player playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * set player's cards changed
     * @param wereCardsChangedArg were player's cards changed
     */
    public void setCardsChanged(boolean wereCardsChangedArg) {
        if (wereCardsChangedArg == wereCardsChanged) {
            return;
        }

        wereCardsChanged = wereCardsChangedArg;

        if (wereCardsChanged) {
            game.increasePlayersWhoChangedCards();
        } else {
            game.decreasePlayersWhoChangedCards();
        }
    }

    /**
     * check if player's cards were changed
     * @return were player's cards changed
     */
    public boolean wereCardsChanged() {
        return wereCardsChanged;
    }

    // call before comparing hands
    /**
     * set player's hand value
     */
    public void calculateHandValue() {
        handValue = playerHand.getHandValue();
    }

    /**
     * get player's hand value
     * @return player's hand value
     */
    public double getHandValue() {
        return handValue;
    }

    // call after calculating hand values of hands
    /**
     * compare player's hand value to the given player's hand value
     */
    @Override
    public int compareTo(Player player) {
        if (handValue > player.handValue) {
            return 1;
        } else if (handValue < player.handValue) {
            return -1;
        }
        return 0;
    }

    /**
     * check if other player is the same as this player
     * @param other other player
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Player.class) {
            return false;
        }
        Player otherPlayer = (Player) other;

        return key.equals(otherPlayer.key);
    }

    /**
     * get player's hash code
     * @return player's hash code
     */
    @Override
    public int hashCode() {
        return key.hashCode();
    }

    /**
     * set player's winner
     * @param isWinnerArg is player winner
     */
    public void setWinner(boolean isWinnerArg) {
        isWinner = isWinnerArg;
    }

    /**
     * check if player is winner
     * @return is player winner
     */
    public boolean isWinner() {
        return isWinner;
    }

    /**
     * set player's money
     * @param amount player's money
     */
    public void setBet(int amount) {
        bet = amount;
    }

    /**
     * set player's money
     * @param amount player's money
     */
    public void addMoney(int amount) {
        money += amount;
    }
}
