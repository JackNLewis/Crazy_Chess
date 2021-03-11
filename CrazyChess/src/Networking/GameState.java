package Networking;

import CrazyChess.pieces.AbstractPiece;

import java.io.Serializable;

public class GameState implements Serializable {
    private AbstractPiece[][] gameState;
    private boolean isTurn;
    private String player;
    private boolean checkMate;
    private boolean check;

    public GameState(AbstractPiece[][] gameState,boolean isTurn){
        this.gameState = gameState;
        this.isTurn = isTurn;
    }

    public GameState(AbstractPiece[][] gameState,boolean isTurn,String player){
        this.gameState = gameState;
        this.isTurn = isTurn;
        this.player = player;
    }
    public boolean isTurn() {
        return isTurn;
    }

    public String getPlayer() {
        return player;
    }

    public AbstractPiece[][] getGameState() {
        return gameState;
    }


}
