package CrazyChess.logic;
import CrazyChess.pieces.*;
/**
 * Class with some utilities for getting pieces from
 * and placing pieces to game states
 * 
 * 
 * @author Darius
 *
 */



public class Utilities
{

	
	/**
	 * Retrieves piece at coordinate location
	 * @param x          the x coordinate
	 * @param y          the y coordinate
	 * @param isDebug    isDebug mode activated
	 * @param gamestate  current game state  
	 * @return piece     the Piece at (x,y), null if invalid
	 */
	public AbstractPiece getPiece(int x, int y, boolean isDebug, AbstractPiece[][] gamestate){
		if(!isOnBoard(x,y)){
			if(isDebug)
				System.out.println("Invalid position in getPiece(x,y)");
			return null;
		}
		return x > 7 || x < 0 || y > 7 || y < 0 ? null : gamestate[x][y];
	}


	/**
	 * Retrieves piece at Position class location
	 * @param pos        the position object to retrieve located piece
	 * @param isDebug    isDebug mode activated
	 * @param gamestate  current game state  
	 * @return           the Piece at the Position, null if invalid
	 */
	public AbstractPiece getPiece(Position pos, boolean isDebug, AbstractPiece[][] gamestate){
		if(!isOnBoard(pos.getXpos(),pos.getYpos())){
			if(isDebug)
				System.out.println("Invalid position in getPiece(pos");
			return null;
		}
		return pos.getXpos() > 7 || pos.getXpos() < 0 || pos.getYpos() > 7 || pos.getYpos() < 0 ? null : gamestate[pos.getXpos()][pos.getYpos()];
	}
	
	/**
	 * Very brute function to place a piece in the game state and return the new game state.
	 * Does not take into consideration any restrictions or rules on movement, will literally
	 * place any piece anywhere on the board.
	 * Should be mostly used on empty boards, or if 100% sure
	 * that the tile in which the piece is about to placed is unoccupied.
	 * !!!BE VERY CAREFUL WHEN USING THIS!!!
	 * @param p          piece to be place
	 * @param isDebug    is debug mode active
	 * @param gamestate  game state to edit
	 * @return           game state with the piece placed
	 */
	public AbstractPiece[][] placePiece(AbstractPiece p, boolean isDebug, AbstractPiece[][] gamestate){
		if(!isOnBoard(p.getPosition())){//checks if the piece coordinates are not off the board
			if(isDebug)
				System.out.println("Invalid piece place!");
			return null;
		}
		AbstractPiece[][] newState = gamestate;
		newState[p.getXpos()][p.getYpos()]=p;
		return newState;
	}
	
	/**
	 * Returns a piece acquired from adding some some offset
	 * values x and y to the coordinates of the starting piece.
	 * Further used to check what piece (if any) is in the
	 * square another piece is about to go to.
	 * @param p           the starting piece to check from
	 * @param x           the up and down movement
	 * @param y           the left and right movement
	 * @param isDebug     is debug mode active
	 * @param gamestate   current game state
	 * @return            piece found at location, null if invalid
	 */
	public AbstractPiece getTargetPiece(AbstractPiece p, int x, int y, boolean isDebug, AbstractPiece[][] gamestate){
		if(p.getXpos()+x > 7 || p.getXpos() < 0 || p.getYpos() > 7 || p.getYpos() < 0 || p == null){
			if(isDebug)
				System.out.println("Invalid relative piece position.");
			return null;
		}
		if(getPiece(p.getXpos() + x, p.getYpos() + y, isDebug, gamestate) == null){
			if(isDebug)
				System.out.println("Relative piece is off the board.");
			return null;
		}
		return getPiece(p.getXpos() + x, p.getYpos() + y, isDebug, gamestate);
	}
	
	/**
	 * Returns a piece acquired from adding some some offset
	 * values x and y to the coordinates of the starting piece.
	 * Further used to check what piece (if any) is in the
	 * square another piece is about to go to.
	 * @param pos         position of the starting piece
	 * @param x           the up and down movement
	 * @param y           the left and right movement
	 * @param isDebug     is debug mode active
	 * @param gamestate   current game state
	 * @return            piece found at location, null if invalid
	 */
	public AbstractPiece getTargetPiece(Position pos, int x, int y, boolean isDebug, AbstractPiece[][] gamestate){
		if(pos.getXpos()+x > 7 || pos.getXpos() < 0 || pos.getYpos() > 7 || pos.getYpos() < 0 || pos == null){
			if(isDebug)
				System.out.println("Invalid relative piece position.");
			return null;
		}
		if(getPiece(pos.getXpos() + x, pos.getYpos() + y, isDebug, gamestate) == null){
			if(isDebug)
				System.out.println("Relative piece is off the board.");
			return null;
		}
		return getPiece(pos.getXpos() + x, pos.getYpos() + y, isDebug, gamestate);
	}
	/**
	 * Checks if a specified coordinates are on the board
	 * @param x   x coordinate
	 * @param y   y coordinate
	 * @return    true if legal position, false if not
	 */
	public boolean isOnBoard(int x, int y){
		return !(x > 7 || x < 0 || y > 7 || y < 0);
	}
	
	/**
	 * Checks if a specific Position object's position is on the board
	 * @param pos  position to be checked
	 * @return     true if legal position, false if not
	 */
	public boolean isOnBoard(Position pos){
		return !(pos.getXpos() > 7 || pos.getXpos() < 0 || pos.getYpos() > 7 || pos.getYpos() < 0);
	}

	/**
	 * Very brute function to place a piece in the specified coordinates x and y of game state and return the new game state.
	 * Does not take into consideration any restrictions or rules on movement, will literally
	 * place any piece anywhere on the board.
	 * Should be mostly used on empty boards, or if 100% sure
	 * that the tile in which the piece is about to placed is unoccupied.
	 * !!!BE VERY CAREFUL WHEN USING THIS!!!
	 * @param p          piece to be place
	 * @param x          x coordinate of the target tile
	 * @param y          y coordinate of the target tile
	 * @param isDebug    is debug mode active
	 * @param gamestate  game state to edit
	 * @return           game state with the piece placed
	 */
	public AbstractPiece[][] placePiece(AbstractPiece p, int x, int y, boolean isDebug, AbstractPiece[][] gamestate){
		if(!isOnBoard(p.getPosition())&&!isOnBoard(new Position(x, y))){//checks if the piece coordinates and target tile are not off the board
			if(isDebug)
				System.out.println("Invalid piece place!");
			return null;
		}
		AbstractPiece[][] newState = gamestate;
		newState[x][y]=p;
		return newState;
	}
	/**
	 * Very brute function to place a piece in the specified position pos of the game state and return the new game state.
	 * Does not take into consideration any restrictions or rules on movement, will literally
	 * place any piece anywhere on the board.
	 * Should be mostly used on empty boards, or if 100% sure
	 * that the tile in which the piece is about to placed is unoccupied.
	 * !!!BE VERY CAREFUL WHEN USING THIS!!!
	 * @param p          piece to be place
	 * @param pos        position of the target tile
	 * @param isDebug    is debug mode active
	 * @param gamestate  game state to edit
	 * @return           game state with the piece placed
	 */
	public AbstractPiece[][] placePiece(AbstractPiece p, Position pos, boolean isDebug, AbstractPiece[][] gamestate){
		if(!isOnBoard(p.getPosition())&&!isOnBoard(pos)){//checks if the piece coordinates and target tile are not off the board
			if(isDebug)
				System.out.println("Invalid piece place!");
			return null;
		}
		AbstractPiece[][] newState = gamestate;
		newState[pos.getXpos()][pos.getYpos()]=p;
		return newState;
	}
	
	/**
	 * Very brute function to move a piece in the game state and return the new game state.
	 * Does not take into consideration any restrictions or rules on movement, will literally
	 * move any piece anywhere on the board. DOES NOT CHANGE THE COORDINATES IN THE PEACE
	 * OBJECT ITSELF!
	 * 
	 * !!!BE VERY CAREFUL WHEN USING THIS!!!
	 * @param p           piece to be moved
	 * @param gamestate   game state to be edited
	 * @param xNew		  x coordinate of piece's new position
	 * @param yNew        y coordinate of piece's new position
	 * @return            game state with the piece moved
	 */
	
	public AbstractPiece[][] relocatePiece(AbstractPiece p, AbstractPiece[][] gamestate, int xNew, int yNew){ 
		
		int oldX=p.getXpos();
		int oldY=p.getYpos();
		
		AbstractPiece[][] newGamestate = gamestate; //not sure if I need this
		newGamestate[xNew][yNew]=p;
		newGamestate[oldX][oldY]=new BlankPiece("Blank", oldX, oldY);
		
		return newGamestate;
	}
	
	/**
	 * Very brute function to move a piece in the game state and return the new game state.
	 * Does not take into consideration any restrictions or rules on movement, will literally
	 * move any piece anywhere on the board.
	 * !!!BE VERY CAREFUL WHEN USING THIS!!!
	 * @param p           piece to be moved
	 * @param gamestate   game state to be edited
	 * @param newPos      new position for the piece
	 * @return            game state with the piece moved
	 */
	
	public AbstractPiece[][] relocatePiece(AbstractPiece p, AbstractPiece[][] gamestate, Position newPos){ 
		
		int oldX=p.getXpos();
		int oldY=p.getYpos();
		
		AbstractPiece[][] newGamestate = gamestate; //not sure if I need this
		newGamestate[newPos.getXpos()][newPos.getYpos()]=p;
		newGamestate[oldX][oldY]=new BlankPiece("Blank", oldX, oldY);
		
		return newGamestate;
	}
}
