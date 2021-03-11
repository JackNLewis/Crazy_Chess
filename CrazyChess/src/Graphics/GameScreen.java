package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import Networking.Client;
import Networking.Move;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GameScreen {

    private Scene scene;
    private BorderPane root;
    private Client client;
    private Label playerLabel;
    private Board board;
    Stage stage;

    public GameScreen(Stage stage, Client client){
        this.stage = stage;
        this.client = client;
        root = new BorderPane();
        scene = new Scene(root,500,800);

        playerLabel = new Label("White");
        root.setTop(playerLabel);

        board = new Board(client);
        root.setCenter(board.getBoard());

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.close();
            }
        });
    }

    public void renderGameState(AbstractPiece[][] gamesState,boolean successMove){
        board.renderGameState(gamesState,successMove);
    }

    public Scene getScene(){
        return this.scene;
    }

    public void updateMoveLabel(){
        if(playerLabel.getText().equalsIgnoreCase("White")){
            playerLabel.setText("Black");
        }else{
            playerLabel.setText("White");
        }
    }

    public void close(){
        MenuScreen menu = new MenuScreen(stage);
        stage.setScene(menu.getScene());
    }


}
