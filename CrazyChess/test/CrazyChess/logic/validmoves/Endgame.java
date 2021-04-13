package CrazyChess.logic.validmoves;

import CrazyChess.logic.MainLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class Endgame {

    private MainLogic mainLogic;

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void WhiteKingCanBeCheckmated() {

    }

    @Test
    public void BlackKingCanBeCheckmated() {

    }

    @Test
    public void Draw_IfNoValidMoves() {

    }

    @Test
    public void Draw_IfOnlyKingsLeftOnTheBoard() {

    }

}
