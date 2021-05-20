package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the King in game
 * 
 *
 */
public class King extends AbstractPiece
{
	boolean	isChecked	= false;	//is king currently under check
	boolean	wasMoved	= false;	//was king moved this game
	int		canCastle	= 0;		//can the king castle and if yes which castle is it

	/**
	 * Constructor by coordinates
	 *
	 * @param color
	 *            color of the king
	 * @param xCoord
	 *            x coordinate
	 * @param yCoord
	 *            y coordinate
	 * @param Type
	 *            type of piece
	 */
	public King(String color, int xCoord, int yCoord, String Type)
	{
		super(color, xCoord, yCoord, Type);
	}

	/**
	 * Constructor by position class
	 *
	 * @param color
	 *            color of the king
	 * @param position
	 *            position object to set initial position
	 * @param Type
	 *            type of piece
	 */
	public King(String color, Position position, String Type)
	{
		super(color, position, Type);
	}

	/**
	 * Setter for isChecked
	 * 
	 * @param input
	 *            Value to change isCheked to
	 */
	public void setIsChecked(boolean input)
	{
		isChecked = input;
	}

	/**
	 * Getter for isChecked
	 * 
	 * @return current value of isChecked
	 */
	public boolean getIsChecked()
	{
		return isChecked;
	}

	/**
	 * Setter for wasMoved
	 * 
	 * @param input
	 *            Value to change wasMoved to
	 */
	public void setWasMoved(boolean input)
	{
		wasMoved = input;
	}

	/**
	 * Getter for wasMoved
	 * 
	 * @return current value of wasMoved
	 */
	public boolean getWasMoved()
	{
		return wasMoved;
	}

	/**
	 * Setter for canCastle
	 * 
	 * @param input
	 *            Value to change canCastle to
	 */
	public void setCanCastle(int input)
	{
		canCastle = input;
	}

	/**
	 * Getter for canCastle
	 * 
	 * @return current value of canCastle
	 */
	public int getCanCastle()
	{
		return canCastle;
	}
}
