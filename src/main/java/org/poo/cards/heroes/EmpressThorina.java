package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Class used to represent hero "Empress Thorina"
 * @see Hero
 */
public final class EmpressThorina extends Hero {

    /**
     * Constructor used to create "Empress Thorina", just calls the
     * constructor from the super Class (Hero)
     * @see Hero
     */
    public EmpressThorina(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void
    lowBlow(final GameTable table, final int affectedRow) {
        ArrayList<Card> affectedCards = table.getTable().get(affectedRow);

        if (affectedCards.isEmpty()) {
            return;
        }

        Card cardToDestroy = affectedCards.getFirst();
        int maxHealth = 0;
        for (Card card : affectedCards) {
            if (card.getHealth() > maxHealth) {
                maxHealth = card.getHealth();
                cardToDestroy = card;
            }
        }
        cardToDestroy.setHealth(0);
        table.removeCard(cardToDestroy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String
    useAbility(final GameTable table, final int affectedRow, final int currentPlayerId) {
        if (table.rowBelongsPlayer(affectedRow, currentPlayerId)) {
            return rowNotEnemy;
        }

        lowBlow(table, affectedRow);
        setHasAttacked(true);

        return null;
    }
}
