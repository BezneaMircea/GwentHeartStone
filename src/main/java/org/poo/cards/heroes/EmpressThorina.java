package org.poo.cards.heroes;

import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;
import org.poo.main.GamesSetup;

import java.util.ArrayList;

public final class EmpressThorina extends Hero {

    public EmpressThorina(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
    }

    private void
    lowBlow(GameTable table, int affectedRow) {
        ArrayList<Card> affectedCards = table.getTable().get(affectedRow);
        if (affectedCards.isEmpty())
            return;

        Card cardToDestroy = affectedCards.getFirst();
        int maxHealth = 0;
        for (Card card : affectedCards)
            if (card.getHealth() > maxHealth) {
                maxHealth = card.getHealth();
                cardToDestroy = card;
            }

        cardToDestroy.setHealth(0);
        table.removeCard(cardToDestroy);
    }

    @Override
    protected String
    useAbility(GameTable table, int affectedRow, int currentPlayerId) {
        if (GameTable.rowBelongsPlayer(affectedRow, currentPlayerId))
            return rowNotEnemy;

        lowBlow(table, affectedRow);

        return Errors.noError;
    }
}
