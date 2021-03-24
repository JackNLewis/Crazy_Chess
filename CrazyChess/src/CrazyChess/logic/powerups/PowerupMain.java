package CrazyChess.logic.powerups;

import CrazyChess.pieces.*;
import CrazyChess.logic.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class PowerupMain
{
	Utilities utils = new Utilities();
	ExtraChecksAndTools ecat = new ExtraChecksAndTools();
	/**
	 * Uses the desired   powerup and returns an altered gamestate
	 * @param powerup     powerup to be used
	 * @param gamestate   gamestate to alter
	 * @param target1     position of the first piece
	 * @param target2     position of the second piece (can be NULL)
	 * @param turnNo	  number representing which turn is it
	 * @param currentTurn string representing which player's turn is it (black or white)
	 * @param isDebug     is debug mode active
	 * @return
	 */
	public AbstractPiece[][] powerupAssigner(String powerup, AbstractPiece[][] gamestate, Position target1, 
												 Position target2, int turnNo, String currentTurn, boolean isDebug) {
		AbstractPiece[][] copiedGamestate = utils.safeCopyGamestate(gamestate);
		
		//add an if statement for each new powerup, and pass it what it needs :)
		if(powerup.equalsIgnoreCase("teleport")) {
			PowerupTeleport teleport = new PowerupTeleport();
			copiedGamestate=teleport.teleport(copiedGamestate, target1, target2, isDebug);
		}
		
		if(powerup.equalsIgnoreCase("minipromote")) {
			PowerupMiniPromote promote = new PowerupMiniPromote();
			copiedGamestate=promote.promote(copiedGamestate, target1,target2, isDebug);
		}
		if(powerup.equalsIgnoreCase("freecard")) {
			PowerupFreeCard freecard = new PowerupFreeCard();
			copiedGamestate=freecard.freecard(copiedGamestate, target1,target2, isDebug);
		}
		if(powerup.equalsIgnoreCase("bomb")) {
			PowerupBomb bomb = new PowerupBomb();
			copiedGamestate=bomb.bomb(copiedGamestate, target1,target2, isDebug);
		}
		if(powerup.equalsIgnoreCase("dummypiece")) {
			PowerupDummyPiece dummy = new PowerupDummyPiece();
			copiedGamestate=dummy.Dummy(copiedGamestate, target1,target2, isDebug);
		}
		
			
		return copiedGamestate;
		
	}
	/**
	 * Returns a gamestate with a pseudo-randomly placed powerup every 5 turns
	 * @param gamestate   current gamestate
	 * @param turnNo	  current turn number
	 * @param isDebug     is debug mode active
	 * @return            gamestate with the powerup on it
	 */
	public AbstractPiece[][] powerupSpawn(AbstractPiece[][] gamestate, int turnNo, boolean isDebug){
		
		AbstractPiece[][] copiedGamestate=utils.safeCopyGamestate(gamestate);
		AbstractPiece piecePw = null;
		ArrayList<AbstractPiece> Bx = ecat.gamestateToPieceArrayList(gamestate);
		for(AbstractPiece x : Bx) {
			if(!(x.getPoweruptype().equalsIgnoreCase("normal"))) {
				piecePw = x;
			}
		}
		
		if(turnNo%5==0) {
			int randomX = ThreadLocalRandom.current().nextInt(0,7+1); //+1 because the method is exclusive
			int randomY = ThreadLocalRandom.current().nextInt(0,7+1);
			AbstractPiece placeToSpawn = utils.getPiece(randomX, randomY, isDebug, copiedGamestate);
			while(!(placeToSpawn instanceof BlankPiece)) {
				randomX = ThreadLocalRandom.current().nextInt(0,7+1); //+1 because the method is exclusive
				randomY = ThreadLocalRandom.current().nextInt(0,7+1);
				placeToSpawn = utils.getPiece(randomX, randomY, isDebug, copiedGamestate);
			}
			
		
			Powerup pw = new Powerup(randomX, randomY,"Normal");
			copiedGamestate=utils.placePiece(pw, isDebug, copiedGamestate);
				}
		if(piecePw != null) {
			copiedGamestate=utils.placePiece(piecePw, isDebug, copiedGamestate);
		}
		
		return copiedGamestate;
	}
	
	/**
	 * Returns a string representation of a random powerup. Should be used to get a random powerup when a powerup piece 
	 * gets captured on the board, then added to whitePowerUps or blackPowerUps ArrayList.
	 * @return    string representation of a random powerup
	 */
	public String randomPowerup(boolean isDebug) {
		int random = ThreadLocalRandom.current().nextInt(1,5+1);  //increase the max here for each new powerup
		switch (random) { //Add a case for each new powerup
		   case 1:
			   return "DummyPiece";
		   case 2:
			   return "Teleport";
		   case 3:
			   return "Bomb";
		   case 4:
			   return "MiniPromote";
		   case 5:
			   return "FreeCard";

		}


		if (isDebug) System.out.println("Something went wrong when returning a random powerup. Returning NULL");
		return null;
	}
	
	public ArrayList<Position> validPowerupMoves (String powerup, AbstractPiece[][] gamestate, Position target1, boolean isDebug){
		
		ArrayList<Position> moves = new ArrayList<Position>(); //ArrayList to store valid moves
		
		if(powerup.equalsIgnoreCase("teleport")) {
			AbstractPiece target = utils.getPiece(target1, isDebug, gamestate); //The piece power up is used on
			if(target.getColor().equalsIgnoreCase("white")) {		
				ArrayList<AbstractPiece> wp = ecat.getWhitePieces(gamestate);
				for(AbstractPiece p : wp) {
					if(!p.getPosition().equals(target.getPosition())) {//add all of the positions of the pieces of the same color as target, except the position of target itself
						moves.add(p.getPosition());
					}
				}
			}else if (target.getColor().equalsIgnoreCase("black")){
				ArrayList<AbstractPiece> bp = ecat.getBlackPieces(gamestate);
				for(AbstractPiece p : bp) {
					if(!p.getPosition().equals(target.getPosition())) {//add all of the positions of the pieces of the same color as target, except the position of target itself
						moves.add(p.getPosition());
					}
				}
				
			}
		}
		else if(powerup.equalsIgnoreCase("minipromote")) {
			AbstractPiece target = utils.getPiece(target1, isDebug, gamestate); //The piece power up is used on		
				ArrayList<AbstractPiece> wp = ecat.getWhitePieces(gamestate);
				for(AbstractPiece p : wp) {
					if(!p.getPosition().equals(target.getPosition()) && ((p instanceof Knight)||(p instanceof Bishop))) {
						moves.add(p.getPosition());
					}
				}
				ArrayList<AbstractPiece> bp = ecat.getBlackPieces(gamestate);
				for(AbstractPiece p : bp) {
					if(!p.getPosition().equals(target.getPosition()) && ((p instanceof Knight)||(p instanceof Bishop))) {
						moves.add(p.getPosition());
					}
				}
		}
		else if(powerup.equalsIgnoreCase("freecard")) {
			AbstractPiece target = utils.getPiece(target1, isDebug, gamestate); //The piece power up is used on		
				ArrayList<AbstractPiece> Bp = ecat.getBlankArrayList(gamestate);
				for(AbstractPiece p : Bp) {
					if((p.getXpos() >= target.getXpos()-2)&&(p.getXpos() <= target.getXpos()+2)&&
							(p.getYpos() <= target.getYpos()+2)&&(p.getYpos() >= target.getYpos()-2)) {
						moves.add(p.getPosition());
					}
				}
		}
		else if(powerup.equalsIgnoreCase("bomb")) {
			AbstractPiece target = utils.getPiece(target1, isDebug, gamestate); //The piece power up is used on		
				
						moves.add(target.getPosition());
		}
		else if(powerup.equalsIgnoreCase("dummypiece")) {
			AbstractPiece target = utils.getPiece(target1, isDebug, gamestate); //The piece power up is used on		
			ArrayList<AbstractPiece> Allp = ecat.getBlankArrayList(gamestate);
			for(AbstractPiece p : Allp) {
					moves.add(p.getPosition());

			}
			
		}
					
		

		return moves;
	}

	/**
	 * Method returns the initial moves that can be used on a power up
	 * @param powerup
	 * @param gamestate
	 * @param turn
	 * @return
	 */
	public ArrayList<Position> initialPowerupMoves(String powerup, AbstractPiece[][] gamestate,String turn){
		ArrayList<Position> moves= new ArrayList<>();
		//Get the initial piee a teleport can be used on
		if(powerup.equalsIgnoreCase("teleport")){
			//return all the non-king players
			for(AbstractPiece[] arr: gamestate){
				for(AbstractPiece piece: arr){
					if(piece.getColor().equalsIgnoreCase(turn) && !(piece instanceof King)){
						moves.add(piece.getPosition());
					}
				}
			}
		}
		//Return all pawns of same colour because only pawns can be promoted
		else if(powerup.equalsIgnoreCase("minipromote")){
			for(AbstractPiece[] arr: gamestate){
				for(AbstractPiece piece: arr){
					if(piece.getColor().equalsIgnoreCase(turn) && piece instanceof Pawn){
						moves.add(piece.getPosition());
					}
				}
			}
		}
		//return everything but the king
		else if(powerup.equalsIgnoreCase("bomb")){
			for(AbstractPiece[] arr: gamestate){
				for(AbstractPiece piece: arr){
					if(piece.getColor().equalsIgnoreCase(turn) && piece instanceof Pawn){
						moves.add(piece.getPosition());
					}
				}
			}
		}
		//return the king
		else if(powerup.equalsIgnoreCase("freecard")){
			for(AbstractPiece[] arr: gamestate){
				for(AbstractPiece piece: arr){
					if(piece.getColor().equalsIgnoreCase(turn) && piece instanceof King){
						moves.add(piece.getPosition());
						return moves;
					}
				}
			}
		}
		//return all empty spaces on board
		else if(powerup.equalsIgnoreCase("dummypiece")){
			for(AbstractPiece[] arr: gamestate){
				for(AbstractPiece piece: arr){
					if(piece.getColor().equalsIgnoreCase(turn) && !(piece instanceof King)){
						moves.add(piece.getPosition());
					}
				}
			}
		}
		return moves;
	}
}
