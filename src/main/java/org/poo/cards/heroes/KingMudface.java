package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Class used to represent the hero "King Mudface"
 * @see Hero
 */
public final class KingMudface extends Hero {
    public static final int kingMudfaceAddedHp = 1;

    /**
     * Constructor used to build "King Mudface", just calls
     * the constructor from the super class (Hero)
     * @see Hero
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected String
    useAbility(GameTable table, int affectedRow, int currentPlayerId) {
        if (!table.rowBelongsPlayer(affectedRow, currentPlayerId))
            return rowNotCur;

        earthBorn(table, affectedRow);
        setHasAttacked(true);

        return null;
    }

}
