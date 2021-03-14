package CrazyChess.logic;

import java.util.ArrayList;

import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.*;



/**
 * This is the main class for the chess game logic.
 * Ties everything to do with logic together.
 * 
 * @author Darius
 *
 */

public class MainLogic
{
	protected AbstractPiece gamestate[][]; //current gamestate
	protected String currentTurn;  //color of the player that is currently doing their turn
	protected int turnNo; // Current turn number
	protected boolean isDebug = true;  //is debug mode active (change this manually if needed)
	
	protected boolean isBlackChecked;  //boolean to show if the black player is under check
	protected boolean isBlackMated;    //boolean to show if the black player is mated
	
	protected boolean isWhiteChecked;  //boolean to show if the white player is under check
	protected boolean isWhiteMated;    //boolean to show if the white player is mated

	protected boolean isDraw;		   //boolean to show if the game is draw
	protected boolean isEndgame;       //boolean to show if the game is ended
	
	ArrayList<String> whitePowerUps = new ArrayList<String>();  //ArrayList to store white's powerups
	ArrayList<String> blackPowerUps = new ArrayList<String>();  //ArrayList tp store black's powerups
	
	Utilities utils = new Utilities();
	BasicValidityChecker bvc = new BasicValidityChecker();
	ExtraChecksAndTools ecat = new ExtraChecksAndTools();
	PowerupMain pwrUp = new PowerupMain();
	/**
	 * Constructor for the MainLogic class.
	 * Initiates the gamestate as an empty board.
	 * Initiates currentTurn with "white"
	 * Initiates turnNo with 1
	 */
	
	public MainLogic(){
		gamestate= new AbstractPiece[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				gamestate[i][j] = new BlankPiece("Blank", i, j);
			}
		}
		currentTurn = "White";
		turnNo = 1;
		
		isBlackChecked = false;
		isBlackMated = false;
		isWhiteChecked = false;
		isWhiteMated = false;
		isDraw = false;
		isEndgame = false;
	}
	
	/**
	 * Getter for debug mode.
	 *
	 * @return   true if debug mode is on, false if not
	 */
	public boolean isDebug(){
		return isDebug;
	}

	/**
	 * Method used to see if a player of specified color in
	 * currently under check
	 * @param color  color of the player to check
	 * @return       true if the specified player is in check, false if not
	 */

	public boolean getCheckStatus(String color) {
		if(color.equalsIgnoreCase("white")) {
			return isWhiteChecked;
		}else if(color.equalsIgnoreCase("black")) {
			return isBlackChecked;
		}
		if(isDebug) {
			System.out.println("Can't get check status for "+color);
		}
		return false;//ideally, should never trigger
	}
	/**
	 * Method used to see if a player of specified color in
	 * currently mated
	 * @param color  color of the player to check
	 * @return       true if the specified player is mated, false if not
	 */
	public boolean getMateStatus(String color) {
		if(color.equalsIgnoreCase("white")) {
			return isWhiteMated;
		}else if(color.equalsIgnoreCase("black")) {
			return isBlackMated;
		}
		if(isDebug) {
			System.out.println("Can't get check status for "+color);
		}
		return false;//ideally, should never trigger
	}
	
	
	/**
	 * Returns the color String of the current turn
	 * @return a string representing the turn
	 */
	public String getTurn(){
		return currentTurn;
	}
	
	/**
	 * Returns the current game state
	 * @return current game state
	 */
	public AbstractPiece[][] getGamestate (){
		return gamestate;
	}
	
	/**
	 * Changes the current game state.
	 * @param newGamestate    new gamestate
	 */
	void setGamestate(AbstractPiece[][] newGamestate){
		gamestate=newGamestate;
	}
	
	/**
	 * Changes the turn to the opposite of what it currently is. 
	 * If for some reason the turn is neither Black nor White
	 * by default the method sets it to white
	 */
	public void changeTurn(){
		if(currentTurn.equalsIgnoreCase("White")){
			currentTurn = "Black";
			turnNo++;
			gamestate=pwrUp.powerupSpawn(gamestate, turnNo, isDebug);
			if(isDebug)
				if(isDebug) {
					System.out.println("It is now Black's turn.");
					System.out.println("Black's powerups: "+blackPowerUps.toString());}
		}
		else{
			currentTurn = "White";
			turnNo++;
			gamestate=pwrUp.powerupSpawn(gamestate, turnNo, isDebug);
			
			if(isDebug) {
				System.out.println("It is now White's turn.");
				System.out.println("White's powerups: "+whitePowerUps.toString());}
		}
	}
	
	/**
	 * Places all pieces in proper starting position
	 */
	public void resetBoard(){
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){// first replaces all pieces with blank
				utils.placePiece(new BlankPiece("Blank", i, j), isDebug, gamestate);
			}
		}
		//Now place all pieces in starting positions
		for(int i = 0; i < 8; i++)
			utils.placePiece(new Pawn("White", i, 1), isDebug, gamestate);
		for(int j = 0; j < 8; j++)
			utils.placePiece(new Pawn("Black", j, 6), isDebug, gamestate);
		//0 y is black pieces, 7 y is white pieces
		utils.placePiece( new Rook("White",  0,0), isDebug, gamestate );
		utils.placePiece( new Rook("White",  7,0), isDebug, gamestate );
		utils.placePiece( new Rook("Black",  0,7), isDebug, gamestate );
		utils.placePiece( new Rook("Black",  7,7), isDebug, gamestate );

		utils.placePiece( new Knight("White",6,0), isDebug, gamestate );
		utils.placePiece( new Knight("White",1,0), isDebug, gamestate );
		utils.placePiece( new Knight("Black",6,7), isDebug, gamestate );
		utils.placePiece( new Knight("Black",1,7), isDebug, gamestate );

		utils.placePiece( new Bishop("White",5,0), isDebug, gamestate );
		utils.placePiece( new Bishop("White",2,0), isDebug, gamestate );
		utils.placePiece( new Bishop("Black",5,7), isDebug, gamestate );
		utils.placePiece( new Bishop("Black",2,7), isDebug, gamestate );

		utils.placePiece( new King("White",  4,0), isDebug, gamestate );
		utils.placePiece( new Queen("White", 3,0), isDebug, gamestate );

		utils.placePiece( new King("Black",  4,7), isDebug, gamestate );
		utils.placePiece( new Queen("Black", 3,7), isDebug, gamestate );
		
		//Code to show that usePowerup is working
//		whitePowerUps.add("Teleport");
//		usePowerup(0, new Position(0,0), new Position(5,7));
		
		//System.out.println("Possible teleports for Rook at (0,0): "+pwrUp.validPowerupMoves("teleport", gamestate, new Position(0,0), isDebug).toString());
		

	}
	
	/**
	 *This method defines a move. Takes all of the rules and restrictions
	 *into consideration. Moves a piece x squares right/left and
	 *y squares up/down. Further used in function moveTo
	 *
	 * @param p     the piece selected to be moved
	 * @param xRel  how far left or right to move
	 * @param yRel  how far up or down to move
	 * @return      true if move was successful, false if not
	 */
	

	protected boolean move(AbstractPiece p, int xRel, int yRel){
		if(p.getXpos() > 7 || p.getXpos() < 0 || p.getYpos() > 7 || p.getYpos() < 0 || p == null){ //Basic check to see if p is on board
			if(isDebug)
				System.out.println("Invalid piece position.");
			return false;
		}
		if(p instanceof BlankPiece){ //Checks if piece is blank
			if(isDebug)
				System.out.println("Bad move! You cannot move a blank space.");
			return false;
		}
		
		
//		System.out.println("Turn number: "+turnNo+". Available moves for "+currentTurn+": "+ecat.possibleGamestatesAfterNextMove(currentTurn, isDebug, gamestate, turnNo).size());
//		for(AbstractPiece[][] gs: ecat.possibleGamestatesAfterNextMove(currentTurn, isDebug, gamestate, turnNo)) {
//			utils.printGameState(gs);
//		}
		
		
		
		
		//Save old position (to place a blank later)
		Position oldPos = new Position(p.getXpos(), p.getYpos());
		//New position, using x and y to be relative
		AbstractPiece newPiece = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		if(oldPos == null || newPiece == null){
			if(isDebug)
				System.out.println("Invalid creation of pieces during move()");
			return false;
		}
		//Is it the same color?
		if(newPiece.getColor().equalsIgnoreCase(p.getColor())){
			if(isDebug)
				System.out.println("Bad move! Can't land on same color.");
			return false;
		}

//		if(!bvc.moveCheckAssigner(p, xRel, yRel, isDebug, gamestate, turnNo)){
//			if(isDebug)
//				System.out.println("Bad move! Illegal move for " + p.getClass().getSimpleName() + ".");
//			return false;
//		}
		
		//Checks the move validity
		ArrayList<Position> moveList = ecat.validMoves(p, isDebug, gamestate, turnNo);
		if(moveList.isEmpty()) {
			if(isDebug)
				System.out.println("Piece has no valid moves");
			return false;
		}
		boolean isValid=false;
		for(Position pos : moveList) {
			if(newPiece.getPosition().equals(pos)) {
				isValid=true;
			}
		}
		
//
//		if(newPiece instanceof King){
//			if(isDebug)
//				System.out.println("Bad move! Kings cannot be captured.");
//			return false;
//		}
		
		//sets up new potential gamestate
//		AbstractPiece pp = utils.safeCopyPiece(p);
//		pp.setPosition(newPiece.getPosition());
//		AbstractPiece[][] newGamestate=utils.relocatePiece(pp, utils.safeCopyGamestate(gamestate), newPiece.getPosition());
//		
		//if king was under check, it checks if the king escaped check in the new gamestate
//		if(isWhiteChecked&&currentTurn.equalsIgnoreCase("white")) {
//			if(ecat.isInCheck(currentTurn, isDebug, newGamestate, turnNo)) {
//				if(isDebug) {
//					System.out.println("Invalid move: King is still under check");
//				}
//				pp.setPosition(oldPos); //resets piece's position because the move is invalid
//				return false;
//			}
//		}
//		if(isBlackChecked&&currentTurn.equalsIgnoreCase("black")) {
//			if(ecat.isInCheck(currentTurn, isDebug, newGamestate, turnNo)) {
//				if(isDebug) {
//					System.out.println("Invalid move: King is still under check");
//				}
//				pp.setPosition(oldPos); //resets piece's position because the move is invalid
//				return false;
//			}
//		}
//		
//		//checks if the new gamestate will be a checkmate for the oponent
//		if(currentTurn.equalsIgnoreCase("white")) {
//			if(ecat.isInCheckmate("black", isDebug, newGamestate, turnNo)) {
//				if(isDebug) {
//					System.out.println("White checkmated black");
//				}
//				isBlackMated=true;
//			}
//		}
//		if(currentTurn.equalsIgnoreCase("black")) {
//			if(ecat.isInCheckmate("white", isDebug, newGamestate, turnNo)) {
//				if(isDebug) {
//					System.out.println("Black checkmated white");
//				}
//				isWhiteMated=true;
//			}
//		}
		
		//All checks passed, update the gamestate
//		gamestate=newGamestate;
		
		
		//Everything checks out, so set the piece's position anew
		//p.setPosition(newPiece.getXpos(), newPiece.getYpos());
		//gamestate=utils.placePiece(p, isDebug, gamestate);//place it according to the new position
		//and set the old position to a Blank place
		//gamestate=utils.placePiece(new BlankPiece("Blank",oldPos.getXpos(), oldPos.getYpos()), isDebug, gamestate);
		
		//Constructing new possible gamestate
		if(isValid) {
			AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
			AbstractPiece copiedPiece = utils.getPiece(p.getPosition(), isDebug, newGamestate);
			copiedPiece.setPosition(newPiece.getXpos(), newPiece.getYpos());
			newGamestate=utils.placePiece(copiedPiece, isDebug, newGamestate);//place it according to the new position
			//and set the old position to a Blank place
			newGamestate=utils.placePiece(new BlankPiece("Blank",oldPos.getXpos(), oldPos.getYpos()), isDebug, newGamestate);
		
		
		
		
		
		
		//if king was under check, it checks if the king escaped check in the new gamestate
//				if(isWhiteChecked&&currentTurn.equalsIgnoreCase("white")) {
//					if(ecat.isInCheck(currentTurn, isDebug, newGamestate, turnNo+1)) {
//						if(isDebug) {
//							System.out.println("Invalid move: King is still under check");
//						}
//						copiedPiece.setPosition(oldPos);//resets piece's position because the move is invalid(?)
//						gamestate=newGamestate;
//						return false;
//					}
//				}
//				if(isBlackChecked&&currentTurn.equalsIgnoreCase("black")) {
//					if(ecat.isInCheck(currentTurn, isDebug, newGamestate, turnNo+1)) {
//						if(isDebug) {
//							System.out.println("Invalid move: King is still under check");
//						}
//						copiedPiece.setPosition(oldPos);//resets piece's position because the move is invalid(?)
//						gamestate=newGamestate;
//						return false;
//					}
//				}
		
		
			//Check if the player is not under check
			if(currentTurn.equalsIgnoreCase("white")) {
				if(ecat.isInCheck("black", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("Black king is now checked!");
						isBlackChecked = true;
					}
				}
			}
			if(currentTurn.equalsIgnoreCase("black")) {
				if(ecat.isInCheck("white", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("White king is now checked!");
						isWhiteChecked = true;
					}
				}
			}

			//checks if the new gamestate will be a checkmate for the oponent
			if(currentTurn.equalsIgnoreCase("white")) {
				if(ecat.isInCheckmate("black", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("White checkmated black");
					}
					isBlackMated = true;
					isEndgame = true;
				}else if(isBlackChecked) System.out.println(ecat.validMoves(ecat.getKing("black", newGamestate), isDebug, newGamestate, turnNo).size());
			}
			if(currentTurn.equalsIgnoreCase("black")) {
				if(ecat.isInCheckmate("white", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("Black checkmated white");
					}
					isWhiteMated = true;
					isEndgame = true;
				}else if(isWhiteChecked) System.out.println(ecat.validMoves(ecat.getKing("white", newGamestate), isDebug, newGamestate, turnNo).size());
			}

			// Check if the new gamestate will be a draw
			if (ecat.isInDraw(currentTurn, isDebug, newGamestate, turnNo+1) && !isEndgame) {
				if (isDebug) {
					System.out.println("The game resulted in a draw");
				}
				isDraw = true;
				isEndgame = true;
			}

			gamestate = newGamestate;
			
			if(newPiece instanceof Powerup) {
				if(currentTurn.equalsIgnoreCase("white")) whitePowerUps.add(pwrUp.randomPowerup(isDebug));
				if(currentTurn.equalsIgnoreCase("black")) blackPowerUps.add(pwrUp.randomPowerup(isDebug));
			}
			
			return true;
		}
		return false;
	}


	/**
	 * Attempts to move a piece to the set parameters. Returns true if success.
	 *
	 * @param p  the piece selected to be moved
	 * @param x  x position to attempt to move to
	 * @param y  y position to attempt to move to
	 * @return   true if move was successful, false if not
	 */
	public boolean moveTo(AbstractPiece p, int x, int y){
		if(!utils.isOnBoard(x,y) || p == null)
			return false;
		int relX = x - p.getXpos();
		int relY = y - p.getYpos();
		System.out.println("RelX: "+ relX + " RelY: "+ relY);
	//	if(!utils.isOnBoard(relX,relY))
	//		return false;
		return move(p, relX, relY);
	}


	/**
	 * Basically just a neat little wrapper for getPiece method in 
	 * CrazyChess.logic.Utilities
	 * @param pos   target position
	 * @return The piece in the specified position
	 */
	
	public AbstractPiece getPiece(Position pos) {
		return utils.getPiece(pos, isDebug, gamestate);
	}
	
	/**
	 * Just a wrapper for the printGameState method in the
	 * Utilities class
	 * 
	 */
	public void printGameState() {
		utils.printGameState(gamestate);
	}
	
	/**
	 * Method for using powerups. Returns true if the use of the powerup process was successful, false if not.
	 * If using a powerup was successful, it will also alter the current gamestate.
	 * @param powerupIndex    index (in whitePowerUps or blackPowerUps) of the powerup to be used 
	 * @param target1         position of the first piece to be used in the powerup
	 * @param target2         position of the second piece to be used in the powerup (can be NULL)
	 * @return                true if the use of the powerup process was successful, false if not
	 */
	public boolean usePowerup(int powerupIndex, Position target1, Position target2) {
		
		ArrayList<String> listToUse=null;
		if(currentTurn.equalsIgnoreCase("white")) {
			listToUse=whitePowerUps;
		}else listToUse=blackPowerUps;
		
		AbstractPiece[][] copiedGamestate = utils.safeCopyGamestate(gamestate);
		
		AbstractPiece[][] gamestateAfterPowerup = pwrUp.powerupAssigner(listToUse.get(powerupIndex).toLowerCase(), copiedGamestate, target1, target2, turnNo, currentTurn, isDebug);
		
		if(gamestateAfterPowerup!=null) {
			if (isDebug) System.out.println(currentTurn+" just used a powerup: "+listToUse.get(powerupIndex));
			gamestate=gamestateAfterPowerup;
			listToUse.remove(powerupIndex);
			return true;
		}
		
		return false;
	}
	
}
