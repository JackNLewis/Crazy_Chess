package CrazyChess.logic.validmoves;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import CrazyChess.pieces.King;
import CrazyChess.pieces.Pawn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CaptureMoves {

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
    public void PawnCapture_Diagonal() {
        Pawn whitePawnA = new Pawn("White", 0, 3,"Normal");
        Pawn whitePawnB = new Pawn("White", 1, 3,"Normal");
        utils.placePiece(whitePawnA, true, gamestate);
        utils.placePiece(whitePawnB, true, gamestate);

        Pawn blackPawnA = new Pawn("Black", 0, 4,"Normal");
        Pawn blackPawnB = new Pawn("Black", 1, 4,"Normal");
        utils.placePiece(blackPawnA, true, gamestate);
        utils.placePiece(blackPawnB, true, gamestate);

        assertTrue(ecat.canCapture(whitePawnA, blackPawnB, true, gamestate, 1));
        assertTrue(ecat.canCapture(blackPawnA, whitePawnB, true, gamestate, 1));
    }

    @Test
    public void PawnCapture_EnPassant() {
//        Pawn whitePawnA = new Pawn("White", 0, 3,"Normal");
//        Pawn whitePawnB = new Pawn("White", 1, 3,"Normal");
//        utils.placePiece(whitePawnA, true, gamestate);
//        utils.placePiece(whitePawnB, true, gamestate);
//
//        Pawn blackPawnA = new Pawn("Black", 0, 4,"Normal");
//        Pawn blackPawnB = new Pawn("Black", 1, 4,"Normal");
//        utils.placePiece(blackPawnA, true, gamestate);
//        utils.placePiece(blackPawnB, true, gamestate);
//
//        assertTrue(ecat.canCapture(whitePawnA, blackPawnB, true, gamestate, 1));
//        assertTrue(ecat.canCapture(blackPawnA, whitePawnB, true, gamestate, 1));
    }

    @Test
    public void BishopCapture_Diagonal() {

    }

    @Test
    public void KnightCapture_LetterLAndJumpOver() {

    }

    @Test
    public void RookCapture_ParallelAndHorizontal() {

    }

    @Test
    public void QueenCapture_AnyDirection() {

    }

    @Test
    public void KingCapture_OneStepAnyDirection() {

    }
}
