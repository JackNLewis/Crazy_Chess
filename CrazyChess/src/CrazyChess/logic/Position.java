package CrazyChess.logic;

import java.io.Serializable;

/**
 * This class is used to hold position of the piece on the board.
 * It also has getters and setters for it.
 * 
 * The positions are represented by x and y coordinates.
 * For example, square A1 would be coordinate (0,0), H8 would be (7,7) and so on.
 *
 */
public class Position implements Serializable
{
	public int xCoord;
	public int yCoord;
	
	/**
	 * Constructor you pass coordinates into
	 * 
	 * @param x    x position coordinates
	 * @param y    y position coordinates
	 */
	public Position(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
	}
	
	/**
	 * Sets x coordinate
	 * @param xNew    The new x value
	 */
	public void setXpos(int xNew) {
		this.xCoord= xNew;
	}
	
	/**
	 * Sets x coordinate
	 * @param yNew    The new x value
	 */
	public void setYpos(int yNew) {
		this.yCoord= yNew;
	}
	
	/**
	 * Gets x coordinate
	 * @return   x coordinate
	 */
	public int getXpos() {
		return xCoord;
	}
	
	/**
	 * Gets y coordinate
	 * @return   y coordinate
	 */
	public int getYpos() {
		return yCoord;
	}
	
	/**
	 * Checks if two positions are equal
	 *
	 * @param pos    position to be checked against this Position object
	 * @return       true if positions match
	 */
	public boolean equals(Position pos){
		if(pos.getXpos() == this.getXpos() && pos.getYpos() == this.getYpos())
			return true;
		return false;
	}
	
	
	/**
	 * Returns the string representation of the position
	 * @return String representation of the position
	 */
	public String toString(){
		return "x position: " + xCoord + ", y position: " + yCoord;
	}
	
	
}
