package Graphics;

import Graphics.multiplayer.GameScreen;
import Networking.Client;
import Networking.Server;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

public class MenuScreen {

    private VBox buttons;
    private Scene scene;
    private Stage stage;
    
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    
    private boolean isSelected1;
    private boolean isSelected2;
    private boolean isSelected3;

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
            	//booleans for rule changes
            	isSelected1 = cb1.isSelected();
            	isSelected2 = cb2.isSelected();
            	isSelected3 = cb3.isSelected();
            	
                SGameScreen sp = new SGameScreen(stage);
                
                //setting rule changes
                sp.setRC1(isSelected1);
                sp.setRC2(isSelected2);
                sp.setRC3(isSelected3);
                System.out.println("rulechange 1 selected " + isSelected1);
                System.out.println("rulechange 2 selected " + isSelected2);
                System.out.println("rulechange 3 selected " + isSelected3);
                
                stage.setScene(sp.getScene());
            }
        });

        //Add Multiplayer Mode
        Button multiplayer = new Button("Multiplayer");
        addMultiplayerButtons(multiplayer);

        //Add vs AI
        Button VsAI = new Button("Player Vs AI");
        VsAI.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	isSelected1 = cb1.isSelected();
            	isSelected2 = cb2.isSelected();
            	isSelected3 = cb3.isSelected();
            	
                SGameScreen sp = new SGameScreen(stage);
                
                sp.setRC1(isSelected1);
                sp.setRC2(isSelected2);
                sp.setRC3(isSelected3);
                System.out.println("rulechange 1 selected " + isSelected1);
                System.out.println("rulechange 2 selected " + isSelected2);
                System.out.println("rulechange 3 selected " + isSelected3);
                
                sp.getBoard().enableAI();
                stage.setScene(sp.getScene());
            }
        });
        
        Label title = new Label("Which rule changes do you want to play with?");
        
        //checkboxes for rule changes
        cb1 = new CheckBox("Bishop-Rook switch");
        cb2 = new CheckBox("Pawns can go backwards");
        cb3 = new CheckBox("Kings move like Queens");

        cb1.setSelected(true);
        cb2.setSelected(true);
        cb3.setSelected(true);
        
        root.getChildren().addAll(newButton,multiplayer,VsAI,title,cb1,cb2,cb3);
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
