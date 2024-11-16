package org.poo.cards;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.main.JsonNode;


public class Hero extends Card {
    public static final int heroInitialHealth = 30;

    public Hero(CardInput cardInput, int belongsTo) {
        super(cardInput, belongsTo);
        setHealth(heroInitialHealth);
    }

    @Override
    public boolean isHero() {
        return true;
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
