package org.poo.game;


import org.poo.cards.Card;
import org.poo.cards.heroes.EmpressThorina;
import org.poo.cards.heroes.GeneralKocioraw;
import org.poo.cards.heroes.KingMudface;
import org.poo.cards.heroes.LordRoyce;
import org.poo.cards.tablecards.special.Disciple;
import org.poo.cards.tablecards.special.Miraj;
import org.poo.cards.tablecards.special.TheCursedOne;
import org.poo.cards.tablecards.special.TheRipper;
import org.poo.cards.tablecards.BackRowCard;
import org.poo.cards.tablecards.special.Tank;
import org.poo.fileio.CardInput;

import java.util.ArrayList;


/**
 * Class used to describe a player of the current game and the actions
 * it can perform.
 */
public final class Player {
    private final ArrayList<Card> deck;
    private Card hero;
    private final ArrayList<Card> hand;
    private int mana;
    private final int playerId;

    /**
     * Constructor used to give the player it s attributes.
     * In this constructor we also check for the name of the cards
     * within the player's deck in order to declare them with the proper
     * dynamic type
     *
     * @param hero     hero of the current player
     * @param deck     deck of the current player
     * @param playerId id of the current player
     */
    public Player(final CardInput hero, final ArrayList<CardInput> deck, final int playerId) {
        final String heroName = hero.getName();
        switch (heroName) {
            case "Lord Royce":
                this.hero = new LordRoyce(hero, playerId);
                break;
            case "Empress Thorina":
                this.hero = new EmpressThorina(hero, playerId);
                break;
            case "King Mudface":
                this.hero = new KingMudface(hero, playerId);
                break;
            case "General Kocioraw":
                this.hero = new GeneralKocioraw(hero, playerId);
                break;
            default:
                System.err.println("Invalid hero name: " + heroName);
        }

        this.deck = new ArrayList<Card>();
        for (final CardInput card : deck) {
            final String name = card.getName();
            switch (name) {
                case "The Ripper":
                    this.deck.add(new TheRipper(card, playerId));
                    break;
                case "Miraj":
                    this.deck.add(new Miraj(card, playerId));
                    break;
                case "The Cursed One":
                    this.deck.add(new TheCursedOne(card, playerId));
                    break;
                case "Disciple":
                    this.deck.add(new Disciple(card, playerId));
                    break;
                case "Goliath", "Warden":
                    this.deck.add(new Tank(card, playerId));
                    break;
                case "Sentinel", "Berserker":
                    this.deck.add(new BackRowCard(card, playerId));
                    break;
                default:
                    System.err.println("Invalid card name: " + name);
            }
        }

        this.hand = new ArrayList<Card>();
        this.playerId = playerId;
        mana = 0;
    }


    /**
     * Method used to draw a card from the players deck and put it in his hand
     */
    public void drawCard() {
        if (deck.isEmpty()) {
            return;
        }

        hand.add(deck.remove(0));
    }


    /**
     * Method used to place a card on the table
     *
     * @param table   the game table where the card will be placed
     * @param handIdx the index of the card to be placed from the player's hand
     * @return an error string if an error occurs or
     * null if no error was found
     */
    public String placeCard(final GameTable table, final int handIdx) {
        final Card card = hand.get(handIdx);

        final String error = table.addCard(card, mana);
        if (error != null) {
            return error;
        }

        hand.remove(handIdx);
        mana -= card.getMana();
        return null;
    }


    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card getHero() {
        return hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getPlayerId() {
        return playerId;
    }

}
