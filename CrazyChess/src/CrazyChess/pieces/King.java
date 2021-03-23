package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the King in game
 * 
 * @author Darius
 *
 */
public class King extends AbstractPiece
{
	boolean isChecked = false; //is king currently under check
	boolean wasMoved = false; //was king moved this game
	boolean canCastle = false; //can the kind castle
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the king
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public King(String color, int xCoord, int yCoord){
		super(color, xCoord, yCoord);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the king
	 * @param position   position object to set initial position
	 */
	public King(String color, Position position){
		super(color, position);
	}
	/**
	 * Setter for isChecked
	 * @param input    Value to change isCheked to
	 */
	public void setIsChecked(boolean input) {
		isChecked=input;
	}
	/**
	 * Getter for isChecked
	 * @return current value of isChecked
	 */
	public boolean getIsChecked() {
		return isChecked;
	}
	/**
	 * Setter for wasMoved
	 * @param input   Value to change wasMoved to
	 */
	public void setWasMoved(boolean input) {
		wasMoved=input;
	}
	/**
	 * Getter for wasMoved
	 * @return current value of wasMoved
	 */
	public boolean getWasMoved() {
		return wasMoved;
	}
	/**
	 * Setter for wasMoved
	 * @param input   Value to change wasMoved to
	 */
	public void setCanCastle(boolean input) {
		canCastle=input;
	}
	/**
	 * Getter for wasMoved
	 * @return current value of wasMoved
	 */
	public boolean getCanCastle() {
		return canCastle;
	}
}
