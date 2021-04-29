package CrazyChess.logic;

import CrazyChess.pieces.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class performs some basic checks to see if a
 * move on the board is at least somewhat valid.
 * i.e. doesn't go through other pieces, doesn't go off the board
 * 
 * @author Darius
 *
 */

public class BasicValidityChecker
{
	Utilities utils = new Utilities();
	private boolean brswitch;
	private boolean pawnswitch;
	private boolean kingswitch;
//	private int counter;
	
	/**
	 * A method that checks what validity checker method to use for the piece 
	 * @param p           input piece that is being checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode active
	 * @param gamestate   current game state
	 * @param moveNo      current move number
	 * @return            true if valid move, false if not
	 */
	public boolean moveCheckAssigner(AbstractPiece p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
	//	brs.setBrswitch();
	//	System.out.println("brswitch " + brswitch);
		if(p instanceof BlankPiece){
			if(isDebug)
				System.out.println("Blank spaces cannot be moved.");
			return false;
		}
		else if(p instanceof Pawn) {
//			if(isDebug&&!validityCheckPawn((Pawn)p, xRel, yRel, isDebug, gamestate, moveNo)) {
//				System.out.println("This pawn move is not valid (basicValidityChecker)");
//			}
			return validityCheckPawn((Pawn)p, xRel, yRel, isDebug, gamestate, moveNo);}
		else if(p instanceof King && kingswitch)
			return validityCheckKingAsQueen((King)p, xRel, yRel, isDebug, gamestate);
		else if(p instanceof King)
			return validityCheckKing((King)p, xRel, yRel, isDebug, gamestate);
		else if(p instanceof Queen)
			return validityCheckQueen((Queen)p, xRel, yRel, isDebug, gamestate);
		else if(p instanceof Knight)
			return validityCheckKnight((Knight)p, xRel, yRel, isDebug, gamestate);
		if(brswitch) { //if rulechange1 is active
			if(p instanceof Rook)
				return validityCheckRooktoBishop((Rook)p, xRel, yRel, isDebug, gamestate);
			else if(p instanceof Bishop)
				return validityCheckBishoptoRook((Bishop)p, xRel, yRel, isDebug, gamestate);
		}
		else if(!brswitch) { //regular rules
			if(p instanceof Rook)
				return validityCheckRook((Rook)p, xRel, yRel, isDebug, gamestate);
			else if(p instanceof Bishop)
				return validityCheckBishop((Bishop)p, xRel, yRel, isDebug, gamestate);
		}
		return false; // Something is very wrong
	}
	/**
	 * Return furthest possible tile if moving in a column or row.
	 * Further used when checking possible moves of rooks and queens
	 * @param x           x coordinate of the checked position
	 * @param y           y coordinate of the checked position
	 * @param dir         string representing direction: up, down, left, or right
	 * @param p           initial piece being checked, doesn't change, just used
	 * @param newPos      the new Position
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            the position of either end of check or right place
	 */
	public Position columnAndRowCheck(int x, int y, String dir, AbstractPiece p, Position newPos, boolean isDebug, AbstractPiece[][] gamestate ){
		if(dir == "up"){
			//Check for blank space
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x,y-1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				//right space found
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				if(!utils.getPiece(x,y, isDebug, gamestate).getColor().equalsIgnoreCase(p.getColor())){
					//capture an enemy piece
					return utils.getPiece(x,y, isDebug, gamestate).getPosition();
				}
				else{
					//Same color piece
					return utils.getPiece(x,y-1, isDebug, gamestate).getPosition();
				}
			}
			else{//if none of the conditions are met, the function recurses with the next tile in the 
				 //specified direction as input
				return columnAndRowCheck(x,y+1,"up",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "down"){
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x,y+1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				if(!utils.getPiece(x,y, isDebug, gamestate).getColor().equalsIgnoreCase(p.getColor())){
					return utils.getPiece(x,y, isDebug, gamestate).getPosition();
				}
				else{
					return utils.getPiece(x,y+1, isDebug, gamestate).getPosition();
				}
			}
			else{
				return columnAndRowCheck(x,y-1,"down",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "left"){
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x+1,y, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				if(!utils.getPiece(x,y, isDebug, gamestate).getColor().equalsIgnoreCase(p.getColor())){
					return utils.getPiece(x,y, isDebug, gamestate).getPosition();
				}
				else{
					return utils.getPiece(x+1,y, isDebug, gamestate).getPosition();
				}
			}
			else{
				return columnAndRowCheck(x-1,y,"left",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "right"){
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x-1,y, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				if(!utils.getPiece(x,y, isDebug, gamestate).getColor().equalsIgnoreCase(p.getColor())){
					return utils.getPiece(x,y, isDebug, gamestate).getPosition();
				}
				else{
					return utils.getPiece(x-1,y, isDebug, gamestate).getPosition();
				}
			}
			else{
				return columnAndRowCheck(x+1, y,"right",p, newPos, isDebug, gamestate);
			}
		}
		return null;
	}


	/**
	 * Returns furthest possible diagonal placement.
	 * Further used for bishops ands queens
	 * @param x           x coordinate of the checked position
	 * @param y           y coordinate of the checked position
	 * @param dir         diagonal directions as strings
	 * @param p           initial piece being checked, doesn't change, just used
	 * @param newPos      the new Position
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            the position of either end of check or right place
	 */
	public Position diagonalCheck(int x, int y, String dir, AbstractPiece p, Position newPos, boolean isDebug, AbstractPiece[][] gamestate){
		if(dir == "upleft"){
			//Checks for blank space
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x+1,y-1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				//right space found
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else{//if none of the conditions are met, the function recurses with the next tile in the 
				 //specified direction as input
				return diagonalCheck(x-1,y+1,"upleft",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "downleft"){
			//Checks for blank space
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x+1,y+1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				//right space found
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else{//if none of the conditions are met, the function recurses with the next tile in the 
				 //specified direction as input
				return diagonalCheck(x-1,y-1,"downleft",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "upright"){
			//Checks for blank space
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x-1,y-1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				//right space found
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else{//if none of the conditions are met, the function recurses with the next tile in the 
				 //specified direction as input
				return diagonalCheck(x+1,y+1,"upright",p, newPos, isDebug, gamestate);
			}
		}

		if(dir == "downright"){
			//Checks for blank space
			if( !utils.isOnBoard(x, y)){
				return utils.getPiece(x-1,y+1, isDebug, gamestate).getPosition();
			}
			else if(newPos.getXpos() == x && newPos.getYpos() == y){
				//right space found
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else if(!(utils.getPiece(x, y, isDebug, gamestate) instanceof BlankPiece)){
				return utils.getPiece(x,y, isDebug, gamestate).getPosition();
			}
			else{//if none of the conditions are met, the function recurses with the next tile in the 
				 //specified direction as input
				return diagonalCheck(x+1,y-1,"downright",p, newPos, isDebug, gamestate);
			}
		}
		return null;
	}


	/**
	 * Checking movement validity for pawns. 
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @param turnNo      number of the current turn
	 * @return            true if move is okay, false if not
	 */
	public boolean validityCheckPawn(Pawn p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate, int turnNo){
		if(p.getColor().equalsIgnoreCase("black")){
			//basic movement
			if(pawnswitch) { //if rulechange2 is active
				if(xRel == 0 && (yRel == -1 || yRel == 1) && !(utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("white")||
						utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
					return true;
			} else { //regular rules
				if(xRel == 0 && yRel == -1 && !(utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("white")||
						utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
					return true;
			}
			//first turn jump
			if(xRel == 0 && yRel == -2 && p.getYpos() == 6 && utils.getPiece(p.getXpos()+xRel, p.getYpos()+yRel, isDebug, gamestate).getColor().equalsIgnoreCase("blank")){
				p.setDoublejump(turnNo);
				return true;
			}
			//Checks En passant
			//Checks if on correct rank
			else if(p.getYpos()==3) {
				//checks if there is a pawn on the right
				if((utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate) instanceof Pawn)&&xRel == 1&&yRel==-1) {
					//checks if that pawn is of the enemy color
					if(utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate).getColor().equalsIgnoreCase("white")){
						Pawn enemyPawn = (Pawn) utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate);
						//checks if that pawn just moved
						if(turnNo-enemyPawn.getDoublejump()==1) {
							((Pawn)utils.getPiece(p.getXpos(),p.getYpos(), isDebug, gamestate)).setEnPassant(true);
							return true;
							}
						}
					}
				//checks if there is a pawn on the left
				if((utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate) instanceof Pawn)&&xRel == -1&&yRel==-1) {
					//checks if that pawn is of the enemy color
					if(utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate).getColor().equalsIgnoreCase("white")){
						Pawn enemyPawn = (Pawn) utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate);
						//checks if that pawn just moved
						if(turnNo-enemyPawn.getDoublejump()==1) {
							((Pawn)utils.getPiece(p.getXpos(),p.getYpos(), isDebug, gamestate)).setEnPassant(true);
							return true;
							}
						}
				}
			}
			//making sure piece about to get captured is of enemy color
			else if((xRel == 1 || xRel == -1) && yRel == -1 && (utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("white")||
															    utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
				return true;
		}
		else if(p.getColor().equalsIgnoreCase("white")){
			//basic advance
			if(pawnswitch) { //if rulechange2 is active
				if(xRel == 0 && (yRel == -1 || yRel == 1) && !(utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("black")||
						utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
					return true;
			} else { //regular rules
				if(xRel == 0 && yRel == 1 && !(utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("black")||
						utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
					return true;
			}
			
			//first turn jump
			if(xRel == 0 && yRel == 2 && p.getYpos() == 1 && utils.getPiece(p.getXpos()+xRel, p.getYpos()+yRel, isDebug, gamestate).getColor().equalsIgnoreCase("blank")){
				p.setDoublejump(turnNo);
				return true;
			}
			/*if(xRel == 0 && yRel == 2 && p.getYpos() == 1){
				p.setDoublejump(turnNo);
				return true;
			}*/
			//Checks En passant
			//Checks if on correct rank
			else if(p.getYpos()==4) {
				//checks if there is a pawn on the right
				if((utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate) instanceof Pawn)&&xRel == 1 && yRel==1) {
					//checks if that pawn is of the enemy color
					if(utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate).getColor().equalsIgnoreCase("black")){
						Pawn enemyPawn = (Pawn) utils.getPiece(p.getXpos()+1,p.getYpos(), isDebug, gamestate);
						//checks if that pawn just moved
						if(turnNo-enemyPawn.getDoublejump()==1) {
							((Pawn)utils.getPiece(p.getXpos(),p.getYpos(), isDebug, gamestate)).setEnPassant(true);
							return true;
							}
						}
					}
				//checks if there is a pawn on the left
				if((utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate) instanceof Pawn)&&xRel == -1 && yRel==1) {
					//checks if that pawn is of the enemy color
					if(utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate).getColor().equalsIgnoreCase("black")){
						Pawn enemyPawn = (Pawn) utils.getPiece(p.getXpos()-1,p.getYpos(), isDebug, gamestate);
						//checks if that pawn just moved
						if(turnNo-enemyPawn.getDoublejump()==1) {
							((Pawn)utils.getPiece(p.getXpos(),p.getYpos(), isDebug, gamestate)).setEnPassant(true);
							return true;
							}
						}
				}
			}
			//making sure piece about to get captured is of enemy color
			else if((xRel == 1 || xRel == -1) && yRel == 1 && (utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("black")||
															   utils.getTargetPiece(p,xRel,yRel,isDebug,gamestate).getColor().equalsIgnoreCase("powerup")))
				return true;
		}
		return false;
	}


	/**
	 * Checks if Rook's move is legal. Checks all legal tiles that
	 * the rook can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	public boolean validityCheckRook(Rook p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		AbstractPiece newPos = utils.getTargetPiece(p,xRel, yRel, isDebug, gamestate);
		if(columnAndRowCheck(p.getXpos(), p.getYpos()+1,"up", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos(), p.getYpos()-1,"down", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()-1, p.getYpos(),"left", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()+1, p.getYpos(),"right", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		//if(isDebug)
		//	System.out.println("Correct position for Rook not found.");
		return false;
	}


	/**
	 * Checks if Bishops's move is legal. Checks all legal tiles that
	 * the bishop can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	public boolean validityCheckBishop(Bishop p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		AbstractPiece newPos = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if(diagonalCheck(p.getXpos()-1, p.getYpos()+1, "upleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()-1, "downright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()-1, p.getYpos()-1, "downleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()+1, "upright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		return false;
	}


	/**
	 * Checks if King's move is legal. Checks all legal tiles that
	 * the king can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	public boolean validityCheckKing(King p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		//TODO: Add Castling
		
		if(xRel > 1 || xRel < -1 || yRel > 1 || yRel < -1){
			//if(isDebug)
			//	System.out.println("Bad move! King cannot move that far.");
			return false;
		}
		AbstractPiece newPos = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if(p.getXpos()+1==newPos.getXpos() && p.getYpos()+0==newPos.getYpos()){//(1,0)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()+1==newPos.getXpos() && p.getYpos()-1==newPos.getYpos()){//(1,-1)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()+0==newPos.getXpos() && p.getYpos()-1==newPos.getYpos()){//(0,-1)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()-1==newPos.getXpos() && p.getYpos()-1==newPos.getYpos()){//(-1,-1)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()-1==newPos.getXpos() && p.getYpos()+0==newPos.getYpos()){//(-1,0)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()-1==newPos.getXpos() && p.getYpos()+1==newPos.getYpos()){//(-1,1)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()+0==newPos.getXpos() && p.getYpos()+1==newPos.getYpos()){//(0,1)
			p.setWasMoved(true);
			return true;
		}
		else if(p.getXpos()+1==newPos.getXpos() && p.getYpos()+1==newPos.getYpos()){//(1,1)
			p.setWasMoved(true);
			return true;
		}
		return false;
	}


	/**
	 * Checks if Queen's move is legal. Checks all legal tiles that
	 * the queen can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	
	public boolean validityCheckQueen(Queen p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		AbstractPiece newPos = utils.getTargetPiece(p,xRel,yRel, isDebug, gamestate);
		if(diagonalCheck(p.getXpos()-1, p.getYpos()+1, "upleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()-1, "downright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()-1, p.getYpos()-1, "downleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()+1, "upright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos(), p.getYpos()+1, "up", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos(), p.getYpos()-1, "down", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()-1, p.getYpos(), "left", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()+1, p.getYpos(), "right", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		return false;
	}


	/**
	 * Checks if Knight's move is legal. Checks all legal tiles that
	 * the knight can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	public boolean validityCheckKnight(Knight p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		AbstractPiece newPos = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if(p.getXpos()+2==newPos.getXpos() && p.getYpos()+1==newPos.getYpos()){ //    (2,1)
			return true;
		}
		else if(p.getXpos()+2==newPos.getXpos() && p.getYpos()-1==newPos.getYpos()){//(2,-1)
			return true;
		}
		else if(p.getXpos()-2==newPos.getXpos() && p.getYpos()+1==newPos.getYpos()){//(-2,1)
			return true;
		}
		else if(p.getXpos()-2==newPos.getXpos() && p.getYpos()-1==newPos.getYpos()){//(-2,-1)
			return true;
		}
		else if(p.getXpos()+1==newPos.getXpos() && p.getYpos()+2==newPos.getYpos()){//(1,2)
			return true;
		}
		else if(p.getXpos()+1==newPos.getXpos() && p.getYpos()-2==newPos.getYpos()){//(1,-2)
			return true;
		}
		else if(p.getXpos()-1==newPos.getXpos() && p.getYpos()+2==newPos.getYpos()){//(-1,2)
			return true;
		}
		else if(p.getXpos()-1==newPos.getXpos() && p.getYpos()-2==newPos.getYpos()){//(-1,-2)
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if Bishop-switched-to-Rook's move is legal. Checks all legal tiles that the rook can move to and then checks if the candidate
	 * position is among them.
	 * 
	 * @param p
	 *            the piece to be checked
	 * @param xRel
	 *            the relative x movement from the piece
	 * @param yRel
	 *            the relative y movement from the piece
	 * @param isDebug
	 *            is debug mode activated
	 * @param gamestate
	 *            the current game state
	 * @return true if move is okay
	 */

	public boolean validityCheckBishoptoRook(Bishop p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate)
	{
		AbstractPiece newPos = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if (columnAndRowCheck(p.getXpos(), p.getYpos() + 1, "up", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (columnAndRowCheck(p.getXpos(), p.getYpos() - 1, "down", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (columnAndRowCheck(p.getXpos() - 1, p.getYpos(), "left", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (columnAndRowCheck(p.getXpos() + 1, p.getYpos(), "right", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		//if(isDebug)
		//	System.out.println("Correct position for Rook not found.");
		return false;
	}

	/**
	 * Checks if Rook-switched-to-Bishops's move is legal. Checks all legal tiles that the bishop can move to and then checks if the
	 * candidate position is among them.
	 * 
	 * @param p
	 *            the piece to be checked
	 * @param xRel
	 *            the relative x movement from the piece
	 * @param yRel
	 *            the relative y movement from the piece
	 * @param isDebug
	 *            is debug mode activated
	 * @param gamestate
	 *            the current game state
	 * @return true if move is okay
	 */

	public boolean validityCheckRooktoBishop(Rook p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate)
	{
		AbstractPiece newPos = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if (diagonalCheck(p.getXpos() - 1, p.getYpos() + 1, "upleft", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (diagonalCheck(p.getXpos() + 1, p.getYpos() - 1, "downright", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (diagonalCheck(p.getXpos() - 1, p.getYpos() - 1, "downleft", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		if (diagonalCheck(p.getXpos() + 1, p.getYpos() + 1, "upright", p, newPos.getPosition(), isDebug, gamestate)
				.equals(newPos.getPosition()))
			return true;
		return false;
	}
	
	/**
	 * Checks if King's move is legal if rulechange3 is active. Checks all legal tiles that
	 * the queen can move to and then checks if the candidate position is among them.
	 * @param p           the piece to be checked
	 * @param xRel        the relative x movement from the piece
	 * @param yRel        the relative y movement from the piece
	 * @param isDebug     is debug mode activated
	 * @param gamestate   the current game state
	 * @return            true if move is okay
	 */
	
	public boolean validityCheckKingAsQueen(King p, int xRel, int yRel, boolean isDebug, AbstractPiece[][] gamestate){
		AbstractPiece newPos = utils.getTargetPiece(p,xRel,yRel, isDebug, gamestate);
		if(diagonalCheck(p.getXpos()-1, p.getYpos()+1, "upleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()-1, "downright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()-1, p.getYpos()-1, "downleft", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(diagonalCheck(p.getXpos()+1, p.getYpos()+1, "upright", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos(), p.getYpos()+1, "up", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos(), p.getYpos()-1, "down", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()-1, p.getYpos(), "left", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		if(columnAndRowCheck(p.getXpos()+1, p.getYpos(), "right", p, newPos.getPosition(), isDebug, gamestate).equals(newPos.getPosition()))
			return true;
		return false;
	}
	
	public boolean getBrs()
	{
		return brswitch;
	}
	
	public void setBrs()
	{
		brswitch = true;
	}
	
	public void endBrs()
	{
		brswitch = false;
	}
	
	public boolean getPS()
	{
		return pawnswitch;
	}
	
	public void setPS()
	{
		pawnswitch = true;
	}
	
	public void endPS()
	{
		pawnswitch = false;
	}
	
	public boolean getKS()
	{
		return kingswitch;
	}
	
	public void setKS()
	{
		kingswitch = true;
	}
	
	public void endKS()
	{
		kingswitch = false;
	}
}

