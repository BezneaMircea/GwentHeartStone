package org.poo.main;


import org.poo.fileio.Coordinates;
import org.poo.cards.Card;

public class Errors {
    public static final String noError = null;
    public static final String isFrozen = "Attacker card is frozen.";
    public static final String cardAlreadyAttacked = "Attacker card has already attacked this turn.";
    public static final String heroAlreadyAttacked = "Hero has already attacked this turn.";
    public static final String attackedDontBelongCur = "Attacked card does not belong to the current player.";
    public static final String attackedDontBelongEnnemy = "Attacked card does not belong to the enemy.";
    public static final String attackerDontBelongCur = "Attacker card does not belong to the current player.";
    public static final String notTank = "Attacked card is not of type 'Tank'.";
    public static final String notEnoughManaCard = "Not enough mana to place card on table.";
    public static final String notEnoughManaHero = "Not enough mana to use hero's ability.";
    public static final String tableIsFull = "Cannot place card on table since row is full.";
    public static final String noCardAtGivenPos = "No card available at that position.";
    public static final String rowNotEnemy = "Selected row does not belong to the enemy.";
    public static final String rowNotCurPlayer = "Selected row does not belong to the current player.";


    public static String
    placeCardError(Card card, GameTable table, int currentMana) {
        if (card.getMana() > currentMana)
            return notEnoughManaCard;

        int rowToPlace = card.getRowToPlace();
        if (!table.canAddOnRow(rowToPlace))
            return tableIsFull;

        return noError;
    }

    public static String noCardAtPositionError(Coordinates coords, GameTable table) {
        Card wantedCard = table.getElement(coords);
        if (wantedCard == null)
            return noCardAtGivenPos;

        return noError;
    }


    public static int getOtherPlayerIdx(int playerIdx) {
        if (playerIdx == GamesSetup.playerOneIdx) {
            return GamesSetup.playerTwoIdx;
        }
        return GamesSetup.playerOneIdx;
    }



//    public static String useAttackHero(Coordinates attackerCord, GameTable table, int currentPlayer) {
//        CardInput attackerCard = table.getElement(attackerCord);
//
//        if (attackerCard == null)
//            return noCardAtGivenPos;
//
//        if (attackerCard.isFrozen())
//            return isFrozen;
//
//        if (attackerCard.getHasAttacked())
//            return cardAlreadyAttacked;
//
//        if (table.doesPlayerHaveTanks(getOtherPlayerIdx(currentPlayer)))
//            return notTank;
//
//        return noError;
//    }
//
//
//    private static boolean
//    affectedRowBelongsPlayer(int affectedRow, Player player) {
//        int playerId = player.getPlayerId();
//
//        if (playerId == 1) {
//            return affectedRow == GameTable.playerOneFrontRow
//                    || affectedRow == GameTable.playerOneBackRow;
//        } else {
//            return affectedRow == GameTable.playerTwoFrontRow
//                    || affectedRow == GameTable.playerTwoBackRow;
//        }
//    }
//
//    public static String
//    useHeroAbilityError(Player player, GameTable table, int affectedRow) {
//        CardInput hero = player.getHerro();
//
//        if (hero.getMana() > player.getMana())
//            return notEnoughManaHero;
//
//        if (hero.getHasAttacked())
//            return heroAlreadyAttacked;
//
//        if (hero.isLordRoyce() || hero.isEmpressThorina())
//            if (affectedRowBelongsPlayer(affectedRow, player))
//                return rowNotEnemy;
//
//        if (hero.isKingMudface() || hero.isGeneralKocioraw())
//            if (!affectedRowBelongsPlayer(affectedRow, player))
//                return rowNotCurPlayer;
//
//        return noError;
//    }
}
