package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Pawn in game
 * 
 * @author Darius
 *
 */



public class Pawn extends AbstractPiece
{
	private int turnDoublejumped = 0; //Turn pawn made his first move and it was a double jump
									  //Left as 0 if pawn is not moved or its first move was a single jump
	
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the pawn
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Pawn(String color, int xCoord, int yCoord){
		super(color, xCoord, yCoord);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the pawn
	 * @param position   position object to set initial position
	 */
	public Pawn(String color, Position position){
		super(color, position);
	}
	
	/**
	 * Setter for turnDoublejumped
	 * @param turnNo   Number of the current turn
	 */
	public void setDoublejump(int turnNo) {
		turnDoublejumped=turnNo;
	}
	
	/**
	 * Getter for turnDoublejumped
	 * @return   The turn the pawn double jumped. 0 if pawn is not moved or its first move was a single jump.
	 */
	public int getDoublejump() {
		return turnDoublejumped;
	}
}
