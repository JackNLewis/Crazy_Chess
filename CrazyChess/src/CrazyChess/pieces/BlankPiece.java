/**
 * This class represents a blank unoccupied square on the board
 * @author Darius
 */


package CrazyChess.pieces;
import CrazyChess.logic.Position;


public class BlankPiece extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color   color of piece
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public BlankPiece(String color, int xCoord, int yCoord)
	{
		super(color, xCoord, yCoord);
		color= "Blank";
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of piece
	 * @param position   position object to set initial position
	 */
	public BlankPiece(String color, Position position){
		super(color, position);
		color = "Blank";
	}
	
	/**
	 * Returns the string representation of the blank piece
	 * @return String representation of the blank piece
	 */
	public String toString(){
		return "Blank space at (" + position.getXpos() + ", " + position.getYpos() + ")";
	}
	
}
