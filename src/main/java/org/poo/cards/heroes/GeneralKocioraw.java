package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Class used to represent hero "General Kocioraw"
 *
 * @see Hero
 */
public final class GeneralKocioraw extends Hero {
    public static final int GENERAL_KOCIORAW_ADDED_DAMAGE = 1;

    /**
     * Constructor used to build "General Kocioraw", just calls
     * the constructor from the super class (Hero)
     *
     * @see Hero
     */
    public GeneralKocioraw(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void bloodThirst(final GameTable table, final int affectedRow) {
        final ArrayList<Card> affectedCards = table.getTable().get(affectedRow);

        for (final Card card : affectedCards) {
            final int currentDamage = card.getAttackDamage();
            card.setAttackDamage(currentDamage + GENERAL_KOCIORAW_ADDED_DAMAGE);
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

        bloodThirst(table, affectedRow);
        setHasAttacked(true);

        return null;
    }
}
