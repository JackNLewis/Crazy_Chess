package CrazyChess.logic.powerups;

import CrazyChess.pieces.*;
import CrazyChess.logic.*;

/**
 * Class for the teleport powerup
 *
 */

public class PowerupTeleport
{
	Utilities utils = new Utilities();
	
	/**
	 * Swap two pieces on the board.
	 * @param gamestate    gamestate to be altered
	 * @param target1      first piece to be swapped
	 * @param target2	   second piece to be swapped
	 * @param isDebug	   is debug mode active
	 * @return			   the altered gamestate, where target1 and target2 are swapped. NULL if the swap is not possible
	 */
	
	//include checks vs moving enemy pieces and powerups
	
	public AbstractPiece[][] teleport(AbstractPiece[][] gamestate, Position target1, Position target2, Boolean isDebug){
		if(!(utils.isOnBoard(target1)&&utils.isOnBoard(target2))) {
			if (isDebug) System.out.println("Targets for teleportation are coordinates that are not on the board. Returning NULL");
			return null;
		}
		
		
		AbstractPiece gamestateCopy[][]=utils.safeCopyGamestate(gamestate);
		AbstractPiece piece1 = utils.getPiece(target1, isDebug, gamestateCopy);
		AbstractPiece piece2 = utils.getPiece(target2, isDebug, gamestateCopy);
		if(piece1 instanceof King||piece2 instanceof King) {
			if (isDebug) System.out.println("Can't swap kings. Returning NULL");
			return null;
		}
		if(!piece1.getColor().equalsIgnoreCase(piece2.getColor())) {
			if (isDebug) System.out.println("You can only swap pieces of your own color. Returning NULL");
			return null;
		}
		if(piece1.getColor().equalsIgnoreCase("blank")||piece2.getColor().equalsIgnoreCase("blank")) {
			if (isDebug) System.out.println("Can't use blank pieces in teleportation. Returning NULL");
			return null;
		}
		Position piece1position = piece1.getPosition();
		Position piece2position = piece2.getPosition();
		piece1.setPosition(piece2position);
		piece2.setPosition(piece1position);
		
		gamestateCopy=utils.placePiece(piece1, isDebug, gamestateCopy);
		gamestateCopy=utils.placePiece(piece2, isDebug, gamestateCopy);
		
		return gamestateCopy;
	}

}
