package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

/**
 * Class to represent the hero "Lord Royce"
 *
 * @see Hero
 */
public final class LordRoyce extends Hero {

    /**
     * Constructor used to build "Lord Royce", just calls
     * the constructor from the super class (Hero)
     *
     * @see Hero
     */
    public LordRoyce(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void subZero(final GameTable table, final int affectedRow) {
        for (final Card card : table.getTable().get(affectedRow)) {
            card.setFrozen(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String
    useAbility(final GameTable table, final int affectedRow, final int currentPlayerId) {
        if (table.rowBelongsPlayer(affectedRow, currentPlayerId)) {
            return ROW_NOT_ENEMY;
        }

        subZero(table, affectedRow);
        setHasAttacked(true);

        return null;
    }
}
