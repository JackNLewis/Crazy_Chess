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
    int turnNo;
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
            turnNo = game.getTurnNo();
            game.printGameState();
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
                boolean check = game.getCheckStatus("black");
                boolean checkMate = game.getMateStatus("black");

                if (moved) {
                    System.out.println("Server: valid move");
                    whiteOutput.reset();
                    blackOutput.reset();

                    GameState whiteGs = new GameState(game.getGamestate(),false,game.getTurnNo());
                    GameState blackGs = new GameState(game.getGamestate(),true, game.getTurnNo());


                    if(check){
                        //black is in check
                        System.out.println("Server: black is in check");
                        whiteGs.setCheck("black");
                        blackGs.setCheck("black");
                    }

                    whiteOutput.writeObject(whiteGs);
                    blackOutput.writeObject(blackGs);

                    waitBlack();
                } else {
                    whiteOutput.reset();
                    GameState gs = new GameState(game.getGamestate(),true, game.getTurnNo());
                    if (check) {
                        gs.setCheck("black");
                    }
                    whiteOutput.writeObject(gs);
                    System.out.println("Server: invalid move");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }catch(ClassNotFoundException e){
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
                boolean check = game.getCheckStatus("white");
                boolean checkMate = game.getMateStatus("white");

                if(moved){

                    System.out.println("Server: valid move");
                    whiteOutput.reset();
                    blackOutput.reset();

                    GameState whiteGs = new GameState(game.getGamestate(),true,game.getTurnNo());
                    GameState blackGs = new GameState(game.getGamestate(),false, game.getTurnNo());


                    if(check){
                        //black is in check
                        System.out.println("Server: white is in check");
                        whiteGs.setCheck("white");
                        blackGs.setCheck("white");
                    }

                    whiteOutput.writeObject(whiteGs);
                    blackOutput.writeObject(blackGs);
                    waitWhite();
                }else{
                    blackOutput.reset();
                    GameState gs = new GameState(game.getGamestate(),true, game.getTurnNo());
                    if(check){
                        gs.setCheck("white");
                    }
                    blackOutput.writeObject(gs);
                    System.out.println("Server: invalid move");
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



}

