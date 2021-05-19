package CrazyChess.pieces;

import CrazyChess.logic.Position;

/**
 * Class that represents the Bishop in game
 * 
 *
 */
public class Powerup extends AbstractPiece
{
	/**
	 * Constructor by coordinates
	 *
	 * @param xCoord    x coordinate
	 * @param yCoord    y coordinate
	 * @param Type the type of piece
	 */
	public Powerup ( int xCoord, int yCoord, String Type){
		super("Powerup", xCoord, yCoord,Type);
		Type = "Normal";
	}
	
	/**
	 * Constructor by position class
	 *
	 * @param position   position object to set initial position
	 * @param Type the type of piece
	 */
	public Powerup( Position position, String Type){
		super("Powerup", position,Type);
		Type = "Normal";
	}
}
