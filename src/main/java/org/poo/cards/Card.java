package org.poo.cards;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import org.poo.fileio.CardInput;
import org.poo.main.GameTable;


public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean hasAttacked;
    private final int belongsTo;

    public Card(CardInput cardInput, int belongsTo) {
        mana = cardInput.getMana();
        health = cardInput.getHealth();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        name = cardInput.getName();
        hasAttacked = false;
        this.belongsTo = belongsTo;
    }


    public String attackCard(GameTable table, int currentPlayerId, Card attackedCard) {
        return null;
    }
    public int getRowToPlace() {return -1; }

    public boolean isFrozen() { return false; }

    public void setFrozen(boolean frozen) {
    }

    public boolean isTank() {return false; }

    public boolean isHero() {
        return false;
    }

    public ObjectNode writeCard() {
        return null;
    }

    public String useCardAbility(Card attackedCard, GameTable table, int currentPlayer) {
        return null;
    }




    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getHasAttacked() {
        return hasAttacked;
    }

    public void setHasAttacked(boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    public int getBelongsTo() {
        return belongsTo;
    }

    public int getAttackDamage() {
        return 0;
    }

    public void setAttackDamage(int attackDamage) {}
}
