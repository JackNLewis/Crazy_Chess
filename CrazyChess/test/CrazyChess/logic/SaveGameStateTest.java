package CrazyChess.logic;

import CrazyChess.logic.savegamestate.SaveGame;
import CrazyChess.pieces.AbstractPiece;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SaveGameStateTest {

    private MainLogic mainLogic;
    private Utilities utils = new Utilities();
    private SaveGame saveGame = new SaveGame();

    @Before
    public void init() {
        mainLogic = new MainLogic();
        mainLogic.resetBoard();
    }

    @Test
    public void CanSaveAndLoadGamestateWithFile() {
        File file = new File("saved.xml");
        AbstractPiece[][] initGamestate = utils.safeCopyGamestate(mainLogic.getGamestate());
        byte[] gameSave = saveGame.save(mainLogic, mainLogic.getGamestate());

        saveGame.saveDataToFile(gameSave, file);

        MainLogic newLogic = new MainLogic();
        byte[] loadedGameSave = saveGame.loadDataFromFile(file);
        saveGame.load(loadedGameSave, newLogic, newLogic.getGamestate());
        AbstractPiece[][] newGamestate = newLogic.getGamestate();
        newLogic.setGamestate(newLogic.getGamestate());

        assertTrue(utils.getPiecesDiff(initGamestate, newGamestate).isEmpty());
    }

}
