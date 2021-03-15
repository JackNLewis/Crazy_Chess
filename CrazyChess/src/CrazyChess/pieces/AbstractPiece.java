package CrazyChess.pieces;

import CrazyChess.logic.Position;

import java.io.Serializable;

/**
 * This is the abstract class for all of the other
 * classes of pieces.
 * All other pieces extend this.
 * 
 * @author Darius
 *
 */


public abstract class AbstractPiece implements Serializable
{

	protected String color;
	protected Position position;
	
	/**
	 * Constructor by coordinates
	 *
	 * @param color   color of piece
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public AbstractPiece(String color, int xCoord, int yCoord){
		this.color = color;
		this.position = new Position(xCoord, yCoord);
	}

	/**
	 * Constructor by position class
	 *
	 * @param color      color of piece
	 * @param position   position object to set initial position
	 */
	public AbstractPiece(String color, Position position){
		this.color = color;
		this.position = position;
	}
	
	/**
	 * Returns color as a String
	 *
	 * @return   Usually "White", "Black", or "Blank"
	 */
	public String getColor(){
		return color;
	}

	/**
	 * Returns the position as a Position object
	 *
	 * @return   position represented by Position object
	 */
	public Position getPosition(){
		return this.position;
	}
	
	/**
	 * Gets X coordinate
	 * 
	 * @return  Piece's x coordinate
	 */
	public int getXpos(){
		return this.position.getXpos();
	}

	/**
	 * Gets Y coordinate
	 * 
	 * @return   Piece's Y coordinate
	 */
	public int getYpos(){
		return this.position.getYpos();
	}

	/**
	 * Sets the position using another position object
	 * 
	 * @param pos   new position object
	 */
	public void setPosition(Position pos){
		if(pos != null)
			this.position = pos;
	}
	/**
	 * Sets the position using coordinates
	 *
	 * @param x   x coordinate
	 * @param y   y coordinate
	 */
	public void setPosition(int x, int y){
		this.position.setXpos(x);
		this.position.setYpos(y);
	}

	/**
	 * Returns the string representation of the piece
	 * @return String representation of the piece
	 */
	
	public String toString(){
		return color + " " + this.getClass().getSimpleName() + " at (" + position.getXpos() + ", " + position.getYpos() + ")";
	}

}
