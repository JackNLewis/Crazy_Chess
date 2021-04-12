package CrazyChess.logic.StageHazards;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import CrazyChess.pieces.King;

import java.util.ArrayList;
import java.util.Random;

public class HazardAssigner {

    final int hazardInterval = 4; //number of go's between hazards spawning
    int untilHazard = 0; //number of turns before next hazard
    int hazardDuration = 2; //number of turns hazards last for
    int hazardTurns = 0; //how many turns hazard has been active
    boolean activeHazard;
    Utilities utils;
    ExtraChecksAndTools ecat = new ExtraChecksAndTools();

    public HazardAssigner(){
        utils = new Utilities();
        activeHazard = false;
    }


    public AbstractPiece[][] assignHazard(AbstractPiece[][] gameState){
            AbstractPiece[][] safeGameState = utils.safeCopyGamestate(gameState);
            // just using frozen hazard atm
        if(untilHazard == 0){

            activeHazard = true;
            hazardTurns = 0;
            untilHazard = hazardInterval;

            //generate which hazard
            Random rand = new Random();
            int randHazardIndex = rand.nextInt(Hazard.values().length);
            Hazard randHazard = Hazard.values()[randHazardIndex];

            //code for randomly choosing hazard
            if(randHazard == Hazard.FROZEN){
                System.out.println("FROZEN");
                return frozenHazard(gameState);
            }else if(randHazard == Hazard.BURN){
                System.out.println("BURN");
                return burnHazard(gameState);
            }
        }
        else{
            if(activeHazard){
                if(hazardTurns == hazardDuration){//needs to despawn hazard
                    hazardTurns=0;
                    System.out.println("DESPAWN HAZARD");
                    activeHazard= false;
                }else{
                    hazardTurns++;
                }
            }else{
                //no active hazard, no need to spawn hazard
                untilHazard--;
            }
        }
        return gameState;
    }

    private AbstractPiece[][] frozenHazard(AbstractPiece[][] gameState){
        ArrayList<AbstractPiece> pieces = ecat.gamestateToPieceArrayList(gameState);
        Random rand = new Random();
        AbstractPiece[][] gs = gameState;
        for(int i =0; i<getThreshhold(pieces.size()) ; i++){
            //only changes first piece at the minuite
            int index = rand.nextInt(pieces.size());
            while(pieces.get(index) instanceof King){
                index = rand.nextInt(pieces.size());
            }
            HazardPiece frozenTile = new HazardPiece(pieces.get(index).getPosition(), Hazard.FROZEN,pieces.get(0));
            gs = utils.placePiece(frozenTile,false,gameState);
        }
        return gs;
    }

    private AbstractPiece[][] burnHazard(AbstractPiece[][] gameState){
        System.out.println("In burn");
        return gameState;
    }

    private int getThreshhold(int size){
        if(size>24){
            return 3;
        }else if(size>12){
            return 2;
        }else if(size>5){
            return 1;
        }else{
            return 0;
        }
    }
    
}
