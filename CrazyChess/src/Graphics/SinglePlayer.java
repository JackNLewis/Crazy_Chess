package Graphics;

import CrazyChess.logic.MainLogic;
import CrazyChess.pieces.AbstractPiece;
import Networking.Client;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SinglePlayer {

    private Scene scene;
    private VBox root;
    private Label playerLabel;
    private SinglePlayerBoard board;
    private HBox boardContainer;
    private Stage stage;
    private MainLogic game;
    private Label infoMessage;

    public SinglePlayer(Stage stage){
        game = new MainLogic();

        this.stage = stage;
        root = new VBox();
        scene = new Scene(root,500,600);
        scene.getStylesheets().add("/Graphics/css/board.css");

        //Add top banner
        addBanner();

        //Add the info message
        infoMessage = new Label();
        infoMessage.getStyleClass().add("info-message");
        root.getChildren().add(infoMessage);
        //infoMessage.setText("Debug text");

        //Add actuall board
        board = new SinglePlayerBoard(game, this);
        game.resetBoard();
        game.printGameState();
        board.renderGameState(game.getGamestate());


        boardContainer = new HBox();
        boardContainer.getChildren().add(board.getBoard());
        boardContainer.setAlignment(Pos.CENTER);
        root.getChildren().add(boardContainer);


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //add close action
            }
        });
    }

    public Scene getScene(){
        return this.scene;
    }

    public void addBanner(){
        HBox hbox = new HBox();
        hbox.getStyleClass().add("banner");
        playerLabel = new Label();
        updateMoveLabel("white");
        playerLabel.getStyleClass().add("banner-text");
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(playerLabel);


        root.getChildren().add(hbox);
    }

    public void updateMoveLabel(String player){
        if(player.equalsIgnoreCase("white")){
            playerLabel.setText("White's Turn");
        }else{
            playerLabel.setText("Black's Turn");
        }
    }

    public void setInfoMessage(String message){
        this.infoMessage.setText(message);
    }

    public void close(){
        MenuScreen menu = new MenuScreen(stage);
        stage.setScene(menu.getScene());
    }

}
