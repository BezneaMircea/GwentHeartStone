package org.poo.game;

import org.poo.cards.Card;
import org.poo.fileio.Coordinates;
import java.util.ArrayList;


/**
 * Class used to represent the general attributes of a game table
 * (ex: nrCols, nrRows), possible output errors after performing
 * actions on the table, methods that can be performed
 * on the table and the game table itself
 */
public final class GameTable {
    private final ArrayList<ArrayList<Card>> table;

    private static final int nrColsOnTable = 5;
    private static final int nrRowsOnTable = 4;
    public static final int playerOneBackRow = 3;
    public static final int playerOneFrontRow = 2;
    public static final int playerTwoFrontRow = 1;
    public static final int playerTwoBackRow = 0;
    public static final String notEnoughManaCard;
    public static final String tableIsFull;
    public static final String noCardAtGivenPos;

    static {
        notEnoughManaCard = "Not enough mana to place card on table.";
        tableIsFull = "Cannot place card on table since row is full.";
        noCardAtGivenPos = "No card available at that position.";
    }

    /**
     * Constructor used to initialize the rows of the gameTable
     */
    public GameTable() {
        table = new ArrayList<>();
        for (int i = 0; i < nrRowsOnTable; i++)
            table.add(new ArrayList<Card>());
    }


    /**
     * Method used to get the Card at some given coordinates on the table
     * @param cord set of coordinates where a card might be
     * @return null if there was no card at the given coordinates
     * (coordinates where out of bounds) or the Card at the given
     * coordinates
     */
    public Card getElement(Coordinates cord) {
        try {
            return table.get(cord.getX()).get(cord.getY());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Method used to remove a card from the table
     * @param card card to remove
     */
    public void removeCard(Card card) {
        table.get(card.getRowToPlace()).remove(card);
    }

    /**
     * Method used to add a card on the table
     * @param card card to be added
     * @param currentMana current mana of the player
     * @return an appropriate error is one occurred, null otherwise
     */
    public String addCard(Card card, int currentMana) {
        if (card.getMana() > currentMana)
            return notEnoughManaCard;

        int rowToPlace = card.getRowToPlace();
        if (table.get(rowToPlace).size() > nrColsOnTable)
            return tableIsFull;

        table.get(card.getRowToPlace()).add(card);

        return null;
    }

    /**
     * Method that checks if a player has at least one tank on table
     * @param playerIdx the player idx
     * @return true if the player has least
     * one tank on table false otherwise
     */
    public boolean doesPlayerHaveTanks(int playerIdx) {
        if (playerIdx == GamesSetup.playerOneIdx) {
            for (Card card : table.get(playerOneFrontRow))
                if (card.isTank())
                    return true;
        } else {
            for (Card card : table.get(playerTwoFrontRow))
                if (card.isTank())
                    return true;
        }
        return false;
    }


    /**
     * Method that checks if a given row index belongs
     * to the given player id
     * @param row the row that is checked
     * @param playerId the player id
     * @return true if the row belongs to player, false otherwise
     */
    public boolean
    rowBelongsPlayer(int row, int playerId) {
        if (playerId == GamesSetup.playerOneIdx)
            return row == playerOneBackRow || row == playerOneFrontRow;
        return row == playerTwoFrontRow || row == playerTwoBackRow;
    }


    /**
     * Method that resets the hasAttacked and the isFrozen attributes of
     * the all cards on the table that belong to the given player
     * @param player the player that will have his cards reset
     */
    public void resetAttack(Player player) {
        if (player.getPlayerId() == GamesSetup.playerOneIdx) {
            resetCardsOnRow(playerOneFrontRow);
            resetCardsOnRow(playerOneBackRow);
        } else {
            resetCardsOnRow(playerTwoFrontRow);
            resetCardsOnRow(playerTwoBackRow);
        }
    }

    private void resetCardsOnRow(int row) {
        for (Card card : table.get(row)) {
            card.setHasAttacked(false);
            card.setFrozen(false);
        }
    }

    public ArrayList<ArrayList<Card>> getTable() {
        return table;
    }
}
