package CrazyChess.logic.powerups;

import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.pieces.*;

public class PowerupMiniPromote {
Utilities utils = new Utilities();
	
	/**
	 * Turn one pawn into a bishop or knight on the board.
	 * @param gamestate    gamestate to be altered
	 * @param target1      first piece to be Promoted
	 * @param target2      second piece that first piece will to be
	 * @param isDebug	   is debug mode active
	 * @return			   the altered gamestate, where target1 turn into target2. NULL if the minipromote is not possible
	 */
	
	//include checks vs moving enemy pieces and powerups
	
	public AbstractPiece[][] promote(AbstractPiece[][] gamestate, Position target1,Position target2, Boolean isDebug){
		if(!(utils.isOnBoard(target1))&&!(utils.isOnBoard(target2))) {
			if (isDebug) System.out.println("Target for Promotation is coordinate that is not on the board. Returning NULL");
			return null;
		}
		
		
		AbstractPiece gamestateCopy[][]=utils.safeCopyGamestate(gamestate);
		AbstractPiece piece1 = utils.getPiece(target1, isDebug, gamestateCopy);
		AbstractPiece piece2 = utils.getPiece(target2, isDebug, gamestateCopy);
		if((piece1.getPoweruptype().equalsIgnoreCase("dummy"))) {
			if (isDebug) System.out.println("Can't promote piece which is a DummyPiece. Returning NULL");
			return null;
		}
		if(!(piece1 instanceof Pawn)) {
			if (isDebug) System.out.println("Can't promote piece which is not Pawn. Returning NULL");
			return null;
		}
		if(!(piece2 instanceof Bishop)&&!(piece2 instanceof Knight)) {
			if (isDebug) System.out.println("The pawn only can turn into Bishop or Knight. Returning NULL");
			return null;
		}
		if(piece1.getColor().equalsIgnoreCase("blank")) {
			if (isDebug) System.out.println("Can't use blank pieces in teleportation. Returning NULL");
			return null;
		}
		
		if(piece2 instanceof Bishop) {
			gamestateCopy=utils.placePiece(new Bishop(piece1.getColor(),piece1.getPosition(),"Normal"), isDebug, gamestateCopy);
		}
		else if(piece2 instanceof Knight) {
			gamestateCopy=utils.placePiece(new Knight(piece1.getColor(),piece1.getPosition(),"Normal"), isDebug, gamestateCopy);
		}
		return gamestateCopy;
	}

}
