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
    getAttackCardOutput(GameTable table, int curPlayerId, Card attackedCard) {
        if (getBelongsTo() != curPlayerId)
            return Errors.attackerDontBelongCur;

        if (attackedCard.getBelongsTo() == curPlayerId)
            return Errors.attackedDontBelongEnnemy;

        if (getHasAttacked())
            return Errors.cardAlreadyAttacked;

        if (isFrozen())
            return Errors.isFrozen;

        int enemyIdx = Errors.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return Errors.notTank;

        return Errors.noError;
    }

    private String
    getAttackHeroOutput(GameTable table, int curPlayerId, Card attackedCard) {
        if (attackedCard.getBelongsTo() == curPlayerId)
            return Errors.attackedDontBelongEnnemy;

        if (isFrozen())
            return Errors.isFrozen;

        if (getHasAttacked())
            return Errors.cardAlreadyAttacked;

        int enemyIdx = Errors.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return Errors.notTank;

        return Errors.noError;
    }

    @Override
    public String
    attackCard(GameTable table, int curPlayerId, Card attackedCard) {
        String output;
        if (attackedCard.isHero()) {
            output = getAttackHeroOutput(table, curPlayerId, attackedCard);
        } else {
            output = getAttackCardOutput(table, curPlayerId, attackedCard);
        }

        if (output != null)
            return output;

        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        setHasAttacked(true);
        if (attackedCard.getHealth() <= 0) {
            attackedCard.setHealth(0);
            if (attackedCard.isHero())
                return Errors.gameEnded(curPlayerId);

            table.removeCard(attackedCard);
        }

        return Errors.noError;
    }



    protected String useAbility(Card attackedCard, GameTable table, int curPlayerId) {
        return null;
    }

    @Override
    public String useCardAbility(Card attackedCard, GameTable table, int curPlayerId) {
        if (attackedCard == null)
            return null;

        if (frozen)
            return Errors.isFrozen;

        if (getHasAttacked())
            return Errors.cardAlreadyAttacked;

        return useAbility(attackedCard, table, curPlayerId);
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
