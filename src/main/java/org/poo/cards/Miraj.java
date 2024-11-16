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

        int aux = getHealth();
        setHealth(attackedCard.getHealth());
        attackedCard.setHealth(aux);

        return Errors.noError;
    }
}
