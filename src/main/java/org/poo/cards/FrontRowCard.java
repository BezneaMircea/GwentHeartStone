package org.poo.cards;

import org.poo.fileio.CardInput;
import org.poo.main.GamesSetup;
import org.poo.main.GameTable;

public class FrontRowCard extends TableCard {
    public FrontRowCard(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);

        if (belongsTo == GamesSetup.playerOneIdx)
            setRowToPlace(GameTable.playerOneFrontRow);
        else
            setRowToPlace(GameTable.playerTwoFrontRow);
    }

}
