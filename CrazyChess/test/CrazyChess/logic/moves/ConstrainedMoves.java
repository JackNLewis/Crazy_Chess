package CrazyChess.logic.moves;

import CrazyChess.logic.MainLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConstrainedMoves {

    private MainLogic mainLogic;

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void PieceCantMoveOrCapture_IfOwnKingChecked() {

    }

    @Test
    public void KingCantCastle_IfOwnKingChecked() {

    }

    @Test
    public void KingCantCastle_IfPathIsChecked() {

    }

}
