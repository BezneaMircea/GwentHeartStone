package org.poo.main;

import org.poo.cards.*;
import org.poo.fileio.CardInput;

import java.util.ArrayList;


public class Player {
    private final ArrayList<Card> deck;
    private final Card hero;
    private final ArrayList<Card> hand;
    private int mana;
    private final int playerId;

    public Player(CardInput hero, ArrayList<CardInput> deck, int playerId) {
        this.hero = new Hero(hero, playerId);

        this.deck = new ArrayList<Card>();
        for (CardInput card : deck) {
            String name = card.getName();
            switch (name) {
                case "The Ripper": this.deck.add(new TheRipper(card, playerId));
                break;
                case "Miraj": this.deck.add(new Miraj(card, playerId));
                break;
                case "The Cursed One": this.deck.add(new TheCursedOne(card, playerId));
                break;
                case "Disciple": this.deck.add(new Disciple(card, playerId));
                break;
                case "Goliath", "Warden": this.deck.add(new Tank(card, playerId));
                break;
                case "Sentinel", "Berserker": this.deck.add(new BackRowCard(card, playerId));
                break;
                default: System.err.println("Invalid card name: " + name);
            }
        }

        this.hand = new ArrayList<Card>();
        this.playerId = playerId;
        mana = 0;
    }

    public void drawCard() {
        if (deck.isEmpty())
            return;

        hand.add(deck.remove(0));
    }

    public String placeCard(GameTable table, int handIdx) {
        Card card = hand.get(handIdx);

        String error = Errors.placeCardError(card, table, mana);
        if (error != null)
            return error;

        table.addCard(card);
        hand.remove(handIdx);
        mana -= card.getMana();
        return null;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card getHerro() {
        return hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getPlayerId() {
        return playerId;
    }
}
