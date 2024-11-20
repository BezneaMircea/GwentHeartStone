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

    private final int nrColsOnTable = 5;
    private final int nrRowsOnTable = 4;

    public static final int PLAYER_ONE_BACK_ROW = 3;
    public static final int PLAYER_ONE_FRONT_ROW = 2;
    public static final int PLAYER_TWO_FRONT_ROW = 1;
    public static final int PLAYER_TWO_BACK_ROW = 0;

    public static final String NOT_ENOUGH_MANA_CARD;
    public static final String TABLE_IS_FULL;
    public static final String NO_CARD_AT_GIVEN_POS;

    static {
        NOT_ENOUGH_MANA_CARD = "Not enough mana to place card on table.";
        TABLE_IS_FULL = "Cannot place card on table since row is full.";
        NO_CARD_AT_GIVEN_POS = "No card available at that position.";
    }

    /**
     * Constructor used to initialize the rows of the gameTable
     */
    public GameTable() {
        table = new ArrayList<>();
        for (int i = 0; i < nrRowsOnTable; i++) {
            table.add(new ArrayList<Card>());
        }
    }


    /**
     * Method used to get the Card at some given coordinates on the table
     *
     * @param cord set of coordinates where a card might be
     * @return null if there was no card at the given coordinates
     * (coordinates where out of bounds) or the Card at the given
     * coordinates
     */
    public Card getElement(final Coordinates cord) {
        try {
            return table.get(cord.getX()).get(cord.getY());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Method used to remove a card from the table
     *
     * @param card card to remove
     */
    public void removeCard(final Card card) {
        table.get(card.getRowToPlace()).remove(card);
    }

    /**
     * Method used to add a card on the table
     *
     * @param card        card to be added
     * @param currentMana current mana of the player
     * @return an appropriate error is one occurred, null otherwise
     */
    public String addCard(final Card card, final int currentMana) {
        if (card.getMana() > currentMana) {
            return NOT_ENOUGH_MANA_CARD;
        }

        int rowToPlace = card.getRowToPlace();
        if (table.get(rowToPlace).size() > nrColsOnTable) {
            return TABLE_IS_FULL;
        }

        table.get(card.getRowToPlace()).add(card);

        return null;
    }

    /**
     * Method that checks if a player has at least one tank on table
     *
     * @param playerIdx the player idx
     * @return true if the player has least
     * one tank on table false otherwise
     */
    public boolean doesPlayerHaveTanks(final int playerIdx) {
        if (playerIdx == GamesSetup.PLAYER_ONE_IDX) {
            for (Card card : table.get(PLAYER_ONE_FRONT_ROW)) {
                if (card.isTank()) {
                    return true;
                }
            }
        } else {
            for (Card card : table.get(PLAYER_TWO_FRONT_ROW)) {
                if (card.isTank()) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Method that checks if a given row index belongs
     * to the given player id
     *
     * @param row      the row that is checked
     * @param playerId the player id
     * @return true if the row belongs to player, false otherwise
     */
    public boolean
    rowBelongsPlayer(final int row, final int playerId) {
        if (playerId == GamesSetup.PLAYER_ONE_IDX) {
            return row == PLAYER_ONE_BACK_ROW || row == PLAYER_ONE_FRONT_ROW;
        }
        return row == PLAYER_TWO_FRONT_ROW || row == PLAYER_TWO_BACK_ROW;
    }


    /**
     * Method that resets the hasAttacked and the isFrozen attributes of
     * the all cards on the table that belong to the given player
     *
     * @param player the player that will have his cards reset
     */
    public void resetAttack(final Player player) {
        if (player.getPlayerId() == GamesSetup.PLAYER_ONE_IDX) {
            resetCardsOnRow(PLAYER_ONE_FRONT_ROW);
            resetCardsOnRow(PLAYER_ONE_BACK_ROW);
        } else {
            resetCardsOnRow(PLAYER_TWO_FRONT_ROW);
            resetCardsOnRow(PLAYER_TWO_BACK_ROW);
        }
    }

    private void resetCardsOnRow(final int row) {
        for (Card card : table.get(row)) {
            card.setHasAttacked(false);
            card.setFrozen(false);
        }
    }

    public ArrayList<ArrayList<Card>> getTable() {
        return table;
    }
}
