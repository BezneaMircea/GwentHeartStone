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
     * @param table the GameTable of the current game
     * @param currentPlayerId the player that performs the action. We need this
     *                        parameter because we don't know if the card given in
     *                        the input really belongs to the player that performs
     *                        the action
     * @param attackedCard the card that is attacked
     * @return null if the action succeeded or an appropriate
     * error in case it fails
     */
    public String attackCard(GameTable table, int currentPlayerId, Card attackedCard) {
        return null;
    }

    /**
     * Method to get the row where to place a current card
     * @return index of the row where to place the current card
     */
    public int getRowToPlace() {return -1; }

    /**
     * Method to find out if a card is frozen or not
     * @return true if the card is frozen, false otherwise
     */
    public boolean isFrozen() { return false; }

    /**
     * Method used to freeze or unfreeze a card
     */
    public void setFrozen(boolean frozen) {
    }

    /**
     * Method to find out if the current card is a tank or not
     * @return true if card is tank, false otherwise
     */
    public boolean isTank() {return false; }

    /**
     * Method to find out if the current card is a hero or not
     * @return true if the card is a hero, false otherwise
     */
    public boolean isHero() {
        return false;
    }

    /**
     * Method used to write a card in JSON format
     * @return the JSON format in the form of an ObjectNode
     */
    public ObjectNode writeCard() {
        return null;
    }

    /**
     * Method applied to use card's special ability
     * @param attackedCard card to attack
     * @param table the current GameTable
     * @param currentPlayer the id of the player that performs the action
     * @return null if no error occurred, or an appropriate error otherwise
     */
    public String useCardAbility(Card attackedCard, GameTable table, int currentPlayer) {
        return null;
    }

    /**
     * Method applied to use a hero's attack special ability.
     * If card is not a hero nothing happens
     * @param table the current GameTable
     * @param affectedRow the row that is affected
     * @param currentPlayer the id of the player that performs the action
     * @return null if the card is not a hero or no error occurred,
     * an appropriate error otherwise
     */
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
