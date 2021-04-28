package Graphics;

import CrazyChess.logic.MainLogic;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SGameScreen {

    private Scene scene;
    private VBox root;
    private Label playerLabel;
    private SBoard board;
    private HBox boardContainer;
    private Stage stage;
    private MainLogic game;
    private Label infoMessage;
    private Label ruleChangeInfo;
    private AskForDraw askForDraw;
    private PowerUpMenu pwrUpMenu;

    public SGameScreen(Stage stage){
        game = new MainLogic();

        this.stage = stage;
        root = new VBox();
        scene = new Scene(root,920,620);
        scene.getStylesheets().add("/Graphics/css/board.css");

        //Add top banner
        addBanner();

        //Add the info message
        infoMessage = new Label();
        infoMessage.getStyleClass().add("info-message");
        root.getChildren().add(infoMessage);
        
        //Add the rule change info
        ruleChangeInfo = new Label();
        ruleChangeInfo.getStyleClass().add("info-message");
        root.getChildren().add(ruleChangeInfo);
        
        //make power up menu
        pwrUpMenu = new PowerUpMenu(this);

        askForDraw = new AskForDraw(this, game);
        
        //Add actuall board
        board = new SBoard(game,this);
        game.resetBoard();
        game.printGameState();
        board.renderGameState(game.getGamestate());

        boardContainer = new HBox();
        boardContainer.setSpacing(10);
        boardContainer.getChildren().addAll(board.getBoard(), pwrUpMenu.getPowerUpMenu(), askForDraw.getAskForDraw());
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


    public PowerUpMenu getPwrUpMenu(){
        return pwrUpMenu;
    }

    public SBoard getBoard(){
        return this.board;
    }

    public Label getRCinfo() {
    	return ruleChangeInfo;
    }
    
    public void selectPower(String powerUp){}
}
