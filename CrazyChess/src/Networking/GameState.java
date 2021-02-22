package Networking;

import CrazyChess.pieces.AbstractPiece;

import java.io.Serializable;

public class GameState implements Serializable {
    private AbstractPiece[][] gameState;
    private boolean isTurn;
    private String debug;


    public GameState(AbstractPiece[][] gameState,boolean isTurn){
        this.gameState = gameState;
        this.isTurn = isTurn;
    }

    public GameState(AbstractPiece[][] gameState,boolean isTurn,String debug){
        this.gameState = gameState;
        this.isTurn = isTurn;
        this.debug = debug;
    }
    public boolean isTurn() {
        return isTurn;
    }

    public String getDebug() {
        return debug;
    }

    public AbstractPiece[][] getGameState() {
        return gameState;
    }
}
