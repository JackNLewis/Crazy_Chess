package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Bishop in game
 * 
 * @author Darius
 *
 */
public class Bishop extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the bishop
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Bishop(String color, int xCoord, int yCoord){
		super(color, xCoord, yCoord);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the bishop
	 * @param position   position object to set initial position
	 */
	public Bishop(String color, Position position){
		super(color, position);
	}
}
