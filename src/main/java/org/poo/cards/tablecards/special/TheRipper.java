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
     *
     * @see FrontRowCard
     */
    public TheRipper(final CardInput cardInput, final int belongsTo) {
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


        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);
        if (attackedCard.getAttackDamage() < 0) {
            attackedCard.setAttackDamage(0);
        }
        setHasAttacked(true);

        return null;
    }
}
