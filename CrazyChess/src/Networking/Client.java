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
    String address = "localhost";
    int port = 5000;
    ObjectOutputStream output;

    Semaphore semaphore;

    GameScreenClient gameScreen;
    ClientReciever clientReciever;
    String player;
    boolean isTurn;
    AbstractPiece[][] currrentGameState;


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
            //input = new ObjectInputStream(socket.getInputStream());

            init();


            /*
            //Recieve first game state used to initialise the game
            GameState gs = reciveGameState();
            currrentGameState = gs.getGameState();
            */
            /*/
            if(gs.isTurn()){
                player = "White";
                semaphore.release();
            }else{
                player = "Black";
                semaphore.release();
                gameStateListener();

            }
             */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        GameState initialGameState = (GameState) input.readObject();
        this.player = initialGameState.getPlayer();
        this.isTurn = initialGameState.isTurn();
        currrentGameState = initialGameState.getGameState();
        System.out.println("Initilaized Client");
        semaphore.release();
        //Creates a client reader thread to recieve gamestates
        clientReciever = new ClientReciever(input ,gameScreen,this);
        Thread thread = new Thread(clientReciever);
        thread.start();
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

    /*
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

    /*
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
     */

/*
    public void setTurn(boolean isTurn){
        this.isTurn = isTurn;
    }
*/
    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn){
        this.isTurn = turn;
    }

    public AbstractPiece[][] getCurrentGameState() {
        return this.currrentGameState;
    }

    public void setCurrrentGameState(AbstractPiece[][] currrentGameState) {
        this.currrentGameState = currrentGameState;
    }

    public String getPlayer() {
        return this.player;
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
