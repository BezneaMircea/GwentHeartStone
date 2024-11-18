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

    public static final String PLAYER_ONE_WON;
    public static final String PLAYER_TWO_WON;

    static {
        PLAYER_ONE_WON = "Player one killed the enemy hero.";
        PLAYER_TWO_WON = "Player two killed the enemy hero.";
    }

    /**
     * Constructor used to create a single game.
     *
     * @param playerOne      Player one of the game
     * @param playerTwo      Player two of the game
     * @param seed           seed of the current game
     * @param startingPlayer the starting player of the game
     */
    public Game(final Player playerOne, final Player playerTwo, final long seed, final int startingPlayer) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.currentPlayer = startingPlayer;
        this.startingPlayer = startingPlayer;
        table = new GameTable();
        manaGiven = GamesSetup.INITIAL_MANA;

        this.seed = new Random();

        this.seed.setSeed(seed);
        ShuffleDeck(playerOne.getDeck(), this.seed);

        this.seed.setSeed(seed);
        ShuffleDeck(playerTwo.getDeck(), this.seed);

        GamesSetup.totalGamesPlayed++;
    }

    private void ShuffleDeck(final ArrayList<Card> deck, final Random seed) {
        Collections.shuffle(deck, this.seed);
    }

    /**
     * Returns a string regarding the message at the end of a game.
     *
     * @param currentPlayerId the id of the player that won
     * @return a string regarding the player that won the game
     */
    public static String gameEnded(final int currentPlayerId) {
        if (currentPlayerId == GamesSetup.PLAYER_ONE_IDX) {
            return PLAYER_ONE_WON;
        }

        return PLAYER_TWO_WON;
    }

    /**
     * Method used in order to perform all the actions of the current game
     * and add them to the output ArrayNode. null ObjectNodes are not added
     *
     * @param actions all actions of the current game
     * @param output  returned output of the current game
     */
    public void performAllActions(final ArrayList<ActionsInput> actions, final ArrayNode output) {
        newRound();
        for (final ActionsInput action : actions) {
            final ObjectNode actionOutput = performAction(action);
            if (actionOutput != null) {
                output.add(actionOutput);
            }
        }
    }

    private ObjectNode performAction(final ActionsInput action) {
        final String command = action.getCommand();
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


    private Player getInstanceOfCurrentPlayer() {
        if (currentPlayer == GamesSetup.PLAYER_ONE_IDX) {
            return playerOne;
        }

        return playerTwo;
    }

    private Player getInstanceOfWaitingPlayer() {
        if (currentPlayer == GamesSetup.PLAYER_ONE_IDX) {
            return playerTwo;
        }

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
        if (manaGiven < GamesSetup.MAX_MANA_GIVEN) {
            manaGiven++;
        }
    }

    private void reset(final Player player) {
        player.getHero().setHasAttacked(false);
        table.resetAttack(player);
    }

    private ObjectNode endPlayerTurn() {
        if (startingPlayer != currentPlayer) {
            newRound();
        }

        if (currentPlayer == GamesSetup.PLAYER_ONE_IDX) {
            reset(playerOne);
            currentPlayer = GamesSetup.PLAYER_TWO_IDX;
        } else {
            reset(playerTwo);
            currentPlayer = GamesSetup.PLAYER_ONE_IDX;
        }
        return null;
    }


    private ObjectNode getPlayerDeck(final ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.PLAYER_ONE_IDX) {
            return JsonNode.writePlayerDeck(action, playerOne.getDeck());
        } else {
            return JsonNode.writePlayerDeck(action, playerTwo.getDeck());
        }
    }


    private ObjectNode getPlayerHero(final ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.PLAYER_ONE_IDX) {
            return JsonNode.writePlayerHero(action, playerOne.getHero());
        } else {
            return JsonNode.writePlayerHero(action, playerTwo.getHero());
        }
    }


    private ObjectNode getPlayerTurn(final ActionsInput action) {
        return JsonNode.writePlayerTurn(action, currentPlayer);
    }


    private ObjectNode placeCard(final ActionsInput action) {
        final Player player = getInstanceOfCurrentPlayer();
        final String error = player.placeCard(table, action.getHandIdx());

        return JsonNode.writePlaceCard(action, error);
    }


    private ObjectNode getCardsInHand(final ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.PLAYER_ONE_IDX) {
            return JsonNode.writeCardsInHand(action, playerOne.getHand());
        } else {
            return JsonNode.writeCardsInHand(action, playerTwo.getHand());
        }
    }


    private ObjectNode getPlayerMana(final ActionsInput action) {
        if (action.getPlayerIdx() == GamesSetup.PLAYER_ONE_IDX) {
            return JsonNode.writePlayerMana(action, playerOne.getMana());
        } else {
            return JsonNode.writePlayerMana(action, playerTwo.getMana());
        }
    }


    private ObjectNode getCardsOnTable(final ActionsInput action) {
        return JsonNode.writeCardsOnTable(action, table);
    }


    private ObjectNode getCardAtPosition(final ActionsInput action) {
        final Coordinates wantedCardCord = new Coordinates(action.getX(), action.getY());
        final Card wantedCard = table.getElement(wantedCardCord);

        String error = null;
        if (wantedCard == null) {
            error = GameTable.NO_CARD_AT_GIVEN_POS;
        }

        return JsonNode.writeCardAtPosition(action, wantedCard, error);
    }


    private ObjectNode cardUsesAttack(final ActionsInput action) {
        final Coordinates attackerCord = action.getCardAttacker();
        final Coordinates attackedCord = action.getCardAttacked();
        final Card attackerCard = table.getElement(attackerCord);
        final Card attackedCard = table.getElement(attackedCord);

        if (attackerCard == null || attackedCard == null) {
            return null;
        }

        final String error = attackerCard.attackCard(table, currentPlayer, attackedCard);

        return JsonNode.writeCardUsesAttack(action, error);
    }


    private ObjectNode cardUsesAbility(final ActionsInput action) {
        final Coordinates attackerCord = action.getCardAttacker();
        final Coordinates attackedCord = action.getCardAttacked();
        final Card attackerCard = table.getElement(attackerCord);
        final Card attackedCard = table.getElement(attackedCord);

        if (attackerCard == null) {
            return null;
        }

        final String error = attackerCard.useCardAbility(attackedCard, table, currentPlayer);
        return JsonNode.writeCardUsesAbility(action, error);
    }


    private ObjectNode useAttackHero(final ActionsInput action) {
        final Coordinates attackerCord = action.getCardAttacker();
        final Card attackerCard = table.getElement(attackerCord);

        if (attackerCard == null) {
            return null;
        }

        final Player player = getInstanceOfWaitingPlayer();
        final String res = attackerCard.attackCard(table, currentPlayer, player.getHero());

        if (PLAYER_ONE_WON.equals(res)) {
            GamesSetup.playerOneWins++;
        }

        if (PLAYER_TWO_WON.equals(res)) {
            GamesSetup.playerTwoWins++;
        }

        return JsonNode.writeUseAttackHero(action, res);
    }


    private ObjectNode useHeroAbility(final ActionsInput action) {
        final int affectedRow = action.getAffectedRow();
        final Player player = getInstanceOfCurrentPlayer();
        final Card hero = player.getHero();

        final String error = hero.useHeroAbility(table, affectedRow, player);

        if (error == null) {
            player.setMana(player.getMana() - hero.getMana());
        }


        return JsonNode.writeUseHeroAbility(action, error);
    }


    private ObjectNode getFrozenCardsOnTable(final ActionsInput action) {
        return JsonNode.writeFrozenCardsOnTable(action, table);
    }


    private ObjectNode getTotalGamesPlayed(final ActionsInput action) {
        final int gamesPlayed = GamesSetup.totalGamesPlayed;
        return JsonNode.writeTotalGamesPlayed(action, gamesPlayed);
    }

}
