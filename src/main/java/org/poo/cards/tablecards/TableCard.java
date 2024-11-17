package org.poo.cards.tablecards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.game.GamesSetup;
import org.poo.game.Game;
import org.poo.game.GameTable;
import org.poo.utils.JsonNode;

/**
 * Class used to represent cards that can be placed on the gameTable.
 * Instances of this class have some extra attributes such as attackDamage,
 * frozen (if the card is frozen or not) and rowToPlace. This class also has
 * some specific errors that can appear when trying to perform methods on
 * a table card.
 */
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


    /**
     * Constructor for the table card, first calls the constructor of the
     * super class with the same params (cardInput and belongsTo).
     * In addition to the super class constructor, this time the
     * frozen attribute appears and is set to the default value (false)
     * @param cardInput input of the card
     * @param belongsTo id of the player that card belongs to
     */
    public TableCard(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
        attackDamage = cardInput.getAttackDamage();
        frozen = false;
    }


    /**
     * Method used to perform a specific TableCard special ability
     * (ex: Disciple, Miraj)
     * @param attackedCard the card that is attacked
     * @param table the current GameTable
     * @param curPlayerId the id of the player that performs the action
     * @return null if no error occurred or an appropriate one otherwise
     */
    protected String
    useAbility(Card attackedCard, GameTable table, int curPlayerId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

        return null;
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

        int enemyIdx = GamesSetup.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return notTank;

        return null;
    }


    private String
    getAttackHeroError(GameTable table, int curPlayerId, Card attackedCard) {
        if (attackedCard.getBelongsTo() == curPlayerId)
            return attackedDontBelongEnnemy;

        if (isFrozen())
            return isFrozen;

        if (getHasAttacked())
            return cardAlreadyAttacked;

        int enemyIdx = GamesSetup.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank())
            return notTank;

        return null;
    }


    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowToPlace() {
        return rowToPlace;
    }

    public void setRowToPlace(int rowToPlace) {
        this.rowToPlace = rowToPlace;
    }
}
