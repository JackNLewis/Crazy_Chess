package CrazyChess.logic.StageHazards;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;

public class HazardPiece extends AbstractPiece {

    protected AbstractPiece originalPiece;
    protected Hazard hazard;

    public HazardPiece(Position position, Hazard type,AbstractPiece originalPiece) {
        super("Blank", position, "Normal");
        this.originalPiece = originalPiece;
        System.out.println("Hazard info:");
        System.out.println("Color: " + getColor());
        System.out.println("Pos: " + getPosition());
        System.out.println("Pw type: " + getPoweruptype());
        System.out.println("Original piece: " + originalPiece);
    }

    public String toString(){
        return "HZ at " + position;
    }

    public Hazard getHazard(){
        return hazard;
    }
}
