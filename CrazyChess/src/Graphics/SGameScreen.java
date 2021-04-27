package Graphics;

import CrazyChess.logic.MainLogic;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
//////////////////////////////////////
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckMenuItem;
//////////////////////////////////////

public class SGameScreen {

    private Scene scene;
    private VBox root;
    private Label playerLabel;
    private SBoard board;
    private HBox boardContainer;
    private Stage stage;
    private MainLogic game;
    private Label infoMessage;
    private PowerUpMenu pwrUpMenu;
    ///////////////////////////////////////
    // setting menu
    private MenuBar menuBar;
    private RadioMenuItem turnOnItem;
    private RadioMenuItem turnOffItem;
    private CheckMenuItem Bomb;
    private CheckMenuItem setBomb;
    private CheckMenuItem chessmove;
    private CheckMenuItem MiniPromote;
    private CheckMenuItem FreeCard;
    private CheckMenuItem DummyPiece;
    private CheckMenuItem Teleport;
    ///////////////////////////////////////


    public SGameScreen(Stage stage){
        game = new MainLogic();

        this.stage = stage;
        root = new VBox();
        
        ////////////////////////////////////
        menuBar = new MenuBar();
        root.getChildren().add(menuBar);
        Menu Setting = new Menu("setting");
        
        Menu musicMenu = new Menu("Music");
        ToggleGroup tG = new ToggleGroup();
        turnOnItem = new RadioMenuItem("TurnOn");
        turnOffItem = new RadioMenuItem("TurnOff");
        Bomb = new CheckMenuItem("Bomb");
        setBomb = new CheckMenuItem("setBomb");
        chessmove = new CheckMenuItem("chessmove");
        MiniPromote = new CheckMenuItem("MiniPromote");
        FreeCard = new CheckMenuItem("FreeCard");
        DummyPiece = new CheckMenuItem("DummyPiece");
        Teleport = new CheckMenuItem("Teleport");
        
        turnOnItem.setToggleGroup(tG);
        turnOnItem.setSelected(true);
        turnOffItem.setToggleGroup(tG);
        
        Bomb.setSelected(true);
        setBomb.setSelected(true);
        chessmove.setSelected(true);
        MiniPromote.setSelected(true);
        FreeCard.setSelected(true);
        DummyPiece.setSelected(true);
        Teleport.setSelected(true);
        
        musicMenu.getItems().addAll(turnOnItem,turnOffItem, new SeparatorMenuItem());
        
        Menu moremenu = new Menu("more..");
        moremenu.getItems().addAll(chessmove,Bomb,setBomb,MiniPromote,FreeCard,DummyPiece,Teleport);
        musicMenu.getItems().add(moremenu);
        
        Setting.getItems().addAll(musicMenu);
        
        menuBar.getMenus().addAll(Setting);
        ////////////////////////////////////
        
        scene = new Scene(root,720,620);
        scene.getStylesheets().add("/Graphics/css/board.css");

        //Add top banner
        addBanner();

        //Add the info message
        infoMessage = new Label();
        infoMessage.getStyleClass().add("info-message");
        root.getChildren().add(infoMessage);
        
        //make power up menu
        pwrUpMenu = new PowerUpMenu(this);

        //Add actuall board
        board = new SBoard(game,this);
        game.resetBoard();
        game.printGameState();
        board.renderGameState(game.getGamestate());



        boardContainer = new HBox();
        boardContainer.setSpacing(10);
        boardContainer.getChildren().addAll(board.getBoard(), pwrUpMenu.getPowerUpMenu());
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
    ///////////////////////////////
    public boolean isMusicOn() {
    	if(turnOffItem.isSelected()) {
    		return false;
    	}
    	return turnOnItem.isSelected();
    }
    
    public boolean isbombOn() {
    	return Bomb.isSelected();
    }
    
    public boolean isSetbombOn() {
        return setBomb.isSelected();
    }
    
    public boolean ischessmoveOn() {
        return chessmove.isSelected();
    }
    
    public boolean isMiniPromoteOn() {
        return MiniPromote.isSelected();
    }
    
    public boolean isFreeCardOn() {
        return FreeCard.isSelected();
    }
    
    public boolean isDummyPieceOn() {
        return DummyPiece.isSelected();
    }
    
    public boolean isTeleportOn() {
        return Teleport.isSelected();
    }
    ////////////////////////////////

    public void selectPower(String powerUp){}
}
