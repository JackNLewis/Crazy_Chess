package Networking;

import CrazyChess.pieces.AbstractPiece;
import Graphics.GameScreenClient;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class ClientReciever implements Runnable{

    ObjectInputStream input;
    GameScreenClient gameScreen;
    Client parentClient;

    public ClientReciever(ObjectInputStream input,GameScreenClient gameScreen,Client parentClient){
        this.gameScreen = gameScreen;
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
                   }
               });

           }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
