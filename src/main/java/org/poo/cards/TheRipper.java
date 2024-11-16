package org.poo.cards;

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
            return Errors.attackedDontBelongEnnemy;

        if (table.doesPlayerHaveTanks(Errors.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank())
            return Errors.notTank;


        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
        if (attackedCard.getAttackDamage() < 0)
            attackedCard.setAttackDamage(0);
        setHasAttacked(true);

        return Errors.noError;
    }
}
