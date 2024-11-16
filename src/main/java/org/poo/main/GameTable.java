package org.poo.main;

import org.poo.cards.Card;
import org.poo.fileio.Coordinates;

import java.util.ArrayList;

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

    public GameTable() {
        table = new ArrayList<>();
        for (int i = 0; i < nrRowsOnTable; i++)
            table.add(new ArrayList<Card>());
    }


    public Card getElement(Coordinates cord) {
        try {
            return table.get(cord.getX()).get(cord.getY());
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void removeCard(Card card) {
        table.get(card.getRowToPlace()).remove(card);
    }


    public String addCard(Card card, int currentMana) {
        if (card.getMana() > currentMana)
            return notEnoughManaCard;

        int rowToPlace = card.getRowToPlace();
        if (table.get(rowToPlace).size() > nrColsOnTable)
            return tableIsFull;

        table.get(card.getRowToPlace()).add(card);

        return Errors.noError;
    }

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

    public boolean
    rowBelongsPlayer(int row, int playerId) {
        if (playerId == GamesSetup.playerOneIdx)
            return row == playerOneBackRow || row == playerOneFrontRow;
        return row == playerTwoFrontRow || row == playerTwoBackRow;
    }


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
