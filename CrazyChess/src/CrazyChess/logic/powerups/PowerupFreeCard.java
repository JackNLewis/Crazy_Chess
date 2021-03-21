package CrazyChess.logic.powerups;

import CrazyChess.logic.*;
import CrazyChess.pieces.*;

public class PowerupFreeCard {
Utilities utils = new Utilities();
	
	/**
	 * Swap one king and a blank on the board.
	 * @param gamestate    gamestate to be altered
	 * @param king         the king to be swapped
	 * @param target	   blank to be swapped
	 * @param isDebug	   is debug mode active
	 * @return			   the altered gamestate, where king and target are swapped. NULL if the swap is not possible
	 */
	
	//include checks vs moving enemy pieces and powerups
	
	public AbstractPiece[][] freecard(AbstractPiece[][] gamestate, Position king, Position target, Boolean isDebug){
		if(!(utils.isOnBoard(king)&&utils.isOnBoard(target))) {
			if (isDebug) System.out.println("Targets for swapping are coordinates that are not on the board. Returning NULL");
			return null;
		}
		
		
		AbstractPiece gamestateCopy[][]=utils.safeCopyGamestate(gamestate);
		AbstractPiece newking = utils.getPiece(king, isDebug, gamestateCopy);
		AbstractPiece blank = utils.getPiece(target, isDebug, gamestateCopy);
		if(!(newking instanceof King)) {
			if (isDebug) System.out.println("Must select your king. Returning NULL");
			return null;
		}
		if(!((blank.getXpos() >= king.getXpos()-2)&&(blank.getXpos() <= king.getXpos()+2)&&
				(blank.getYpos() <= king.getYpos()+2)&&(blank.getYpos() >= king.getYpos()-2))) {
			if (isDebug) System.out.println("Not 5X5. Returning NULL");
			return null;
		}
		if(!blank.getColor().equalsIgnoreCase("blank")) {
			if (isDebug) System.out.println("Cant move to other pieces' position which is not blank. Returning NULL");
			return null;
		}
		Position newkingposition = newking.getPosition();
		Position blankposition = blank.getPosition();
		newking.setPosition(blankposition);
		blank.setPosition(newkingposition);
		
		gamestateCopy=utils.placePiece(newking, isDebug, gamestateCopy);
		gamestateCopy=utils.placePiece(blank, isDebug, gamestateCopy);
		
		return gamestateCopy;
	}

}
