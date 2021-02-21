package Networking;

import CrazyChess.logic.MainLogic;
import CrazyChess.pieces.AbstractPiece;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class Server implements Runnable{

    ServerSocket ss;
    Socket whitePlayer;
    Socket blackPlayer;
    MainLogic game;

    ObjectInputStream whiteInput;
    ObjectInputStream blackInput;


    ObjectOutputStream whiteOutput;
    ObjectOutputStream blackOutput;
    public Server(){

    }

    @Override
    public void run() {
        try{
            ss = new ServerSocket(5000);
            System.out.println("Listening on port 5000");

            //Wait for White player to connect
            whitePlayer = ss.accept();
            whiteInput = new ObjectInputStream(whitePlayer.getInputStream());
            whiteOutput = new ObjectOutputStream(whitePlayer.getOutputStream());
            System.out.println("White player connected");

            /*//Wait for BlackPlayer to Connect
            blackPlayer = ss.accept();
            blackInput = new ObjectInputStream(blackPlayer.getInputStream());
            blackOutput = new ObjectOutputStream(blackPlayer.getOutputStream());
            System.out.println("Black player connected");
            */
            game = new MainLogic();
            game.resetBoard();
            game.printGameState();
            waitWhite();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void waitWhite(){
        while(true){
            try {
                Move move = (Move) whiteInput.readObject();
                AbstractPiece piece = game.getGamestate()[move.getStart().getXpos()][move.getStart().getYpos()];
                int endX = move.getEnd().getXpos();
                int endY = move.getEnd().getXpos();
                boolean moved = game.moveTo(piece, endX,endY);
                if(moved){
                    System.out.println("Server: valid move");
                }else{
                    System.out.println("Server: invalid move");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

