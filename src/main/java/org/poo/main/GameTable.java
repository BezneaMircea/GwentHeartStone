package org.poo.main;

import org.poo.cards.Card;
import org.poo.fileio.Coordinates;

import java.util.ArrayList;

public final class GameTable {
    private final ArrayList<ArrayList<Card>> table;
    public static final int playerOneBackRow = 3;
    public static final int playerOneFrontRow = 2;
    public static final int playerTwoFrontRow = 1;
    public static final int playerTwoBackRow = 0;

    public GameTable() {
        table = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            table.add(new ArrayList<Card>());
    }

    public static boolean
    rowBelongsPlayer(int row, int playerId) {
        if (playerId == GamesSetup.playerOneIdx)
            return row == playerOneBackRow || row == playerOneFrontRow;
        return row == playerTwoFrontRow || row == playerTwoBackRow;
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

    public boolean canAddOnRow(int row) {
        return table.get(row).size() < 5;
    }

    public void addCard(Card card) {
        table.get(card.getRowToPlace()).add(card);
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

    public void resetAttack() {
        for (ArrayList<Card> arrayList : table)
            for (Card card : arrayList) {
                card.setHasAttacked(false);
                card.setFrozen(false);
            }
    }

    public ArrayList<ArrayList<Card>> getTable() {
        return table;
    }
}
