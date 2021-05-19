package CrazyChess.logic;

import CrazyChess.pieces.*;

import java.util.ArrayList;
/**
 * Class for implementing castling
 *
 * 
 * 
 * @author Andrei
 *
 */
public class Castle
{
	Utilities utils = new Utilities();
	ExtraChecksAndTools ect = new ExtraChecksAndTools();
	
	/**
	 * Checks if a player can castle
	 * @param p          King of the player
	 * @param xRel       relative movement of the King on the X axis
	 * @param yRel       relative movement of the King on the Y axis
	 * @param isDebug    is debug mode active
	 * @param gamestate  current gamestate
	 * @param moveNo     current move number
	 * @return           true if player is able to castle, false if not
	 */
	public boolean castleCheck(King p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(p.getWasMoved() == false) {
			if(xRel == 2 && yRel == 0) { //castle to the right
				//castle for white and rook not moved
				if(p.getColor().equalsIgnoreCase("white") && utils.getPiece(7,0,isDebug,gamestate) instanceof Rook && ((Rook)utils.getPiece(7,0,isDebug,gamestate)).getWasMoved() == false){
					//check if all the spaces are free
					if(utils.getPiece(5,0,isDebug,gamestate) instanceof BlankPiece && utils.getPiece(6,0,isDebug,gamestate) instanceof BlankPiece) {
						//sets color of blank spaces to white so that they can use the capturableBy function
						utils.getPiece(5,0,isDebug,gamestate).setColor("White");
						utils.getPiece(6,0,isDebug,gamestate).setColor("White");
						//check if the spaces the king passes trough are not attacked
						if(ect.capturableBy(utils.getPiece(5,0,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() && 
						   ect.capturableBy(utils.getPiece(6,0,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty()) {
							utils.getPiece(5,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(6,0,isDebug,gamestate).setColor("Blank");
							p.setCanCastle(1);
							return true;
						}else{
							utils.getPiece(5,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(6,0,isDebug,gamestate).setColor("Blank");
						}
					}
				}
				//castle for black and rook not moved
				if(p.getColor().equalsIgnoreCase("black") && utils.getPiece(7,7,isDebug,gamestate) instanceof Rook && ((Rook)utils.getPiece(7,7,isDebug,gamestate)).getWasMoved() == false){
					//check if all the spaces are free
					if(utils.getPiece(5,7,isDebug,gamestate) instanceof BlankPiece && utils.getPiece(6,7,isDebug,gamestate) instanceof BlankPiece) {
						//sets color of blank spaces to black so that they can use the capturableBy function
						utils.getPiece(5,7,isDebug,gamestate).setColor("Black");
						utils.getPiece(6,7,isDebug,gamestate).setColor("Black");
						//check if the spaces the king passes trough are not attacked and reset spaces to blank
						if(ect.capturableBy(utils.getPiece(5,7,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() && 
						   ect.capturableBy(utils.getPiece(6,7,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty()) {
							utils.getPiece(5,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(6,7,isDebug,gamestate).setColor("Blank");
							p.setCanCastle(2);
							return true;
						}else{
							//reset spaces to blank
							utils.getPiece(5,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(6,7,isDebug,gamestate).setColor("Blank");
						}
					}
				}
			}
			if(xRel == -2 && yRel == 0) { //castle to the left
				//castle for white and rook not moved
				if(p.getColor().equalsIgnoreCase("white") && utils.getPiece(0,0,isDebug,gamestate) instanceof Rook && ((Rook)utils.getPiece(0,0,isDebug,gamestate)).getWasMoved() == false){
					//check if all the spaces are free
					if(utils.getPiece(1,0,isDebug,gamestate) instanceof BlankPiece && 
					   utils.getPiece(2,0,isDebug,gamestate) instanceof BlankPiece && 
					   utils.getPiece(3,0,isDebug,gamestate) instanceof BlankPiece) {
						//sets color of blank spaces to white so that they can use the capturableBy function
						utils.getPiece(1,0,isDebug,gamestate).setColor("White");
						utils.getPiece(2,0,isDebug,gamestate).setColor("White");
						utils.getPiece(3,0,isDebug,gamestate).setColor("White");
						//check if the spaces the king passes trough are not attacked, reset spaces to blank and finish
						if(ect.capturableBy(utils.getPiece(1,0,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() && 
						   ect.capturableBy(utils.getPiece(2,0,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() &&
						   ect.capturableBy(utils.getPiece(3,0,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty()) {
							utils.getPiece(1,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(2,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(3,0,isDebug,gamestate).setColor("Blank");
							p.setCanCastle(3);
							return true;
						}else{
							//reset spaces to blank in case fail
							utils.getPiece(1,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(2,0,isDebug,gamestate).setColor("Blank");
							utils.getPiece(3,0,isDebug,gamestate).setColor("Blank");
						}
					}
				}	
				//castle for black and rook not moved
				if(p.getColor().equalsIgnoreCase("black") && utils.getPiece(0,7,isDebug,gamestate) instanceof Rook && ((Rook)utils.getPiece(0,7,isDebug,gamestate)).getWasMoved() == false){
					//check if all the spaces are free
					if(utils.getPiece(1,7,isDebug,gamestate) instanceof BlankPiece && 
					   utils.getPiece(2,7,isDebug,gamestate) instanceof BlankPiece && 
					   utils.getPiece(3,7,isDebug,gamestate) instanceof BlankPiece) {
						//sets color of blank spaces to white so that they can use the capturableBy function
						utils.getPiece(1,7,isDebug,gamestate).setColor("Black");
						utils.getPiece(2,7,isDebug,gamestate).setColor("Black");
						utils.getPiece(3,7,isDebug,gamestate).setColor("Black");
						//check if the spaces the king passes trough are not attacked, reset spaces to blank and finish
						if(ect.capturableBy(utils.getPiece(1,7,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() && 
						   ect.capturableBy(utils.getPiece(2,7,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty() &&
						   ect.capturableBy(utils.getPiece(3,7,isDebug,gamestate),isDebug,gamestate,moveNo).isEmpty()) {
							utils.getPiece(1,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(2,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(3,7,isDebug,gamestate).setColor("Blank");
							p.setCanCastle(4);
							return true;
						}else{
							//reset spaces to blank in case fail
							utils.getPiece(1,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(2,7,isDebug,gamestate).setColor("Blank");
							utils.getPiece(3,7,isDebug,gamestate).setColor("Blank");
						}
					}
				}	
			}
		}
		return false;
	}
	/**
	 * Actually performs the castling action
	 * @param p          King of the player
	 * @param xRel       relative movement of the King on the X axis
	 * @param yRel       relative movement of the King on the Y axis
	 * @param isDebug    is debug mode active
	 * @param gamestate  current gamestate
	 * @param moveNo     current move number
	 */
	public void castle(King p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate, int moveNo) {
		if (p.getCanCastle() == 1) {
			gamestate=utils.placePiece(new BlankPiece("Blank", 7, 0,"Normal"), isDebug, gamestate);
			gamestate=utils.placePiece(new Rook("White", 5, 0,"Normal"), isDebug, gamestate);
			((Rook)utils.getPiece(5,0,isDebug,gamestate)).setWasMoved(true);
			p.setWasMoved(true);
		}else if(p.getCanCastle() == 2) {
			gamestate=utils.placePiece(new BlankPiece("Blank", 7, 7,"Normal"), isDebug, gamestate);
			gamestate=utils.placePiece(new Rook("Black", 5, 7,"Normal"), isDebug, gamestate);
			((Rook)utils.getPiece(5,7,isDebug,gamestate)).setWasMoved(true);
			p.setWasMoved(true);
		}else if(p.getCanCastle() == 3) {
			gamestate=utils.placePiece(new BlankPiece("Blank", 0, 0,"Normal"), isDebug, gamestate);
			gamestate=utils.placePiece(new Rook("White", 3, 0,"Normal"), isDebug, gamestate);
			((Rook)utils.getPiece(3,0,isDebug,gamestate)).setWasMoved(true);
			p.setWasMoved(true);
		}else if(p.getCanCastle() == 4) {
			gamestate=utils.placePiece(new BlankPiece("Blank", 0, 7,"Normal"), isDebug, gamestate);
			gamestate=utils.placePiece(new Rook("Black", 3, 7,"Normal"), isDebug, gamestate);
			((Rook)utils.getPiece(3,7,isDebug,gamestate)).setWasMoved(true);
			p.setWasMoved(true);
		}else {
			System.out.println("Something went wrong, king can't castle");
		}
	}
}
