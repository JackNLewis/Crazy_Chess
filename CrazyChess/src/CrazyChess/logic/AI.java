package CrazyChess.logic;
import java.util.ArrayList;
import java.util.HashMap;

import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.*;

public class AI
{
	private MainLogic chess;
	private Utilities utils = new Utilities();
	private ExtraChecksAndTools ect = new ExtraChecksAndTools();
	private PowerupMain pwrUp = new PowerupMain();
	public void AI () {
		//null constructor
	}

	public AbstractPiece[][] AI (MainLogic chess) {
		this.chess = chess;

		//System.out.println("turn is "+chess.getTurn());
		//ExtraChecksAndTools ect = new ExtraChecksAndTools();
		//AbstractPiece[][] bestBoard = minimax(chess.getGamestate(),1,chess.getTurn());
		//ArrayList <AbstractPiece[][]> possg = ect.possibleGamestatesAfterNextMove(chess.getTurn(), false, chess.getGamestate(), 0);
		AbstractPiece[][] bestMove = minimax(chess.getGamestate(),3,chess.getTurn());
		//System.out.println("best move is:");
		//utils.printGameState(bestMove);
		return bestMove;
		/*AbstractPiece[][] bestBoard = findBestOutcome(possg,chess.getTurn());
		System.out.println("Best board is:");
		utils.printGameState(bestBoard);

		System.out.println("Current board val is "+evaluateBoard(chess.getGamestate()));

		System.out.println("chess.getTurn is "+chess.getTurn());
		int blackMove = findWorstOutcome(possg,"Black");
		System.out.println("black worst board is: "+blackMove);

		int whiteMove = findWorstOutcome(possg,"White");
		System.out.println("white worst board is: "+whiteMove);

		return bestBoard;*/

	}

	//the board entering this is not the chess gamestate, there is one function managing this one. If that doesn't happen
	public AbstractPiece[][] minimax (AbstractPiece[][] board, int max_depth, String whoseAI){
		//don't need to know whoseTurn because its always AI's turn at the 0th board
		//possg are all the possible immediate moves the AI can take in the current gamestate's turn
		HashMap<AbstractPiece[][], Integer> possgWithPwr = ect.possibleGamestatesAfterNextMove(whoseAI, false, board, 0, chess.getPowerUps(whoseAI));
		ArrayList<AbstractPiece[][]> possg = new ArrayList<>(possgWithPwr.keySet());
		//need to pass whoseTurn to next function and it will be whoever is not AI's turn so whoseTurn is decided below
		String whoseTurn;
		if (whoseAI.equals("Black")) {
			whoseTurn="White";
		}
		else {
			whoseTurn="Black";
		}

		//we pass each of these into the explore paths function and choose the one with the best worst-case outcome
		AbstractPiece[][] bestMove = null;
		//bestMove will store the board containing the least risky move
		int bestValue;
		//will store the value of bestMove
		int temp;

		//System.out.println("in minimax:");
		if (whoseAI.equals("Black")) {
			//in black's case the lower the value the better
			bestValue = Integer.MAX_VALUE;
			//sets bestval to max value so it will be set to the first returning value from the explorePaths function
			//System.out.println("Bestval is init as: "+bestValue);
			for (int i=0;i<possg.size();i++) {
				temp = explorePaths(possg.get(i),1,max_depth,whoseAI,whoseTurn,Integer.MAX_VALUE,Integer.MIN_VALUE);
				//if temp (this move) has a lower risk for black then set bestMove and bestValue to temp and the current board
				if (temp<bestValue) {
					bestValue=temp;
					bestMove=possg.get(i);
					//System.out.println("New best value of "+bestValue+" for board: ");
					utils.printGameState(bestMove);
				}
			}
		}
		else {
			//in white's case its the same except the higher the value the better
			bestValue = Integer.MIN_VALUE;
			//sets bestval to min value so it will be set to the first returning value from the explorePaths function
			//System.out.println("Bestval is init as: "+bestValue);
			for (int i=0;i<possg.size();i++) {
				temp = explorePaths(possg.get(i),1,max_depth,whoseAI,whoseTurn,Integer.MAX_VALUE,Integer.MIN_VALUE);
				//if temp (this move) has a lower risk for black then set bestMove and bestValue to temp and the current board
				if (temp>bestValue) {
					bestValue=temp;
					bestMove=possg.get(i);
					//System.out.println("New best value of "+bestValue+" for board: ");
					utils.printGameState(bestMove);
				}
			}
		}
		//System.out.println("final best value of "+bestValue+" for board: ");
		utils.printGameState(bestMove);
		return bestMove;
	}

	/*
	 * pre alpha beta notes for rules
	 *  IF NOT LAST LAYER (not findworst/best outcome functions
	 *   when maxing (white):
	 *    STORE highest from current solutions, PASS (max) value on to recursing explorePaths OR find(worst/best)outcome - pass MIN_VAL if no highest val yet
	 *     IN NEXT LAYER (with passed on max val)
	 *     if there is a lower solution than found (the passed on value (on another branch)
	 *      STOP, return MIN_VAL (or other value worse than current best)
	 *     else
	 *      RETURN new best value
	 *   for min (black) do same for min
	 *  ELSE (bottom layer) 
	 *   do similar to the above but for findworst/bestoutcome?
	 */


	public int explorePaths (AbstractPiece[][] board,int curr_depth, int max_depth, String whoseAI, String whoseTurn, int preMax, int preMin) {
		HashMap<AbstractPiece[][], Integer> possgWithPwr = ect.possibleGamestatesAfterNextMove(whoseAI, false, board, 0, chess.getPowerUps(whoseAI));
		ArrayList<AbstractPiece[][]> possg = new ArrayList<>(possgWithPwr.keySet());
		if(curr_depth==max_depth-1) {
			//-1 because we use possg so we look one more move ahead
			/*if (whoseAI.contentEquals(whoseTurn)) {
				int worst=findWorstOutcome(possg,whoseAI,preMax,preMin);
				return worst;
			}		
			else {*/
			//System.out.println("Sending to findnBestOutcome with max: "+preMax+" min: "+preMin);
			int best = findBestOutcome(possg,whoseTurn,preMax,preMin);
			return best;
			//}

		}
		else {
			//once again possg contains all the possible moves of the the current player's turn
			int temp;
			int worst, best;
			String whoseTurnNext;

			//sets whoseTurnNext for the next recursion of explorePaths, need this for the turns to be swapped properly in recursion
			//cannot change whoseTurn because it's used to decide whether to min or max
			if(whoseTurn.equals("Black")) {
				whoseTurnNext="White";
			}
			else {
				whoseTurnNext="Black";
			}
			//if black is AI, this if statement is likely redundant, remove if there's time
			//if (whoseAI.equals("Black")) {
			//and its white's turn, we will choose the worst (min) outcome as black needs to consider the worst outcome as AI
			if (whoseTurn.equals("White")) {
				worst = Integer.MIN_VALUE;
				//the higher the worse
				for (int i=0;i<possg.size();i++) {
					//System.out.println("Sending with preMin of "+worst+" as white (to next black layer)");
					temp = explorePaths(possg.get(i),(curr_depth+1),max_depth,whoseAI,whoseTurnNext,Integer.MAX_VALUE,worst);
					//if the path explored is too big for black to choose it in the previous layer cancel (alpha beta pruning)
					if(temp>preMax) {
						//System.out.println("\nStopping early as "+temp+" > "+preMax+" on White's turn\n");
						return temp; 
						//temp will suffice as it's too large to be chosen and will be ignored via the if statement
					}

					if(temp>worst) {
						//worst becomes the worst outcome of all possg branches
						worst=temp;
					}
				}
				//return worst up the tree
				//System.out.println("\nSearched the whole branch (no alpha beta)\n");
				return worst;
			}
			//but if it's black's turn we choose the best (max) outcome as black controls the outcome
			else {
				best = Integer.MAX_VALUE;
				//lower the better for black so start at highest
				for (int i=0;i<possg.size();i++) {
					//System.out.println("Sending with preMax of "+best+" as black (to next white layer)");
					temp = explorePaths(possg.get(i),(curr_depth+1),max_depth,whoseAI,whoseTurnNext,best,Integer.MIN_VALUE);

					if(temp<preMin) {
						//if too small for earlier white optimal layer to choose this branch stop searching and return
						//System.out.println("\nStopping early as "+temp+" < "+preMin+" on Black's turn\n");
						return temp;
					}

					if (temp<best) {
						//the lower the possibility the better the outcome, so max as black will choose lower
						best=temp;
					}
				}
				//System.out.println("\nNo pruning, whole tree searched\n");
				return best;
			}
			/*}
			//if white is AI
			else {
				//if white is AI it must assume black will choose the worst (lowest) outcome -- the min part of minmax
				if (whoseTurn.equals("Black")) {
					worst = Integer.MAX_VALUE;
					//lower the worse for white
					for (int i=0;i<possg.size();i++) {
						temp = explorePaths(possg.get(i),(curr_depth+1),max_depth,whoseAI,whoseTurnNext);
						if (temp<worst) {
							//same for worst as above except higher the worse
							worst = temp;
						}
					}
					return worst;
				}
				else {
					//if white is the AI chooses best/highest outcome (max)
					best = Integer.MIN_VALUE;
					//higher the better for white
					for (int i=0;i<possg.size();i++) {
						temp = explorePaths(possg.get(i),(curr_depth+1),max_depth,whoseAI,whoseTurnNext);
						if (temp>best) {
							//stores highest outcome
							best=temp;
						}
					}
					return best;
				}

			}
			 */
		}
	}

	//boards is the result of possiblenextgamestates method
	public int findBestOutcome (ArrayList <AbstractPiece[][]> boards, String whoseTurn,int preMax,int preMin) {

		int currentBoardVal;

		int highest = evaluateBoard(boards.get(0));
		int lowest = highest;

		for (int i=1;i<boards.size();i++) {
			currentBoardVal=evaluateBoard(boards.get(i));
			//check if it violates preMin or preMax (if the layer before won't accept this branch's findbestoutcome stop searching now
			if (currentBoardVal>preMax||currentBoardVal<preMin) {
				//System.out.println("Pruned out in findBestOutcome to values: current="+currentBoardVal+" preMax="+preMax+" preMin:"+preMin);
				return currentBoardVal;
			}
			//System.out.println("shouldnt show up after pruned out statment");
			//statement above didnt show up after pruning so pruning definitely works in this layer
			if (currentBoardVal<lowest) {
				lowest = currentBoardVal;
			}
			else if (currentBoardVal>highest) {
				highest = currentBoardVal;
			}
		}
		//System.out.println("No pruning performed in findBestOutcome on "+whoseTurn+"'s turn");
		if (whoseTurn.equals("Black")) {
			return lowest;
		}
		else {
			return highest;
		}
	}

	//not a necessary function, unless we expand to have easier difficulty ai
	/*	
public int findBestOutcome (ArrayList <AbstractPiece[][]> boards, String whoseTurn) {
		//if already at leaf nodes of decision tree (end of recursive calls)
		//if(depth==max_depth) {
		int currentBoardVal;
		int best = evaluateBoard(boards.get(0));
		AbstractPiece[][] bestBoard = boards.get(0);

		//System.out.println("Starting worst board is:");
		//utils.printGameState(boards.get(0));

		if (whoseTurn=="Black") {
			//System.out.println("blacks turn, all poss boards and vals:");
			//black is negative (more negative is good for black) white is positive
			for (int i=1;i<boards.size();i++) {
				currentBoardVal=evaluateBoard(boards.get(i));


				//utils.printGameState(boards.get(i));
				//System.out.println("bval for above is: "+currentBoardVal);


				//if the value for this board is higher that means black is in a worse position so worst = currentBoardVal
				if (currentBoardVal<   best) {
					//System.out.println("new worst board for black is: ");
					//utils.printGameState(boards.get(i));
					best = currentBoardVal;
					bestBoard = boards.get(i);
				}
			}
		}
		else {
			//vice versa for white
			//System.out.println("whites turn");
			for (int i=1;i<boards.size();i++) {
				currentBoardVal=evaluateBoard(boards.get(i));
				if (currentBoardVal>best) {
					best = currentBoardVal;
					bestBoard = boards.get(i);
				}
			}
		}

		return best;
		//}
		//else {
		//its not max depth yet
		//HERE RECURSIVELY CALL THE SAME FUNCTION, THEN CHOOSE THE LOWEST/HIGHEST VALUE AS RETURN THAT
		//got to account for turn changes

		//}
	}
	 */

	/*
	 * Piece values (positive for white, negative for black)
	 * pawn = 1
	 * knight = 3
	 * bishop = 3
	 * rook = 5
	 * queen = 9
	 * king = 0 (don't factor king into this, theres no point if you have no king
	 */
	public int valuePiece(AbstractPiece piece) {
		//System.out.println("piece is "+piece+" in string is "+piece.toString());
		if (piece instanceof Pawn){
			if (piece.getColor()=="Black") {
				//System.out.println("black pawn");
				return -1;
			}
			else {
				//System.out.println("white pawn");
				return 1;
			}
		}
		else if (piece instanceof Rook){
			if (piece.getColor()=="Black") {
				//System.out.println("black rook");
				return -5;
			}
			else {
				//System.out.println("white rook");
				return 5;
			}
		}
		else if (piece instanceof Bishop){
			if (piece.getColor()=="Black") {
				//System.out.println("black bishop");
				return -3;
			}
			else {
				//System.out.println("white bishop");
				return 3;
			}
		}
		else if (piece instanceof King){
			if (piece.getColor()=="Black") {
				//System.out.println("black pawn");
				return -100;
			}
			else {
				//System.out.println("white pawn");
				return 100;
			}
		}
		else if (piece instanceof Queen){
			if (piece.getColor()=="Black") {
				//System.out.println("black queen");
				return -9;
			}
			else {
				//System.out.println("white queen");
				return 9;
			}
		}
		else if (piece instanceof Knight){
			if (piece.getColor()=="Black") {
				//System.out.println("black knight");
				return -3;
			}
			else {
				//System.out.println("white knight");
				return 3;
			}
		}
		System.out.println("valuePiece function very broken to reach here");
		return 999;
	}

	public int evaluateBoard (AbstractPiece[][] board) {
		ExtraChecksAndTools ect = new ExtraChecksAndTools();
		ArrayList<AbstractPiece> whitePieces = ect.getWhitePieces(board);
		ArrayList<AbstractPiece> blackPieces = ect.getBlackPieces(board);
		//System.out.println("no black="+blackPieces.size()+" no white="+whitePieces.size());

		int value = 0;
		for (int i=0;i<whitePieces.size();i++) {
			value += valuePiece(whitePieces.get(i));
		}
		for (int j=0;j<blackPieces.size();j++) {
			value += valuePiece(blackPieces.get(j));
		}

		//System.out.println("current value is "+value);
		return value;
	}

}
