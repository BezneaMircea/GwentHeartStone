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

    protected static final String IS_FROZEN;
    protected static final String CARD_ALREADY_ATTACKED;
    protected static final String ATTACKED_DONT_BELONG_CUR;
    protected static final String ATTACKED_DONT_BELONG_ENNEMY;
    protected static final String ATTACKER_DONT_BELONG_CUR;
    protected static final String NOT_TANK;

    static {
        IS_FROZEN = "Attacker card is frozen.";
        CARD_ALREADY_ATTACKED = "Attacker card has already attacked this turn.";
        ATTACKED_DONT_BELONG_CUR = "Attacked card does not belong to the current player.";
        ATTACKED_DONT_BELONG_ENNEMY = "Attacked card does not belong to the enemy.";
        ATTACKER_DONT_BELONG_CUR = "Attacker card does not belong to the current player.";
        NOT_TANK = "Attacked card is not of type 'Tank'.";
    }


    /**
     * Constructor for the table card, first calls the constructor of the
     * super class with the same params (cardInput and belongsTo).
     * In addition to the super class constructor, this time the
     * frozen attribute appears and is set to the default value (false)
     *
     * @param cardInput input of the card
     * @param belongsTo id of the player that card belongs to
     */
    public TableCard(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
        attackDamage = cardInput.getAttackDamage();
        frozen = false;
    }


    /**
     * Method used to perform a specific TableCard special ability
     * (ex: Disciple, Miraj)
     *
     * @param attackedCard the card that is attacked
     * @param table        the current GameTable
     * @param curPlayerId  the id of the player that performs the action
     * @return null if no error occurred or an appropriate one otherwise
     */
    protected String
    useAbility(final Card attackedCard, final GameTable table, final int curPlayerId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String
    useCardAbility(final Card attackedCard, final GameTable table, final int curPlayerId) {
        if (attackedCard == null) {
            return null;
        }

        if (frozen) {
            return IS_FROZEN;
        }

        if (getHasAttacked()) {
            return CARD_ALREADY_ATTACKED;
        }

        return useAbility(attackedCard, table, curPlayerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String
    attackCard(final GameTable table, final int curPlayerId, final Card attackedCard) {
        String error;
        if (attackedCard.isHero()) {
            error = getAttackHeroError(table, curPlayerId, attackedCard);
        } else {
            error = getAttackCardError(table, curPlayerId, attackedCard);
        }

        if (error != null) {
            return error;
        }

        attackedCard.setHealth(attackedCard.getHealth() - attackDamage);
        setHasAttacked(true);
        if (attackedCard.getHealth() <= 0) {
            attackedCard.setHealth(0);
            if (attackedCard.isHero()) {
                return Game.gameEnded(curPlayerId);
            }

            table.removeCard(attackedCard);
        }

        return null;
    }


    private String
    getAttackCardError(final GameTable table, final int curPlayerId, final Card attackedCard) {
        if (getBelongsTo() != curPlayerId) {
            return ATTACKER_DONT_BELONG_CUR;
        }

        if (attackedCard.getBelongsTo() == curPlayerId) {
            return ATTACKED_DONT_BELONG_ENNEMY;
        }

        if (getHasAttacked()) {
            return CARD_ALREADY_ATTACKED;
        }

        if (isFrozen()) {
            return IS_FROZEN;
        }

        int enemyIdx = GamesSetup.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank()) {
            return NOT_TANK;
        }

        return null;
    }


    private String
    getAttackHeroError(final GameTable table, final int curPlayerId, final Card attackedCard) {
        if (attackedCard.getBelongsTo() == curPlayerId) {
            return ATTACKED_DONT_BELONG_ENNEMY;
        }

        if (isFrozen()) {
            return IS_FROZEN;
        }

        if (getHasAttacked()) {
            return CARD_ALREADY_ATTACKED;
        }

        int enemyIdx = GamesSetup.getOtherPlayerIdx(curPlayerId);
        if (table.doesPlayerHaveTanks(enemyIdx) && !attackedCard.isTank()) {
            return NOT_TANK;
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public final ObjectNode writeCard() {
        ObjectNode minionNode = JsonNode.MAPPER.createObjectNode();

        minionNode.put("mana", getMana());
        minionNode.put("attackDamage", getAttackDamage());
        minionNode.put("health", getHealth());
        minionNode.put("description", getDescription());
        ArrayNode colorArray = JsonNode.writeColors(getColors());
        minionNode.set("colors", colorArray);
        minionNode.put("name", getName());

        return minionNode;
    }

    /**
     * for coding style
     */
    @Override
    public final int getAttackDamage() {
        return attackDamage;
    }

    /**
     * for coding style
     */
    @Override
    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isFrozen() {
        return frozen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getRowToPlace() {
        return rowToPlace;
    }

    /**
     * for coding style
     */
    public final void setRowToPlace(final int rowToPlace) {
        this.rowToPlace = rowToPlace;
    }
}
