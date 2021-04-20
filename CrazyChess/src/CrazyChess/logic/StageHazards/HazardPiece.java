package CrazyChess.logic.StageHazards;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;

public class HazardPiece extends AbstractPiece {

    protected AbstractPiece originalPiece;
    protected Hazard hazard;

    public HazardPiece(Position position, Hazard type,AbstractPiece originalPiece) {
        super("Blank", position, "Normal");
        this.originalPiece = originalPiece;
        this.hazard = type;
    }

    public String toString(){
        return "HZ at " + position;
    }

    public Hazard getHazard(){
        return hazard;
    }

    public AbstractPiece getOriginalPiece() {
        return originalPiece;
    }
}
