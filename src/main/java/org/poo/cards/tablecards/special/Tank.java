package org.poo.cards.tablecards.special;

import org.poo.cards.tablecards.FrontRowCard;
import org.poo.fileio.CardInput;

/**
 * Class used to represent a "Tank" card
 */
public final class Tank extends FrontRowCard {

    /**
     * Constructor to create a tank, just calls the constructor
     * from the super class (FrontRowCard)
     *
     * @see FrontRowCard
     */
    public Tank(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTank() {
        return true;
    }

}
