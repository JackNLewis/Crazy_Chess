package CrazyChess.logic.game;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(JUnit4.class)
public class RulechangesTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();

    @Before
    public void init() {
        mainLogic = new MainLogic();
    }

    @Test
    public void RuleChangeOne_SwapBishopRook() {
        setKing();
        AbstractPiece[][] testGameState = mainLogic.getGamestate();

        AbstractPiece whiteRook = new Rook("White", 7, 1, "Normal");
        AbstractPiece whiteBishop = new Bishop("White", 6, 0,"Normal");
        AbstractPiece blackRook = new Rook("Black", 7, 6, "Normal");
        AbstractPiece blackBishop = new Bishop("Black", 6, 7,"Normal");
        utils.placePiece(whiteRook, true, testGameState);
        utils.placePiece(whiteBishop, true, testGameState);
        utils.placePiece(blackRook, true, testGameState);
        utils.placePiece(blackBishop, true, testGameState);

        mainLogic.setRC1(true);
        boolean whiteTurn = true;
        while (!mainLogic.getEcat().getBrs()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertTrue(mainLogic.moveTo(whiteRook, 6, 2));
            assertTrue(mainLogic.moveTo(blackBishop, 5, 7));
        } else {
            assertTrue(mainLogic.moveTo(blackRook, 6, 5));
            assertTrue(mainLogic.moveTo(whiteBishop, 5, 0));
        }

        while (mainLogic.getEcat().getBrs()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertFalse(mainLogic.moveTo(whiteRook, 7, 1));
        } else {
            assertFalse(mainLogic.moveTo(blackRook, 7, 6));
        }
    }

    @Test
    public void RuleChangeTwo_PawnGoBackward() {
        setKing();
        AbstractPiece[][] testGameState = mainLogic.getGamestate();

        AbstractPiece whitePawn = new Rook("White", 7, 2, "Normal");
        AbstractPiece blackPawn = new Rook("Black", 7, 5, "Normal");
        utils.placePiece(whitePawn, true, testGameState);
        utils.placePiece(whitePawn, true, testGameState);

        mainLogic.setRC2(true);
        boolean whiteTurn = true;
        while (!mainLogic.getEcat().getPS()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertTrue(mainLogic.moveTo(whitePawn, 7, 1));
            assertTrue(mainLogic.moveTo(blackPawn, 7, 6));
        } else {
            assertTrue(mainLogic.moveTo(blackPawn, 7, 6));
            assertTrue(mainLogic.moveTo(whitePawn, 7, 1));
        }

        while (mainLogic.getEcat().getPS()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertFalse(mainLogic.moveTo(whitePawn, 7, 0));
        } else {
            assertFalse(mainLogic.moveTo(blackPawn, 7, 7));
        }
    }

    @Test
    public void RuleChangeThree_KingMovesLikeQueen() {
        AbstractPiece[][] testGameState = mainLogic.getGamestate();
        AbstractPiece whiteKing = new King("White",  1,0,"Normal");
        AbstractPiece blackKing = new King("Black",  6,7,"Normal");
        utils.placePiece(whiteKing, true, testGameState);
        utils.placePiece(blackKing, true, testGameState);
        utils.placePiece(new Pawn("White", 1, 1, "Normal"), true, testGameState);
        utils.placePiece(new Pawn("Black", 6, 6, "Normal"), true, testGameState);

        mainLogic.setRC3(true);
        boolean whiteTurn = true;
        while (!mainLogic.getEcat().getKS()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertTrue(mainLogic.moveTo(whiteKing, 3, 0));
            assertTrue(mainLogic.moveTo(blackKing, 4, 7));
        } else {
            assertTrue(mainLogic.moveTo(blackKing, 4, 7));
            assertTrue(mainLogic.moveTo(whiteKing, 3, 0));
        }

        while (mainLogic.getEcat().getKS()) {
            mainLogic.changeTurn();
            whiteTurn = whiteTurn ^ true;
        }

        if (whiteTurn) {
            assertFalse(mainLogic.moveTo(whiteKing, 1, 0));
        } else {
            assertFalse(mainLogic.moveTo(blackKing, 6, 7));
        }
    }


    public void setKing() {
        utils.placePiece(new King("White",  0,0,"Normal"), true, mainLogic.getGamestate());
        utils.placePiece(new King("Black",  0,7,"Normal"), true, mainLogic.getGamestate());
    }
}