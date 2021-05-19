package CrazyChess.logic;

import java.util.ArrayList;

import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardAssigner;
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

	protected boolean isDrawAsked;		//boolean to show if a draw was asked
	protected boolean isDraw;		   //boolean to show if the game is draw
	protected boolean isEndgame;       //boolean to show if the game is ended
	

	protected boolean rulechange1;     //boolean to show if rule change 1 was selected to play with
	protected boolean rulechange2;     //boolean to show if rule change 1 was selected to play with
	protected boolean rulechange3;     //boolean to show if rule change 1 was selected to play with
	
	//for bomb limit
	protected int WBlt;
	protected int BBlt;
	protected int WB;
	protected int BB;
	
	ArrayList<String> whitePowerUps = new ArrayList<String>();  //ArrayList to store white's powerups
	ArrayList<String> blackPowerUps = new ArrayList<String>();  //ArrayList tp store black's powerups
	
	Utilities utils = new Utilities();
//	BasicValidityChecker bvc = new BasicValidityChecker();
	private ExtraChecksAndTools ecat = new ExtraChecksAndTools(); //todo getter setter
	PowerupMain pwrUp = new PowerupMain();
	Castle cstl = new Castle();
//	BishopRookSwitch brs = new BishopRookSwitch();

	HazardAssigner hazards = new HazardAssigner();
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
				gamestate[i][j] = new BlankPiece("Blank", i, j,"Normal");
			}
		}
		currentTurn = "White";
		turnNo = 1;
		WBlt = 0;
		BBlt = 0;
		BB = 0;
		WB = 0;
		
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
//	??????????????????
	public void setCurrentTurnColor(String currentTurn) {
		this.currentTurn = currentTurn;
	}
	public void setCurrentTurn(int currentTurn) {
		this.turnNo = currentTurn;
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
	public void setGamestate(AbstractPiece[][] newGamestate){
		gamestate=newGamestate;
		String oppColor = utils.oppositeColor(getTurn());
		if(ecat.isInCheck(oppColor,false,newGamestate,turnNo)){
			if(oppColor.equalsIgnoreCase("black")){
				isBlackChecked = true;
			}else{
				isWhiteChecked = true;
			}
		}else{
			if(oppColor.equalsIgnoreCase("black")){
				isBlackChecked = false;
			}else{
				isWhiteChecked = false;
			}
		}
		if(ecat.isInCheckmate(oppColor,false,newGamestate,turnNo+1, getPowerUps(currentTurn))){
			if(oppColor.equalsIgnoreCase("black")){
				isBlackMated = true;
			}else{
				isWhiteMated = true;
			}
			isEndgame = true;
		}
		if (ecat.isInDraw(currentTurn, isDebug, newGamestate, turnNo+1, getPowerUps(currentTurn)) && !isEndgame) {
			if (isDebug) {
				System.out.println("The game resulted in a draw");
			}
			isDraw = true;
			isEndgame = true;
		}
	}
	
	/**
	 * Changes the turn to the opposite of what it currently is. 
	 * If for some reason the turn is neither Black nor White
	 * by default the method sets it to white
	 */
	public void changeTurn(){
		
		
		if(currentTurn.equalsIgnoreCase("White")){
			currentTurn = "Black";
			
			//set bomb limit 
			ArrayList<AbstractPiece> Bx = ecat.gamestateToPieceArrayList(gamestate);
			for(AbstractPiece x : Bx) {
				if((x.getPoweruptype().equalsIgnoreCase("bomb"))) {
					if(x.getColor().equalsIgnoreCase("white")&&(WBlt == 0)) {
						WBlt = turnNo;
					}
					else if(x.getColor().equalsIgnoreCase("black")&&(BBlt == 0)) {
						BBlt = turnNo;
					}
				}
			}
			
			//change turn number
			turnNo++;
			
			//check bomb limit
			for(AbstractPiece x : Bx) {
				if((x.getPoweruptype().equalsIgnoreCase("bomb"))) {
					if(x.getColor().equalsIgnoreCase("white")&&(turnNo - WBlt >= 5)) {
						System.out.println("Bomb!!!");
				        if(utils.isOnBoard(x.getXpos() -1, x.getYpos() -1)&&!((utils.getPiece(x.getXpos() -1, x.getYpos() -1, isDebug,gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() -1,x.getYpos())&&!((utils.getPiece(x.getXpos() -1, x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() , x.getYpos() -1)&&!((utils.getPiece(x.getXpos() , x.getYpos() -1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos(), x.getYpos())&&!((utils.getPiece(x.getXpos() , x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() -1)&&!((utils.getPiece(x.getXpos() +1, x.getYpos() -1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() -1, x.getYpos() +1)&&!((utils.getPiece(x.getXpos() -1, x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos() +1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() +1)&&!((utils.getPiece(x.getXpos() +1, x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos() +1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() )&&!((utils.getPiece(x.getXpos() +1, x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() , x.getYpos() +1)&&!((utils.getPiece(x.getXpos() , x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos() +1,"Normal"), isDebug, gamestate);
				        WBlt = 0;
					}
					else if(x.getColor().equalsIgnoreCase("black")&&(turnNo - BBlt >= 5)) {
						System.out.println("Bomb!!!");
						if(utils.isOnBoard(x.getXpos() -1, x.getYpos() -1)&&!((utils.getPiece(x.getXpos() -1, x.getYpos() -1, isDebug,gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() -1,x.getYpos())&&!((utils.getPiece(x.getXpos() -1, x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() , x.getYpos() -1)&&!((utils.getPiece(x.getXpos() , x.getYpos() -1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos(), x.getYpos())&&!((utils.getPiece(x.getXpos() , x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() -1)&&!((utils.getPiece(x.getXpos() +1, x.getYpos() -1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos() -1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() -1, x.getYpos() +1)&&!((utils.getPiece(x.getXpos() -1, x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() -1,x.getYpos() +1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() +1)&&!((utils.getPiece(x.getXpos() +1, x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos() +1,"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() +1, x.getYpos() )&&!((utils.getPiece(x.getXpos() +1, x.getYpos() , isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos() +1,x.getYpos(),"Normal"), isDebug, gamestate);
				        if(utils.isOnBoard(x.getXpos() , x.getYpos() +1)&&!((utils.getPiece(x.getXpos() , x.getYpos() +1, isDebug, gamestate)) instanceof King))gamestate=utils.placePiece(new BlankPiece("Blank", x.getXpos(),x.getYpos() +1,"Normal"), isDebug, gamestate);
				        BBlt = 0;
					}
				}
			}
			
			gamestate=pwrUp.powerupSpawn(gamestate, turnNo, isDebug);
			
			if(rulechange1) {
				ecat.updateRuleChange1();
			}
			if(rulechange2) {
				ecat.updateRuleChange2();
			}
			if(rulechange3) {
				ecat.updateRuleChange3();
			}
			
			System.out.println("brswitch " + ecat.getBrs());

			
			if(isDebug)
				if(isDebug) {
					System.out.println("It is now Black's turn.");
					System.out.println("Black's powerups: "+blackPowerUps.toString());}
		}
		else{
			currentTurn = "White";
			turnNo++;
			
			if(rulechange1) {
				ecat.updateRuleChange1();
			}
			if(rulechange2) {
				ecat.updateRuleChange2();
			}
			if(rulechange3) {
				ecat.updateRuleChange3();
			}
			
			System.out.println("brswitch " + ecat.getBrs());
			
			gamestate=pwrUp.powerupSpawn(gamestate, turnNo, isDebug);
			
			if(isDebug) {
				System.out.println("It is now White's turn.");
				System.out.println("White's powerups: "+whitePowerUps.toString());}
		}

		//=======================================STAGE HAZARDS================================================//
		//GAMESTATE = NEW GAMESTATE
		gamestate= hazards.assignHazard(gamestate);
		//=======================================STAGE HAZARDS================================================//

	}
	
	/**
	 * Switches the turn to the opposite of what it currently is, but does not increase the number of turns.
	 * Used for the "ask for draw" button.
	 * If for some reason the turn is neither Black nor White
	 * by default the method sets it to white
	 */
	public void switchTurn(){
		if(currentTurn.equalsIgnoreCase("White")){
			currentTurn = "Black";
			gamestate=pwrUp.powerupSpawn(gamestate, turnNo, isDebug);
			if(isDebug)
				if(isDebug) {
					System.out.println("It is now Black's turn.");
					System.out.println("Black's powerups: "+blackPowerUps.toString());}
		}
		else{
			currentTurn = "White";
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
				utils.placePiece(new BlankPiece("Blank", i, j,"Normal"), isDebug, gamestate);
			}
		}
		//Now place all pieces in starting positions
		for(int i = 0; i < 8; i++)
			utils.placePiece(new Pawn("White", i, 1,"Normal"), isDebug, gamestate);
		for(int j = 0; j < 8; j++)
			utils.placePiece(new Pawn("Black", j, 6,"Normal"), isDebug, gamestate);
		//0 y is black pieces, 7 y is white pieces
		utils.placePiece( new Rook("White",  0,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Rook("White",  7,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Rook("Black",  0,7,"Normal"), isDebug, gamestate );
		utils.placePiece( new Rook("Black",  7,7,"Normal"), isDebug, gamestate );

		utils.placePiece( new Knight("White",6,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Knight("White",1,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Knight("Black",6,7,"Normal"), isDebug, gamestate );
		utils.placePiece( new Knight("Black",1,7,"Normal"), isDebug, gamestate );

		utils.placePiece( new Bishop("White",5,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Bishop("White",2,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Bishop("Black",5,7,"Normal"), isDebug, gamestate );
		utils.placePiece( new Bishop("Black",2,7,"Normal"), isDebug, gamestate );

		utils.placePiece( new King("White",  4,0,"Normal"), isDebug, gamestate );
		utils.placePiece( new Queen("White", 3,0,"Normal"), isDebug, gamestate );

		utils.placePiece( new King("Black",  4,7,"Normal"), isDebug, gamestate );
		utils.placePiece( new Queen("Black", 3,7,"Normal"), isDebug, gamestate );
		
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
		if(isDrawAsked || isDraw){
			return false;
		}
		
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
		if(p.getPoweruptype().equalsIgnoreCase("dummy")) {
			if(isDebug)
				System.out.println("You cannot move a Dummy piece.");
			return false;
		}
		//TODO ADD CHECK FOR HAZARD PIECE
//		System.out.println("Turn number: "+turnNo+". Available moves for "+currentTurn+": "+ecat.possibleGamestatesAfterNextMove(currentTurn, isDebug, gamestate, turnNo).size());
//		for(AbstractPiece[][] gs: ecat.possibleGamestatesAfterNextMove(currentTurn, isDebug, gamestate, turnNo)) {
//			utils.printGameState(gs);
//		}
		

		
		
		//Save old position (to place a blank later)
		Position oldPos = new Position(p.getXpos(), p.getYpos());
		//New position, using x and y to be relative
		System.out.println("p:"+utils.getPiece(p.getPosition(), isDebug, gamestate).getPoweruptype());
		AbstractPiece newPiece = utils.getTargetPiece(p, xRel, yRel, isDebug, gamestate);
		System.out.println("newpiece:"+utils.getPiece(newPiece.getPosition(), isDebug, gamestate).getPoweruptype());
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
		if(isValid == false && p instanceof King) {
			isValid = cstl.castleCheck((King)p, xRel, yRel, isDebug, gamestate, turnNo);
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
		//First checks if it can castle
		if(isValid){
			if(p instanceof King)
				cstl.castleCheck((King)p, xRel, yRel, isDebug, gamestate, turnNo);
			if(p instanceof King && ((King)p).getCanCastle() != 0)
				cstl.castle((King)p, xRel, yRel, isDebug, gamestate, turnNo);
			AbstractPiece[][] newGamestate = utils.safeCopyGamestate(gamestate);
			AbstractPiece copiedPiece = utils.safeCopyPiece(p);
			copiedPiece.setPosition(newPiece.getXpos(), newPiece.getYpos());
			if(p instanceof Pawn && ((Pawn)p).getEnPassant() == true){ 
				if(p.getColor().equalsIgnoreCase("white"))
					newGamestate=utils.placePiece(new BlankPiece("Blank",copiedPiece.getXpos(), (copiedPiece.getYpos()-1), "Normal"), isDebug, newGamestate);
				else 
					newGamestate=utils.placePiece(new BlankPiece("Blank",copiedPiece.getXpos(), (copiedPiece.getYpos()+1), "Normal"), isDebug, newGamestate);
			}
			if(copiedPiece instanceof King)
				((King)copiedPiece).setWasMoved(true);
			if(copiedPiece instanceof Rook) {
				((Rook)copiedPiece).setWasMoved(true);
			}
			
			
			if(newPiece.getPoweruptype().equalsIgnoreCase("bomb")) {
				System.out.println("Bomb!!!");
		        if(utils.isOnBoard(newPiece.getXpos() -1, newPiece.getYpos() -1)&&!((utils.getPiece(newPiece.getXpos() -1, newPiece.getYpos() -1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() -1,newPiece.getYpos() -1,"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() -1,newPiece.getYpos())&&!((utils.getPiece(newPiece.getXpos() -1, newPiece.getYpos() , isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() -1,newPiece.getYpos(),"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() , newPiece.getYpos() -1)&&!((utils.getPiece(newPiece.getXpos() , newPiece.getYpos() -1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos(),newPiece.getYpos() -1,"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() +1, newPiece.getYpos() -1)&&!((utils.getPiece(newPiece.getXpos() +1, newPiece.getYpos() -1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() +1,newPiece.getYpos() -1,"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() -1, newPiece.getYpos() +1)&&!((utils.getPiece(newPiece.getXpos() -1, newPiece.getYpos() +1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() -1,newPiece.getYpos() +1,"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() +1, newPiece.getYpos() +1)&&!((utils.getPiece(newPiece.getXpos() +1, newPiece.getYpos() +1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() +1,newPiece.getYpos() +1,"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() +1, newPiece.getYpos() )&&!((utils.getPiece(newPiece.getXpos() +1, newPiece.getYpos() , isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos() +1,newPiece.getYpos(),"Normal"), isDebug, newGamestate);
		        if(utils.isOnBoard(newPiece.getXpos() , newPiece.getYpos() +1)&&!((utils.getPiece(newPiece.getXpos() , newPiece.getYpos() +1, isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos(),newPiece.getYpos() +1,"Normal"), isDebug, newGamestate);
			    
		        if(!(copiedPiece instanceof King)) {
		        	if(utils.isOnBoard(newPiece.getXpos(), newPiece.getYpos())&&!((utils.getPiece(newPiece.getXpos() , newPiece.getYpos() , isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(new BlankPiece("Blank", newPiece.getXpos(),newPiece.getYpos(),"Normal"), isDebug, newGamestate);
		        }
		        else if((copiedPiece instanceof King)) {
		        	if(utils.isOnBoard(newPiece.getXpos(), newPiece.getYpos())&&!((utils.getPiece(newPiece.getXpos() , newPiece.getYpos() , isDebug, newGamestate)) instanceof King))newGamestate=utils.placePiece(copiedPiece, isDebug, newGamestate);
		        }
		        
		        if(newPiece.getColor().equalsIgnoreCase("white")) {
			    	WBlt = 0;
			    	WB = 1;
			    }
			    if(newPiece.getColor().equalsIgnoreCase("black")) {
			    	BBlt = 0;
			    	BB = 1;
			    }
			}
			else {
			newGamestate=utils.placePiece(copiedPiece, isDebug, newGamestate);//place it according to the new position
			//and set the old position to a Blank place
			}
			newGamestate=utils.placePiece(new BlankPiece("Blank",oldPos.getXpos(), oldPos.getYpos(),"Normal"), isDebug, newGamestate);
		
		
		
		
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
				//utils.printGameState(newGamestate);
				if(ecat.isInCheck("black", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("Black king is now checked!");
						isBlackChecked = true;
					}
				}else{
					isBlackChecked = false;
				}
			}
			if(currentTurn.equalsIgnoreCase("black")) {
				if(ecat.isInCheck("white", isDebug, newGamestate, turnNo+1)) {
					if(isDebug) {
						System.out.println("White king is now checked!");
						isWhiteChecked = true;
					}
				}else{
					isWhiteChecked = false;
				}
			}

			//checks if the new gamestate will be a checkmate for the oponent
			if(currentTurn.equalsIgnoreCase("white")) {
				if(ecat.isInCheckmate("black", isDebug, newGamestate, turnNo+1, getPowerUps(currentTurn))) {
					if(isDebug) {
						System.out.println("White checkmated black");
					}
					isBlackMated = true;
					isEndgame = true;
				}else if(isBlackChecked) System.out.println(ecat.validMoves(ecat.getKing("black", newGamestate), isDebug, newGamestate, turnNo));
			}
			if(currentTurn.equalsIgnoreCase("black")) {
				if(ecat.isInCheckmate("white", isDebug, newGamestate, turnNo+1, getPowerUps(currentTurn))) {
					if(isDebug) {
						System.out.println("Black checkmated white");
					}
					isWhiteMated = true;
					isEndgame = true;
				}else if(isWhiteChecked) System.out.println(ecat.validMoves(ecat.getKing("white", newGamestate), isDebug, newGamestate, turnNo).size());
			}

			// Check if the new gamestate will be a draw

			if (ecat.isInDraw(currentTurn, isDebug, newGamestate, turnNo+1, getPowerUps(currentTurn)) && !isEndgame) {
				if (isDebug) {
					System.out.println("The game resulted in a draw");
				}
				isDraw = true;
				isEndgame = true;
			}

			
			if(newPiece instanceof Powerup) {
				if(currentTurn.equalsIgnoreCase("white")) whitePowerUps.add(pwrUp.randomPowerup(isDebug));
				if(currentTurn.equalsIgnoreCase("black")) blackPowerUps.add(pwrUp.randomPowerup(isDebug));
				        
			}


			gamestate = newGamestate;
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
		//System.out.println("RelX: "+ relX + " RelY: "+ relY);
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


	public int getTurnNo() {
		return turnNo;
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

	public ArrayList<String> getPowerUps(String player){
		if(player.equalsIgnoreCase("black")){
			return blackPowerUps;
		}else{
			return whitePowerUps;
		}
	}
	
	public boolean getBrs() {
		return ecat.getBrs();
	}
	
	public boolean getPS() {
		return ecat.getPS();
	}
	
	public boolean getKS() {
		return ecat.getKS();
	}
	
	public boolean getDrawAsked(){
		return isDrawAsked;
	}
	
	public void setDrawAsked(){
		isDrawAsked = true;
	}
	
	public boolean getDraw(){
		return isDraw;
	}
	
	public void setDraw(){
		isDraw = true;
	}
	
	public void refuseDraw(){
		isDrawAsked = false;
	}

	public void setCheck(String player,boolean check){
		if(player.equalsIgnoreCase("white")){
			isWhiteChecked = check;
		}else{
			isBlackChecked = check;
		}
	}

	public void setMate(String player, boolean mate){
		if(player.equalsIgnoreCase("white")){
			isWhiteMated = mate;
		}else{
			isBlackMated = mate;
		}
	}
	
	public void resetWBombsound() {
		WB = 0;
	}
	public void resetBBombsound() {
		BB = 0;
	}
	
	public int getWB() {
		return WB;
	}
	
	public int getBB() {
		return BB;
	}
	public int getWBlt() {
		return WBlt;
	}
	
	public int getBBlt() {
		return BBlt;
	}
	
	public ExtraChecksAndTools getEcat() {
		return ecat;
	}
	
	public int getCounter() {
		return ecat.getCounter();
	}
	
	public boolean getRC1() {
		return rulechange1;
	}
	
	public boolean getRC2() {
		return rulechange2;
	}
	
	public boolean getRC3() {
		return rulechange3;
	}
	
	public void setRC1(boolean rulechange1) {
		this.rulechange1 = rulechange1;
	}
	
	public void setRC2(boolean rulechange2) {
		this.rulechange2 = rulechange2;
	}
	
	public void setRC3(boolean rulechange3) {
		this.rulechange3 = rulechange3;
	}

	/**
	 * This takes a gamestate and tell the player if a pawn promotion is available
	 * @param gamestate
	 * @return the pawn for promotion or null if none exists
	 */
	public AbstractPiece isPawnPromote(AbstractPiece[][] gamestate){
		for(int i=0; i<7;i++){
			//check for white pawn
			if((gamestate[i][0] instanceof Pawn) && (gamestate[i][0].getColor().equalsIgnoreCase("black"))){
				System.out.println("Promote black");
				return gamestate[i][0];
			}
			if((gamestate[i][7] instanceof Pawn) && (gamestate[i][7].getColor().equalsIgnoreCase("white"))){
				System.out.println("Promote white");
				return gamestate[i][7];
			}
		}
		return null;
	}

	/**
	 * Promotes a pawn. newPice is 'q', 'k', 'r,' b', for queen, king, rook, bishop respectively
	 * @param piece
	 * @param newPiece
	 */
	public void promote(AbstractPiece piece, String newPiece){
		AbstractPiece[][] safeGs = utils.safeCopyGamestate(gamestate);
		Position pos = piece.getPosition();

		if(newPiece.equalsIgnoreCase("q")){
			Queen q = new Queen(piece.getColor(), pos,"Normal");
			utils.placePiece(q,isDebug,safeGs);
		}
		else if(newPiece.equalsIgnoreCase("k")){
			Knight k = new Knight(piece.getColor(), pos,"Normal");
			utils.placePiece(k,isDebug,safeGs);
		}
		else if(newPiece.equalsIgnoreCase("r")){
			Rook r = new Rook(piece.getColor(), pos,"Normal");
			utils.placePiece(r,isDebug,safeGs);
		}
		else if(newPiece.equalsIgnoreCase("b")){
			Bishop b = new Bishop(piece.getColor(), pos,"Normal");
			utils.placePiece(b,isDebug,safeGs);
		}
		setGamestate(safeGs);
	}
}
