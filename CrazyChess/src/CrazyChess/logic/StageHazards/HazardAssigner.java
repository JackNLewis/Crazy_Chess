package CrazyChess.logic.StageHazards;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;

import java.util.ArrayList;
import java.util.Random;

public class HazardAssigner {

    int untilHazard = 0;
    int hazardDuration;
    Utilities utils;
    ExtraChecksAndTools ecat = new ExtraChecksAndTools();

    public HazardAssigner(){
        utils = new Utilities();
    }

    public AbstractPiece[][] assignHazard(AbstractPiece[][] gameState){
            AbstractPiece[][] safeGameState = utils.safeCopyGamestate(gameState);
            // just using frozen hazard atm
            return frozenHazard(safeGameState);
    }

    private AbstractPiece[][] frozenHazard(AbstractPiece[][] gameState){
        ArrayList<AbstractPiece> pieces = ecat.gamestateToPieceArrayList(gameState);
        Random rand = new Random();
        AbstractPiece[][] gs = gameState;
        for(int i =0; i<2 ; i++){
            //only changes first piece at the minuite
            int index = rand.nextInt(pieces.size());
            HazardPiece frozenTile = new HazardPiece(pieces.get(index).getPosition(), Hazard.FROZEN,pieces.get(0));
            gs = utils.placePiece(frozenTile,false,gameState);
        }
        return gs;
    }

    
}
