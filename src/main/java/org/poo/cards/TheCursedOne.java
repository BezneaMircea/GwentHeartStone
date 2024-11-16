package org.poo.cards;


import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;

public final class TheCursedOne extends BackRowCard {
    public TheCursedOne(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    @Override
    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer)
            return Errors.attackedDontBelongEnnemy;

        int cardAttackDamage = attackedCard.getAttackDamage();
        attackedCard.setAttackDamage(attackedCard.getHealth());
        attackedCard.setHealth(cardAttackDamage);

        if(attackedCard.getHealth() == 0)
            table.removeCard(attackedCard);

        return Errors.noError;
    }
}
