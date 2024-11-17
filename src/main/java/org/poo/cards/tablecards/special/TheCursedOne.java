package org.poo.cards.tablecards.special;


import org.poo.cards.tablecards.BackRowCard;
import org.poo.cards.Card;
import org.poo.cards.tablecards.FrontRowCard;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

/**
 * Class used to represent the card "The Cursed One"
 */
public final class TheCursedOne extends BackRowCard {
    /**
     * Constructor used to create "The Cursed One" card, just calls
     * the constructor from the super class (BackRowCard)
     * @see BackRowCard
     */
    public TheCursedOne(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer)
            return attackedDontBelongEnnemy;

        if (table.doesPlayerHaveTanks(Errors.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank())
            return notTank;

        int cardAttackDamage = attackedCard.getAttackDamage();
        attackedCard.setAttackDamage(attackedCard.getHealth());
        attackedCard.setHealth(cardAttackDamage);

        if(attackedCard.getHealth() == 0)
            table.removeCard(attackedCard);
        setHasAttacked(true);

        return Errors.noError;
    }
}
