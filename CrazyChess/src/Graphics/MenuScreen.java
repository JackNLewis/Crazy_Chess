package Graphics;

import Graphics.multiplayer.GameScreen;
import Networking.Client;
import Networking.Server;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

public class MenuScreen {

    private VBox buttons;
    private Scene scene;
    private Stage stage;

    public MenuScreen(Stage stage){
        buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        scene = new Scene(buttons,500,600); // make 500,600
        this.stage = stage;
        addButtons(buttons);
        stage.setResizable(false);
    }

    public void addButtons(VBox root){

        //Add New Game
        Button newButton = new Button("Local Game");
        newButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                SinglePlayer sp = new SinglePlayer(stage);
                stage.setScene(sp.getScene());
            }
        });

        //Add Multiplayer Mode
        Button multiplayer = new Button("Multiplayer");
        addMultiplayerButtons(multiplayer);

        //Add vs AI
        Button VsAI = new Button("Player Vs AI");


        root.getChildren().addAll(newButton,multiplayer,VsAI);
    }

    public void addMultiplayerButtons(Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Removes buttons currently on the scene
                buttons.getChildren().clear();

                //Create a label to display messages
                Label infoLabel = new Label("Test");

                //Create the connect button
                Button connect = new Button("Connect");
                connect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //semaphore used to make sure the client is initilaized first
                        Semaphore semaphore = new Semaphore(0);
                        Client client = new Client(semaphore);
                        Thread thread = new Thread(client);
                        thread.start();

                        try {
                            //Aquire semaphore to show that client has been initialized
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //Check if client has connected to the sever
                        if(client.isConnected()){
                            GameScreen gs = new GameScreen(stage,client);
                            client.setGameScreen(gs);
                            stage.setScene(gs.getScene());
                            stage.show();
                        }else{
                            System.out.println("Not connected");
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                   infoLabel.setText("Failed to connect");
                                }
                            });
                        }
                    }
                });

                //=====================================================================================//
                //Create the host button
                Button host = new Button("Host");
                host.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Server server = new Server();
                        Thread thread = new Thread(server);
                        thread.start();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                infoLabel.setText("Server Started");
                            }
                        });
                    }
                });

                buttons.getChildren().addAll(connect,host,infoLabel);
            }
        });
    }

    public Scene getScene(){
        return this.scene;
    }
}
