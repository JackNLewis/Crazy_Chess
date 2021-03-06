package CrazyChess.logic.StageHazards;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.King;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that holds the functionality to assign stage hazards to gamestates
 *
 */

public class HazardAssigner
{

	final int			hazardInterval	= 5;						// number of go's between hazards spawning
	int					untilHazard;								// number of turns before next hazard
	int					hazardDuration	= 3;						// number of turns hazards last for
	int					hazardTurns		= 0;						// how many turns hazard has been active
	boolean				activeHazard;
	Utilities			utils;
	ExtraChecksAndTools	ecat			= new ExtraChecksAndTools();

	/**
	 * Constructor for the HazzardAssigner class
	 */

	public HazardAssigner()
	{
		utils = new Utilities();
		activeHazard = false;
		untilHazard = hazardInterval;
	}

	/**
	 * Assigns hazards to a desired gamestate
	 * 
	 * @param gameState
	 *            desired gamestate
	 * @return a gamestate with hazards assigned
	 */

	public AbstractPiece[][] assignHazard(AbstractPiece[][] gameState)
	{
		AbstractPiece[][] safeGameState = utils.safeCopyGamestate(gameState);
		// spawn a hazard
		if (untilHazard == 0)
		{
			activeHazard = true;
			hazardTurns = 0;
			untilHazard = hazardInterval;

			// generate which hazard
			Random rand = new Random();
			int randHazardIndex = rand.nextInt(Hazard.values().length);
			Hazard randHazard = Hazard.values()[randHazardIndex];

			//code for randomly choosing hazard
			if (randHazard == Hazard.FROZEN)
			{
				System.out.println("FROZEN");
				return frozenHazard(gameState);
			}
			else if (randHazard == Hazard.BURN)
			{
				System.out.println("BURN");
				return burnHazard(gameState);
			}

		}
		else
		{
			if (activeHazard)
			{
				if (hazardTurns == hazardDuration)
				{// needs to despawn hazard
					hazardTurns = 0;
					System.out.println("DESPAWN HAZARD");
					activeHazard = false;
					return despawn(safeGameState);
				}
				else
				{
					hazardTurns++;
				}
			}
			else
			{
				// no active hazard, no need to spawn hazard
				untilHazard--;
			}
		}
		return gameState;
	}

	/**
	 * Freezes tiles on a desired gamestate
	 * 
	 * @param gameState
	 *            desired gamestate
	 * @return a gamestate with new frozen tiles on it
	 */

	private AbstractPiece[][] frozenHazard(AbstractPiece[][] gameState)
	{
		ArrayList<AbstractPiece> pieces = ecat.gamestateToPieceArrayList(gameState);
		Random rand = new Random();
		AbstractPiece[][] gs = gameState;
		for (int i = 0; i < getThreshhold(pieces.size()); i++)
		{
			// only changes first piece at the minuite
			int index = rand.nextInt(pieces.size());
			while (pieces.get(index) instanceof King)
			{
				index = rand.nextInt(pieces.size());
			}
			HazardPiece frozenTile = new HazardPiece(pieces.get(index).getPosition(), Hazard.FROZEN, pieces.get(index));
			System.out.println("Freezing: " + frozenTile.getOriginalPiece());
			gs = utils.placePiece(frozenTile, false, gameState);
		}
		return gs;
	}

	/**
	 * Sets tiles on fire in a desired gamestate
	 * 
	 * @param gameState
	 *            desired gamestate
	 * @return a gamestate with new burning tiles on it
	 */
	private AbstractPiece[][] burnHazard(AbstractPiece[][] gameState)
	{
		// System.out.println("In burn");
		// player cannot move onto that tile if it is a burnTile but only blank tiles
		// affected

		ArrayList<AbstractPiece> blankPieces = ecat.getBlankArrayList(gameState);
		Random rand = new Random();
		AbstractPiece[][] gs = gameState;
		for (int i = 0; i < getThreshhold(blankPieces.size()); i++)
		{
			int index = rand.nextInt(blankPieces.size());
			HazardPiece burnTile = new HazardPiece(blankPieces.get(index).getPosition(), Hazard.BURN, blankPieces.get(index));
			System.out.println("Burning: " + burnTile.getOriginalPiece());
			System.out.println(burnTile.getHazard().toString());
			gs = utils.placePiece(burnTile, false, gameState);
		}
		utils.printGameState(gameState);
		return gs;
	}

	/**
	 * Despawns hazzards from a desired gamestate
	 * 
	 * @param gameState
	 *            desired gamestate
	 * @return gamestate without any stage hazards
	 */
	private AbstractPiece[][] despawn(AbstractPiece[][] gameState)
	{
		System.out.println("=======================================");
		System.out.println("Despawn the stage hazards");
		AbstractPiece[][] gs = utils.safeCopyGamestate(gameState);
		for (AbstractPiece[] row : gs)
		{
			for (AbstractPiece p : row)
			{
				if (p instanceof HazardPiece)
				{
					AbstractPiece org = ((HazardPiece) p).getOriginalPiece();
					System.out.println("Original piece: " + org);
					utils.placePiece(org, org.getPosition(), false, gs);
				}
			}
		}
		utils.printGameState(gs);
		System.out.println("=======================================");
		return gs;
	}

	/**
	 * Method is used to decide how many tiles on the board will be affected by a stage hazard, depending on
	 * 
	 * @param size
	 *            the number of tiles a stage hazard could affect
	 * @return the number of tiles stage hazard will affect
	 */

	private int getThreshhold(int size)
	{
		if (size > 24)
		{
			return 3;
		}
		else if (size > 12)
		{
			return 2;
		}
		else if (size > 5)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

}
