package CrazyChess.logic.powerups;

import java.util.ArrayList;


import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.King;
import CrazyChess.pieces.Pawn;

public class PowerupBomb {
	Utilities utils = new Utilities();
	ExtraChecksAndTools ecat = new ExtraChecksAndTools();
	
	/**
	 * Turn one pawn into a bishop or knight on the board.
	 * @param gamestate    gamestate to be altered
	 * @param target1      Bomb piece
	 * @param target2      confirm the Bomb piece
	 * @param isDebug	   is debug mode active
	 * @return			   the altered gamestate, where target1 turn into target2. NULL if the bomb is not possible
	 */
	
	//include checks vs moving enemy pieces and powerups
	
	public AbstractPiece[][] bomb(AbstractPiece[][] gamestate, Position target1,Position target2, Boolean isDebug){
		
		
		if(!(utils.isOnBoard(target1))&&!(utils.isOnBoard(target2))) {
			if (isDebug) System.out.println("Target for Bomb is coordinate that is not on the board. Returning NULL");
			return null;
		}
		
		AbstractPiece gamestateCopy[][]=utils.safeCopyGamestate(gamestate);
		AbstractPiece piece1 = utils.getPiece(target1, isDebug, gamestateCopy);
		//AbstractPiece piece2 = utils.getPiece(target2, isDebug, gamestateCopy);
		
		ArrayList<AbstractPiece> Bx = ecat.gamestateToPieceArrayList(gamestate);
		for(AbstractPiece x : Bx) {
			if((x.getPoweruptype().equalsIgnoreCase("bomb"))) {
				if(piece1.getColor() == x.getColor()) {
					if (isDebug) System.out.println("Can have two or more bomb!");
					return null;
				}
			}
		}
		
		if(!(piece1 instanceof Pawn)) {
			if (isDebug) System.out.println("Can't add Bomb on a piece which is not pawn. Returning NULL");
			return null;
		}
		
		if((piece1.getColor().equalsIgnoreCase("blank"))) {
			if (isDebug) System.out.println("Can't add Bomb on the blank. Returning NULL");
			return null;
		}
		
		piece1.changePowerup("Bomb");
		gamestateCopy=utils.placePiece(piece1, isDebug, gamestateCopy);
		 
		
			
		return gamestateCopy;
	}
}
