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

}
