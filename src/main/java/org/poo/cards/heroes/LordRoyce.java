package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

public final class LordRoyce extends Hero {

    public LordRoyce(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void subZero(GameTable table, int affectedRow) {
        for (Card card : table.getTable().get(affectedRow))
            card.setFrozen(true);
    }

    @Override
    protected String
    useAbility(GameTable table, int affectedRow, int currentPlayerId) {
        if (table.rowBelongsPlayer(affectedRow, currentPlayerId))
            return rowNotEnemy;

        subZero(table, affectedRow);
        setHasAttacked(true);

        return Errors.noError;
    }
}
