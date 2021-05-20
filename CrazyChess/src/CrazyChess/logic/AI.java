package CrazyChess.logic;

import java.util.ArrayList;
import java.util.HashMap;

import CrazyChess.pieces.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class responsible for the Artificial Intelligence in our game. It uses the minimax algorithm with alpha-beta pruning to decide on
 * what moves to make while also taking powerups, stage hazards and rulechanges into account.
 *
 */
public class AI
{
	private MainLogic			chess;
	private Utilities			utils	= new Utilities();
	private ExtraChecksAndTools	ect;

	/**
	 * The null constructor for the class
	 */
	public void AI()
	{
		//null constructor
	}

	/**
	 * This method makes an AI move and returns the new gamestate
	 * 
	 * @param chess
	 *            MainLogic object being used
	 * @return a gamestate altered by the AI
	 */
	public AbstractPiece[][] AI(MainLogic chess)
	{
		this.chess = chess;
		this.ect = chess.getEcat();

		BoardDetails currentBoard = new BoardDetails(this.chess.getGamestate(), this.chess.getPowerUps("white"),
				this.chess.getPowerUps("black"));
		BoardDetails bestMoveDetails = minimax(currentBoard, 3, chess.getTurn());
		AbstractPiece[][] bestMove = bestMoveDetails.getGamestate();

		ArrayList<AbstractPiece> diffPieces = utils.getPiecesDiff(currentBoard.getGamestate(), bestMove);
		if (!diffPieces.isEmpty())
		{
			for (AbstractPiece p : diffPieces)
			{
				if (p.getColor().equalsIgnoreCase("powerup"))
				{
					this.chess.getPowerUps(chess.getTurn()).add(ect.pwrUp.randomPowerup(false));
					break;
				}
			}
		}

		int usedPowerupIndex = bestMoveDetails.getUsedPowerup();
		if (usedPowerupIndex > -1)
		{
			this.chess.getPowerUps(chess.getTurn()).remove(usedPowerupIndex);
		}
		return bestMove;
	}

	/**
	 * This method makes an AI move and returns the new gamestate, takes difficulty into account
	 * 
	 * @param chess
	 *            the MainLogic object to use
	 * @param difficulty
	 *            the difficulty setting ("easy", "medium" or "hard")
	 * @return a gamestate altered by the AI
	 */
	public AbstractPiece[][] AI(MainLogic chess, String difficulty)
	{
		this.chess = chess;
		this.ect = chess.getEcat();

		BoardDetails currentBoard = new BoardDetails(this.chess.getGamestate(), this.chess.getPowerUps("white"),
				this.chess.getPowerUps("black"));

		int max_depth;
		if (difficulty.contentEquals("easy"))
		{
			max_depth = 2;
		}
		else if (difficulty.contentEquals("medium"))
		{
			max_depth = 3;
		}
		else
		{
			max_depth = 4;
		}

		BoardDetails bestMoveDetails = minimax(currentBoard, max_depth, chess.getTurn());
		AbstractPiece[][] bestMove = bestMoveDetails.getGamestate();

		ArrayList<AbstractPiece> diffPieces = utils.getPiecesDiff(currentBoard.getGamestate(), bestMove);
		if (!diffPieces.isEmpty())
		{
			for (AbstractPiece p : diffPieces)
			{
				if (p.getColor().equalsIgnoreCase("powerup"))
				{
					this.chess.getPowerUps(chess.getTurn()).add(ect.pwrUp.randomPowerup(false));
					break;
				}
			}
		}

		int usedPowerupIndex = bestMoveDetails.getUsedPowerup();
		if (usedPowerupIndex > -1)
		{
			chess.getPowerUps(chess.getTurn()).remove(usedPowerupIndex);
		}

		return bestMove;
	}

	//the board entering this is not the chess gamestate, there is one function managing this one. If that doesn't happen
	/**
	 * The main method for the minimax algorithm
	 * 
	 * @param board
	 *            the BoardDetails obeject for the board
	 * @param max_depth
	 *            how many turns into the future the algorithm will look
	 * @param whoseAI
	 *            color of the AI player
	 * @return A BoardDetails object, where the best move has been made
	 */
	public BoardDetails minimax(BoardDetails board, int max_depth, String whoseAI)
	{
		//don't need to know whoseTurn because its always AI's turn at the 0th board
		//possg are all the possible immediate moves the AI can take in the current gamestate's turn
		HashMap<AbstractPiece[][], Integer> possgWithPwr = ect.possibleGamestatesAfterNextMove(whoseAI, false, board.getGamestate(), 0,
				board.getPowerUps(whoseAI));
		ArrayList<AbstractPiece[][]> possg = new ArrayList<>(possgWithPwr.keySet());
		Collections.shuffle(possg);
		//need to pass whoseTurn to next function and it will be whoever is not AI's turn so whoseTurn is decided below
		String whoseTurn;
		if (whoseAI.equals("Black"))
		{
			whoseTurn = "White";
		}
		else
		{
			whoseTurn = "Black";
		}

		//we pass each of these into the explore paths function and choose the one with the best worst-case outcome
		BoardDetails bestMove = null;
		//bestMove will store the board containing the least risky move
		int bestValue;
		//will store the value of bestMove
		int temp;

		//System.out.println("in minimax:");
		if (whoseAI.equals("Black"))
		{
			//in black's case the lower the value the better
			bestValue = Integer.MAX_VALUE;
			//sets bestval to max value so it will be set to the first returning value from the explorePaths function
			for (int i = 0; i < possg.size(); i++)
			{
				// Create a new chess board details to account powerup usage
				BoardDetails newBoard = new BoardDetails(possg.get(i));
				newBoard.setPowerUps("white", new ArrayList<String>(board.getPowerUps("white")));
				newBoard.setPowerUps("black", new ArrayList<String>(board.getPowerUps("black")));
				int usedPowerup = possgWithPwr.get(newBoard.getGamestate());
				if (usedPowerup > -1)
				{
					newBoard.setUsedPowerup(usedPowerup);
					newBoard.getPowerUps(whoseAI).remove(usedPowerup);
				}

				temp = explorePaths(newBoard, 1, max_depth, whoseAI, whoseTurn, Integer.MAX_VALUE, Integer.MIN_VALUE);
				//if temp (this move) has a lower risk for black then set bestMove and bestValue to temp and the current board
				if (temp < bestValue)
				{
					bestValue = temp;
					bestMove = newBoard;
					utils.printGameState(bestMove.getGamestate());
				}
			}
		}
		else
		{
			//in white's case its the same except the higher the value the better
			bestValue = Integer.MIN_VALUE;
			//sets bestval to min value so it will be set to the first returning value from the explorePaths function
			for (int i = 0; i < possg.size(); i++)
			{
				// Create a new chess board details to account powerup usage
				BoardDetails newBoard = new BoardDetails(possg.get(i));
				newBoard.setPowerUps("white", new ArrayList<String>(board.getPowerUps("white")));
				newBoard.setPowerUps("black", new ArrayList<String>(board.getPowerUps("black")));
				int usedPowerup = possgWithPwr.get(newBoard.getGamestate());
				if (usedPowerup > -1)
				{
					newBoard.setUsedPowerup(usedPowerup);
					newBoard.getPowerUps(whoseAI).remove(usedPowerup);
				}

				temp = explorePaths(newBoard, 1, max_depth, whoseAI, whoseTurn, Integer.MAX_VALUE, Integer.MIN_VALUE);
				//if temp (this move) has a lower risk for black then set bestMove and bestValue to temp and the current board
				if (temp > bestValue)
				{
					bestValue = temp;
					bestMove = newBoard;
					utils.printGameState(bestMove.getGamestate());
				}
			}
		}
		utils.printGameState(bestMove.getGamestate());
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

	/**
	 * Method that explores the possible paths using recursion
	 * 
	 * @param board
	 *            the BoardDetails obeject for the board
	 * @param curr_depth
	 *            how deep the search is currently
	 * @param max_depth
	 *            max depth for the search
	 * @param whoseAI
	 *            color of the AI player
	 * @param whoseTurn
	 *            color of the current turn
	 * @param preMax
	 *            previous maximum value of the gamestate (used for pruning)
	 * @param preMin
	 *            previous minimum value of the gamestate (used for pruning)
	 * @return the value of the gamestate currently being explored
	 */
	public int explorePaths(BoardDetails board, int curr_depth, int max_depth, String whoseAI, String whoseTurn, int preMax, int preMin)
	{
		ArrayList<String> powerupToCheck;
		if (whoseTurn.equalsIgnoreCase(utils.oppositeColor(whoseAI)))
		{
			powerupToCheck = new ArrayList<String>();
		}
		else
		{
			powerupToCheck = board.getPowerUps(whoseTurn).stream().filter(s -> !s.equalsIgnoreCase("teleport"))
					.filter(s -> !s.equalsIgnoreCase("dummypiece")).filter(s -> !s.equalsIgnoreCase("minipromote"))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		HashMap<AbstractPiece[][], Integer> possgWithPwr = ect.possibleGamestatesAfterNextMove(whoseTurn, false, board.getGamestate(), 0,
				powerupToCheck);

		ArrayList<AbstractPiece[][]> possg = new ArrayList<>(possgWithPwr.keySet());
		Collections.shuffle(possg);
		if (curr_depth == max_depth - 1)
		{
			//-1 because we use possg so we look one more move ahead
			// Check if there will be no possible gamestate
			if (possg.isEmpty())
			{
				if (whoseTurn.equals("Black"))
				{
					return preMin;
				}
				else
				{
					return preMax;
				}
			}

			int best = findBestOutcome(possg, whoseTurn, preMax, preMin);
			return best;

		}
		else
		{
			//once again possg contains all the possible moves of the the current player's turn
			int temp;
			int worst, best;
			String whoseTurnNext;

			//sets whoseTurnNext for the next recursion of explorePaths, need this for the turns to be swapped properly in recursion
			//cannot change whoseTurn because it's used to decide whether to min or max
			if (whoseTurn.equals("Black"))
			{
				whoseTurnNext = "White";
			}
			else
			{
				whoseTurnNext = "Black";
			}

			long funcStartTime = System.currentTimeMillis();
			long funcTimeout = 300;
			if (whoseTurn.equals("White"))
			{
				worst = Integer.MIN_VALUE;
				//the higher the worse (for black)
				for (int i = 0; i < possg.size(); i++)
				{
					// Create a new chess board details to account powerup usage
					BoardDetails newBoard = new BoardDetails(possg.get(i));
					newBoard.setPowerUps("white", new ArrayList<String>(board.getPowerUps("white")));
					newBoard.setPowerUps("black", new ArrayList<String>(board.getPowerUps("black")));
					int usedPowerup = possgWithPwr.get(newBoard.getGamestate());
					if (usedPowerup > -1)
					{
						newBoard.getPowerUps(whoseTurn).remove(usedPowerup);
					}

					temp = explorePaths(newBoard, (curr_depth + 1), max_depth, whoseAI, whoseTurnNext, Integer.MAX_VALUE, worst);
					//if the path explored is too big for black to choose it in the previous layer cancel (alpha beta pruning)
					if (temp > preMax)
					{
						return temp;
						//temp will suffice as it's too large to be chosen and will be ignored via the if statement
					}

					if (temp > worst)
					{
						//worst becomes the worst outcome of all possg branches
						worst = temp;
					}

					if ((System.currentTimeMillis() - funcStartTime) >= funcTimeout)
					{
						break;
					}
				}
				//return worst up the tree
				return worst;
			}
			//but if it's black's turn we choose the best (max) outcome as black controls the outcome
			else
			{
				best = Integer.MAX_VALUE;
				//lower the better for black so start at highest
				for (int i = 0; i < possg.size(); i++)
				{
					// Create a new chess board details to account powerup usage
					BoardDetails newBoard = new BoardDetails(possg.get(i));
					newBoard.setPowerUps("white", new ArrayList<String>(board.getPowerUps("white")));
					newBoard.setPowerUps("black", new ArrayList<String>(board.getPowerUps("black")));
					int usedPowerup = possgWithPwr.get(newBoard.getGamestate());
					if (usedPowerup > -1)
					{
						newBoard.getPowerUps(whoseTurn).remove(usedPowerup);
					}

					temp = explorePaths(newBoard, (curr_depth + 1), max_depth, whoseAI, whoseTurnNext, best, Integer.MIN_VALUE);

					if (temp < preMin)
					{
						//if too small for earlier white optimal layer to choose this branch stop searching and return
						return temp;
					}

					if (temp < best)
					{
						//the lower the possibility the better the outcome, so max as black will choose lower
						best = temp;
					}

					if ((System.currentTimeMillis() - funcStartTime) >= funcTimeout)
					{
						break;
					}
				}
				return best;
			}

		}
	}

	//boards is the result of possiblenextgamestates method
	/**
	 * 
	 * @param boards
	 *            ArrayList of gamestates to be evaluated
	 * @param whoseTurn
	 *            color the current turn
	 * @param preMax
	 *            previous maximum value of the gamestate (used for pruning)
	 * @param preMin
	 *            previous minimum value of the gamestate (used for pruning)
	 * @return the gamestate value of of the best outcome
	 */
	public int findBestOutcome(ArrayList<AbstractPiece[][]> boards, String whoseTurn, int preMax, int preMin)
	{

		int currentBoardVal;

		int highest = evaluateBoard(boards.get(0), whoseTurn);
		int lowest = highest;

		for (int i = 1; i < boards.size(); i++)
		{
			currentBoardVal = evaluateBoard(boards.get(i), whoseTurn);
			//check if it violates preMin or preMax (if the layer before won't accept this branch's findbestoutcome stop searching now
			if (currentBoardVal > preMax || currentBoardVal < preMin)
			{
				return currentBoardVal;
			}
			//statement above didnt show up after pruning so pruning definitely works in this layer
			if (currentBoardVal < lowest)
			{
				lowest = currentBoardVal;
			}
			else if (currentBoardVal > highest)
			{
				highest = currentBoardVal;
			}
		}
		if (whoseTurn.equals("Black"))
		{
			return lowest;
		}
		else
		{
			return highest;
		}
	}

	/**
	 * Piece values (positive for white, negative for black) pawn = 1 knight = 3 bishop = 3 rook = 5 queen = 9 king = 0 (don't factor king
	 * into this, theres no point if you have no king)
	 * 
	 * @param piece
	 *            piece to be evaluated
	 * @param whoseTurn
	 *            color of the current turn
	 * @return the value for the inputed piece
	 */
	public int valuePiece(AbstractPiece piece, String whoseTurn)
	{
		if (piece instanceof Pawn)
		{
			if (piece.getColor() == "Black")
			{
				return -1;
			}
			else
			{
				return 1;
			}
		}
		else if (piece instanceof Rook)
		{
			if (piece.getColor() == "Black")
			{
				return -5;
			}
			else
			{
				return 5;
			}
		}
		else if (piece instanceof Bishop)
		{
			if (piece.getColor() == "Black")
			{
				return -3;
			}
			else
			{
				return 3;
			}
		}
		else if (piece instanceof King)
		{
			if (piece.getColor() == "Black")
			{
				return -100;
			}
			else
			{
				return 100;
			}
		}
		else if (piece instanceof Queen)
		{
			if (piece.getColor() == "Black")
			{
				return -9;
			}
			else
			{
				return 9;
			}
		}
		else if (piece instanceof Knight)
		{
			if (piece.getColor() == "Black")
			{
				return -3;
			}
			else
			{
				return 3;
			}
		}
		else if (piece instanceof Powerup)
		{
			if (whoseTurn.equalsIgnoreCase("white"))
			{
				return -2;
			}
			else
			{
				return 2;
			}
		}
		else if (piece.getColor().equalsIgnoreCase("blank"))
		{
			return 0;
		}
		System.out.println("valuePiece function very broken to reach here");
		System.out.println(piece.getColor());
		return 999;
	}

	/**
	 * Evaluates a gamestate
	 * 
	 * @param board
	 *            gamestate to be evaluated
	 * @param whoseTurn
	 *            color of the curent turn
	 * @return a value for the inputed gamestate
	 */
	public int evaluateBoard(AbstractPiece[][] board, String whoseTurn)
	{
		ExtraChecksAndTools ect = new ExtraChecksAndTools();
		ArrayList<AbstractPiece> pieces = ect.gamestateToPieceArrayList(board);

		int value = 0;
		for (AbstractPiece p : pieces)
		{
			value += valuePiece(p, whoseTurn);
		}

		return value;
	}

	/**
	 * A class to hold information about the game to be used by the AI.
	 *
	 */
	class BoardDetails
	{
		AbstractPiece[][]	gamestate;
		ArrayList<String>	whitePowerUps;
		ArrayList<String>	blackPowerUps;
		int					usedPowerup	= -1;	//indicates if a power up is used during a move, -1 means it is not used,

		/**
		 * A constructor for the BoardDetails class
		 * 
		 * @param gamestate
		 *            current gamestate
		 */
		public BoardDetails(AbstractPiece[][] gamestate)
		{
			this.gamestate = gamestate;
		}

		/**
		 * A constructor for the BoardDetails class, that takes powerups into account
		 * 
		 * @param gamestate
		 *            current gamestate
		 * @param whitePowerUps
		 *            PowerUps that white has
		 * @param blackPowerUps
		 *            PowerUps that black has
		 */
		public BoardDetails(AbstractPiece[][] gamestate, ArrayList<String> whitePowerUps, ArrayList<String> blackPowerUps)
		{
			this.gamestate = gamestate;
			this.whitePowerUps = whitePowerUps;
			this.blackPowerUps = blackPowerUps;
		}

		/**
		 * Gets the gamestate from this BoardDetails object
		 * 
		 * @return gamestate store in this object
		 */
		public AbstractPiece[][] getGamestate()
		{
			return this.gamestate;
		}

		/**
		 * Get powerups of a player from this BoardDetails object
		 * 
		 * @param player
		 *            color of the player
		 * @return an ArrayList of powerups
		 */
		public ArrayList<String> getPowerUps(String player)
		{
			if (player.equalsIgnoreCase("black"))
			{
				return blackPowerUps;
			}
			else
			{
				return whitePowerUps;
			}
		}

		/**
		 * Sets the powerup list in this BoardDetails object for a player of certain color
		 * 
		 * @param player
		 *            color of the player
		 * @param powerups
		 *            ArrayList of power ups you want to use
		 */
		public void setPowerUps(String player, ArrayList<String> powerups)
		{
			if (player.equalsIgnoreCase("black"))
			{
				this.blackPowerUps = powerups;
			}
			else
			{
				this.whitePowerUps = powerups;
			}
		}

		/**
		 * Gets the value of usedPowerup
		 * 
		 * @return the index of the powerup that was used by the AI
		 */
		public int getUsedPowerup()
		{
			return this.usedPowerup;
		}

		/**
		 * Sets the value of usedPowerup
		 * 
		 * @param powerupIndex
		 *            new value (-1 - powerup not used, 0-x - index of the powerup)
		 */
		public void setUsedPowerup(int powerupIndex)
		{
			this.usedPowerup = powerupIndex;
		}
	}

}
