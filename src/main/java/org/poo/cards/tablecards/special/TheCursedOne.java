package org.poo.cards.tablecards.special;


import org.poo.cards.tablecards.BackRowCard;
import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GamesSetup;
import org.poo.game.GameTable;

/**
 * Class used to represent the card "The Cursed One"
 */
public final class TheCursedOne extends BackRowCard {
    /**
     * Constructor used to create "The Cursed One" card, just calls
     * the constructor from the super class (BackRowCard)
     *
     * @see BackRowCard
     */
    public TheCursedOne(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String useAbility(final Card attackedCard, final GameTable table, final int currentPlayer) {
        if (attackedCard.getBelongsTo() == currentPlayer) {
            return ATTACKED_DONT_BELONG_ENNEMY;
        }

        if (table.doesPlayerHaveTanks(GamesSetup.getOtherPlayerIdx(currentPlayer))
                && !attackedCard.isTank()) {
            return NOT_TANK;
        }

        final int cardAttackDamage = attackedCard.getAttackDamage();
        attackedCard.setAttackDamage(attackedCard.getHealth());
        attackedCard.setHealth(cardAttackDamage);

        if (attackedCard.getHealth() == 0) {
            table.removeCard(attackedCard);
        }
        setHasAttacked(true);

        return null;
    }
}
