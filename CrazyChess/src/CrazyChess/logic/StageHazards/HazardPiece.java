package CrazyChess.logic.StageHazards;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
/**
 * A class for the stage hazzard piece
 *
 */
public class HazardPiece extends AbstractPiece {

    protected AbstractPiece originalPiece;
    protected Hazard hazard;

    /**
     * Constructor for the HazzardPiece
     * @param position         position of the piece
     * @param type			   type of hazard (either BURN of FROZEN)
     * @param originalPiece    type of piece that was on the board before the tile hazard
     */
    public HazardPiece(Position position, Hazard type,AbstractPiece originalPiece) {
        super("Blank", position, "Normal");
        this.originalPiece = originalPiece;
        this.hazard = type;
    }
    /**
     * Returns a string representation of the stage hazard
     */
    public String toString(){
        return "HZ at " + position;
    }
    /**
     * Gets the type of the hazard
     * @return type of the hazzard (BURN or FROZEN)
     */
    public Hazard getHazard(){
        return hazard;
    }
    /**
     * Gets the original piece, that was on the tile before the stage hazard spawned
     * @return the original piece, that was on the tile before the stage hazard spawned
     */
    public AbstractPiece getOriginalPiece() {
        return originalPiece;
    }
    
}

