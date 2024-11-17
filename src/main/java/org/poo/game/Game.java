package org.poo.game;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;


import org.poo.cards.Card;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;
import org.poo.utils.JsonNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class that is used in order to handle a single game's actions and
 * some commands regarding statistics about the current game series.
 */
public class Game {
    private final Player playerOne;
    private final Player playerTwo;
    private final Random seed;
    private int manaGiven;
    private int currentPlayer;
    private final int startingPlayer;
    private final GameTable table;

    public static final String playerOneWon;
    public static final String playerTwoWon;

    static {
        playerOneWon = "Player one killed the enemy hero.";
        playerTwoWon = "Player two killed the enemy hero.";
    }

    /**
     * Constructor used to create a single game.
     * @param playerOne Player one of the game
     * @param playerTwo Player two of the game
     * @param seed seed of the current game
     * @param startingPlayer the starting player of the game
     */
    public Game(Player playerOne, Player playerTwo, long seed, int startingPlayer) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.currentPlayer = startingPlayer;
        this.startingPlayer = startingPlayer;
        table = new GameTable();
        manaGiven = GamesSetup.initialMana;

        this.seed = new Random();

        this.seed.setSeed(seed);
        ShuffleDeck(playerOne.getDeck(), this.seed);

        this.seed.setSeed(seed);
        ShuffleDeck(playerTwo.getDeck(), this.seed);

        GamesSetup.totalGamesPlayed++;
    }

    /**
     * Returns a string regarding the message at the end of a game.
     * @param currentPlayerId the id of the player that won
     * @return a string regarding the player that won the game
     */
    public static String gameEnded(int currentPlayerId) {
        if (currentPlayerId == GamesSetup.playerOneIdx)
            return playerOneWon;

        return playerTwoWon;
    }

    /**
     * Method used in order to perform all the actions of the current game
     * @param actions all actions of the current game
     * @param output returned output of the current game
     */
    public void performAllActions(ArrayList<ActionsInput> actions, ArrayNode output) {
        newRound();
        for (ActionsInput action : actions) {
            ObjectNode actionOutput = performAction(action);
            if (actionOutput != null)
                output.add(actionOutput);
        }
    }

    private ObjectNode performAction(ActionsInput action) {
        String command = action.getCommand();
        return switch (command) {
            case "getPlayerDeck" -> getPlayerDeck(action);
            case "getPlayerHero" -> getPlayerHero(action);
            case "getPlayerTurn" -> getPlayerTurn(action);
            case "getCardsInHand" -> getCardsInHand(action);
            case "getPlayerMana" -> getPlayerMana(action);
            case "getCardsOnTable" -> getCardsOnTable(action);
            case "getCardAtPosition" -> getCardAtPosition(action);
            case "getFrozenCardsOnTable" -> getFrozenCardsOnTable(action);
            case "cardUsesAttack" -> cardUsesAttack(action);
            case "cardUsesAbility" -> cardUsesAbility(action);
            case "useAttackHero" -> useAttackHero(action);
            case "useHeroAbility" -> useHeroAbility(action);
            case "placeCard" -> placeCard(action);
            case "endPlayerTurn" -> endPlayerTurn();
            case "getTotalGamesPlayed" -> getTotalGamesPlayed(action);
            case "getPlayerOneWins" -> JsonNode.writePlayerWins(action, GamesSetup.playerOneWins);
            case "getPlayerTwoWins" -> JsonNode.writePlayerWins(action, GamesSetup.playerTwoWins);
            default -> null;
        };

    }


    private void ShuffleDeck(ArrayList<Card> deck, Random seed) {
        Collections.shuffle(deck, this.seed);
    }

    private Player getInstanceOfCurrentPlayer() {
        if (currentPlayer == GamesSetup.playerOneIdx)
            return playerOne;

        return playerTwo;
    }

    private Player getInstanceOfWaitingPlayer() {
        if (currentPlayer == GamesSetup.playerOneIdx)
            return playerTwo;

        return playerOne;
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

    private void reset(Player player) {
        player.getHero().setHasAttacked(false);
        table.resetAttack(player);
    }

    private ObjectNode endPlayerTurn() {
        if (startingPlayer != currentPlayer)
            newRound();

        if (currentPlayer == GamesSetup.playerOneIdx) {
            reset(playerOne);
            currentPlayer = GamesSetup.playerTwoIdx;
        } else {
            reset(playerTwo);
            currentPlayer = GamesSetup.playerOneIdx;
        }
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
            return JsonNode.writePlayerHero(action, playerOne.getHero());
        else
            return JsonNode.writePlayerHero(action, playerTwo.getHero());
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
        if (action.getPlayerIdx() == GamesSetup.playerOneIdx)
            return JsonNode.writeCardsInHand(action, playerOne.getHand());
        else
            return JsonNode.writeCardsInHand(action, playerTwo.getHand());
    }

    private ObjectNode getPlayerMana(ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.playerOneIdx)
            return JsonNode.writePlayerMana(action, playerOne.getMana());
        else
            return JsonNode.writePlayerMana(action, playerTwo.getMana());
    }

    private ObjectNode getCardsOnTable(ActionsInput action) {
        return JsonNode.writeCardsOnTable(action, table);
    }

    private ObjectNode getCardAtPosition(ActionsInput action) {
        Coordinates wantedCardCord = new Coordinates(action.getX(), action.getY());
        Card wantedCard = table.getElement(wantedCardCord);

        String error = null;
        if (wantedCard == null)
            error = GameTable.noCardAtGivenPos;

        return JsonNode.writeCardAtPosition(action, wantedCard, error);
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


    private ObjectNode useAttackHero(ActionsInput action) {
        Coordinates attackerCord = action.getCardAttacker();
        Card attackerCard = table.getElement(attackerCord);

        if (attackerCard == null)
            return null;

        Player player = getInstanceOfWaitingPlayer();
        String res = attackerCard.attackCard(table, currentPlayer, player.getHero());

        if (playerOneWon.equals(res))
            GamesSetup.playerOneWins++;

        if (playerTwoWon.equals(res))
            GamesSetup.playerTwoWins++;

        return JsonNode.writeUseAttackHero(action, res);
    }

    private ObjectNode useHeroAbility(ActionsInput action) {
        int affectedRow = action.getAffectedRow();
        Player player = getInstanceOfCurrentPlayer();
        Card hero = player.getHero();

        String error = hero.useHeroAbility(table, affectedRow, player);

        if (error == null)
            player.setMana(player.getMana() - hero.getMana());


        return JsonNode.writeUseHeroAbility(action, error);
    }

    private ObjectNode getFrozenCardsOnTable(ActionsInput action) {
        return JsonNode.writeFrozenCardsOnTable(action, table);
    }

    private ObjectNode getTotalGamesPlayed(ActionsInput action) {
        int gamesPlayed = GamesSetup.totalGamesPlayed;
        return JsonNode.writeTotalGamesPlayed(action, gamesPlayed);
    }

}
