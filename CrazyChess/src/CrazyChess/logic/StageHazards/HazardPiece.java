package CrazyChess.logic.StageHazards;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;

/**
 * Class for a hazard piece
 */
public class HazardPiece extends AbstractPiece {

    protected AbstractPiece originalPiece;
    protected Hazard hazard;

    /**
     * Constructor for a hazard piece
     * @param position
     * @param type
     * @param originalPiece
     */
    public HazardPiece(Position position, Hazard type,AbstractPiece originalPiece) {
        super("Blank", position, "Normal");
        this.originalPiece = originalPiece;
        this.hazard = type;
    }

    /**
     *
     * @return the string of the hazard piece
     */
    public String toString(){
        return "HZ at " + position;
    }

    /**
     *
     * @return the hazard of the piece
     */
    public Hazard getHazard(){
        return hazard;
    }

    /**
     *
     * @return the original piece before the hazard
     */
    public AbstractPiece getOriginalPiece() {
        return originalPiece;
    }
    
}

