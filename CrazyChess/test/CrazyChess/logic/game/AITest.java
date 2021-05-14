package CrazyChess.logic.game;

import CrazyChess.logic.AI;
import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.savegamestate.SaveGame;
import CrazyChess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AITest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private ExtraChecksAndTools ecat = new ExtraChecksAndTools();
    private AI ai;

    @Before
    public void init() {
        mainLogic = new MainLogic();
        ai = new AI();
    }

    @Test
    public void AIOnlyMoveOnePiecePerTurn() {
        mainLogic.resetBoard();

        AbstractPiece[][] initGamestate = mainLogic.getGamestate();
        AbstractPiece[][] nextGamestate = ai.AI(mainLogic);

        // Difference is two since the piece swapped place with a BlankPiece
        assertTrue(utils.getPiecesDiff(initGamestate, nextGamestate).size() == 2);
    }

    @Test
    public void AICanRecognizeSimpleCheckmatePattern_MediumOrHard() {
        AbstractPiece[][] testGamestate = mainLogic.getGamestate();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                testGamestate[i][j] = new BlankPiece("Blank", i, j,"Normal");
            }
        }
        utils.placePiece(new King("White", 4, 0, "Normal"), 4, 0, true, testGamestate);
        utils.placePiece(new King("Black", 4, 7, "Normal"), 4, 7, true, testGamestate);
        utils.placePiece(new Rook("Black", 7, 1, "Normal"), 7, 1, true, testGamestate);
        utils.placePiece(new Queen("Black", 0, 7, "Normal"), 0, 7, true, testGamestate);

        mainLogic.changeTurn();
        AbstractPiece[][] nextGamestate = ai.AI(mainLogic, "medium");

        assertTrue(ecat.isInCheckmate("White", true, nextGamestate, 1, new ArrayList<String>()));
    }

}
