package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

import java.util.ArrayList;

public final class KingMudface extends Hero {
    public static final int kingMudfaceAddedHp = 1;

    public KingMudface(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void earthBorn(GameTable table, int affectedRow) {
        ArrayList<Card> affectedCards = table.getTable().get(affectedRow);

        for (Card card : affectedCards) {
            int currentHealth = card.getHealth();
            card.setHealth(currentHealth + kingMudfaceAddedHp);
        }
    }

    @Override
    protected String
    useAbility(GameTable table, int affectedRow, int currentPlayerId) {
        if (!table.rowBelongsPlayer(affectedRow, currentPlayerId))
            return rowNotCur;

        earthBorn(table, affectedRow);
        setHasAttacked(true);

        return Errors.noError;
    }

}
