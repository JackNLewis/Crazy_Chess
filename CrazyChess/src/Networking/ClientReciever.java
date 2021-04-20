package Networking;

import Graphics.multiplayer.GameScreen;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;


public class ClientReciever implements Runnable{

    ObjectInputStream input;
    GameScreen gameScreen;
    Client client;

    public ClientReciever(ObjectInputStream input,Client client){
        this.input = input;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("Created Client Reciever");
            while(true){
               GameState gs = (GameState) input.readObject();
               if(gs.getCheck()!=null){
                   System.out.println(gs.getCheck() + " is in check");
               }
               client.setCurrrentGameState(gs.getGameState());
               boolean success = client.isTurn() != gs.isTurn();
               String winner;
               if(gs.getCheckMate() != null){
                   winner = (gs.getCheckMate().equals("white")) ? "Black" : "White";
               }else{
                   winner = null;
               }
               client.setTurn(gs.isTurn());
               client.setTurnNo(gs.getTurnNo());
               Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
                       if(gs.getCheck()!=null){
                          gameScreen.setInfoMessage(gs.getCheck() + " in check");
                       }
                       if(winner != null){
                           gameScreen.setInfoMessage(winner + " Wins!");
                       }
                       gameScreen.renderGameState(gs.getGameState(),success);
                       gameScreen.updateMoveLabel();
                   }
               });

           }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setGameScreen(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }


}
