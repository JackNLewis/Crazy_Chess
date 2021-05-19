package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Knight in game
 * 
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
	 * @param Type 		type of piece
	 */
	public Knight(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord, Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the knight
	 * @param position   position object to set initial position
	 * @param Type the type of piece
	 */
	public Knight(String color, Position position, String Type){
		super(color, position,Type);
	}
}
