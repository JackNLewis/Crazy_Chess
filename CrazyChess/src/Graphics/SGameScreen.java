package Graphics;

import java.io.File;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.savegamestate.*;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import static java.lang.Thread.sleep;

/**
 * It conects different components of the game screen such as board, power up menu.
 */
public class SGameScreen
{

	private Scene			scene;
	private VBox			root;
	private Label			playerLabel;
	private SBoard			board;
	private HBox			boardContainer;
	private Stage			stage;
	private MainLogic		game;
	private Label			infoMessage;
	private Label			ruleChangeInfo;
	private AskForDraw		askForDraw;
	private PowerUpMenu		pwrUpMenu;
	private MenuItem		reset;
	private MenuItem		exit;
	// setting menu for music control
	private RadioMenuItem	turnOnItem;
	private RadioMenuItem	turnOffItem;
	private CheckMenuItem	Bomb;
	private CheckMenuItem	setBomb;
	private CheckMenuItem	chessmove;
	private CheckMenuItem	MiniPromote;
	private CheckMenuItem	FreeCard;
	private CheckMenuItem	DummyPiece;
	private CheckMenuItem	Teleport;

	StackPane				endScreen;
	VBox					root2;

	private MenuBar			menuBar	= new MenuBar();

	//buttons for promotes
	HBox					promotes;

	/**
	 * Constructor for SGameScreen
	 * 
	 * @param newgame
	 *            - the mainlogic passed
	 * @param stage
	 *            - the stage of the window
	 */
	public SGameScreen(MainLogic newgame, Stage stage)
	{
		if (newgame != null)
		{
			game = newgame;
		}
		else
		{
			game = new MainLogic();
		}

		endScreen = new StackPane();
		root2 = new VBox();
		root2.setAlignment(Pos.CENTER);
		endScreen.getChildren().add(root2);

		this.stage = stage;
		root = new VBox();

		scene = new Scene(root, 800, 680);
		scene.getStylesheets().add("/Graphics/css/board.css");

		//Options menu
		Menu optionsMenu = new Menu("Options");

		//Menu items
		MenuItem save = new MenuItem("Save");
		save.setOnAction(e -> {
			File file = new File("saved.xml");
			SaveGame saveState = new SaveGame();

			byte[] bytes = saveState.save(game, game.getGamestate());

			try
			{
				saveState.saveDataToFile(bytes, file);
				System.out.println("saved successfully");
			}
			catch (Exception exc)
			{
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
		turnOnItem = new RadioMenuItem("Turn on");
		turnOffItem = new RadioMenuItem("Turn off");
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

		musicMenu.getItems().addAll(turnOnItem, turnOffItem, new SeparatorMenuItem());

		Menu moremenu = new Menu("more..");
		moremenu.getItems().addAll(chessmove, Bomb, setBomb, MiniPromote, FreeCard, DummyPiece, Teleport);
		musicMenu.getItems().add(moremenu);

		//Main menu bar
		menuBar.getMenus().addAll(optionsMenu, musicMenu);
		menuBar.setId("menuBar");

		((VBox) scene.getRoot()).getChildren().add(0, menuBar);
		root.getChildren().add(endScreen);

		//Add top banner
		addBanner();

		//Add the info message
		infoMessage = new Label();
		infoMessage.getStyleClass().add("info-message");
		root2.getChildren().add(infoMessage);

		//Add the rule change info
		ruleChangeInfo = new Label();
		ruleChangeInfo.getStyleClass().add("info-message");
		root2.getChildren().add(ruleChangeInfo);

		//make power up menu
		pwrUpMenu = new PowerUpMenu(this);
		promotes = new HBox();

		askForDraw = new AskForDraw(this, game);

		//Add actuall board
		board = new SBoard(game, this);
		game.printGameState();
		board.renderGameState(game.getGamestate());

		boardContainer = new HBox();
		boardContainer.setSpacing(10);
		boardContainer.getChildren().addAll(board.getBoard(), pwrUpMenu.getPowerUpMenu());
		boardContainer.setAlignment(Pos.CENTER);
		root2.getChildren().add(boardContainer);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				//add close action
			}
		});

		stage.setOnCloseRequest(e -> Platform.exit());
	}

	public void loadLogic(MainLogic newgame)
	{
		if (newgame != null)
		{
			game = newgame;
		}
		else
		{
			game = new MainLogic();

		}
	}

	/**
	 * returns the scene
	 * 
	 * @return the scene of the screen
	 */
	public Scene getScene()
	{
		return this.scene;
	}

	/**
	 * Adds the banner image
	 */
	public void addBanner()
	{
		HBox hbox = new HBox();
		hbox.getStyleClass().add("banner");
		playerLabel = new Label();
		updateMoveLabel("white");
		playerLabel.getStyleClass().add("banner-text");
		hbox.setAlignment(Pos.CENTER);
		hbox.getChildren().add(playerLabel);

		root2.getChildren().add(hbox);
	}

	/**
	 * Updates the label for current players move
	 * 
	 * @param player
	 *            - the player to display on the label
	 */
	public void updateMoveLabel(String player)
	{
		if (player.equalsIgnoreCase("white"))
		{
			playerLabel.setText("White's Turn");
		}
		else
		{
			playerLabel.setText("Black's Turn");
		}
	}

	/**
	 * Sets the info message
	 * 
	 * @param message
	 *            - the message to display
	 */
	public void setInfoMessage(String message)
	{
		this.infoMessage.setText(message);
	}

	/**
	 * closes the application
	 */
	public void close()
	{
		MenuScreen menu = new MenuScreen(stage);
		stage.setScene(menu.getScene());
	}

	/**
	 * returns the power up menu
	 * 
	 * @return the PowerUpMenu object
	 */
	public PowerUpMenu getPwrUpMenu()
	{
		return pwrUpMenu;
	}

	/**
	 * @return returns the board
	 */
	public SBoard getBoard()
	{
		return this.board;
	}

	/**
	 *
	 * @return true if music is enables
	 */
	///all for check the music menuItem is or is not selected
	public boolean isMusicOn()
	{
		if (turnOffItem.isSelected())
		{
			return false;
		}
		return turnOnItem.isSelected();
	}

	/**
	 * @return true if bomb is on
	 */
	public boolean isbombOn()
	{
		return Bomb.isSelected();
	}

	/**
	 *
	 * @return true if bomb is on
	 */
	public boolean isSetbombOn()
	{
		return setBomb.isSelected();
	}

	public boolean ischessmoveOn()
	{
		return chessmove.isSelected();
	}

	/**
	 *
	 * @return true if mini promote is on
	 */
	public boolean isMiniPromoteOn()
	{
		return MiniPromote.isSelected();
	}

	/**
	 *
	 * @return true if free card is on
	 */
	public boolean isFreeCardOn()
	{
		return FreeCard.isSelected();
	}

	/**
	 *
	 * @return true if dummy piece is on
	 */
	public boolean isDummyPieceOn()
	{
		return DummyPiece.isSelected();
	}

	/**
	 *
	 * @return true if teleport is on
	 */
	public boolean isTeleportOn()
	{
		return Teleport.isSelected();
	}

	/**
	 *
	 * @return the label of rule changes
	 */
	public Label getRCinfo()
	{
		return ruleChangeInfo;
	}

	public boolean getRC1()
	{
		return game.getRC1();
	}

	public boolean getRC2()
	{
		return game.getRC2();
	}

	public boolean getRC3()
	{
		return game.getRC3();
	}

	/**
	 * sets rule change 1
	 * 
	 * @param rulechange1
	 *            - true if rule 1 is enabled
	 */
	public void setRC1(boolean rulechange1)
	{
		game.setRC1(rulechange1);
	}

	/**
	 * sets rule change 2
	 * 
	 * @param rulechange2
	 *            - true if rule 2 is enabled
	 */
	public void setRC2(boolean rulechange2)
	{
		game.setRC2(rulechange2);
	}

	/**
	 * sets rule change 3
	 * 
	 * @param rulechange3
	 *            - true if rule 3 is enabled
	 */
	public void setRC3(boolean rulechange3)
	{
		game.setRC3(rulechange3);
	}

	public void selectPower(String powerUp)
	{
	}

	/**
	 * shows the promote buttons
	 */
	public void showPromotes()
	{
		promotes.getChildren().clear();
		String color = (game.getTurn().equalsIgnoreCase("white")) ? "W_" : "B_";

		ImageView queenPromote = new ImageView();
		queenPromote.setImage(new Image("/resources/pieces/" + color + "queen.png"));
		queenPromote.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				board.promte("q");
			}
		});

		ImageView rookPromote = new ImageView();
		rookPromote.setImage(new Image("/resources/pieces/" + color + "rook.png"));
		rookPromote.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				board.promte("r");
			}
		});
		ImageView knightPromote = new ImageView();
		knightPromote.setImage(new Image("/resources/pieces/" + color + "knight.png"));
		knightPromote.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				board.promte("k");
			}
		});
		ImageView bishopPromote = new ImageView();
		bishopPromote.setImage(new Image("/resources/pieces/" + color + "bishop.png"));
		bishopPromote.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				board.promte("b");
			}
		});
		promotes.getChildren().addAll(queenPromote, rookPromote, knightPromote, bishopPromote);

		VBox pwrContainer = getPwrUpMenu().getPowerUpMenu();
		pwrContainer.getChildren().add(promotes);
	}

	/**
	 * hides the promote buttons
	 */
	public void hidePromotes()
	{
		VBox pwrContainer = getPwrUpMenu().getPowerUpMenu();
		pwrContainer.getChildren().remove(promotes);
	}

	/**
	 * Getter for the reset button
	 * 
	 * @return reset button
	 */
	public MenuItem getReset()
	{
		return reset;
	}

	/**
	 * Getter for the reset button
	 * 
	 * @return reset button
	 */
	public MenuItem getExit()
	{
		return exit;
	}

	/**
	 * Getter for the reset button
	 * 
	 * @return reset button
	 */
	public Stage getStage()
	{
		return stage;
	}

	/**
	 * Getter for the getTurnOnItem menu item
	 * 
	 * @return getTurnOnItem menu item
	 */
	public RadioMenuItem getTurnOnItem()
	{
		return turnOnItem;
	}

	/**
	 * Getter for the getTurnOffItem menu item
	 * 
	 * @return getTurnOffItem menu item
	 */
	public RadioMenuItem getTurnOffItem()
	{
		return turnOffItem;
	}

	/**
	 * Created the menu bar for draws
	 */
	public void setDrawMenu()
	{
		if (!board.getAIEnabled())
		{
			Menu drawMenu = new Menu("Draw");

			//Menu items
			MenuItem draw = new MenuItem("Ask for draw");
			draw.setOnAction(e -> {
				System.out.println(game.getTurn() + " asked for draw");
				game.switchTurn();
				game.setDrawAsked();
				updateMoveLabel(game.getTurn());
				drawPopup();
			});
			drawMenu.getItems().add(draw);
			menuBar.getMenus().addAll(drawMenu);
		}
	}

	/**
	 * Creates the popup for draws
	 */
	public void drawPopup()
	{
		Stage drawStage = new Stage();
		drawStage.initStyle(StageStyle.UNDECORATED);
		VBox container = new VBox();
		Label label = new Label("Do you accept?");
		Button yes = new Button("yes");
		Button no = new Button("no");
		container.getChildren().addAll(label, yes, no);
		container.getStylesheets().add("/Graphics/css/popup.css");
		Scene stageScene = new Scene(container, 200, 120);
		drawStage.setScene(stageScene);
		drawStage.centerOnScreen();
		drawStage.show();

		yes.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				setInfoMessage("It's a draw!");
				game.setDraw();
				drawStage.hide();
				System.out.println(game.getTurn() + " accepted the draw, the game ended.");
			}
		});
		no.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				System.out.println(game.getTurn() + " refused the draw");
				setInfoMessage(game.getTurn() + " refused the draw");
				game.refuseDraw();
				game.switchTurn();
				updateMoveLabel(game.getTurn());
				drawStage.hide();
			}
		});
	}

	/**
	 * Creates and exit button when checkmated
	 */
	public void showEndScreen()
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					System.out.println("sleeping");
					sleep(1000);
					System.out.println("awake");
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						Rectangle screen = new Rectangle(800, 648, Color.rgb(1, 1, 1, 0.6));
						endScreen.setStyle("-fx-padding: 5 0 0 0");
						Button button = new Button("Exit");
						button.setOnMouseClicked(new EventHandler<MouseEvent>()
						{
							@Override
							public void handle(MouseEvent event)
							{
								MenuScreen menuScreen = new MenuScreen(stage);
								stage.setScene(menuScreen.getScene());
							}
						});
						endScreen.getChildren().addAll(screen, button);
					}
				});
			}
		});
		thread.start();
	}
}
