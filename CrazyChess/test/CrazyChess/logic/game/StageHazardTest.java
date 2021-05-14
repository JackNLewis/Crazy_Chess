package CrazyChess.logic.game;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StageHazardTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private ExtraChecksAndTools ecat = new ExtraChecksAndTools();

    @Before
    public void init() {
        mainLogic = new MainLogic();
    }
}
