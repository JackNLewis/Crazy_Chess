package Networking;

import Graphics.GameScreen;
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

               client.setCurrrentGameState(gs.getGameState());
               boolean success = client.isTurn() != gs.isTurn();
               client.setTurn(gs.isTurn());
               client.setTurnNo(gs.getTurnNo());
               Platform.runLater(new Runnable() {
                   @Override
                   public void run() {
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
