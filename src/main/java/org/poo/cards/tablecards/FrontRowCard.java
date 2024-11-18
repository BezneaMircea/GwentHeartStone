package org.poo.cards.tablecards;

import org.poo.fileio.CardInput;
import org.poo.game.GamesSetup;
import org.poo.game.GameTable;

/**
 * Class used for cards that must be placed on the front row
 */
public class FrontRowCard extends TableCard {

    /**
     * Constructor for the current class that calls the constructor from
     * the super class with the same params and also sets the rowToPlace
     * attribute
     *
     * @param cardInput the specific input of the given card
     * @param belongsTo the id of the player that the card belongs to
     */
    public FrontRowCard(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);

        if (belongsTo == GamesSetup.PLAYER_ONE_IDX) {
            setRowToPlace(GameTable.PLAYER_ONE_FRONT_ROW);
        } else {
            setRowToPlace(GameTable.PLAYER_TWO_FRONT_ROW);
        }
    }

}
