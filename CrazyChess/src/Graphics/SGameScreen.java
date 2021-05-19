package Graphics;

import java.io.File;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.savegamestate.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * It conects different components of the game screen such as board, power up menu.
 */
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
    private MenuItem reset;
    private MenuItem exit;
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


    //buttons for promotes
	HBox promotes;

	/**
	 * Constructor for SGameScreen
	 * @param newgame
	 * @param stage
	 */
    public SGameScreen(MainLogic newgame, Stage stage){
    	if(newgame != null) {
    		game = newgame;
    	}
    	else {
    		game = new MainLogic();
    	}


        this.stage = stage;
        root = new VBox();

        scene = new Scene(root, 800,680);
        scene.getStylesheets().add("/Graphics/css/board.css");

        
        //Options menu
        Menu optionsMenu = new Menu("Options");
        
        //Menu items
        MenuItem save = new MenuItem("Save");
        save.setOnAction(e -> {
        	File file = new File("saved.xml");
        	SaveGame saveState = new SaveGame();

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

        reset = new MenuItem("Reset");
        optionsMenu.getItems().add(reset);
        
        optionsMenu.getItems().add(new SeparatorMenuItem());
        exit = new MenuItem("Exit");
        optionsMenu.getItems().add(exit);
	    
		//all music menu
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
        
        //Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(optionsMenu,musicMenu);
	    //menuBar.setStyle("-fx-background-color:gray");
        menuBar.setId("menuBar");
		//BorderPane menu = new BorderPane();
		//menu.setTop(menuBar);


        ((VBox) scene.getRoot()).getChildren().add(0,menuBar);

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
        //addPromotes();
        promotes = new HBox();
		//pwrContainer.getChildren().add(promotes);



        askForDraw = new AskForDraw(this, game);
        
        //Add actuall board
        board = new SBoard(game, this);
//        game.resetBoard();
        game.printGameState();
        board.renderGameState(game.getGamestate());

        boardContainer = new HBox();
        boardContainer.setSpacing(10);
        //Removed draw button temporarily
        //boardContainer.getChildren().addAll(board.getBoard(), pwrUpMenu.getPowerUpMenu(), askForDraw.getAskForDraw());
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

	/**
	 * returns the scene
	 * @return
	 */
	public Scene getScene() {
		return this.scene;
	}

	/**
	 * Adds the banner image
	 */
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

	/**
	 * Updates the label for current players move
	 * @param player
	 */
	public void updateMoveLabel(String player) {
		if (player.equalsIgnoreCase("white")) {
			playerLabel.setText("White's Turn");
		} else {
			playerLabel.setText("Black's Turn");
		}
	}

	/**
	 * Sets the info message
	 * @param message
	 */
	public void setInfoMessage(String message) {
		this.infoMessage.setText(message);
	}

	/**
	 * closes the application
	 */
	public void close() {
		MenuScreen menu = new MenuScreen(stage);
		stage.setScene(menu.getScene());
	}

	/**
	 * returns the power up menu
	 * @return
	 */
	public PowerUpMenu getPwrUpMenu() {
		return pwrUpMenu;
	}

	/**
	 * returns the board
	 * @return
	 */
	public SBoard getBoard() {
		return this.board;
	}


	/**
	 *
	 * @return true if music is enables
	 */
	///all for check the music menuItem is or is not selected
	public boolean isMusicOn() {
    	if(turnOffItem.isSelected()) {
    		return false;
    	}
    	return turnOnItem.isSelected();
    	}

	/**
	 *
	 * @return true if bomb is on
	 */
	public boolean isbombOn() {
    	return Bomb.isSelected();
    }

	/**
	 *
	 * @return true if bomb is on
	 */
    public boolean isSetbombOn() {
        return setBomb.isSelected();
    }

    public boolean ischessmoveOn() {
        return chessmove.isSelected();
    }

	/**
	 *
	 * @return true if mini promote is on
	 */
    public boolean isMiniPromoteOn() {
        return MiniPromote.isSelected();
    }

	/**
	 *
	 * @return true if free card is on
	 */
    public boolean isFreeCardOn() {
        return FreeCard.isSelected();
    }

	/**
	 *
	 * @return true if dummy piece is on
	 */
    public boolean isDummyPieceOn() {
       	return DummyPiece.isSelected();
    }

	/**
	 *
	 * @return true if teleport is on
	 */
    public boolean isTeleportOn() {
        return Teleport.isSelected();
    }



    public Label getRCinfo() {
    	return ruleChangeInfo;
    }
    
    public boolean getRC1() {
		return game.getRC1();
	}
	
	public boolean getRC2() {
		return game.getRC2();
	}
	
	public boolean getRC3() {
		return game.getRC3();
	}

	/**
	 * sets rule change 1
	 * @param rulechange1
	 */
	public void setRC1(boolean rulechange1) {
		game.setRC1(rulechange1);
	}

	/**
	 * sets rule change 2
	 * @param rulechange2
	 */
	public void setRC2(boolean rulechange2) {
		game.setRC2(rulechange2);
	}

	/**
	 * sets rule change 3
	 * @param rulechange3
	 */
	public void setRC3(boolean rulechange3) {
		game.setRC3(rulechange3);
	}
    
    public void selectPower(String powerUp){}

	/**
	 * shows the promote buttons
	 */
	public void showPromotes(){
    	promotes.getChildren().clear();
		String color = (game.getTurn().equalsIgnoreCase("white")) ? "W_" : "B_";

		ImageView queenPromote = new ImageView();
		queenPromote.setImage(new Image("/resources/pieces/" + color + "queen.png"));
		queenPromote.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) { board.promte("q"); }
		});

		ImageView rookPromote = new ImageView();
		rookPromote.setImage(new Image("/resources/pieces/" + color + "rook.png"));
		rookPromote.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				board.promte("r");
			}
		});
		ImageView knightPromote = new ImageView();
		knightPromote.setImage(new Image("/resources/pieces/" + color + "knight.png"));
		knightPromote.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				board.promte("k");
			}
		});
		ImageView bishopPromote = new ImageView();
		bishopPromote.setImage(new Image("/resources/pieces/" + color + "bishop.png"));
		bishopPromote.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				board.promte("b");
			}
		});
		promotes.getChildren().addAll(queenPromote,rookPromote,knightPromote,bishopPromote);

		VBox pwrContainer = getPwrUpMenu().getPowerUpMenu();
		pwrContainer.getChildren().add(promotes);
	}

	/**
	 * hides the promote buttons
	 */
	public void hidePromotes(){
		VBox pwrContainer = getPwrUpMenu().getPowerUpMenu();
		pwrContainer.getChildren().remove(promotes);
	}
	
	/**
	 * Getter for the reset button
	 * @return	reset button
	 */
	public MenuItem getReset() {
		return reset;
	}
	
	/**
	 * Getter for the reset button
	 * @return	reset button
	 */
	public MenuItem getExit() {
		return exit;
	}
	
	/**
	 * Getter for the reset button
	 * @return	reset button
	 */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * Getter for the getTurnOnItem menu item
	 * @return	getTurnOnItem menu item
	 */
	public RadioMenuItem getTurnOnItem() {
		return turnOnItem;
	}
	
	/**
	 * Getter for the getTurnOffItem menu item
	 * @return	getTurnOffItem menu item
	 */
	public RadioMenuItem getTurnOffItem() {
		return turnOffItem;
	}
	
}
