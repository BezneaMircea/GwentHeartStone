package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;
import org.poo.cards.Card;
import org.poo.game.Game;
import org.poo.game.GameTable;

import java.util.ArrayList;

/**
 * Helper class used to write actions
 * input + (output)/(output errors) in JSON format
 */
public final class JsonNode {
    public static final ObjectMapper MAPPER = new ObjectMapper();


    private JsonNode() {
        throw new UnsupportedOperationException("Utility Class");
    }
    /**
     * Method used to write a color list in JSON format
     *
     * @param colors the color list
     * @return and ArrayNode representing the JSON format of the color's list
     */
    public static ArrayNode writeColors(final ArrayList<String> colors) {
        ArrayNode colorArray = MAPPER.createArrayNode();
        for (String color : colors) {
            colorArray.add(color);
        }

        return colorArray;
    }

    private static ObjectNode writeCard(final Card card) {
        return card.writeCard();
    }

    private static ArrayNode
    writeDeck(final ArrayList<Card> deck) {
        ArrayNode deckNode = MAPPER.createArrayNode();
        for (Card card : deck) {
            deckNode.add(writeCard(card));
        }

        return deckNode;
    }

    /**
     * Method used to write in JSON format the input and output
     * of the getPlayerDeck command
     *
     * @param action the current action
     * @param deck   the deck that is to be written
     * @return the JSON format of the given action + deck in the form
     * of an ObjectNode
     */
    public static ObjectNode
    writePlayerDeck(final ActionsInput action, final ArrayList<Card> deck) {
        ObjectNode deckNode = MAPPER.createObjectNode();
        deckNode.put("command", action.getCommand());
        deckNode.put("playerIdx", action.getPlayerIdx());
        deckNode.set("output", writeDeck(deck));

        return deckNode;
    }

    /**
     * Method used to write in JSON format the input and output
     * of the getPlayerHero command
     *
     * @param action the current action
     * @param hero   hero that is to be written
     * @return the JSON format in the form of an ObjectNode
     */
    public static ObjectNode
    writePlayerHero(final ActionsInput action, final Card hero) {
        ObjectNode heroNode = MAPPER.createObjectNode();
        heroNode.put("command", action.getCommand());
        heroNode.put("playerIdx", action.getPlayerIdx());
        heroNode.set("output", writeCard(hero));

        return heroNode;
    }


    /**
     * Method used to write the input and output ot the getPlayerTurn command
     *
     * @param action        the current action
     * @param currentPlayer the current player at turn given in the form of id
     *                      (currently 1 and 2)
     * @return the JSON format of the given command input and output
     */
    public static ObjectNode
    writePlayerTurn(final ActionsInput action, final int currentPlayer) {
        ObjectNode turnNode = MAPPER.createObjectNode();
        turnNode.put("command", action.getCommand());
        turnNode.put("output", currentPlayer);

        return turnNode;
    }


    /**
     * Method used to write in JSON format the input and output of
     * the placeCard command in case an error occurred
     *
     * @param action the current action
     * @param error  the output error or null if no error occurred
     * @return the JSON format of the given action input and output error
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writePlaceCard(final ActionsInput action, final String error) {
        if (error == null) {
            return null;
        }

        ObjectNode placeCardNode = MAPPER.createObjectNode();
        placeCardNode.put("command", action.getCommand());
        placeCardNode.put("handIdx", action.getHandIdx());
        placeCardNode.put("error", error);

        return placeCardNode;
    }


    /**
     * Method used to write the input and output of the getCardsInHand command
     * in JSON format
     *
     * @param action the current action
     * @param hand   list of cards representing the cards in one of the players
     *               hand
     * @return the JSON format of the input and output of the given command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writeCardsInHand(final ActionsInput action, final ArrayList<Card> hand) {
        return writePlayerDeck(action, hand);
    }

    /**
     * Method used to write the input and output of the getPlayerMana
     * command in JSON format.
     *
     * @param action the current action
     * @param mana   mana of the player
     * @return the JSON format of the input and output of the given command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writePlayerMana(final ActionsInput action, final int mana) {
        ObjectNode playerManaNode = MAPPER.createObjectNode();
        playerManaNode.put("command", action.getCommand());
        playerManaNode.put("playerIdx", action.getPlayerIdx());
        playerManaNode.put("output", mana);

        return playerManaNode;
    }

    /**
     * Method used to write the input and output of the getCardsOnTable command
     *
     * @param action the current action
     * @param table  the table that contains the cards
     * @return the Json format of the input and output of the given command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writeCardsOnTable(final ActionsInput action, final GameTable table) {
        ObjectNode tableNode = MAPPER.createObjectNode();
        tableNode.put("command", action.getCommand());

        ArrayNode cardsOnTableNode = MAPPER.createArrayNode();
        for (ArrayList<Card> row : table.getTable()) {
            cardsOnTableNode.add(writeDeck(row));
        }
        tableNode.set("output", cardsOnTableNode);

        return tableNode;
    }


    /**
     * Method used to write the input and output of the getCardAtPosition
     * command in JSON format. The output is the card if no error occurred
     * or the error otherwise
     *
     * @param action the current action
     * @param card   the card at the given position (null if there was no card)
     * @param error  the output error or null if no error occurred
     * @return the JSON format of the given action input and output error
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writeCardAtPosition(final ActionsInput action, final Card card, final String error) {
        ObjectNode positionNode = MAPPER.createObjectNode();
        positionNode.put("command", action.getCommand());
        positionNode.put("x", action.getX());
        positionNode.put("y", action.getY());

        if (error == null) {
            positionNode.set("output", writeCard(card));
        } else {
            positionNode.put("output", error);
        }

        return positionNode;
    }


    private static ObjectNode
    writeCoordinates(final Coordinates cord) {
        ObjectNode coordinatesNode = MAPPER.createObjectNode();
        coordinatesNode.put("x", cord.getX());
        coordinatesNode.put("y", cord.getY());

        return coordinatesNode;
    }


    /**
     * Method used to write the input and output error of the
     * cardUsesAttack command.
     *
     * @param action the current action
     * @param error  the output error or null if no error occurred
     * @return the JSON format of the given action input and output error
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writeCardUsesAttack(final ActionsInput action, final String error) {
        if (error == null) {
            return null;
        }

        ObjectNode cardAttacks = MAPPER.createObjectNode();
        cardAttacks.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
        cardAttacks.set("cardAttacked", writeCoordinates(action.getCardAttacked()));
        cardAttacks.put("command", action.getCommand());
        cardAttacks.put("error", error);

        return cardAttacks;
    }


    /**
     * Method used to write in JSON format the input and output error
     * of a given command
     *
     * @param action the current action
     * @param error  the output error or null if no error occurred
     * @return the JSON format of the given action input and output error
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writeCardUsesAbility(final ActionsInput action, final String error) {
        if (error == null) {
            return null;
        }

        ObjectNode cardUsesAbility = MAPPER.createObjectNode();
        cardUsesAbility.put("command", action.getCommand());
        cardUsesAbility.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
        cardUsesAbility.set("cardAttacked", writeCoordinates(action.getCardAttacked()));
        cardUsesAbility.put("error", error);

        return cardUsesAbility;
    }

    /**
     * Method used to write the input and output of the useAttackHero command
     *
     * @param action the current action
     * @param res    result of the action (either an error or gameEnded)
     * @return the JSON format of the given action input and output
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writeUseAttackHero(final ActionsInput action, final String res) {
        if (res == null) {
            return null;
        }

        ObjectNode useAttackHeroNode = MAPPER.createObjectNode();

        if (res.equals(Game.PLAYER_ONE_WON) || res.equals(Game.PLAYER_TWO_WON)) {
            useAttackHeroNode.put("gameEnded", res);
        } else {
            useAttackHeroNode.put("command", action.getCommand());
            useAttackHeroNode.set("cardAttacker", writeCoordinates(action.getCardAttacker()));
            useAttackHeroNode.put("error", res);
        }

        return useAttackHeroNode;
    }

    /**
     * Method used to write the input and output error of the given command
     *
     * @param action the current action
     * @param error  the output error or null if no error occurred
     * @return the JSON format of the given action input and output error
     * in the form of an ObjectNode or null if no error occurred
     */
    public static ObjectNode
    writeUseHeroAbility(final ActionsInput action, final String error) {
        if (error == null) {
            return null;
        }

        ObjectNode heroAbilityNode = MAPPER.createObjectNode();

        heroAbilityNode.put("command", action.getCommand());
        heroAbilityNode.put("affectedRow", action.getAffectedRow());
        heroAbilityNode.put("error", error);

        return heroAbilityNode;
    }

    /**
     * Method used to write the input and output of the
     * getFrozenCardsOnTable command
     *
     * @param action the action input
     * @param table  the GameTable where we will look for the frozen cards
     * @return the JSON format of the input and output of the given command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writeFrozenCardsOnTable(final ActionsInput action, final GameTable table) {
        ObjectNode frozenCardsNode = MAPPER.createObjectNode();

        ArrayNode frozenCards = MAPPER.createArrayNode();
        for (ArrayList<Card> row : table.getTable()) {
            for (Card card : row) {
                if (card.isFrozen()) {
                    frozenCards.add(writeCard(card));
                }
            }
        }

        frozenCardsNode.put("command", action.getCommand());
        frozenCardsNode.set("output", frozenCards);

        return frozenCardsNode;
    }


    /**
     * Method used to write the input and output of the statistics command
     * getTotalGamesPlayed
     *
     * @param action           the current action
     * @param totalGamesPlayed the total nr of games played by the two players
     * @return the JSON format of the input and output of the current command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writeTotalGamesPlayed(final ActionsInput action, final int totalGamesPlayed) {
        ObjectNode totalGamesPlayedNode = MAPPER.createObjectNode();

        totalGamesPlayedNode.put("command", action.getCommand());
        totalGamesPlayedNode.put("output", totalGamesPlayed);

        return totalGamesPlayedNode;
    }


    /**
     * Method used to write the input and output of the statistics command
     * getPlayerOneWins or getPlayerTwoWins.
     *
     * @param action     the current action
     * @param playerWins the total of the wanted player wins
     * @return the JSON format of the input and output of the current command
     * in the form of an ObjectNode
     */
    public static ObjectNode
    writePlayerWins(final ActionsInput action, final int playerWins) {
        ObjectNode playerWinsNode = MAPPER.createObjectNode();

        playerWinsNode.put("command", action.getCommand());
        playerWinsNode.put("output", playerWins);

        return playerWinsNode;
    }

}
