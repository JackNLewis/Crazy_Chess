package Networking;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Utilities;
import CrazyChess.pieces.AbstractPiece;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{

    ServerSocket ss;
    Socket whitePlayer;
    Socket blackPlayer;
    MainLogic game;
    Utilities utils;

    ObjectInputStream whiteInput;
    ObjectInputStream blackInput;


    ObjectOutputStream whiteOutput;
    ObjectOutputStream blackOutput;
    public Server(){
        utils = new Utilities();
    }

    @Override
    public void run() {
        try{
            ss = new ServerSocket(5000);
            System.out.println("Listening on port 5000");
            game = new MainLogic();
            game.resetBoard();

            //Wait for White player to connect
            whitePlayer = ss.accept();
            whiteInput = new ObjectInputStream(whitePlayer.getInputStream());
            whiteOutput = new ObjectOutputStream(whitePlayer.getOutputStream());
            System.out.println("White player connected");

            //Set white players turn to true
            whiteOutput.writeObject(new GameState(game.getGamestate(),true,"white"));


            //Wait for BlackPlayer to Connect
            blackPlayer = ss.accept();
            blackInput = new ObjectInputStream(blackPlayer.getInputStream());
            blackOutput = new ObjectOutputStream(blackPlayer.getOutputStream());
            System.out.println("Black player connected");

            //set black players turn to false
            blackOutput.writeObject(new GameState(game.getGamestate(),false,"black"));


            //wait for white to make a move
            waitWhite();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void waitWhite() {
        while (true) {
            try {
                Move move = (Move) whiteInput.readObject();

                AbstractPiece p = game.getPiece(move.getStart());
                int endX = move.getEnd().getXpos();
                int endY = move.getEnd().getYpos();

                boolean moved = game.moveTo(game.getPiece(move.getStart()), endX, endY);

                if (moved) {
                    System.out.println("Server: valid move");
                    whiteOutput.reset();
                    blackOutput.reset();
                    whiteOutput.writeObject(new GameState(game.getGamestate(),false));
                    blackOutput.writeObject(new GameState(game.getGamestate(),true));

                    waitBlack();
                } else {
                    whiteOutput.writeObject(new GameState(game.getGamestate(),true));
                    System.out.println("Server: invalid move");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void waitBlack(){
        while(true){
            try {
                Move move = (Move) blackInput.readObject();

                AbstractPiece p = game.getPiece(move.getStart());
                int endX = move.getEnd().getXpos();
                int endY = move.getEnd().getYpos();

                boolean moved = game.moveTo(game.getPiece(move.getStart()), endX,endY);

                if(moved){
                    System.out.println("Server: valid move");
                    whiteOutput.reset();
                    blackOutput.reset();
                    whiteOutput.writeObject(new GameState(game.getGamestate(),true));
                    blackOutput.writeObject(new GameState(game.getGamestate(),false));
                    waitWhite();
                }else{
                    blackOutput.writeObject(new GameState(game.getGamestate(),true));
                    System.out.println("Server: invalid move");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



}

