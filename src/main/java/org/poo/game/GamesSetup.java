package org.poo.game;


import java.util.ArrayList;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.*;


/**
 * Class that contains general attributes about a game/a series of games.
 * It handles the given input of a series of games and for each of the
 * games it creates a new Game class instance and starts the game
 *
 * @see Game
 */
public final class GamesSetup {
    public static final int PLAYER_ONE_IDX = 1;
    public static final int PLAYER_TWO_IDX = 2;
    public static final int INITIAL_MANA = 1;
    public static final int MAX_MANA_GIVEN = 10;
    private static int totalGamesPlayed;
    private static int playerOneWins;
    private  static int playerTwoWins;

    private final DecksInput playerOneDecks;
    private final DecksInput playerTwoDecks;
    private final ArrayList<GameInput> games;
    private final ArrayNode output;

    /**
     * Constructor used to set up the generalities about the current
     * series of games.
     *
     * @param input  The entire input of the series of games
     * @param output ArrayNode that will help us return the output after
     *               performing all the actions withing the games
     */
    public GamesSetup(final Input input, final ArrayNode output) {
        playerOneDecks = input.getPlayerOneDecks();
        playerTwoDecks = input.getPlayerTwoDecks();
        games = input.getGames();
        this.output = output;
        totalGamesPlayed = 0;
        playerOneWins = 0;
        playerTwoWins = 0;
    }

    /**
     * Helper method
     *
     * @return the other player's index
     */
    public static int getOtherPlayerIdx(final int playerIdx) {
        if (playerIdx == PLAYER_ONE_IDX) {
            return PLAYER_TWO_IDX;
        }
        return PLAYER_ONE_IDX;
    }

    /**
     * Method used in order to play all the games
     */
    public void playTheGames() {
        for (final GameInput gameInput : games) {
            final Game newGame = currentGameSetup(gameInput);
            playTheGame(newGame, gameInput.getActions());
        }
    }

    private void playTheGame(final Game game, final ArrayList<ActionsInput> actions) {
        game.performAllActions(actions, this.output);
    }


    private long getSeed(final StartGameInput startGameInput) {
        return startGameInput.getShuffleSeed();
    }

    private int getPlayerDeckIndex(final int currentPlayer, final StartGameInput startGameInput) {
        if (currentPlayer == PLAYER_ONE_IDX) {
            return startGameInput.getPlayerOneDeckIdx();
        } else {
            return startGameInput.getPlayerTwoDeckIdx();
        }
    }

    private ArrayList<CardInput> getPlayerDeck(final int currentPlayer, final int playerDeckIndex) {
        if (currentPlayer == PLAYER_ONE_IDX) {
            return playerOneDecks.getDecks().get(playerDeckIndex);
        } else {
            return playerTwoDecks.getDecks().get(playerDeckIndex);
        }
    }

    private CardInput getPlayerHero(final int currentPlayer, final StartGameInput startGameInput) {
        if (currentPlayer == PLAYER_ONE_IDX) {
            return startGameInput.getPlayerOneHero();
        } else {
            return startGameInput.getPlayerTwoHero();
        }
    }

    private Game currentGameSetup(final GameInput gameInput) {
        final StartGameInput startGameInput = gameInput.getStartGame();

        int playerOneDeckIndex = getPlayerDeckIndex(PLAYER_ONE_IDX, startGameInput);
        final int playerTwoDeckIndex = getPlayerDeckIndex(PLAYER_TWO_IDX, startGameInput);
        final ArrayList<CardInput> playerOneDeck = getPlayerDeck(PLAYER_ONE_IDX,
                                                                 playerOneDeckIndex);
        final ArrayList<CardInput> playerTwoDeck = getPlayerDeck(PLAYER_TWO_IDX,
                                                                 playerTwoDeckIndex);

        final CardInput playerOneHero = getPlayerHero(PLAYER_ONE_IDX, startGameInput);
        final CardInput playerTwoHero = getPlayerHero(PLAYER_TWO_IDX, startGameInput);

        final Player playerOne = new Player(playerOneHero, playerOneDeck, PLAYER_ONE_IDX);
        final Player playerTwo = new Player(playerTwoHero, playerTwoDeck, PLAYER_TWO_IDX);

        final long seed = getSeed(startGameInput);

        final int startingPlayer = startGameInput.getStartingPlayer();

        return new Game(playerOne, playerTwo, seed, startingPlayer);
    }


    public static int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public static void setTotalGamesPlayed(final int totalGamesPlayed) {
        GamesSetup.totalGamesPlayed = totalGamesPlayed;
    }

    public static int getPlayerOneWins() {
        return playerOneWins;
    }

    public static void setPlayerOneWins(final int playerOneWins) {
        GamesSetup.playerOneWins = playerOneWins;
    }

    public static int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public static void setPlayerTwoWins(final int playerTwoWins) {
        GamesSetup.playerTwoWins = playerTwoWins;
    }
}
