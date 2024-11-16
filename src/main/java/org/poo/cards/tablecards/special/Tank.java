package org.poo.cards.tablecards.special;

import org.poo.cards.tablecards.FrontRowCard;
import org.poo.fileio.CardInput;

public final class Tank extends FrontRowCard {
    public Tank(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    @Override
    public boolean isTank() {return true; }

}
