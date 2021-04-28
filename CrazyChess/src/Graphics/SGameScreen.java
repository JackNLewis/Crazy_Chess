package Graphics;

import java.io.File;

import CrazyChess.logic.MainLogic;
import CrazyChess.logic.savegamestate.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private PowerUpMenu pwrUpMenu;
	
 // setting menu for music control
    private RadioMenuItem turnOnItem;
    private RadioMenuItem turnOffItem;
    private CheckMenuItem Bomb;
    private CheckMenuItem setBomb;
    private CheckMenuItem chessmove;
    private CheckMenuItem MiniPromote;
    private CheckMenuItem FreeCard;
    private CheckMenuItem DummyPiece;
    private CheckMenuItem Teleport;

    public SGameScreen(MainLogic newgame, Stage stage){
    	if(newgame != null) {
    		game = newgame;
    	}
    	else {
    		game = new MainLogic();
    	}
//        game = new MainLogic();

        this.stage = stage;
        root = new VBox();
        scene = new Scene(root,720,630);
        scene.getStylesheets().add("/Graphics/css/board.css");
        
      //Options menu
        Menu optionsMenu = new Menu("Options");
        
        //Menu items
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
        	File file = new File("saved.xml");
        	SaveGame saveState = new SaveGame();
//        	System.out.println("gamestate before " + game);
        	byte[] bytes = saveState.save(game, game.getGamestate());
        	
        	try {
        		saveState.saveDataToFile(bytes, file);
        		System.out.println("saved successfully");
        	}
        	catch (Exception exc) {
        		System.out.println("Couldn't save: " + exc.getMessage());
        	}
    	});
        optionsMenu.getItems().add(save);
        
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(e -> {
        	//game.resetBoard();
        });
        optionsMenu.getItems().add(reset);
//        optionsMenu.getItems().add(new MenuItem("Reset"));
        
        optionsMenu.getItems().add(new SeparatorMenuItem());
        optionsMenu.getItems().add(new MenuItem("Exit"));
	    
	//all music menu
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
        
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(optionsMenu,Setting);
        
		BorderPane menu = new BorderPane();
		menu.setTop(menuBar);


        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);

        //Add top banner
        addBanner();

        //Add the info message
        infoMessage = new Label();
        infoMessage.getStyleClass().add("info-message");
        root.getChildren().add(infoMessage);

        //make power up menu
        pwrUpMenu = new PowerUpMenu(this);

        //Add actuall board
        board = new SBoard(game, this);
//        game.resetBoard();
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
    
	public void loadLogic(MainLogic newgame){
		if(newgame != null) {
			game = newgame;
		}
		else {
			 game = new MainLogic();
			
		}
	}

	public Scene getScene() {
		return this.scene;
	}

	public void addBanner() {
		HBox hbox = new HBox();
		hbox.getStyleClass().add("banner");
		playerLabel = new Label();
		updateMoveLabel("white");
		playerLabel.getStyleClass().add("banner-text");
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(playerLabel);

		root.getChildren().add(hbox);
	}

	public void updateMoveLabel(String player) {
		if (player.equalsIgnoreCase("white")) {
			playerLabel.setText("White's Turn");
		} else {
			playerLabel.setText("Black's Turn");
		}
	}

	public void setInfoMessage(String message) {
		this.infoMessage.setText(message);
	}

	public void close() {
		MenuScreen menu = new MenuScreen(stage);
		stage.setScene(menu.getScene());
	}

	public PowerUpMenu getPwrUpMenu() {
		return pwrUpMenu;
	}

	public SBoard getBoard() {
		return this.board;
	}

	public void selectPower(String powerUp) {
	}
	
	///all for check the music menuItem is or is not selected
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
  
}
