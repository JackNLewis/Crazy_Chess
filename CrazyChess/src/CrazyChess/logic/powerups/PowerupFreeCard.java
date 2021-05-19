package CrazyChess.logic.powerups;

import CrazyChess.logic.*;
import CrazyChess.pieces.*;
/**
 * Class for the "get out of check free card" powerup
 *
 */
public class PowerupFreeCard {
	Utilities utils = new Utilities();
	MainLogic main = new MainLogic();
	ExtraChecksAndTools ecat = new ExtraChecksAndTools();
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
		if(!ecat.isInCheck(newking.getColor(), isDebug, gamestateCopy, main.getTurnNo())) {
			if (isDebug) System.out.println("Cant move the king which is not in check. Returning NULL");
			return null;
		}
		if(!(ecat.BlankcapturableBy(blank, newking.getColor(), isDebug,gamestateCopy, main.getTurnNo()).isEmpty())) {
			if (isDebug) System.out.println("Cant move the king to a place which is in check. Returning NULL");
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
