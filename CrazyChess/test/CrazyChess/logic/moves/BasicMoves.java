package CrazyChess.logic.moves;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BasicMoves {

    private AbstractPiece[][] gamestate;
    private Utilities utils = new Utilities();
    private ExtraChecksAndTools ecat = new ExtraChecksAndTools();

    @Before
    public void init() {
        gamestate = new AbstractPiece[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                gamestate[i][j] = new BlankPiece("Blank", i, j,"Normal");
            }
        }
        utils.placePiece(new King("White",  0,0,"Normal"), true, gamestate);
        utils.placePiece(new King("Black",  0,7,"Normal"), true, gamestate);
    }

    @Test
    public void PawnFirstMoves_OneOrTwoStepForward() {
        Pawn whitePawn = new Pawn("White", 0, 1,"Normal");
        utils.placePiece(whitePawn, true, gamestate);

        Pawn blackPawn = new Pawn("Black", 0, 6,"Normal");
        utils.placePiece(blackPawn, true, gamestate);

        assertEquals(2, ecat.validMoves(whitePawn, true, gamestate, 0).size());
        assertEquals(2, ecat.validMoves(blackPawn, true, gamestate, 0).size());
    }

    @Test
    public void PawnSubsequentMoves_OneStepForward() {
        Pawn whitePawn = new Pawn("White", 0, 2,"Normal");
        whitePawn.setDoublejump(1);
        utils.placePiece(whitePawn, true, gamestate);

        Pawn blackPawn = new Pawn("Black", 0, 5,"Normal");
        blackPawn.setDoublejump(1);
        utils.placePiece(blackPawn, true, gamestate);

        assertEquals(1, ecat.validMoves(whitePawn, true, gamestate, 0).size());
        assertEquals(1, ecat.validMoves(blackPawn, true, gamestate, 0).size());
    }

    @Test
    public void BishopMoves_Diagonal() {
        Bishop whiteBishop = new Bishop("White", 5, 3,"Normal");
        utils.placePiece(whiteBishop, true, gamestate);

        Bishop blackBishop = new Bishop("Black", 5, 4,"Normal");
        utils.placePiece(blackBishop, true, gamestate);

        assertEquals(11, ecat.validMoves(whiteBishop, true, gamestate, 0).size());
        assertEquals(11, ecat.validMoves(blackBishop, true, gamestate, 0).size());
    }

    @Test
    public void KnightMoves_LetterLAndJumpOver() {
        for (int i = 4; i < 7; i++) {
            for (int j = 2; j < 4; j++) {
                utils.placePiece(new Pawn("White", i, j,"Normal"), true, gamestate);
            }
            for (int j = 4; j < 6; j++) {
                utils.placePiece(new Pawn("Black", i, j,"Normal"), true, gamestate);
            }
        }

        Knight whiteKnight = new Knight("White", 5, 3,"Normal");
        utils.placePiece(whiteKnight, true, gamestate);

        Knight blackKnight = new Knight("Black", 5, 4,"Normal");
        utils.placePiece(blackKnight, true, gamestate);

        assertEquals(8, ecat.validMoves(whiteKnight, true, gamestate, 0).size());
        assertEquals(8, ecat.validMoves(blackKnight, true, gamestate, 0).size());
    }

    @Test
    public void RookMoves_VerticalAndHorizontal() {
        Rook whiteRook = new Rook("White", 5, 2,"Normal");
        utils.placePiece(whiteRook, true, gamestate);

        Rook blackRook = new Rook("Black", 5, 5,"Normal");
        utils.placePiece(blackRook, true, gamestate);

        assertEquals(12, ecat.validMoves(whiteRook, true, gamestate, 0).size());
        assertEquals(12, ecat.validMoves(blackRook, true, gamestate, 0).size());
    }

    @Test
    public void QueenMoves_AnyDirection() {
        Queen whiteQueen = new Queen("White", 5, 3,"Normal");
        utils.placePiece(whiteQueen, true, gamestate);

        Queen blackQueen = new Queen("Black", 5, 4,"Normal");
        utils.placePiece(blackQueen, true, gamestate);

        assertEquals(22, ecat.validMoves(whiteQueen, true, gamestate, 0).size());
        assertEquals(22, ecat.validMoves(blackQueen, true, gamestate, 0).size());
    }

    @Test
    public void KingMoves_OneStepAnyDirection() {
        utils.placePiece(new BlankPiece("Blank",  0,0,"Normal"), true, gamestate);
        utils.placePiece(new BlankPiece("Blank",  0,7,"Normal"), true, gamestate);

        King whiteKing = new King("White",  1,2,"Normal");
        utils.placePiece(whiteKing, true, gamestate);

        King blackKing = new King("Black",  1,5,"Normal");
        utils.placePiece(blackKing, true, gamestate);

        assertEquals(8, ecat.validMoves(whiteKing, true, gamestate, 0).size());
        assertEquals(8, ecat.validMoves(blackKing, true, gamestate, 0).size());
    }

}
