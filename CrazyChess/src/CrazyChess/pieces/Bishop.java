package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Bishop in game
 * 
 *
 */
public class Bishop extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color
	 *            color of the bishop
	 * @param xCoord
	 *            x coordinate
	 * @param yCoord
	 *            y coordinate
	 * @param Type
	 *            the type of piece
	 */
	public Bishop(String color, int xCoord, int yCoord, String Type)
	{
		super(color, xCoord, yCoord, Type);
	}

	/**
	 * Constructor by position class
	 *
	 * @param color
	 *            color of the bishop
	 * @param position
	 *            position object to set initial position
	 * @param Type
	 *            the type of piece
	 */
	public Bishop(String color, Position position, String Type)
	{
		super(color, position, Type);
	}
}
