package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Knight in game
 * 
 * @author Darius
 *
 */
public class Knight extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the knight
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Knight(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord, Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the knight
	 * @param position   position object to set initial position
	 */
	public Knight(String color, Position position, String Type){
		super(color, position,Type);
	}
}
