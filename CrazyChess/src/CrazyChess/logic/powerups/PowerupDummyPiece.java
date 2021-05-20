package CrazyChess.logic.powerups;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.*;

/**
 * Class for the Dummy Piece powerup
 * 
 */

public class PowerupDummyPiece
{
	Utilities			utils	= new Utilities();
	ExtraChecksAndTools	ecat	= new ExtraChecksAndTools();

	/**
	 * Turn one pawn into a bishop or knight on the board.
	 * 
	 * @param gamestate
	 *            gamestate to be altered
	 * @param target1
	 *            Dummy piece type
	 * @param target2
	 *            blank
	 * @param isDebug
	 *            is debug mode active
	 * @return the altered gamestate, where target1 turn into target2. NULL if the change is not possible
	 */

	//include checks vs moving enemy pieces and powerups

	public AbstractPiece[][] Dummy(AbstractPiece[][] gamestate, Position target1, Position target2, Boolean isDebug)
	{
		if (!(utils.isOnBoard(target1)) && !(utils.isOnBoard(target2)))
		{
			if (isDebug)
				System.out.println("Target for Dummy Piece is coordinate that is not on the board. Returning NULL");
			return null;
		}

		AbstractPiece gamestateCopy[][] = utils.safeCopyGamestate(gamestate);
		AbstractPiece piece1 = utils.getPiece(target1, isDebug, gamestateCopy);
		AbstractPiece piece2 = utils.getPiece(target2, isDebug, gamestateCopy);

		if ((piece1 instanceof King))
		{
			if (isDebug)
				System.out.println("Dummy piece cant be the King. Returning NULL");
			return null;
		}
		if (!piece2.getColor().equalsIgnoreCase("blank"))
		{
			if (isDebug)
				System.out.println("Cannot add a Dummy piece on other Pieces. Returning NULL");
			return null;
		}

		if ((piece1 instanceof Bishop))
			gamestateCopy = utils.placePiece(new Bishop(piece1.getColor(), piece2.getXpos(), piece2.getYpos(), "Dummy"), isDebug,
					gamestateCopy);
		if ((piece1 instanceof Knight))
			gamestateCopy = utils.placePiece(new Knight(piece1.getColor(), piece2.getXpos(), piece2.getYpos(), "Dummy"), isDebug,
					gamestateCopy);
		if ((piece1 instanceof Pawn))
			gamestateCopy = utils.placePiece(new Pawn(piece1.getColor(), piece2.getXpos(), piece2.getYpos(), "Dummy"), isDebug,
					gamestateCopy);
		if ((piece1 instanceof Rook))
			gamestateCopy = utils.placePiece(new Rook(piece1.getColor(), piece2.getXpos(), piece2.getYpos(), "Dummy"), isDebug,
					gamestateCopy);
		if ((piece1 instanceof Queen))
			gamestateCopy = utils.placePiece(new Queen(piece1.getColor(), piece2.getXpos(), piece2.getYpos(), "Dummy"), isDebug,
					gamestateCopy);

		return gamestateCopy;
	}
}
