package Networking;

import Graphics.GameScreen;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;


public class ClientReciever implements Runnable{

    ObjectInputStream input;
    GameScreen gameScreen;
    Client parentClient;

    public ClientReciever(ObjectInputStream input,Client parentClient){
        this.input = input;
        this.parentClient = parentClient;
    }

    @Override
    public void run() {
        try {
            System.out.println("Created Client Reciever");
           while(true){
               GameState gs = (GameState) input.readObject();
               parentClient.setCurrrentGameState(gs.getGameState());
               boolean success = parentClient.isTurn() != gs.isTurn();
               parentClient.setTurn(gs.isTurn());
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
