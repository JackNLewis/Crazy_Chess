package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Queen in game
 * 
 * @author Darius
 *
 */
public class Queen extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the queen
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Queen(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord,Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the queen
	 * @param position   position object to set initial position
	 */
	public Queen(String color, Position position, String Type){
		super(color, position,Type);
	}
}
