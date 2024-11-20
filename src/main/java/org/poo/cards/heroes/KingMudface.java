package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Class used to represent the hero "King Mudface"
 *
 * @see Hero
 */
public final class KingMudface extends Hero {
    public static final int KING_MUDFACE_ADDED_HP = 1;

    /**
     * Constructor used to build "King Mudface", just calls
     * the constructor from the super class (Hero)
     *
     * @see Hero
     */
    public KingMudface(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void earthBorn(final GameTable table, final int affectedRow) {
        ArrayList<Card> affectedCards = table.getTable().get(affectedRow);

        for (Card card : affectedCards) {
            int currentHealth = card.getHealth();
            card.setHealth(currentHealth + KING_MUDFACE_ADDED_HP);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String
    useAbility(final GameTable table, final int affectedRow, final int currentPlayerId) {
        if (!table.rowBelongsPlayer(affectedRow, currentPlayerId)) {
            return ROW_NOT_CUR;
        }

        earthBorn(table, affectedRow);
        setHasAttacked(true);

        return null;
    }

}
