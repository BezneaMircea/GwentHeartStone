package org.poo.cards;

import org.poo.fileio.CardInput;
import org.poo.main.GameTable;
import org.poo.main.GamesSetup;

public class BackRowCard extends TableCard {
    public BackRowCard(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);

        if (belongsTo == GamesSetup.playerOneIdx)
            setRowToPlace(GameTable.playerOneBackRow);
        else
            setRowToPlace(GameTable.playerTwoBackRow);
    }

}
