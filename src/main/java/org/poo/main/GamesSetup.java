package org.poo.main;


import java.util.ArrayList;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.*;

public class GamesSetup {
    public static final int playerOneIdx = 1;
    public static final int playerTwoIdx = 2;
    public static final int initialMana = 1;
    public static final int maxManaGiven = 10;
    private final DecksInput playerOneDecks;
    private final DecksInput playerTwoDecks;
    private final ArrayList<GameInput> games;
    private final ArrayNode output;

    public GamesSetup(Input input, ArrayNode output) {
        playerOneDecks = input.getPlayerOneDecks();
        playerTwoDecks = input.getPlayerTwoDecks();
        games = input.getGames();
        this.output = output;
    }

    private long getSeed(StartGameInput startGameInput) {
        return startGameInput.getShuffleSeed();
    }

    private int getPlayerDeckIndex(int currentPlayer, StartGameInput startGameInput) {
        if (currentPlayer == playerOneIdx)
            return startGameInput.getPlayerOneDeckIdx();
        else
            return startGameInput.getPlayerTwoDeckIdx();
    }

    private ArrayList<CardInput> getPlayerDeck(int currentPlayer, int playerDeckIndex) {
        if (currentPlayer == playerOneIdx)
            return playerOneDecks.getDecks().get(playerDeckIndex);
        else
            return playerTwoDecks.getDecks().get(playerDeckIndex);
    }

    private CardInput getPlayerHero(int currentPlayer, StartGameInput startGameInput) {
        if (currentPlayer == playerOneIdx)
            return startGameInput.getPlayerOneHero();
        else
            return startGameInput.getPlayerTwoHero();
    }

    private Game currentGameSetup(GameInput gameInput) {
        StartGameInput startGameInput = gameInput.getStartGame();

        int playerOneDeckIndex = getPlayerDeckIndex(playerOneIdx, startGameInput);
        int playerTwoDeckIndex = getPlayerDeckIndex(playerTwoIdx, startGameInput);
        ArrayList<CardInput> playerOneDeck = getPlayerDeck(playerOneIdx , playerOneDeckIndex);
        ArrayList<CardInput> playerTwoDeck = getPlayerDeck(playerTwoIdx , playerTwoDeckIndex);

        CardInput playerOneHero = getPlayerHero(playerOneIdx, startGameInput);
        CardInput playerTwoHero = getPlayerHero(playerTwoIdx, startGameInput);

        Player playerOne = new Player(playerOneHero, playerOneDeck, playerOneIdx);
        Player playerTwo = new Player(playerTwoHero, playerTwoDeck, playerTwoIdx);

        long seed = getSeed(startGameInput);

        int startingPlayer = startGameInput.getStartingPlayer();
        return new Game(playerOne, playerTwo, seed, startingPlayer);
    }

    private void playTheGame(Game game, ArrayList<ActionsInput> actions) {
        game.performAllActions(actions, this.output);
    }

    public void playTheGames() {
        for (GameInput gameInput : games) {
            Game newGame = currentGameSetup(gameInput);
            playTheGame(newGame, gameInput.getActions());
        }
    }

    public ArrayNode getOutput() {
        return output;
    }

}
