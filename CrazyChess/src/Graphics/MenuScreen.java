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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Semaphore;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.savegamestate.*;
import CrazyChess.pieces.AbstractPiece;

public class MenuScreen {

    private VBox buttons;
    private Scene scene;
    private Stage stage;
    
//    MainLogic game = new MainLogic();
//    private AbstractPiece[][] gamestate;

    public MenuScreen(Stage stage){
        buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        scene = new Scene(buttons,500,600); // make 500,600
        scene.getStylesheets().add("/Graphics/css/menu.css");
        this.stage = stage;


        //Add Banner Image
        ImageView img = new ImageView();
        img.setImage(new Image("/resources/menu_text.png"));
        img.setFitWidth(400);
        img.setPreserveRatio(true);
        buttons.getChildren().add(img);
        addButtons(buttons);
        stage.setResizable(false);
    }

    public void addButtons(VBox root){

        //Add New Game
        Button newButton = new Button("Local Game");
        newButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	buttons.getChildren().clear();
            	
            	Button load = new Button("Load Game");
            	load.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						
					    File file = new File("saved.xml");
					    SaveGame loadState = new SaveGame();
					   
					    byte[] bytes = loadState.loadDataFromFile(file);
//						System.out.println("converted to bytes successfully");
					    MainLogic game = new MainLogic();
					    
						try {
//							System.out.println("tried loading");
//							loadState.load(bytes);
//							MainLogic game = new MainLogic();
							loadState.load(bytes, game, game.getGamestate());
							game.setGamestate(game.getGamestate());
							
//							game.printGameState();
							
			                SGameScreen sp = new SGameScreen(game, stage);
			                stage.setScene(sp.getScene());
							System.out.println("Loaded successfully");
							
						} catch (Exception exc) {
							System.out.println("Couldn't load: " + exc.getMessage());
						}
					}
            	});
            	
            	Button newGame = new Button("New Game");
            	newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MainLogic newgame = new MainLogic();
						newgame.resetBoard();
						SGameScreen sp = new SGameScreen(newgame, stage);
//						sp.loadLogic(newgame);
		                stage.setScene(sp.getScene());
					}
            	});
            	buttons.getChildren().addAll(load,newGame);
            };
        });
        
        
        //Add Multiplayer Mode
        Button multiplayer = new Button("Multiplayer");
        addMultiplayerButtons(multiplayer);

        //Add vs AI
        Button VsAI = new Button("Player Vs AI");
        VsAI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	MainLogic newgame = new MainLogic();
            	newgame.resetBoard();
                SGameScreen sp = new SGameScreen(newgame, stage);
//                sp.loadLogic(newgame);
                sp.getBoard().enableAI();
                stage.setScene(sp.getScene());
            }
        });

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
