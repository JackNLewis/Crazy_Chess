package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Rook in game
 * 
 * @author Darius
 *
 */
public class Rook extends AbstractPiece
{
	private boolean wasMoved = false; //Boolean recording if the rook has been moved
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the rook
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Rook(String color, int xCoord, int yCoord){
		super(color, xCoord, yCoord);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the rook
	 * @param position   position object to set initial position
	 */
	public Rook(String color, Position position){
		super(color, position);
	}
	/**
	 * Setter for wasMoved
	 * @param bool   if the piece has moved
	 */
	public void setWasMoved(boolean bool) {
		wasMoved=bool;
	}
	
	/**
	 * Getter for wasMoved
	 * @return if the rook has moved or not
	 */
	public boolean getWasMoved() {
		return wasMoved;
	}
}
