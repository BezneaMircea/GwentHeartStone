package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;
import org.poo.cards.Card;
import java.util.ArrayList;

public final class JsonNode {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static ArrayNode writeColors(ArrayList<String> colors) {
        ArrayNode colorArray = mapper.createArrayNode();
        for (String color : colors)
            colorArray.add(color);

        return colorArray;
    }

    private static ObjectNode writeCard(Card card) {
        return card.writeCard();
    }

    private static ArrayNode
    writeDeck(ArrayList<Card> deck) {
        ArrayNode deckNode = mapper.createArrayNode();
        for (Card card : deck)
            deckNode.add(writeCard(card));

        return deckNode;
    }

    public static ObjectNode
    writePlayerDeck(ActionsInput action, ArrayList<Card> deck) {
        ObjectNode deckNode = mapper.createObjectNode();
        deckNode.put("command", action.getCommand());
        deckNode.put("playerIdx", action.getPlayerIdx());
        deckNode.set("output", writeDeck(deck));

        return deckNode;
    }

    public static ObjectNode
    writePlayerHero(ActionsInput actions, Card hero) {
        ObjectNode heroNode = mapper.createObjectNode();
        heroNode.put("command", actions.getCommand());
        heroNode.put("playerIdx", actions.getPlayerIdx());
        heroNode.set("output", writeCard(hero));

        return heroNode;
    }

    public static ObjectNode
    writePlayerTurn(ActionsInput actions, int currentPlayer) {
        ObjectNode turnNode = mapper.createObjectNode();
        turnNode.put("command", actions.getCommand());
        turnNode.put("output", currentPlayer);

        return turnNode;
    }

    public static ObjectNode
    writePlaceCard(ActionsInput action, String error) {
        if (error == null)
            return null;

        ObjectNode placeCardNode = mapper.createObjectNode();
        placeCardNode.put("command", action.getCommand());
        placeCardNode.put("handIdx", action.getHandIdx());
        placeCardNode.put("error", error);

        return placeCardNode;
    }

    public static ObjectNode writeCardsInHand(ActionsInput actions, ArrayList<Card> hand) {
        return writePlayerDeck(actions, hand);
    }

    public static ObjectNode writePlayerMana(ActionsInput actionsInput, int mana) {
        ObjectNode playerManaNode = mapper.createObjectNode();
        playerManaNode.put("command", actionsInput.getCommand());
        playerManaNode.put("playerIdx", actionsInput.getPlayerIdx());
        playerManaNode.put("output", mana);

        return playerManaNode;
    }

    public static ObjectNode writeCardsOnTable(ActionsInput actions, GameTable table) {
        ObjectNode tableNode = mapper.createObjectNode();
        tableNode.put("command", actions.getCommand());

        ArrayNode cardsOnTableNode = mapper.createArrayNode();
        for (ArrayList<Card> row : table.getTable())
            cardsOnTableNode.add(writeDeck(row));
        tableNode.set("output", cardsOnTableNode);

        return tableNode;
    }

    public static ObjectNode writeCardAtPosition(ActionsInput action, Card card, String error) {
        ObjectNode positionNode = mapper.createObjectNode();
        positionNode.put("command", action.getCommand());
        positionNode.put("x", action.getX());
        positionNode.put("y", action.getY());

        if (error == null)
            positionNode.set("output", writeCard(card));
        else
            positionNode.put("output", error);

        return positionNode;
    }



    private static ObjectNode writeCoordinates(Coordinates cord) {
        ObjectNode coordinatesNode = mapper.createObjectNode();
        coordinatesNode.put("x", cord.getX());
        coordinatesNode.put("y", cord.getY());

        return coordinatesNode;
    }

    public static ObjectNode writeCardUsesAttack(ActionsInput action, String error, Card a) {
        if (error == null)
            return null;

        ObjectNode cardAttacks = mapper.createObjectNode();
        cardAttacks.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
        cardAttacks.set("cardAttacked", writeCoordinates(action.getCardAttacked()));
        cardAttacks.put("error", error);
        cardAttacks.put("name", a.getName());


        return cardAttacks;
    }

    public static ObjectNode writeCardUsesAbility(ActionsInput action, String error) {
        if (error == null)
            return null;

        ObjectNode cardUsesAbility = mapper.createObjectNode();
        cardUsesAbility.put("command", action.getCommand());
        cardUsesAbility.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
        cardUsesAbility.set("cardAttacked", writeCoordinates(action.getCardAttacked()));
        cardUsesAbility.put("error", error);

        return cardUsesAbility;
    }





//

//
//
//
//
//
//

//
//
//    public static ObjectNode writeUseAttackHero(ActionsInput action, String error, String result) {
//        if (result == null && error == null)
//            return null;
//
//        ObjectNode useAttackHeroNode = mapper.createObjectNode();
//        if (result != null)
//            useAttackHeroNode.put("gameEnded", result);
//
//        if (error != null) {
//            useAttackHeroNode.put("command", action.getCommand());
//            useAttackHeroNode.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
//            useAttackHeroNode.put("error", error);
//        }
//
//        return useAttackHeroNode;
//    }

}