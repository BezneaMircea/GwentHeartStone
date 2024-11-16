package org.poo.main;


public class Errors {
    public static final String noError = null;


    public static int getOtherPlayerIdx(int playerIdx) {
        if (playerIdx == GamesSetup.playerOneIdx) {
            return GamesSetup.playerTwoIdx;
        }
        return GamesSetup.playerOneIdx;
    }

}
