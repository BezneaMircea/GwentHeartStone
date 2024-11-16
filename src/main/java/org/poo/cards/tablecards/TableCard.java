package org.poo.cards.tablecards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.main.Errors;
import org.poo.main.Game;
import org.poo.main.GameTable;
import org.poo.main.JsonNode;


public class TableCard extends Card {
    private int attackDamage;
    private boolean frozen;
    private int rowToPlace;

    protected static final String isFrozen;
    protected static final String cardAlreadyAttacked;
    protected static final String attackedDontBelongCur;
    protected static final String attackedDontBelongEnnemy;
    protected static final String attackerDontBelongCur;
    protected static final String notTank;

    static {
        isFrozen = "Attacker card is frozen.";
        cardAlreadyAttacked = "Attacker card has already attacked this turn.";
        attackedDontBelongCur = "Attacked card does not belong to the current player.";
        attackedDontBelongEnnemy = "Attacked card does not belong to the enemy.";
        attackerDontBelongCur = "Attacker card does not belong to the current player.";
        notTank = "Attacked card is not of type 'Tank'.";
    }

    public TableCard(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
        attackDamage = cardInput.getAttackDamage();
        frozen = false;
    }


    protected String
    useAbility(Card attackedCard, GameTable table, int curPlayerId) {
        return null;
    }

    @Override
    public String
    useCardAbility(Card attackedCard, GameTable table, int curPlayerId) {
        if (attackedCard == null)
            return null;

        if (frozen)
            return isFrozen;

        if (getHasAttacked())
            return cardAlreadyAttacked;

        return useAbility(attackedCard, table, curPlayerId);
    }

    @Override
    public String
    attackCard(GameTable table, int curPlayerId, Card attackedCard) {
        String error;
        if (attackedCard.isHero()) {
            error = getAttackHeroError(table, curPlayerId, attackedCard);
        } else {
            error = getAttackCardError(table, curPlayerId, attackedCard);
        }

        if (error != null)
            return error;

        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        setHasAttacked(true);
        if (attackedCard.getHealth() <= 0) {
            attackedCard.setHealth(0);
            if (attackedCard.isHero())
                return Game.gameEnded(curPlayerId);

            table.removeCard(attackedCard);
        }

        return Errors.noError;
    }


    private String
    getAttackCardError(GameTable table, int curPlayerId, Card attackedCard) {
        if (getBelongsTo() != curPlayerId)
            return attackerDontBelongCur;

        if (attackedCard.getBelongsTo() == curPlayerId)
            return attackedDontBelongEnnemy;

        if (getHasAttacked())
            return cardAlreadyAttacked;

        if (isFrozen())
            return isFrozen;

        int enemyIdx = Errors.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return notTank;

        return Errors.noError;
    }


    private String
    getAttackHeroError(GameTable table, int curPlayerId, Card attackedCard) {
        if (attackedCard.getBelongsTo() == curPlayerId)
            return attackedDontBelongEnnemy;

        if (isFrozen())
            return isFrozen;

        if (getHasAttacked())
            return cardAlreadyAttacked;

        int enemyIdx = Errors.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return notTank;

        return Errors.noError;
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
