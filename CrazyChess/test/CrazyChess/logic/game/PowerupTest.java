package CrazyChess.logic.game;

import CrazyChess.logic.MainLogic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PowerupTest {

    private MainLogic mainLogic;

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void test() {
        assertTrue(true);
    }
}