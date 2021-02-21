package Networking;

import CrazyChess.pieces.AbstractPiece;

import java.io.Serializable;

public class GameState implements Serializable {
    private AbstractPiece[][] gameState;

    public GameState(AbstractPiece[][] gameState){
        this.gameState = gameState;
    }
}
