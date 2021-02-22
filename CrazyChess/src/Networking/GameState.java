package Networking;

import CrazyChess.pieces.AbstractPiece;

import java.io.Serializable;

public class GameState implements Serializable {
    private AbstractPiece[][] gameState;
    private boolean isTurn;
    private String player;

    public GameState(AbstractPiece[][] gameState,boolean isTurn){
        this.gameState = gameState;
        this.isTurn = isTurn;
    }

    public GameState(AbstractPiece[][] gameState,boolean isTurn,String debug){
        this.gameState = gameState;
        this.isTurn = isTurn;
        this.player = debug;
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
