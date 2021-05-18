package CrazyChess.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.logic.powerups.PowerupMain;
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
	PowerupMain pwrUp;
	Castle cstl;

	// Default constructor
	public ExtraChecksAndTools() {
		pwrUp = new PowerupMain();
		cstl = new Castle();
	}

	// Constructor to prevent circular dependencies
	public ExtraChecksAndTools(int createCode) {
		if (createCode != 1) {
			pwrUp = new PowerupMain();
			cstl = new Castle();
		}
	}

	private int counter = -1;
	private Random r = new Random();
	
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
	 * Function that returns and ArrayList of blank pieces from
	 * a given game state
	 * @param gamestate    any game state
	 * @return An ArrayList of pieces in the game state
	 */
	
	public ArrayList<AbstractPiece> getBlankArrayList(AbstractPiece[][] gamestate){
		ArrayList<AbstractPiece> blank = new ArrayList<AbstractPiece>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if((gamestate[j][i] instanceof BlankPiece)) {
					blank.add(gamestate[j][i]);
				}
			}
		}
		
		return blank;
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
		if((defender.getColor().equalsIgnoreCase("white")||defender.getColor().equalsIgnoreCase("powerup")) && attacker.getColor().equalsIgnoreCase("black")) {
			return true;
		}
		else if((defender.getColor().equalsIgnoreCase("black")||defender.getColor().equalsIgnoreCase("powerup")) && attacker.getColor().equalsIgnoreCase("white")) {
			return true;
		}
		else if(defender.getColor().equalsIgnoreCase("blank")) {
			return true;
		}
		else
			System.out.println("no....");
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
		else if(target.getColor().equalsIgnoreCase("powerup")){
			for(int i = 0; i < blackPieces.size(); i++){
				if(canCapture(blackPieces.get(i), target, isDebug, gamestate, moveNo)){
					pieceList.add(blackPieces.get(i));
				}
			}
			for(int i = 0; i < whitePieces.size(); i++){
				if(canCapture(whitePieces.get(i), target, isDebug, gamestate, moveNo)){
					pieceList.add(whitePieces.get(i));
				}
			}
		}
		else {
			return null;
		}
		return pieceList;
	}
	
	/**
	 * Method to get a list of pieces that can capture a blank target
	 * @param target     The target piece
	 * @param isDebug    is debug mode active
	 * @param gamestate  game state currently to be examined
	 * @param moveNo	 current move number
	 * @return           ArrayList of pieces that can capture the target
	 */
	
	public ArrayList<AbstractPiece> BlankcapturableBy(AbstractPiece target, String color,boolean isDebug, AbstractPiece[][] gamestate, int moveNo){
		if(!utils.isOnBoard(target.getXpos(),target.getYpos()) || target == null) //checks if target has legal coordinates
			return null;
		ArrayList<AbstractPiece> pieceList = new ArrayList<AbstractPiece>();
		ArrayList<AbstractPiece> whitePieces = getWhitePieces(gamestate);
		ArrayList<AbstractPiece> blackPieces = getBlackPieces(gamestate);
		if(color.equalsIgnoreCase("black")){
			for(int i = 0; i < whitePieces.size(); i++){
				if(canCapture(whitePieces.get(i), target, isDebug, gamestate, moveNo)){
					pieceList.add(whitePieces.get(i));
				}
			}
		}
		else if(color.equalsIgnoreCase("white")){
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
		if(color.equalsIgnoreCase("white") && getWhitePieces(gamestate).size() > 0){
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
				AbstractPiece targetTile = utils.safeCopyPiece(gamestate[j][i]);
				if(targetTile instanceof HazardPiece){
					continue;
				}
				if(!(p.getXpos()==targetTile.getXpos()&&p.getYpos()==targetTile.getYpos())) {
					if(bvc.moveCheckAssigner(p, targetTile.getXpos()-p.getXpos(), targetTile.getYpos()-p.getYpos(), isDebug, gamestate, moveNo)||
					  ((p instanceof King)&&(cstl.castleCheck((King)p, targetTile.getXpos()-p.getXpos(), targetTile.getYpos()-p.getYpos(), isDebug, gamestate, moveNo)))) {
						if(!targetTile.getColor().equalsIgnoreCase(p.getColor())){ //checks if the candidate tile doesn't have a piece of the same color on it
							AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
							newGamestate=utils.relocatePiece(p, newGamestate, targetTile.getPosition());
							if(!isInCheck(p.getColor(), isDebug, newGamestate, moveNo)) {//check if the new possition doesn't put the player in check

								String oppColor = utils.oppositeColor(p.getColor());

								King enemyKing = getKing(oppColor, newGamestate);
								if(enemyKing == null){
									System.out.println("Opp Color : " + oppColor);
									System.out.println("enemy king is null");
									continue;
								}

								if(!targetTile.getPosition().equals(enemyKing.getPosition())) {
									//checks if the new position isn't an enemy king (because you can't capture kings)
									//If all checks pass, move is valid :)
									movesList.add(new Position(j, i));
								}
							}
						}
					}
				}
			}
		}
		return movesList;
	}
	
	
	/**
	 * This function returns all possible game states after one turn
	 * in the game state inputed with powerups
	 * 
	 * @param whoseTurn   string of a color of the player whose turn is currently happening
	 * @param isDebug     is debug mode active
	 * @param gamestate   starting game state
	 * @param moveNo      current move number
	 * @return            ArrayList of possible game states after the turn is completed with powerup
	 */
	
	public HashMap<AbstractPiece[][], Integer> possibleGamestatesAfterNextMove (String whoseTurn, boolean isDebug, AbstractPiece[][] gamestate, int moveNo, ArrayList<String> powerUps){
		String oppColor = utils.oppositeColor(whoseTurn);
		ArrayList<AbstractPiece> piecesToCheck = new ArrayList<AbstractPiece>();
		if(whoseTurn.equalsIgnoreCase("white")) {
			piecesToCheck = getWhitePieces(gamestate);
		} else {
			piecesToCheck = getBlackPieces(gamestate);
		}

		HashMap<AbstractPiece[][], Integer> listOfGamestates = new HashMap<AbstractPiece[][], Integer>();
		//System.out.println("Getting white pieces");
		ArrayList<AbstractPiece> whitePieces = getWhitePieces(gamestate);
		//System.out.println("Got the white pieces");
		for(AbstractPiece p : piecesToCheck) {
			// Regular game states
			ArrayList<Position> validPositions = validMoves(p, isDebug, gamestate, moveNo);
			for(Position vp : validPositions) {
				//generate gamestate for each one. Excluding moves where you capture enemy king
				AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
				if(!(vp.getXpos()==getKing(oppColor, newGamestate).getXpos() && vp.getYpos()==getKing(oppColor, newGamestate).getYpos())) {
					newGamestate=utils.relocatePiece(p, newGamestate, vp.getXpos(), vp.getYpos()); //might cause some bugs
					listOfGamestates.put(newGamestate, -1);
				}
			}

			// Powerup game states
			ArrayList<String> checkedPowerup = new ArrayList<String>();
			for (int i = 0; i < powerUps.size(); i++) {
				String pwrUpStr = powerUps.get(i);
				if (checkedPowerup.contains(pwrUpStr)) {
					continue;
				} else {
					checkedPowerup.add(pwrUpStr);
				}
				AbstractPiece[][] copiedGamestate = utils.safeCopyGamestate(gamestate);
				ArrayList<Position> validPowerupMoves = pwrUp.validPowerupMoves(pwrUpStr, copiedGamestate, p.getPosition(), isDebug);
				if (!validPowerupMoves.isEmpty()) {
					for (Position finalPos: validPowerupMoves) {
						AbstractPiece[][] modifiedGames = pwrUp.usePowerupGivenGamestate(pwrUpStr, copiedGamestate, p.getColor(), p.getPosition(), finalPos, isDebug);
						if (modifiedGames != null) {
							listOfGamestates.put(modifiedGames, i);
						}
					}
				}
			}
		}

		if (listOfGamestates == null) {
			if(isDebug) { //if the code is working, it should never reach this line of the method
				System.out.println("When trying to get possible gamestates null was returned. Something is very wrong");
			}
			return null;
		}
		return listOfGamestates;

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
	public boolean isInCheckmate(String color, boolean isDebug, AbstractPiece[][] gamestate, int moveNo, ArrayList<String> powerUps){
		
		boolean isMated = false;
		
		//just to be safe, check for a check
		if(isInCheck(color, isDebug, gamestate, moveNo)) {
			isMated=true;
			HashMap<AbstractPiece[][], Integer> nextMoves = possibleGamestatesAfterNextMove(color, isDebug, gamestate, moveNo, powerUps);
			for(AbstractPiece[][] g : nextMoves.keySet()) {
				if(!isInCheck(color, isDebug, g, moveNo)) {
					isMated =  false;
					//utils.printGameState(g);
				}
			}
		}
		
		return isMated;
	}

	/**
	 * This method checks if the game state is in draw
	 * @param currentTurn       color of the player the moves recently
	 * @param isDebug     is debug mode activated
	 * @param gamestate   game state to be examined
	 * @param moveNo      current move number
	 * @return            true if the game state is in draw, false if it still has possible moves
	 */
	public boolean isInDraw(String currentTurn, boolean isDebug, AbstractPiece[][] gamestate, int moveNo, ArrayList<String> powerUps){

		ArrayList<AbstractPiece> piecesToCheck;
		ArrayList<AbstractPiece> currentPieces = gamestateToPieceArrayList(gamestate);

		// Check if there are only kings left on the board
		if (currentPieces.size() == 2) {
			boolean isKings = true;
			for (AbstractPiece piece : currentPieces) {
				isKings = isKings && (piece instanceof King);
			}
			
			if (isKings) {
				return true;
			} else {
				System.out.println("Unexpected Game State: There is only one king on the board!");
			}
		}

		// Determine pieces to check against based on the last move
		if (currentTurn.equalsIgnoreCase("white")) {
			piecesToCheck = getBlackPieces(gamestate);
		} else {
			piecesToCheck = getWhitePieces(gamestate);
		}

		// Return true (draw) if there are no possible gamestate left
		HashMap<AbstractPiece[][], Integer> nextMoves = possibleGamestatesAfterNextMove(utils.oppositeColor(currentTurn), isDebug, gamestate, moveNo, powerUps);
		for (AbstractPiece[][] nextGamestate: nextMoves.keySet()) {
			if(isInCheck(currentTurn, isDebug, nextGamestate, moveNo)) {
				nextMoves.remove(gamestate);
			}
		}

		if (nextMoves.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * A method that randomly triggers rule change 1 (bishop-rook switch)
	 */
	
	public void updateRuleChange1() {
		if (!bvc.getBrs() && !bvc.getPS() && !bvc.getKS())
		{
			if (r.nextInt(20) == 0) //will modify this for the final game to happen less often
			{
				bvc.setBrs();
				counter = 2 + r.nextInt((6)/2) * 2; //random, between 2 and 8 turns
				System.out.println("Bishop-Rook switch rule change active. Remaining turns: " + counter);
			}
		}
		else if (bvc.getBrs())
		{
			if (counter == 1)
			{
				bvc.endBrs();
				counter = -1;
				System.out.println("Bishop-Rook switch rule change switched off.");
			}
			else
			{
				counter--;
				if(counter == 1) {
					System.out.println("Bishop-Rook switch rule change active. Last turn!");
				} else {
					System.out.println("Bishop-Rook switch rule change active. Remaining turns: " + counter);
				}
			}
		}
	}
	
	/**
	 * A method that randomly triggers rule change 2 (pawns can go backwards)
	 */
	
	public void updateRuleChange2() {
		if (!bvc.getBrs() && !bvc.getPS() && !bvc.getKS())
		{
			if (r.nextInt(20) == 0) //will modify this for the final game to happen less often
			{
				bvc.setPS();
				counter = 2 + r.nextInt((6)/2) * 2; //random, between 2 and 8 turns
				System.out.println("Pawns can go backwards. Remaining turns: " + counter);
			}
		}
		else if (bvc.getPS())
		{
			if (counter == 1)
			{
				bvc.endPS();
				counter = -1;
				System.out.println("Pawn rule change switched off.");
			}
			else
			{
				counter--;
				if(counter == 1) {
					System.out.println("Pawns can go backwards. Last turn!");
				} else {
					System.out.println("Pawns can go backwards. Remaining turns: " + counter);
				}
			}
		}
	}
	
	public void updateRuleChange3() {
		if (!bvc.getBrs() && !bvc.getPS() && !bvc.getKS())
		{
			if (r.nextInt(2) == 0) //will modify this for the final game to happen less often
			{
				bvc.setKS();
				counter = 2; //this rule change is very powerful so it shouldn't last longer than 2 turns
				System.out.println("Kings can move like Queens. Remaining turns: " + counter);
			}
		}
		else if (bvc.getKS())
		{
			if (counter == 1)
			{
				bvc.endKS();
				counter = -1;
				System.out.println("King rule change switched off.");
			}
			else
			{
				counter--;
				if(counter == 1) {
					System.out.println("Kings can move like Queens. Last turn!");
				} else {
					System.out.println("Kings can move like Queens. Remaining turns: " + counter);
				}
			}
		}
	}
	
	public boolean getBrs() {
		return bvc.getBrs();
	}
	
	public boolean getPS() {
		return bvc.getPS();
	}
	
	public boolean getKS() {
		return bvc.getKS();
	}
	
	public int getCounter() {
		return counter;
	}
	/**
	 * get the number of powerups piece on the board
	 */
	public int getPowerupNum(AbstractPiece[][] gamestate) {
		ArrayList<AbstractPiece> Gs = gamestateToPieceArrayList(gamestate);
		int num = 0;
		for(AbstractPiece s : Gs) {
			if (s instanceof Powerup) {
				num = num + 1;
			}
		}
		return num;
	}
}
