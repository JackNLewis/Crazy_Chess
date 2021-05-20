
package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * The class for blank pieces (basically empty tiles on the board)
 *
 */
public class BlankPiece extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color
	 *            color of piece
	 * @param xCoord
	 *            x coordinate
	 * @param yCoord
	 *            y coordinate
	 * @param Type
	 *            the type of piece
	 */
	public BlankPiece(String color, int xCoord, int yCoord, String Type)
	{
		super(color, xCoord, yCoord, Type);
		color = "Blank";
	}

	/**
	 * Constructor by position class
	 *
	 * @param color
	 *            color of piece
	 * @param position
	 *            position object to set initial position
	 * @param Type
	 *            the type of piece
	 */
	public BlankPiece(String color, Position position, String Type)
	{
		super(color, position, Type);
		color = "Blank";
	}

	/**
	 * Returns the string representation of the blank piece
	 * 
	 * @return String representation of the blank piece
	 */
	public String toString()
	{
		return "Blank space at (" + position.getXpos() + ", " + position.getYpos() + ")";
	}

}
