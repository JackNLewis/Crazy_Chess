package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Pawn in game
 * 
 *
 */



public class Pawn extends AbstractPiece
{
	private int turnDoublejumped = 0; //Turn pawn made his first move and it was a double jump
									  //Left as 0 if pawn is not moved or its first move was a single jump
	private boolean canEnPassant = false; //If the pawn can do an enPassant move
	
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the pawn
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 * @param Type 		type of piece
	 */
	public Pawn(String color, int xCoord, int yCoord, String Type){
		super(color, xCoord, yCoord, Type);
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the pawn
	 * @param position   position object to set initial position
	 * @param Type 		type of piece
	 *
	 */
	public Pawn(String color, Position position, String Type){
		super(color, position,Type);
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
	
	/**
	 * Setter for canEnPassant
	 * @param bool   if the pawn can enPassant
	 */
	
	public void setEnPassant(boolean bool) {
		canEnPassant = bool;
	}
	/**
	 * Getter for canEnPassant
	 * @return if the pawn can enPassant
	 */
	public boolean getEnPassant() {
		return canEnPassant;
	}

}
