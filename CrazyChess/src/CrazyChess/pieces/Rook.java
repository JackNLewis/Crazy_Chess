package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Rook in game
 * 
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
	 * @param Type the type of piece
	 */
	public Rook(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord,Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the rook
	 * @param position   position object to set initial position
	 * @param Type the type of piece
	 */
	public Rook(String color, Position position, String Type){
		super(color, position, Type);
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
