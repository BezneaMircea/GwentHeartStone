package org.poo.cards;


import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

public final class Miraj extends FrontRowCard {
    public Miraj(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer)
            return Errors.attackedDontBelongEnnemy;

        if (table.doesPlayerHaveTanks(Errors.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank())
            return Errors.notTank;

        int aux = getHealth();
        setHealth(attackedCard.getHealth());
        attackedCard.setHealth(aux);
        setHasAttacked(true);

        return Errors.noError;
    }
}
