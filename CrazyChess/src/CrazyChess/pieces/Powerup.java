package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Bishop in game
 * 
 * @author Darius
 *
 */
public class Powerup extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param color     color of the bishop
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 */
	public Powerup ( int xCoord, int yCoord, String Type){
		super("Powerup", xCoord, yCoord,Type);
		Type = "Normal";
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param color      color of the bishop
	 * @param position   position object to set initial position
	 */
	public Powerup( Position position, String Type){
		super("Powerup", position,Type);
		Type = "Normal";
	}
}
