package org.poo.cards;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import org.poo.fileio.CardInput;
import org.poo.main.GameTable;
import org.poo.main.Player;

/**
 * Class used to represent the generalities of a card.
 * This is the first class within the cards hierarchy
 * @see org.poo.cards.heroes.Hero
 * @see org.poo.cards.tablecards.TableCard
 */
public class Card {
    private int mana;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean hasAttacked;
    private final int belongsTo;

    /**
     * Constructor used to set the general card values
     * @param cardInput the card data
     * @param belongsTo id of the player that the card belongs to
     */
    public Card(CardInput cardInput, int belongsTo) {
        mana = cardInput.getMana();
        health = cardInput.getHealth();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        name = cardInput.getName();
        hasAttacked = false;
        this.belongsTo = belongsTo;
    }

    /**
     * Method used to attack a given card that is placed on the table
     * @param table
     * @param currentPlayerId the player
     * @param attackedCard
     * @return null if the action succeeded or an appropriate
     * error in case it fails
     */
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

    public String
    useHeroAbility(GameTable table, int affectedRow, Player currentPlayer) {
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
