package CrazyChess.logic.moves;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CaptureMovesTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private AbstractPiece whiteKing;
    private AbstractPiece blackKing;

    @Before
    public void init() {
        mainLogic = new MainLogic();
        whiteKing = new King("White",  0, 0, "Normal");
        blackKing = new King("Black",  0, 7, "Normal");
        utils.placePiece(whiteKing, true, mainLogic.getGamestate());
        utils.placePiece(blackKing, true, mainLogic.getGamestate());
    }

    @Test
    public void PawnCapture_Diagonal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Pawn whitePawnA = new Pawn("White", 0, 3,"Normal");
        Pawn whitePawnB = new Pawn("White", 1, 3,"Normal");
        utils.placePiece(whitePawnA, true, testGamestate);
        utils.placePiece(whitePawnB, true, testGamestate);

        Pawn blackPawnA = new Pawn("Black", 0, 4,"Normal");
        Pawn blackPawnB = new Pawn("Black", 1, 4,"Normal");
        utils.placePiece(blackPawnA, true, testGamestate);
        utils.placePiece(blackPawnB, true, testGamestate);

        assertTrue(mainLogic.moveTo(whitePawnA, blackPawnB.getXpos(), blackPawnA.getYpos()));
        assertTrue(mainLogic.moveTo(blackPawnA, whitePawnB.getXpos(), whitePawnA.getYpos()));
    }

    @Test
    public void PawnCapture_EnPassant() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Pawn whitePawnA = new Pawn("White", 1, 4,"Normal");
        whitePawnA.setEnPassant(true);
        Pawn whitePawnB = new Pawn("White", 7, 3,"Normal");
        utils.placePiece(whitePawnA, true, testGamestate);
        utils.placePiece(whitePawnB, true, testGamestate);

        Pawn blackPawnA = new Pawn("Black", 0, 4,"Normal");
        Pawn blackPawnB = new Pawn("Black", 6, 3,"Normal");
        blackPawnB.setEnPassant(true);
        utils.placePiece(blackPawnA, true, testGamestate);
        utils.placePiece(blackPawnB, true, testGamestate);
        mainLogic.printGameState();

        assertTrue(mainLogic.moveTo(whitePawnA, 0, 5));
        assertTrue(mainLogic.moveTo(blackPawnB, 7, 2));
    }

    @Test
    public void BishopCapture_Diagonal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Bishop whiteBishop = new Bishop("White", 7, 3,"Normal");
        Bishop blackBishop = new Bishop("Black", 7, 4,"Normal");
        utils.placePiece(whiteBishop, true, testGamestate);
        utils.placePiece(blackBishop, true, testGamestate);

        utils.placePiece(new Pawn("White", 3, 0,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 3, 7,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteBishop, 3, 7));
        assertTrue(mainLogic.moveTo(blackBishop, 3, 0));
    }

    @Test
    public void KnightCapture_LetterLAndJumpOver() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Knight whiteKnight = new Knight("White", 7, 3,"Normal");
        Knight blackKnight = new Knight("Black", 7, 4,"Normal");
        utils.placePiece(whiteKnight, true, testGamestate);
        utils.placePiece(blackKnight, true, testGamestate);

        utils.placePiece(new Pawn("White", 6, 2,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 6, 5,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteKnight, 6, 5));
        assertTrue(mainLogic.moveTo(blackKnight, 6, 2));
    }

    @Test
    public void RookCapture_Horizontal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Rook whiteRook = new Rook("White", 7, 3,"Normal");
        Rook blackRook = new Rook("Black", 7, 4,"Normal");
        utils.placePiece(whiteRook, true, testGamestate);
        utils.placePiece(blackRook, true, testGamestate);

        utils.placePiece(new Pawn("White", 6, 4,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 6, 3,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteRook, 6, 3));
        assertTrue(mainLogic.moveTo(blackRook, 6, 4));
    }

    @Test
    public void RookCapture_Vertical() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Rook whiteRook = new Rook("White", 7, 3,"Normal");
        Rook blackRook = new Rook("Black", 7, 4,"Normal");
        utils.placePiece(whiteRook, true, testGamestate);
        utils.placePiece(blackRook, true, testGamestate);

        utils.placePiece(new Pawn("White", 7, 5,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 7, 2,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteRook, 7, 2));
        assertTrue(mainLogic.moveTo(blackRook, 7, 5));
    }

    @Test
    public void QueenCapture_Horizontal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Queen whiteQueen = new Queen("White", 7, 3,"Normal");
        Queen blackQueen = new Queen("Black", 7, 4,"Normal");
        utils.placePiece(whiteQueen, true, testGamestate);
        utils.placePiece(blackQueen, true, testGamestate);

        utils.placePiece(new Pawn("White", 6, 4,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 6, 3,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteQueen, 6, 3));
        assertTrue(mainLogic.moveTo(blackQueen, 6, 4));
    }

    @Test
    public void QueenCapture_Vertical() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Queen whiteQueen = new Queen("White", 7, 3,"Normal");
        Queen blackQueen = new Queen("Black", 7, 4,"Normal");
        utils.placePiece(whiteQueen, true, testGamestate);
        utils.placePiece(blackQueen, true, testGamestate);

        utils.placePiece(new Pawn("White", 7, 5,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 7, 2,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteQueen, 7, 2));
        assertTrue(mainLogic.moveTo(blackQueen, 7, 5));
    }

    @Test
    public void QueenCapture_Diagonal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        Queen whiteQueen = new Queen("White", 7, 3,"Normal");
        Queen blackQueen = new Queen("Black", 7, 4,"Normal");
        utils.placePiece(whiteQueen, true, testGamestate);
        utils.placePiece(blackQueen, true, testGamestate);

        utils.placePiece(new Pawn("White", 6, 5,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 6, 2,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteQueen, 6, 2));
        assertTrue(mainLogic.moveTo(blackQueen, 6, 5));
    }

    @Test
    public void KingCapture_Horizontal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new Pawn("White", 1, 7,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 1, 0,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteKing, 1, 0));
        assertTrue(mainLogic.moveTo(blackKing, 1, 7));
    }

    @Test
    public void KingCapture_Vertical() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new Pawn("White", 0, 6,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 0, 1,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteKing, 0, 1));
        assertTrue(mainLogic.moveTo(blackKing, 0, 6));
    }

    @Test
    public void KingCapture_Diagonal() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        utils.placePiece(new Pawn("White", 1, 6,"Normal"), true, testGamestate);
        utils.placePiece(new Pawn("Black", 1, 1,"Normal"), true, testGamestate);

        assertTrue(mainLogic.moveTo(whiteKing, 1, 1));
        assertTrue(mainLogic.moveTo(blackKing, 1, 6));
    }
}
