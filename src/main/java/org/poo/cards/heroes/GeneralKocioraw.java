package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Class used to represent hero "General Kocioraw"
 * @see Hero
 */
public final class GeneralKocioraw extends Hero {
    public static final int generalKociorawAddedDamage = 1;

    /**
     * Constructor used to build "General Kocioraw", just calls
     * the constructor from the super class (Hero)
     * @see Hero
     */
    public GeneralKocioraw(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void bloodThirst(GameTable table, int affectedRow) {
        ArrayList<Card> affectedCards = table.getTable().get(affectedRow);

        for (Card card : affectedCards) {
            int currentDamage = card.getAttackDamage();
            card.setAttackDamage(currentDamage + generalKociorawAddedDamage);
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

        bloodThirst(table, affectedRow);
        setHasAttacked(true);

        return null;
    }
}
