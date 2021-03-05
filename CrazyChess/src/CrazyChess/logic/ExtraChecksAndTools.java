package CrazyChess.logic;

import java.util.ArrayList;
import CrazyChess.pieces.*;





/**
 * This class is used to perform some extra checks and to provide
 * some extra tools that might be useful further in the project.
 * This class might be very useful for people making the AI 
 * because of methods like canCapture, capturableBy and
 * possibleGamestatesAfterNextMove.
 * This class also holds methods for checking for checks
 * and checkmates. (should probably split it up in the future)
 * 
 * 
 * @author Darius
 *
 */

public class ExtraChecksAndTools
{
	BasicValidityChecker bvc = new BasicValidityChecker();
	Utilities utils = new Utilities();
	
	/**
	 * Function that returns and ArrayList of pieces from
	 * a given game state
	 * @param gamestate    any game state
	 * @return An ArrayList of pieces in the game state
	 */
	
	public ArrayList<AbstractPiece> gamestateToPieceArrayList(AbstractPiece[][] gamestate){
		ArrayList<AbstractPiece> temp = new ArrayList<AbstractPiece>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(!(gamestate[j][i] instanceof BlankPiece)) {
					temp.add(gamestate[j][i]);
				}
			}
		}
		
		return temp;
	}
	/**
	 * Function that returns and ArrayList of white pieces from
	 * a given game state
	 * @param gamestate    any game state
	 * @return An ArrayList of white pieces in the game state
	 */
	public ArrayList<AbstractPiece> getWhitePieces(AbstractPiece[][] gamestate){
		
		ArrayList<AbstractPiece> allPieces = gamestateToPieceArrayList(gamestate);
		ArrayList<AbstractPiece> whitePieces = new ArrayList<AbstractPiece>();
		
		for(AbstractPiece p : allPieces) {
			if(p.getColor().equalsIgnoreCase("white")) {
				whitePieces.add(p);
			}
		}
		
		return whitePieces;
	}
	/**
	 * Function that returns and ArrayList of black pieces from
	 * a given game state
	 * @param gamestate    any game state
	 * @return An ArrayList of black pieces in the game state
	 */
	public ArrayList<AbstractPiece> getBlackPieces(AbstractPiece[][] gamestate){
		
		ArrayList<AbstractPiece> allPieces = gamestateToPieceArrayList(gamestate);
		ArrayList<AbstractPiece> blackPieces = new ArrayList<AbstractPiece>();
		
		for(AbstractPiece p : allPieces) {
			//System.out.println(p);
			if(p.getColor().equalsIgnoreCase("black")) {
				blackPieces.add(p);
			}
		}
		
		return blackPieces;
	}
	/**
	 * This method checks if piece p is a part of the inputed game state
	 * @param p           the piece in question
 	 * @param gamestate   the game state to be examined
	 * @return            true if the piece is a part of the game state, false if not 
	 */
	
	public boolean isPieceOnBoard(AbstractPiece p, AbstractPiece[][] gamestate) {
		
		ArrayList<AbstractPiece> allPieces = gamestateToPieceArrayList(gamestate);
		
		if(allPieces.contains(p)) {
			return true;
		}else return false;
	}
	
	/**
	 * A checker for whether or not the selected piece can move.
	 * Does not account for uncovered checks.
	 * @param p           the selected piece
	 * @param isDebug     is debug mode active
	 * @param gamestate   game state to be curently examined
	 * @param moveNo      current move number
	 * @return            true if the piece can move, false if not
	 */
	
	public boolean canMove(AbstractPiece p, boolean isDebug, AbstractPiece[][] gamestate, int moveNo) {
		if(!isPieceOnBoard(p, gamestate))
			return false;
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				AbstractPiece targetTile = gamestate[i][j];
				if(!(p.getXpos()==targetTile.getXpos()&&p.getYpos()==targetTile.getYpos())) {
					if(bvc.moveCheckAssigner(p, targetTile.getXpos()-p.getXpos(), targetTile.getYpos()-p.getYpos(), isDebug, gamestate, moveNo)){
						if(!targetTile.getColor().equalsIgnoreCase(p.getColor())){ //checks if the candidate tile doesn't have a piece of the same color on it
							return true;
						}
				}
				
			}
		}
	  }
		return false;
	}
	
	
	/**
	 * Checks if the inputed attacker piece can capture the
	 * inputed defender piece
	 * @param attacker    attacking piece
	 * @param defender    defending piece
	 * @param isDebug     is debug mode active
	 * @param gamestate   game state to be currently examined
	 * @param moveNo      current move number
	 * @return            true if attacker can capture a piece at location, false if not
	 */
	public boolean canCapture(AbstractPiece attacker, AbstractPiece defender, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(canMove(attacker, isDebug, gamestate, moveNo) == false || !utils.isOnBoard(defender.getPosition())) //primary basic checks 
			return false;
		// Do move check
		if(!bvc.moveCheckAssigner(attacker, defender.getXpos() - attacker.getXpos(), defender.getYpos() - attacker.getYpos(), isDebug, gamestate, moveNo)){
			return false;
		}//the 2 if statements bellow check if attacker and defender are different colors
		if(defender.getColor().equalsIgnoreCase("white") && attacker.getColor().equalsIgnoreCase("black")) {
			return true;
		}
		else if(defender.getColor().equalsIgnoreCase("black") && attacker.getColor().equalsIgnoreCase("white")) {
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Method to get a list of pieces that can capture a certain target
	 * @param target     The target piece
	 * @param isDebug    is debug mode active
	 * @param gamestate  game state currently to be examined
	 * @param moveNo	 current move number
	 * @return           ArrayList of pieces that can capture the target
	 */
	
	public ArrayList<AbstractPiece> capturableBy(AbstractPiece target, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(!utils.isOnBoard(target.getXpos(),target.getYpos()) || target == null) //checks if target has legal coordinates
			return null;
		ArrayList<AbstractPiece> pieceList = new ArrayList<AbstractPiece>();
		ArrayList<AbstractPiece> whitePieces = getWhitePieces(gamestate);
		ArrayList<AbstractPiece> blackPieces = getBlackPieces(gamestate);
		if(target.getColor().equalsIgnoreCase("black")){
			for(int i = 0; i < whitePieces.size(); i++){
				if(canCapture(whitePieces.get(i), target, isDebug, gamestate, moveNo)){
					pieceList.add(whitePieces.get(i));
				}
			}
		}
		else if(target.getColor().equalsIgnoreCase("white")){
			for(int i = 0; i < blackPieces.size(); i++){
				if(canCapture(blackPieces.get(i), target, isDebug, gamestate, moveNo)){
					pieceList.add(blackPieces.get(i));
				}
			}
		}
		else {
			return null;
		}
		return pieceList;
	}
	
	/**
	 * Returns the King object of the specified color
	 * @param color       string of the color of king needed
	 * @param gamestate   game state currently to be examined
	 * @return            king of the specified color
	 */
	
	public King getKing(String color, AbstractPiece[][] gamestate){
		if(color.equalsIgnoreCase("black") && getBlackPieces(gamestate).size() > 0){
			for(AbstractPiece p : getBlackPieces(gamestate)){
				if(p instanceof King){
					return (King)p;
				}
			}
		}
		if(color.equalsIgnoreCase("white") && getBlackPieces(gamestate).size() > 0){
			for(AbstractPiece p : getWhitePieces(gamestate)){
				if(p instanceof King){
					return (King)p;
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Checks if the king of the specified color is in check in
	 * the game state provided
	 * @param color       color of the king to be examined
	 * @param isDebug     is debug mode active
	 * @param gamestate   game state to be currently examined
	 * @param moveNo      current move number
	 * @return            true if the king is in check, false if not 
	 */
	public boolean isInCheck(String color, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(color.equalsIgnoreCase("black")){
//			for(int i = 0; i < getWhitePieces(gamestate).size(); i++){
//				King k = getKing("black", gamestate);
//				if(canCapture(getWhitePieces(gamestate).get(i),k, isDebug, gamestate, moveNo))
//					return true;
//			}
			King k = getKing("black", gamestate);
			if(!capturableBy(k, isDebug, gamestate, moveNo).isEmpty()) {
				return true;
			}
		}
		else if(color.equalsIgnoreCase("white")){
//			for(int i = 0; i < getBlackPieces(gamestate).size(); i++){ //why does the i++ come up as "Dead code"????
//				King k = getKing("white", gamestate);
//				if(canCapture(getBlackPieces(gamestate).get(i),k, isDebug, gamestate, moveNo));
//					return true;
//			}
			King k = getKing("white", gamestate);
			if(!capturableBy(k, isDebug, gamestate, moveNo).isEmpty()) {
				return true;
			}
			
		}
		return false;
	}
	
	/**
	 * Returns a list of moves that a piece p can make
	 * Does not account for checks and/or uncovered checks
	 * @param p          piece to be examined
	 * @param isDebug	 is debug mode activated
	 * @param gamestate  game state to be examined
	 * @param moveNo     current move number
	 * @return         	 ArrayList of Positions a piece can go to
	 */
	
	public ArrayList<Position> validMoves( AbstractPiece p, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		ArrayList<Position> movesList = new ArrayList<Position>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				AbstractPiece targetTile = gamestate[j][i];
				if(!(p.getXpos()==targetTile.getXpos()&&p.getYpos()==targetTile.getYpos())) {
					if(bvc.moveCheckAssigner(p, targetTile.getXpos()-p.getXpos(), targetTile.getYpos()-p.getYpos(), isDebug, gamestate, moveNo)) {
						if(!targetTile.getColor().equalsIgnoreCase(p.getColor())){ //checks if the candidate tile doesn't have a piece of the same color on it
							movesList.add(new Position(j, i));
						}
					}
				}
			}
		}
		
		return movesList;
	}
	
	
	/**
	 * This function returns all possible game states after one turn
	 * in the game state inputed
	 * 
	 * @param whoseTurn   string of a color of the player whose turn is currently happening
	 * @param isDebug     is debug mode active
	 * @param gamestate   starting game state
	 * @param moveNo      current move number
	 * @return            ArrayList of possible game states after the turn is completed
	 */
	
	public ArrayList<AbstractPiece[][]> possibleGamestatesAfterNextMove (String whoseTurn, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(whoseTurn.equalsIgnoreCase("white")) {
			ArrayList<AbstractPiece[][]> listOfGamestates = new ArrayList<AbstractPiece[][]>();
			//System.out.println("Getting white pieces");
			ArrayList<AbstractPiece> whitePieces = getWhitePieces(gamestate);
			//System.out.println("Got the white pieces");
			for(AbstractPiece p : whitePieces) {
				ArrayList<Position> validPositions = validMoves(p, isDebug, gamestate, moveNo);
				for(Position vp : validPositions) {
					//generate gamestate for each one. Excluding moves where you capture enemy king
					AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
					if(!(vp.getXpos()==getKing("black", newGamestate).getXpos()&&vp.getYpos()==getKing("black", newGamestate).getXpos())) {
						newGamestate=utils.relocatePiece(p, newGamestate, vp.getXpos(), vp.getYpos()); //might cause some bugs
						listOfGamestates.add(newGamestate);
					}
					
				}
			}
			return listOfGamestates;
		}
		else if (whoseTurn.equalsIgnoreCase("black")) {
			ArrayList<AbstractPiece[][]> listOfGamestates = new ArrayList<AbstractPiece[][]>();
			ArrayList<AbstractPiece> blackPieces = getBlackPieces(gamestate);
			for(AbstractPiece p : blackPieces) {
				ArrayList<Position> validPositions = validMoves(p, isDebug, gamestate, moveNo);
				for(Position vp : validPositions) {
					//generate gamestate for each one. Excluding moves where you capture enemy king
					AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
					if(!(vp.getXpos()==getKing("white", newGamestate).getXpos()&&vp.getYpos()==getKing("white", newGamestate).getXpos())) {
						newGamestate=utils.relocatePiece(p, newGamestate, vp.getXpos(), vp.getYpos()); //might cause some bugs
						listOfGamestates.add(newGamestate);
					}
					
				}
			}
			return listOfGamestates;
		}
		if(isDebug) { //if the code is working, it should never reach this line of the method
			System.out.println("When trying to get possible gamestates null was returned. Something is very wrong");
		}
		
		return null;
	}
	
	/**
	 * This method checks if the player of specified color
	 * is in checkmate in the current game state
	 * @param color       color of the player being examined for checkmate
	 * @param isDebug     is debug mode activated
	 * @param gamestate   game state to be examined
	 * @param moveNo      current move number
	 * @return            true if the player is in checkmate, false if they still have possible moves
	 */
	public boolean isInCheckmate(String color, boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		
		boolean isMated = false;
		
		//just to be safe, check for a check
		if(isInCheck(color, isDebug, gamestate, moveNo)) {
			isMated=true;
			ArrayList<AbstractPiece[][]> nextMoveGamestates = possibleGamestatesAfterNextMove(color, isDebug, gamestate, moveNo);
			for(AbstractPiece[][] g : nextMoveGamestates) {
				if(!isInCheck(color, isDebug, g, moveNo)) {
					isMated =  false;
				}
			}
		}
		
		return isMated;
	}
}
