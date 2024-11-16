package org.poo.cards.tablecards.special;

import org.poo.cards.Card;
import org.poo.cards.tablecards.FrontRowCard;
import org.poo.main.Errors;
import org.poo.fileio.CardInput;
import org.poo.main.GameTable;


public final class TheRipper extends FrontRowCard {
    public TheRipper(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer)
            return attackedDontBelongEnnemy;

        if (table.doesPlayerHaveTanks(Errors.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank())
            return notTank;


        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
        if (attackedCard.getAttackDamage() < 0)
            attackedCard.setAttackDamage(0);
        setHasAttacked(true);

        return Errors.noError;
    }
}
