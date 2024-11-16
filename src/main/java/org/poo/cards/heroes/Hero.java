package org.poo.cards.heroes;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Card;
import org.poo.fileio.CardInput;
import org.poo.main.GameTable;
import org.poo.main.JsonNode;
import org.poo.main.Player;


public class Hero extends Card {
    public static final int heroInitialHealth;
    public static final String noManaHero;
    public static final String alreadyAttacked;
    public static final String rowNotEnemy;
    public static final String rowNotCur;

    static {
        heroInitialHealth = 30;
        noManaHero = "Not enough mana to use hero's ability.";
        alreadyAttacked = "Hero has already attacked this turn.";
        rowNotEnemy = "Selected row does not belong to the enemy.";
        rowNotCur = "Selected row does not belong to the current player.";
    }

    public Hero(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
        setHealth(heroInitialHealth);
    }

    @Override
    public boolean isHero() {
        return true;
    }

    protected String
    useAbility(GameTable table, int affectedRow, int currentPlayerId) {
        return null;
    }

    @Override
    public String
    useHeroAbility(GameTable table, int affectedRow, Player currentPlayer) {
        if (getMana() > currentPlayer.getMana())
            return noManaHero;

        if (getHasAttacked())
            return alreadyAttacked;

        return useAbility(table, affectedRow, currentPlayer.getPlayerId());
    }

    @Override
    public ObjectNode writeCard() {
        ObjectNode heroNode = JsonNode.mapper.createObjectNode();

        heroNode.put("mana", getMana());
        heroNode.put("description", getDescription());
        ArrayNode colorArray = JsonNode.writeColors(getColors());
        heroNode.set("colors", colorArray);
        heroNode.put("name", getName());
        heroNode.put("health", getHealth());

        return heroNode;
    }

}
