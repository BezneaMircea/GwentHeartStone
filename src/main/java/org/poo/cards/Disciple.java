package org.poo.cards;


import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

public final class Disciple extends BackRowCard {
    public Disciple(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() != currentPlayer)
            return Errors.attackedDontBelongCur;

        if (table.doesPlayerHaveTanks(Errors.getOtherPlayerIdx(currentPlayer))
            && !attackedCard.isTank())
            return Errors.notTank;

        attackedCard.setHealth(attackedCard.getHealth() + 2);

        return Errors.noError;
    }
}
