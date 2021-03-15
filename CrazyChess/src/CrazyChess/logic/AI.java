package CrazyChess.logic;
import java.util.ArrayList;
import CrazyChess.pieces.*;

public class AI
{
	private MainLogic chess;
	private Utilities utils = new Utilities();
	//private ExtraChecksAndTools ect;
	public void AI () {
		//null constructor
	}

	public AbstractPiece[][] AI (MainLogic chess) {
		this.chess = chess;

		System.out.println("turn is "+chess.getTurn());
		ExtraChecksAndTools ect = new ExtraChecksAndTools();
		//AbstractPiece[][] bestBoard = minimax(chess.getGamestate(),1,chess.getTurn());
		ArrayList <AbstractPiece[][]> possg = ect.possibleGamestatesAfterNextMove(chess.getTurn(), false, chess.getGamestate(), 0);
		AbstractPiece[][] bestBoard = findBestOutcome(possg,chess.getTurn());
		System.out.println("Best board is:");
		utils.printGameState(bestBoard);
		
		return bestBoard;
		
	}
		//takes in the board, calc possgamestates and sends to decision tree and returns the best choice for a board
		public AbstractPiece[][] minimax (AbstractPiece[][] board, int max_depth, String whosePlaying){
			AbstractPiece[][] bestBoard = null;
			ExtraChecksAndTools ect = new ExtraChecksAndTools();
			
			ArrayList <AbstractPiece[][]> possg = ect.possibleGamestatesAfterNextMove(whosePlaying, false, board, 0);
			
			//feed each possibility into decisionTree and choose the best-worst case scenario for whoever AI is
			int temp;
			if (whosePlaying.equals("Black")) {
				int lowest = Integer.MAX_VALUE;
				//for black the lower the better
				for (int i=0;i<possg.size();i++) {
					//parameters are each board in possg, "Black" is playing (will be swapped to white at the beginning of the decision tree function
					temp = decisionTree(possg.get(i),whosePlaying,1,max_depth,whosePlaying);
					//if lowest>temp then temp is a board with a better outcome so lowest=temp and bestBoard = possg.get(i)
					if (lowest>temp) {
						lowest = temp;
						//sets the best board to the new best-worst case
						bestBoard = possg.get(i);
					}
				}
			}
			else {
				int highest = Integer.MIN_VALUE;
				//higher the better for white
				for (int i=0;i<possg.size();i++) {
					//parameters are each board in possg, depth=1;max_depth=3,"White" is playing (will be swapped to white at the beginning of the decision tree function
					temp = decisionTree(possg.get(i),whosePlaying,1,max_depth,whosePlaying);
					//inverse of black statement (see if above)
					if (highest<temp) {
						highest = temp;
						bestBoard = possg.get(i);
					}
				}
			}
			
			if (bestBoard == null) {
				System.out.println("ERROR- BESTBOARD = NULL, MINMAX NOT WORKING");
			}
			return bestBoard;
						
		}

	//the board entering this is not the chess gamestate, there is one function managing this one. If that doesn't happen
	//then the turn changing needs to be fixed
	public int decisionTree (AbstractPiece[][] board,String whoseTurn,int depth, int max_depth, String whosePlaying) {
		ExtraChecksAndTools ect = new ExtraChecksAndTools();

		//changes turn
		if (whoseTurn.equals("Black")) {
			whoseTurn = "White";
		}
		else {
			whoseTurn = "Black";
		}
		ArrayList<AbstractPiece[][]> poss_boards = ect.possibleGamestatesAfterNextMove(whoseTurn, false, board, 0);

		if (depth==max_depth) {
			//if at max depth use findWorstOutcome and return
			return (findWorstOutcome(poss_boards,whosePlaying));
		}
		else {
			//if we are calculating for blacks turn in the overall decision tree we want to avoid a higher number
			int temp;
			if (whosePlaying.equals("Black")) {
				int highest = Integer.MIN_VALUE;
				for (int i=0;i<poss_boards.size();i++) {
					temp = decisionTree(poss_boards.get(i),whoseTurn,depth++,max_depth,whosePlaying);
					if (highest<temp) {
						highest = temp;
					}
				}
				//highest should be the highest (worst) value for black out of all possiblities in this branch now so we can return it
				return highest;
			}
			//if whites is the AI then everything is inverted
			else {
				int lowest = Integer.MAX_VALUE;
				for (int i=0;i<poss_boards.size();i++) {
					temp = decisionTree(poss_boards.get(i),whoseTurn,depth++,max_depth,whosePlaying);
					if (lowest>temp) {
						lowest = temp;
					}
				}
				//lowest should be worst outcome for white now
				return lowest;
			}
		}
	}

	public int findWorstOutcome (ArrayList <AbstractPiece[][]> boards, String whoseTurn) {
		//if already at leaf nodes of decision tree (end of recursive calls)
		//if(depth==max_depth) {
		int currentBoardVal;
		int worst = evaluateBoard(boards.get(0));

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
				if (currentBoardVal>   worst) {
					//System.out.println("new worst board for black is: ");
					//utils.printGameState(boards.get(i));
					worst = currentBoardVal;
				}
			}
		}
		else {
			//vice versa for white
			//System.out.println("whites turn");
			for (int i=1;i<boards.size();i++) {
				currentBoardVal=evaluateBoard(boards.get(i));
				if (currentBoardVal<worst) {
					worst = currentBoardVal;
				}
			}
		}

		return worst;	
	}
	
	public AbstractPiece[][] findBestOutcome (ArrayList <AbstractPiece[][]> boards, String whoseTurn) {
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

		return bestBoard;
		//}
		//else {
		//its not max depth yet
		//HERE RECURSIVELY CALL THE SAME FUNCTION, THEN CHOOSE THE LOWEST/HIGHEST VALUE AS RETURN THAT
		//got to account for turn changes

		//}
	}

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
			return 0;
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
