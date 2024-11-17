package org.poo.cards.tablecards.special;

import org.poo.cards.Card;
import org.poo.cards.tablecards.FrontRowCard;
import org.poo.game.GamesSetup;
import org.poo.fileio.CardInput;
import org.poo.game.GameTable;

/**
 * Class used to represent the card "The Ripper"
 */
public final class TheRipper extends FrontRowCard {
    /**
     * Constructor used to create "The Ripper" card, just calls
     * the constructor from the super class.
     * @see FrontRowCard
     */
    public TheRipper(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer)
            return attackedDontBelongEnnemy;

        if (table.doesPlayerHaveTanks(GamesSetup.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank())
            return notTank;


        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
        if (attackedCard.getAttackDamage() < 0)
            attackedCard.setAttackDamage(0);
        setHasAttacked(true);

        return null;
    }
}
