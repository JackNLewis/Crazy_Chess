package CrazyChess.logic.game;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.King;
import CrazyChess.pieces.Pawn;
import CrazyChess.pieces.Rook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class EndgameTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();

    @Before
    public void init() {
        mainLogic = new MainLogic();
    }

    @Test
    public void WhiteKingCanBeChecked() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new King("White",  0,0,"Normal"), true, testGamestate);
        utils.placePiece(new King("Black",  7,7,"Normal"), true, testGamestate);

        AbstractPiece testRook = new Rook("Black",  6,7,"Normal");
        utils.placePiece(testRook, true, testGamestate);

        mainLogic.changeTurn();
        mainLogic.moveTo(testRook, 0,7);
        assertTrue(mainLogic.getCheckStatus("White"));
    }

    @Test
    public void BlackKingCanBeChecked() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new King("White",  0,0,"Normal"), true, testGamestate);
        utils.placePiece(new King("Black",  7,7,"Normal"), true, testGamestate);

        AbstractPiece testRook = new Rook("White",  1,0,"Normal");
        utils.placePiece(testRook, true, testGamestate);

        mainLogic.moveTo(testRook, 7,0);
        assertTrue(mainLogic.getCheckStatus("Black"));
    }

    @Test
    public void WhiteKingCanBeCheckmated() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new King("White",  0,0,"Normal"), true, testGamestate);
        utils.placePiece(new King("Black",  7,7,"Normal"), true, testGamestate);

        AbstractPiece testRookA = new Rook("Black",  6,7,"Normal");
        AbstractPiece testRookB = new Rook("Black",  1,6,"Normal");
        utils.placePiece(testRookA, true, testGamestate);
        utils.placePiece(testRookB, true, testGamestate);

        mainLogic.changeTurn();
        mainLogic.moveTo(testRookA, 0,7);
        assertTrue(mainLogic.getMateStatus("White"));
    }

    @Test
    public void BlackKingCanBeCheckmated() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new King("White",  0,0,"Normal"), true, testGamestate);
        utils.placePiece(new King("Black",  7,7,"Normal"), true, testGamestate);

        AbstractPiece testRookA = new Rook("White",  1,0,"Normal");
        AbstractPiece testRookB = new Rook("White",  6,1,"Normal");
        utils.placePiece(testRookA, true, testGamestate);
        utils.placePiece(testRookB, true, testGamestate);

        mainLogic.moveTo(testRookA, 7, 0);
        assertTrue(mainLogic.getMateStatus("Black"));
    }

    @Test
    public void Draw_IfNoValidMoves() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        AbstractPiece blackKing = new King("Black",  1,3,"Normal");
        utils.placePiece(blackKing, true, testGamestate);
        utils.placePiece(new King("White",  1,0,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black",  1,1,"Normal"), true, testGamestate);

        mainLogic.changeTurn();
        mainLogic.moveTo(blackKing, 1, 2);
        assertTrue(mainLogic.getDraw());
    }

    @Test
    public void Draw_IfOnlyKingsLeftOnTheBoard() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        AbstractPiece blackKing = new King("Black",  1,7,"Normal");
        utils.placePiece(blackKing, true, testGamestate);
        utils.placePiece(new King("White",  1,0,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("White",  1,6,"Normal"), true, testGamestate);

        mainLogic.changeTurn();
        mainLogic.moveTo(blackKing, 1, 6);
        assertTrue(mainLogic.getDraw());
    }

}
