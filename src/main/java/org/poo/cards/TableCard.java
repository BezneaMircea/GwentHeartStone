package org.poo.cards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.GameTable;
import org.poo.main.JsonNode;


public class TableCard extends Card {
    private int attackDamage;
    private boolean frozen;
    private int rowToPlace;

    public TableCard(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
        attackDamage = cardInput.getAttackDamage();
        frozen = false;
    }

    @Override
    public ObjectNode writeCard() {
        ObjectNode minionNode = JsonNode.mapper.createObjectNode();

        minionNode.put("mana", getMana());
        minionNode.put("attackDamage", getAttackDamage());
        minionNode.put("health", getHealth());
        minionNode.put("description", getDescription());
        ArrayNode colorArray = JsonNode.writeColors(getColors());
        minionNode.set("colors", colorArray);
        minionNode.put("name", getName());

        return minionNode;
    }


    private String
    getAttackError(GameTable table, int currentPlayerId, Card attackedCard) {
        if (getBelongsTo() != currentPlayerId)
            return Errors.attackerDontBelongCur;

        if (attackedCard.getBelongsTo() == currentPlayerId)
            return Errors.attackedDontBelongEnnemy;

        if (getHasAttacked())
            return Errors.cardAlreadyAttacked;

        if (isFrozen())
            return Errors.isFrozen;

        int enemyIdx = Errors.getOtherPlayerIdx(currentPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return Errors.notTank;

        return Errors.noError;
    }

    @Override
    public String attackCard(GameTable table, int currentPlayerId, Card attackedCard) {
        String error = getAttackError(table, currentPlayerId, attackedCard);

        if (error != null)
            return error;

        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        setHasAttacked(true);
        if (attackedCard.getHealth() <= 0) {
            attackedCard.setHealth(0);
            table.removeCard(attackedCard);
        }

        return Errors.noError;
    }



    protected String useAbility(Card attackedCard, GameTable table, int currentPlayer) {
        return null;
    }

    @Override
    public String useCardAbility(Card attackedCard, GameTable table, int currentPlayer) {
        if (attackedCard == null)
            return null;

        if (frozen)
            return Errors.isFrozen;

        if (getHasAttacked())
            return Errors.cardAlreadyAttacked;

        return useAbility(attackedCard, table, currentPlayer);
    }


    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public int getRowToPlace() {
        return rowToPlace;
    }

    public void setRowToPlace(int rowToPlace) {
        this.rowToPlace = rowToPlace;
    }
}
