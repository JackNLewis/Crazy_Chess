package CrazyChess.logic.moves;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ConstrainedMovesTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void PieceCantMoveOrCapture_IfOwnKingChecked() {
        AbstractPiece[][] testGamestate = setKingAndRook();

        AbstractPiece whiteRook = mainLogic.getPiece(new Position(0, 0));
        AbstractPiece blackRook = mainLogic.getPiece(new Position(0, 7));

        utils.placePiece(new Queen("White", 3, 6,"Normal"), true, testGamestate);
        utils.placePiece(new Queen("Black", 3, 1,"Normal"), true, testGamestate);

        assertFalse(mainLogic.moveTo(whiteRook, 0, 1));
        assertFalse(mainLogic.moveTo(blackRook, 0, 6));
    }

    @Test
    public void KingMoves_Castle() {
        setKingAndRook();

        AbstractPiece whiteKing = mainLogic.getPiece(new Position(4, 0));
        AbstractPiece blackKing = mainLogic.getPiece(new Position(4, 7));

        assertTrue(mainLogic.moveTo(whiteKing, 2, 0));
        assertTrue(mainLogic.moveTo(blackKing, 2, 7));
    }

    @Test
    public void KingCantCastle_IfOwnKingMoved() {
        setKingAndRook();

        King whiteKing = (King) mainLogic.getPiece(new Position(4, 0));
        King blackKing = (King) mainLogic.getPiece(new Position(4, 7));

        whiteKing.setWasMoved(true);
        blackKing.setWasMoved(true);

        assertFalse(mainLogic.moveTo(whiteKing, 2, 0));
        assertFalse(mainLogic.moveTo(blackKing, 2, 7));
    }

    @Test
    public void KingCantCastle_IfOwnKingCheckedOrPathChecked() {
        AbstractPiece[][] testGamestate = setKingAndRook();

        AbstractPiece whiteKing = mainLogic.getPiece(new Position(4, 0));
        AbstractPiece blackKing = mainLogic.getPiece(new Position(4, 7));

        utils.placePiece(new Queen("White", 3, 6,"Normal"), true, testGamestate);
        utils.placePiece(new Queen("Black", 3, 1,"Normal"), true, testGamestate);

        assertFalse(mainLogic.moveTo(whiteKing, 2, 0));
        assertFalse(mainLogic.moveTo(blackKing, 2, 7));
    }

    public AbstractPiece[][] setKingAndRook() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        for (int i = 1; i < 7; i++) {
            if (i == 4) continue;
            Position pos = new Position(i, 0);
            AbstractPiece p = mainLogic.getPiece(pos);
            utils.placePiece(new BlankPiece("Blank", i, 0,"Normal"), true, testGamestate);

            pos = new Position(i, 7);
            p = mainLogic.getPiece(pos);
            utils.placePiece(new BlankPiece("Blank", i, 7,"Normal"), true, testGamestate);
        }
        return testGamestate;
    }

}
