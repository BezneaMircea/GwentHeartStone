package org.poo.cards.tablecards.special;


import org.poo.cards.Card;
import org.poo.cards.tablecards.FrontRowCard;
import org.poo.fileio.CardInput;
import org.poo.game.GamesSetup;
import org.poo.game.GameTable;

/**
 * Class used to represent the card "Miraj"
 */
public final class Miraj extends FrontRowCard {

    /**
     * Constructor for the Miraj card, just calls the constructor
     * from the super class (FrontRowCard)
     *
     * @see FrontRowCard
     */
    public Miraj(final CardInput cardInput, final int belongsTo) {
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

        final int aux = getHealth();
        setHealth(attackedCard.getHealth());
        attackedCard.setHealth(aux);
        setHasAttacked(true);

        return null;
    }
}
