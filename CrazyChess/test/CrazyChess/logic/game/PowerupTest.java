package CrazyChess.logic.game;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PowerupTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private ExtraChecksAndTools ecat = new ExtraChecksAndTools();
    private PowerupMain powerupMain = new PowerupMain();

    @Before
    public void init() {
        mainLogic = new MainLogic();
    }

    @Test
    public void PowerupSpawnsAfterFiveTurns() {
        mainLogic.resetBoard();
        for (int i = 0; i < 6; i++) {
            mainLogic.changeTurn();
        }
        boolean powerupExists = false;
        for (AbstractPiece p: ecat.gamestateToPieceArrayList(mainLogic.getGamestate())) {
            if (p instanceof Powerup) powerupExists = true;
        }

        assertTrue(powerupExists);
    }

    @Test
    public void DummyPieceCreateImmovablePiece() {
        mainLogic.resetBoard();
        mainLogic.getPowerUps("White").add("dummypiece");
        boolean useDummy = mainLogic.usePowerup(0, new Position(0, 1), new Position(0, 2));
        assertTrue(useDummy);

        AbstractPiece dummyPiece = mainLogic.getPiece(new Position(0, 2));
        assertFalse(mainLogic.moveTo(dummyPiece, 0, 3));
    }

    @Test
    public void MiniPromoteChangePiece() {
        mainLogic.resetBoard();
        mainLogic.getPowerUps("White").add("minipromote");
        boolean useMiniPromote = mainLogic.usePowerup(0, new Position(0, 1), new Position(2, 0));
        assertTrue(useMiniPromote);

        AbstractPiece promotedPiece = mainLogic.getPiece(new Position(0, 1));
        assertTrue(promotedPiece instanceof Bishop);

        // cannot use minipromote to other piece except knight and bishop
        mainLogic.getPowerUps("White").add("minipromote");
        boolean miniPromoteToQueen = mainLogic.usePowerup(0, new Position(0, 1), new Position(3, 0));
        assertFalse(miniPromoteToQueen);
    }

    @Test
    public void TeleportSwapPiecesPosition() {
        mainLogic.resetBoard();
        mainLogic.getPowerUps("White").add("teleport");
        boolean useTeleport = mainLogic.usePowerup(0, new Position(0, 1), new Position(3, 0));
        assertTrue(useTeleport);

        AbstractPiece swappedPieceA = mainLogic.getPiece(new Position(0, 1));
        AbstractPiece swappedPieceB = mainLogic.getPiece(new Position(3, 0));
        assertTrue(swappedPieceA instanceof Queen);
        assertTrue(swappedPieceB instanceof Pawn);

        // cannot use teleport with king
        mainLogic.getPowerUps("White").add("teleport");
        boolean useTeleportWithKing = mainLogic.usePowerup(0, new Position(3, 0), new Position(4, 0));
        assertFalse(useTeleportWithKing);
    }

    @Test
    public void FreeCardCanMoveKingWhenChecked() {
        AbstractPiece[][] testGameState = mainLogic.getGamestate();
        AbstractPiece whiteKing = new King("White",  4,0,"Normal");
        AbstractPiece blackKing = new King("Black",  7,7,"Normal");
        utils.placePiece(whiteKing, true, testGameState);
        utils.placePiece(blackKing, true, testGameState);
        utils.placePiece(new Rook("White", 0, 1, "Normal"), true, testGameState);
        utils.placePiece(new Rook("Black", 7, 2, "Normal"), true, testGameState);

        mainLogic.changeTurn();
        mainLogic.moveTo(mainLogic.getPiece(new Position(7, 2)), 7, 0);
        mainLogic.changeTurn();

        mainLogic.getPowerUps("White").add("freecard");
        assertTrue(mainLogic.getCheckStatus("white"));

        boolean useFreecard = mainLogic.usePowerup(0, whiteKing.getPosition(), new Position(4, 2));
        assertTrue(useFreecard);
        assertFalse(mainLogic.getMateStatus("white"));
    }

    @Test
    public void BombClearSorroundingPieces() {
        mainLogic.resetBoard();
        mainLogic.getPowerUps("White").add("bomb");
        boolean useBomb = mainLogic.usePowerup(0, new Position(1, 1), new Position(0, 0));
        powerupMain.doBomb(
                mainLogic.getGamestate(),
                mainLogic.getPiece(new Position(1, 1)),
                new BlankPiece("Blank", 0, 2,"Normal"),
                utils,
                true
        );
        assertTrue(useBomb);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertTrue(mainLogic.getPiece(new Position(i, j)) instanceof BlankPiece);
            }
        }
    }

    @Test
    public void RandomPowerupReturnsRandomPowerupString() {
        String powerUps = "TeleportFreeCardDummyPieceBombMiniPromote";
        assertTrue(powerUps.contains(powerupMain.randomPowerup(true)));
    }

    @Test
    public void ValidPowerupMovesReturnsPositions() {
        mainLogic.resetBoard();
        AbstractPiece[][] testGameState = mainLogic.getGamestate();
        ArrayList<String> powerupList = new ArrayList<>(Arrays.asList("teleport", "minipromote", "bomb", "dummypiece"));
        for (String s: powerupList) {
            ArrayList<Position> validPositions = powerupMain.validPowerupMoves(
                    s,
                    testGameState,
                    new Position(0, 1),
                    true
            );
            assertTrue(validPositions instanceof ArrayList);
            assertFalse(validPositions.isEmpty());
            validPositions = powerupMain.validPowerupMoves(
                    s,
                    testGameState,
                    new Position(0, 6),
                    true
            );
            assertTrue(validPositions instanceof ArrayList);
            assertFalse(validPositions.isEmpty());
        }
    }

    @Test
    public void InitialPowerupMovesReturnsValidInitialPositions() {
        mainLogic.resetBoard();
        AbstractPiece[][] testGameState = mainLogic.getGamestate();
        ArrayList<String> powerupList = new ArrayList<>(Arrays.asList("teleport", "minipromote", "bomb", "dummypiece"));
        for (String s: powerupList) {
            ArrayList<Position> validPositions = powerupMain.initialPowerupMoves(s, testGameState, "white");
            assertTrue(validPositions instanceof ArrayList);
            assertFalse(validPositions.isEmpty());
            validPositions = powerupMain.initialPowerupMoves(s, testGameState,"black");
            assertTrue(validPositions instanceof ArrayList);
            assertFalse(validPositions.isEmpty());
        }
    }
}