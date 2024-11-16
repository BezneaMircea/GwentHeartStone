package org.poo.main;


import org.poo.fileio.Coordinates;
import org.poo.cards.Card;

public class Errors {
    public static final String noError = null;
    public static final String isFrozen = "Attacker card is frozen.";
    public static final String cardAlreadyAttacked = "Attacker card has already attacked this turn.";
    public static final String heroAlreadyAttacked = "Hero has already attacked this turn.";
    public static final String attackedDontBelongCur = "Attacked card does not belong to the current player.";
    public static final String attackedDontBelongEnnemy = "Attacked card does not belong to the enemy.";
    public static final String attackerDontBelongCur = "Attacker card does not belong to the current player.";
    public static final String notTank = "Attacked card is not of type 'Tank'.";
    public static final String notEnoughManaCard = "Not enough mana to place card on table.";
    public static final String notEnoughManaHero = "Not enough mana to use hero's ability.";
    public static final String tableIsFull = "Cannot place card on table since row is full.";
    public static final String noCardAtGivenPos = "No card available at that position.";
    public static final String rowNotEnemy = "Selected row does not belong to the enemy.";
    public static final String rowNotCurPlayer = "Selected row does not belong to the current player.";
    public static final String playerOneWon = "Player one killed the enemy hero.";
    public static final String playerTwoWon = "Player two killed the enemy hero.";

    public static String gameEnded(int currentPlayerId) {
        if (currentPlayerId == GamesSetup.playerOneIdx)
            return playerOneWon;

        return playerTwoWon;
    }

    public static String
    placeCardError(Card card, GameTable table, int currentMana) {
        if (card.getMana() > currentMana)
            return notEnoughManaCard;

        int rowToPlace = card.getRowToPlace();
        if (!table.canAddOnRow(rowToPlace))
            return tableIsFull;

        return noError;
    }

    public static String noCardAtPositionError(Coordinates coords, GameTable table) {
        Card wantedCard = table.getElement(coords);
        if (wantedCard == null)
            return noCardAtGivenPos;

        return noError;
    }


    public static int getOtherPlayerIdx(int playerIdx) {
        if (playerIdx == GamesSetup.playerOneIdx) {
            return GamesSetup.playerTwoIdx;
        }
        return GamesSetup.playerOneIdx;
    }

}
