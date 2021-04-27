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
	protected String Pwtype;
	
	
	/**
	 * Constructor by coordinates
	 *
	 * @param color   color of piece
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public AbstractPiece(String color, int xCoord, int yCoord, String type){
		this.color = color;
		this.position = new Position(xCoord, yCoord);
		this.Pwtype = type;
	}

	/**
	 * Constructor by position class
	 *
	 * @param color      color of piece
	 * @param position   position object to set initial position
	 */
	public AbstractPiece(String color, Position position, String type){
		this.color = color;
		this.position = position;
		this.Pwtype = type;
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
	 * Sets color (only used in checking for castling)
	 *
	 * 
	 */
	public void setColor(String col){
		color = col;
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

	/**
	 * Returns the position copy as a Position object, without referencing the original object's position
	 * This is a very hacky solution and should be changed in the future
	 *
	 * @return   position represented by Position object
	 */
	public Position getPositionCopy(){
		
		int x=0;
		int y=0;
		
		for(x=0; x<position.getXpos(); x++) {
		}
		
		for(y=0; y<position.getYpos(); y++) {
		}
		
		
		return new Position(x,y);
	}
	
	public void changePowerup(String type){
		this.Pwtype = type;
	}
	
	public String getPoweruptype(){
		return Pwtype;
	}


	
	
	
}
