package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import org.poo.cards.Card;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    private final Player playerOne;
    private final Player playerTwo;
    private final Random seed;
    private int manaGiven;
    private int currentPlayer;
    private final int startingPlayer;
    private final GameTable table;

    public Game(Player playerOne, Player playerTwo, long seed, int currentPlayer) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.currentPlayer = currentPlayer;
        startingPlayer = currentPlayer;
        table = new GameTable();
        manaGiven = 1;

        this.seed = new Random();

        this.seed.setSeed(seed);
        ShuffleDeck(playerOne.getDeck(), this.seed);

        this.seed.setSeed(seed);
        ShuffleDeck(playerTwo.getDeck(), this.seed);
    }

    private void ShuffleDeck(ArrayList<Card> deck, Random seed) {
        Collections.shuffle(deck, this.seed);
    }


    private Player getInstanceOfCurrentPlayer() {
        if (currentPlayer == 1)
            return playerOne;

        return playerTwo;
    }

    private Player getInstanceOfWaitingPlayer() {
        if (currentPlayer == 1)
            return playerTwo;

        return playerOne;
    }

    private String gameEnded() {
        if (currentPlayer == 1)
            return "Player one killed the enemy hero.";

        return "Player two killed the enemy hero.";
    }

    private void addMana() {
        playerOne.setMana(playerOne.getMana() + manaGiven);
        playerTwo.setMana(playerTwo.getMana() + manaGiven);
    }

    private void newRound() {
        playerOne.drawCard();
        playerTwo.drawCard();
        addMana();
        if (manaGiven < GamesSetup.maxManaGiven)
            manaGiven++;
    }

    private ObjectNode endPlayerTurn() {
        if (startingPlayer != currentPlayer)
            newRound();

        if (currentPlayer == GamesSetup.playerOneIdx)
            currentPlayer = GamesSetup.playerTwoIdx;
        else
            currentPlayer = GamesSetup.playerOneIdx;

        table.resetAttack();
        return null;
    }


    private ObjectNode getPlayerDeck(ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.playerOneIdx)
            return JsonNode.writePlayerDeck(action, playerOne.getDeck());
        else
            return JsonNode.writePlayerDeck(action, playerTwo.getDeck());
    }

    private ObjectNode getPlayerHero(ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.playerOneIdx)
            return JsonNode.writePlayerHero(action, playerOne.getHerro());
        else
            return JsonNode.writePlayerHero(action, playerTwo.getHerro());
    }

    private ObjectNode getPlayerTurn(ActionsInput action) {
        return JsonNode.writePlayerTurn(action, currentPlayer);
    }

    private ObjectNode placeCard(ActionsInput action) {
        Player player = getInstanceOfCurrentPlayer();
        String error = player.placeCard(table, action.getHandIdx());

        return JsonNode.writePlaceCard(action, error);
    }

    private ObjectNode getCardsInHand(ActionsInput action) {
        if (action.getPlayerIdx() == 1)
            return JsonNode.writeCardsInHand(action, playerOne.getHand());
        else
            return JsonNode.writeCardsInHand(action, playerTwo.getHand());
    }

    private ObjectNode getPlayerMana(ActionsInput action) {
        if (action.getPlayerIdx() == 1)
            return JsonNode.writePlayerMana(action, playerOne.getMana());
        else
            return JsonNode.writePlayerMana(action, playerTwo.getMana());
    }

    private ObjectNode getCardsOnTable(ActionsInput action) {
        return JsonNode.writeCardsOnTable(action, table);
    }

    private ObjectNode getCardAtPosition(ActionsInput action) {
        Coordinates wantedCardCord = new Coordinates(action.getX(), action.getY());

        String error = Errors.noCardAtPositionError(wantedCardCord, table);
        return JsonNode.writeCardAtPosition(action, table.getElement(wantedCardCord), error);
    }

    private ObjectNode cardUsesAttack(ActionsInput action) {
        Coordinates attackerCord = action.getCardAttacker();
        Coordinates attackedCord = action.getCardAttacked();
        Card attackerCard = table.getElement(attackerCord);
        Card attackedCard = table.getElement(attackedCord);

        if (attackerCard == null || attackedCard == null)
            return null;

        String error = attackerCard.attackCard(table, currentPlayer, attackedCard);

        return JsonNode.writeCardUsesAttack(action, error);
    }

    private ObjectNode cardUsesAbility(ActionsInput action) {
        Coordinates attackerCord = action.getCardAttacker();
        Coordinates attackedCord = action.getCardAttacked();
        Card attackerCard = table.getElement(attackerCord);
        Card attackedCard = table.getElement(attackedCord);

        if (attackerCard == null)
            return null;

        String error = attackerCard.useCardAbility(attackedCard, table, currentPlayer);
        return JsonNode.writeCardUsesAbility(action, error);
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
//    private ObjectNode useAttackHero(ActionsInput action) {
//        Coordinates attackerCord = action.getCardAttacker();
//
//        String error = Errors.useAttackHero(attackerCord, table, currentPlayer);
//        String result = null;
//        if (error == null) {
//            Player player = getInstanceOfWaitingPlayer();
//            CardInput attackerCard = table.getElement(attackerCord);
//            attackerCard.attackCard(player.getHerro());
//            attackerCard.setHasAttacked(true);
//            if (player.getHerro().getHealth() <= 0)
//                result = gameEnded();
//        }
//
//        return JsonNode.writeUseAttackHero(action, error, result);
//    }

//    private ObjectNode useHeroAbility(ActionsInput action) {
//        int affectedRow = action.getAffectedRow();
//        Player player = getInstanceOfCurrentPlayer();
//
//    }

    private ObjectNode performAction(ActionsInput action) {
        String command = action.getCommand();
        return switch (command) {
            case "getPlayerDeck" -> getPlayerDeck(action);
            case "getPlayerHero" -> getPlayerHero(action);
            case "getPlayerTurn" -> getPlayerTurn(action);
            case "endPlayerTurn" -> endPlayerTurn();
            case "placeCard" -> placeCard(action);
            case "getCardsInHand" -> getCardsInHand(action);
            case "getPlayerMana" -> getPlayerMana(action);
            case "getCardsOnTable" -> getCardsOnTable(action);
            case "getCardAtPosition" -> getCardAtPosition(action);
            case "cardUsesAttack" -> cardUsesAttack(action);
            case "cardUsesAbility" -> cardUsesAbility(action);
//            case "useAttackHero" -> useAttackHero(action);
            ///case "useHeroAbility" -> useHeroAbility(action);
            default -> null;
        };

    }

    public void performAllActions(ArrayList<ActionsInput> actions, ArrayNode output) {
        newRound();
        /* make later*/
        for (ActionsInput action : actions) {
            ObjectNode actionOutput = performAction(action);
            if (actionOutput != null)
                output.add(actionOutput);
        }
    }
}
