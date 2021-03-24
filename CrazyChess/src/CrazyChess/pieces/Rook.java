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
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the rook
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Rook(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord,Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the rook
	 * @param position   position object to set initial position
	 */
	public Rook(String color, Position position, String Type){
		super(color, position, Type);
	}
}
