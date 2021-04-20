package Networking;

import CrazyChess.pieces.AbstractPiece;

import java.io.Serializable;

public class GameState implements Serializable {
    private AbstractPiece[][] gameState;
    private int turnNo;
    private boolean isTurn;
    private String player;
    private String checkMate;
    private String check;

    public GameState(AbstractPiece[][] gameState,boolean isTurn,int turnNo){
        this.gameState = gameState;
        this.isTurn = isTurn;
        this.turnNo = turnNo;
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

    public int getTurnNo(){
        return turnNo;
    }

    public AbstractPiece[][] getGameState() {
        return gameState;
    }

    public GameState setCheckMate(String player){
        this.checkMate = player;
        return this;
    }

    public GameState setCheck(String player){
        this.check = player;
        return this;
    }

    public String getCheckMate(){
        return checkMate;
    }

    public String getCheck(){
        return check;
    }
}
