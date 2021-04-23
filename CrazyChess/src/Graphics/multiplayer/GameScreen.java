package Graphics.multiplayer;

import CrazyChess.logic.MainLogic;
import CrazyChess.pieces.AbstractPiece;
import Graphics.AskForDraw;
import Graphics.MenuScreen;
import Networking.Client;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GameScreen {

    private Scene scene;
    private VBox root;
    private Client client;
    private Label playerLabel;
    private Board board;
    private HBox boardContainer;
    Stage stage;
    private Label infoMessage;
    private AskForDraw askForDraw;
    private MainLogic game;

    public GameScreen(Stage stage, Client client){
        this.stage = stage;
        this.client = client;
        root = new VBox();
        // root.setSpacing(20);
        scene = new Scene(root,670,600);
        scene.getStylesheets().add("/Graphics/css/board.css");
        

        //Add top banner
        addBanner();

        //Add the info message
        infoMessage = new Label();
        infoMessage.getStyleClass().add("info-message");
        root.getChildren().add(infoMessage);
        infoMessage.setText("Debug text");
        
        game = new MainLogic();
        askForDraw = new AskForDraw(this, game, client);
        
        //Add actuall board
        board = new Board(client);
        boardContainer = new HBox();
        boardContainer.setSpacing(20);
        boardContainer.getChildren().addAll(board.getBoard(), askForDraw.getAskForDraw());
        boardContainer.setAlignment(Pos.CENTER);
        root.getChildren().add(boardContainer);


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
               //add close action
            }
        });
    }
    
    public void renderGameState(AbstractPiece[][] gamesState,boolean successMove){
        board.renderGameState(gamesState,successMove);
    }

    public Scene getScene(){
        return this.scene;
    }

    public void addBanner(){
        HBox hbox = new HBox();
        hbox.getStyleClass().add("banner");
        playerLabel = new Label();
        updateMoveLabel();
        playerLabel.getStyleClass().add("banner-text");
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(playerLabel);


        root.getChildren().add(hbox);
    }

    public void updateMoveLabel(){
        if(client.isTurn()){
            playerLabel.setText("Your Turn");
        }else{
            playerLabel.setText("Waiting");
          //  askForDraw.setHide();
        }
        /*
        if(playerLabel.getText().equalsIgnoreCase("White")){
            playerLabel.setText("Black");
        }else{
            playerLabel.setText("White");
        }*/
    }

    public void setInfoMessage(String message){
        this.infoMessage.setText(message);
    }
    public void close(){
        MenuScreen menu = new MenuScreen(stage);
        stage.setScene(menu.getScene());
    }


}
