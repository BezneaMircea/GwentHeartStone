package org.poo.cards.heroes;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.poo.cards.Card;

import org.poo.fileio.CardInput;

import org.poo.game.GameTable;
import org.poo.game.Player;
import org.poo.utils.JsonNode;


/**
 * Class used to represent a "Hero" card and the constants of a hero.
 */
public class Hero extends Card {
    public static final int HERO_INITIAL_HEALTH;
    public static final String NO_MANA_HERO;
    public static final String ALREADY_ATTACKED;
    public static final String ROW_NOT_ENEMY;
    public static final String ROW_NOT_CUR;

    static {
        HERO_INITIAL_HEALTH = 30;
        NO_MANA_HERO = "Not enough mana to use hero's ability.";
        ALREADY_ATTACKED = "Hero has already attacked this turn.";
        ROW_NOT_ENEMY = "Selected row does not belong to the enemy.";
        ROW_NOT_CUR = "Selected row does not belong to the current player.";
    }

    /**
     * Constructor used to build a Hero card, calls the constructor
     * from the super class Card and also sets the heroInitialHealth
     */
    public Hero(final CardInput cardInput, final int belongsTo) {
        super(cardInput, belongsTo);
        setHealth(HERO_INITIAL_HEALTH);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHero() {
        return true;
    }

    /**
     * Method applied to use a hero's ability
     *
     * @param table           the current Game Table
     * @param affectedRow     the affected row
     * @param currentPlayerId the id of the player that has this hero
     * @return null if no error occurred, or an appropriate error otherwise
     */
    protected String
    useAbility(final GameTable table, final int affectedRow, final int currentPlayerId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String
    useHeroAbility(final GameTable table, final int affectedRow, final Player currentPlayer) {
        if (getMana() > currentPlayer.getMana()) {
            return NO_MANA_HERO;
        }

        if (getHasAttacked()) {
            return ALREADY_ATTACKED;
        }

        return useAbility(table, affectedRow, currentPlayer.getPlayerId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectNode writeCard() {
        final ObjectNode heroNode = JsonNode.mapper.createObjectNode();

        heroNode.put("mana", getMana());
        heroNode.put("description", getDescription());
        final ArrayNode colorArray = JsonNode.writeColors(getColors());
        heroNode.set("colors", colorArray);
        heroNode.put("name", getName());
        heroNode.put("health", getHealth());

        return heroNode;
    }

}
