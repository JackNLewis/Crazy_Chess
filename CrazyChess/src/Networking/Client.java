package Networking;

import CrazyChess.pieces.AbstractPiece;
import Graphics.GameScreenClient;
import Graphics.Tile;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Client implements Runnable{
    Socket socket;
    ObjectOutputStream output;
    ObjectInputStream input;
    String address = "localhost";
    AbstractPiece[][] currrentGameState;
    int port = 5000;
    boolean isTurn;
    Semaphore semaphore;
    String player;
    GameScreenClient gameScreen;

    public Client(Semaphore semaphore,GameScreenClient gameScreen){
        this.semaphore = semaphore;
        this.gameScreen = gameScreen;
    }

    @Override
    public void run() {
        System.out.println("Created Client");
        try {
            socket = new Socket(address, port);

            //Set up output stream
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            System.out.println("Initialized input and output stream");
            //Recieve first game state used to initialise the game
            GameState gs = reciveGameState();
            currrentGameState = gs.getGameState();

            if(gs.isTurn()){
                player = "White";
                semaphore.release();
            }else{
                player = "Black";
                semaphore.release();
                gameStateListener();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMove(Move move){
        try {
            output.writeObject(move);
            /*System.out.println("Send Move: \n" +
                    "From " + move.getStart() +
                    "\nTo " + move.getEnd());*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameStateListener(){
        try {
            GameState gs = (GameState) input.readObject();
            isTurn = gs.isTurn();
            //UPDATE BLACK PLAYERS board
            System.out.println("Black recieved new board");
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    gameScreen.renderGameState(gs.getGameState());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public GameState reciveGameState() {
        try {
            GameState gs = (GameState) input.readObject();
            isTurn = gs.isTurn();
            this.currrentGameState = gs.getGameState();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setTurn(boolean isTurn){
        this.isTurn = isTurn;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public AbstractPiece[][] getCurrentGameState() {
        return currrentGameState;
    }

    public String getPlayer() {
        return player;
    }

    public void close(){
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printGameState(GameState gameState) {
        String line =" ";
        for(int i=0; i<8; i++) {
            System.out.println(line);
            line =" ";
            for(int j=0; j<8; j++) {
                String piece;
                piece="[]";
                if(!gameState.getGameState()[j][i].getColor().equalsIgnoreCase("blank")) {
                    piece=twoLetterPiece(gameState.getGameState()[j][i]);
                }

                line=line+piece;
            }
        }
        System.out.println(line);
    }

    protected String twoLetterPiece(AbstractPiece p) {
        String result = " ";

        if(p.getColor().equalsIgnoreCase("black")) {
            result = "B";
        }else result="W";

        result=result+p.getClass().getSimpleName().charAt(0);

        return result;
    }
}
