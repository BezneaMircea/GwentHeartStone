package org.poo.cards.tablecards.special;


import org.poo.cards.tablecards.BackRowCard;
import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

/**
 * Class used to represent the card "Disciple"
 */
public final class Disciple extends BackRowCard {

    /**
     * Constructor for the Disciple card, just calls the constructor
     * from the super class (BackRowCard)
     * @see BackRowCard
     */
    public Disciple(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() != currentPlayer)
            return attackedDontBelongCur;

        attackedCard.setHealth(attackedCard.getHealth() + 2);
        setHasAttacked(true);

        return null;
    }
}
