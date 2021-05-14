package CrazyChess.logic.game;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class StageHazardTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private ExtraChecksAndTools ecat = new ExtraChecksAndTools();

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void StageHazardSpawnsAfterFourTurns() {
        for (int i = 0; i < 5; i++) {
            mainLogic.changeTurn();
        }
        HazardPiece pieceToCheck = null;
        boolean hazardPieceExists = false;
        for (AbstractPiece p: ecat.gamestateToPieceArrayList(mainLogic.getGamestate())) {
            if (p instanceof HazardPiece) {
                hazardPieceExists = true;
                pieceToCheck = (HazardPiece) p;
            }
        }

        assertTrue(hazardPieceExists);
        System.out.println(pieceToCheck);
        System.out.println("HZ at " + pieceToCheck.getPosition());
        assertTrue(("HZ at " + pieceToCheck.getPosition()).equalsIgnoreCase(pieceToCheck.toString()));
        assertTrue(Hazard.getValue(pieceToCheck.getHazard()) != -1);
    }

    @Test
    public void StageHazardDespawnsAfter7Turns() {
        for (int i = 0; i < 8; i++) {
            mainLogic.changeTurn();
        }
        boolean hazardPieceExists = false;
        for (AbstractPiece p: ecat.gamestateToPieceArrayList(mainLogic.getGamestate())) {
            if (p instanceof HazardPiece) {
                hazardPieceExists = true;
            }
        }

        assertTrue(!hazardPieceExists);
    }

    @Test
    public void CantMovePieceWithHazard() {
        for (int i = 0; i < 5; i++) {
            mainLogic.changeTurn();
        }
        HazardPiece pieceToCheck = null;
        for (AbstractPiece p: ecat.gamestateToPieceArrayList(mainLogic.getGamestate())) {
            if (p instanceof HazardPiece) {
                pieceToCheck = (HazardPiece) p;
            }
        }

        assertTrue(ecat.validMoves(pieceToCheck, true, mainLogic.getGamestate(), 1).size() == 0);
    }
}
