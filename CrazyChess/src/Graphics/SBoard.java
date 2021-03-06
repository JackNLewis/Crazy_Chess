package Graphics;

import CrazyChess.logic.*;
import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.Bishop;
import CrazyChess.pieces.BlankPiece;
import CrazyChess.pieces.Knight;
import CrazyChess.pieces.Pawn;
import CrazyChess.pieces.Powerup;
import CrazyChess.pieces.Queen;
import CrazyChess.pieces.Rook;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;

/**
 * This class is for board and how the player interacts with it
 *
 */
public class SBoard
{

	//Main screen window connects other components
	private SGameScreen			SGameScreen;

	//Used for checking
	private MainLogic			game;
	private ExtraChecksAndTools	ect;
	private Utilities			util;

	//board logic
	private boolean				selected;				// shows if a tile is already selected
	private ArrayList<Tile>		tiles;
	private GridPane			board;
	private double				boardSize;
	private Tile				selectedTile;

	private AskForDraw			askForDraw;

	//to do with power up
	private ArrayList<Position>	validMoves;
	private PowerUpMenu			powerUps;
	private PowerupMain			powerMain;				// Used to see valid powered moves

	private music				sound;					// Used to play sound

	//to add pawn promotion button
	boolean						promoteWait	= false;
	AbstractPiece				promotePiece;

	private HBox				Wpawnpormote;
	private HBox				Bpawnpormote;

	private boolean				musicOn		= true;

	//Ai stuff
	private boolean				aiEnabled	= false;
	private AI					ai			= new AI();

	//    private HazardPiece hazardPiece;
	private boolean				aiTurn		= false;

	/**
	 * The constructor for SBoard
	 *
	 * @param game
	 *            - the main logic
	 * @param SGameScreen
	 *            - the SGameScreen
	 */
	public SBoard(MainLogic game, SGameScreen SGameScreen)
	{
		initBoard("white");
		this.game = game;
		this.SGameScreen = SGameScreen;
		selected = false;
		util = new Utilities();
		ect = new ExtraChecksAndTools();
		askForDraw = new AskForDraw(SGameScreen, game);
		powerUps = SGameScreen.getPwrUpMenu();
		powerMain = new PowerupMain();
		sound = new music();

		//adds even handlers for music turn on or of, reset and exit, as music is initialized in SBoard for some reason and not in SGameScreen, couldn't add them elsewhere to also stop the music
		SGameScreen.getTurnOffItem().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				sound.turnOff();
				musicOn = false;
			}
		});
		SGameScreen.getTurnOnItem().setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent e)
			{
				sound.turnOn();
				musicOn = true;
			}
		});
		SGameScreen.getReset().setOnAction(e -> {
			MainLogic refresh = new MainLogic();
			refresh.resetBoard();
			SGameScreen restart = new SGameScreen(refresh, SGameScreen.getStage());
			SGameScreen.getStage().setScene(restart.getScene());
			restart.getBoard().setAIEnabled(aiEnabled);
			sound.getMediaPlayer().stop();
			restart.getBoard().setMusic(musicOn);
			refresh.setRC1(game.getRC1());
			refresh.setRC2(game.getRC2());
			refresh.setRC3(game.getRC3());
			restart.setDrawMenu();
		});
		SGameScreen.getExit().setOnAction(e -> {
			MenuScreen menu = new MenuScreen(SGameScreen.getStage());
			sound.getMediaPlayer().stop();
			SGameScreen.getStage().setScene(menu.getScene());
			SGameScreen.getStage().centerOnScreen();
		});

	}

	/**
	 * This function initialises the board
	 *
	 * @param player
	 *            - the starting player
	 */
	public void initBoard(String player)
	{
		int squareSize = 66;
		boardSize = 50 * 8;
		board = new GridPane();
		tiles = new ArrayList<Tile>();

		for (int i = 0; i < 8; i++)
		{
			board.getColumnConstraints().add(new ColumnConstraints(squareSize));
			board.getRowConstraints().add(new RowConstraints(squareSize));
		}

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				Tile tile = new Tile(new Position(i, j), squareSize);
				tiles.add(tile);
				StackPane sp = tile.getSP();
				setDefaultColor(tile);

				if (player.equalsIgnoreCase("white"))
				{
					board.add(sp, i, 7 - j);
				}
				else if (player.equalsIgnoreCase("black"))
				{
					board.add(sp, i, j);
				}
				else
				{
					System.out.println("doesnt recognise player for init");
				}
			}
		}
		board.setMaxSize(boardSize, boardSize);

		addMoveListeners();
	}

	/**
	 * Renders the gamestate onto the board
	 *
	 * @param gamesState
	 *            - the gamestate your rendering
	 */
	public void renderGameState(AbstractPiece[][] gamesState)
	{
		for (Tile tile : tiles)
		{
			int x = tile.getPos().getXpos();
			int y = tile.getPos().getYpos();
			AbstractPiece piece = gamesState[x][y];
			setDefaultColor(tile);
			tile.removeImg();
			if (!(piece instanceof BlankPiece))
			{
				ImageView img = getImageView(piece);
				tile.addImg(img);
				if (piece instanceof HazardPiece)
				{
					ImageView imgOrig = getImageView(((HazardPiece) piece).getOriginalPiece());
					tile.addImg(imgOrig);
				}
			}
		}
	}

	/**
	 * Sets up the event handlers on the board
	 */
	public void addMoveListeners()
	{
		for (Tile tile : tiles)
		{
			tile.getSP().setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent event)
				{
					if (promoteWait)
					{
						System.out.println("need to promote first");
						return;
					}
					if (aiTurn)
					{
						System.out.println("AI is Thinking");
						return;
					}

					String currentColor = game.getTurn();
					String selectedColor = game.getPiece(tile.getPos()).getColor();

					boolean success = false;

					//If tile not selected
					if (!selected)
					{
						if (game.getDrawAsked() || game.getDraw())
						{
							return;
						}
						//Make sure you only select tiles of your colour
						if (selectedColor.equalsIgnoreCase(currentColor))
						{
							selected = true;
							selectedTile = tile;
							// check if power up selected
							if (powerUps.getSelectedIndex() != -1)
							{
								if (powerUps.getSelectedStr().equalsIgnoreCase("bomb"))
								{
									System.out.println("place bomb and change turn");
									boolean poweredMove = game.usePowerup(powerUps.getSelectedIndex(), tile.getPos(), null);
									if (poweredMove)
									{
										playPwSound();
										System.out.println("Successful bomb place");
										success = true;
									}
								}
								else
								{
									showPowerMoves();
								}
							}
							else
							{
								showMoves();
							}
						}
					}
					//Tile is selected so execute a move
					else
					{
						if (tile.equals(selectedTile) || game.getDrawAsked() || game.getDraw())
						{
							//deselect current tile
							renderGameState(game.getGamestate());
							validMoves = null;
							selectedTile = null;
							selected = false;
							if (powerUps.getSelectedIndex() != -1)
							{
								showInitPowerMoves();
							}
							return;
						}

						//Execute a Move
						//check if a power up is selected
						if (powerUps.getSelectedIndex() != -1)
						{
							boolean poweredMove = game.usePowerup(powerUps.getSelectedIndex(), selectedTile.getPos(), tile.getPos());
							if (poweredMove)
							{
								// SUCCESFFUL POWERED MOVE
								//play sound effects
								playPwSound();
								System.out.println("Successful powered up move");
								success = true;

								//check for pawn promote
								AbstractPiece promote = game.isPawnPromote(game.getGamestate());
								if (promote != null)
								{
									SGameScreen.showPromotes();
									promotePiece = promote;
									promoteWait = true;
								}
								updateGui();
							}
							else
							{
								System.out.println("Unsucessful powered move");
								return;
							}
						}

						//NORMAL MOVE if no power up selected
						else
						{
							boolean normalMove = game.moveTo(game.getPiece(selectedTile.getPos()), tile.getPos().getXpos(),
									tile.getPos().getYpos());
							//Normal move was successful
							if (normalMove)
							{
								System.out.println("Successful move");
								updateGui();
								playNormalSound();
								success = true;

								//check for pawn promote
								AbstractPiece promote = game.isPawnPromote(game.getGamestate());
								if (promote != null)
								{
									SGameScreen.showPromotes();
									promotePiece = promote;
									promoteWait = true;
								}
							}
							//Normal move was unsuccessful
							else
							{
								System.out.println("Unsuccessful move");
								return;
							}
						}
					}
					if (success)
					{
						ArrayList<String> powerUpList = game.getPowerUps(game.getTurn());
						powerUps.setPowerUps(powerUpList, game.getTurn());
						game.changeTurn();
						SGameScreen.updateMoveLabel(game.getTurn());
						updateRuleChangeInfo();
						selectedTile = null;
						validMoves = null;
						selected = false;
						powerUps.setSelectedIndex(-1);
						renderGameState(game.getGamestate());

						//check if the player is starting their go and checking someone
						if (ect.isInCheck(util.oppositeColor(game.getTurn()), false, game.getGamestate(), game.getTurnNo()))
						{
							System.out.println("==============================================");
							System.out.println("Player " + game.getTurn() + "Wins!!!!!!");
							System.out.println("==============================================");
							SGameScreen.setInfoMessage("Player " + game.getTurn() + "Wins!!");
							sound.turnOff();
						}

						//check if the player is starting there go in check
						if (ect.isInCheck(game.getTurn(), false, game.getGamestate(), game.getTurnNo()))
						{
							System.out.println("==============================================");
							System.out.println("Player " + game.getTurn() + game.getCheckStatus(game.getTurn()));
							System.out.println("==============================================");
						}

						//If ai is enabled make the ai move
						if (aiEnabled)
						{
							aiMove();
						}
					}

				}
			});
		}
	}

	/**
	 * returns the correct image for the piece
	 * 
	 * @param p
	 *            piece in question
	 * @return ImageView object of the piece
	 */
	public ImageView getImageView(AbstractPiece p)
	{
		String filename = "";
		String name;
		String color;
		if (p == null)
		{
			System.out.println("p is null in getImage");
		}

		else if (p instanceof HazardPiece)
		{
			System.out.println("is hazard piece");
			HazardPiece hazardPiece = (HazardPiece) p;
			if (hazardPiece.getHazard() == Hazard.FROZEN)
			{
				filename = "ice.png";
				System.out.println("frozen");

			}
			else if (hazardPiece.getHazard() == Hazard.BURN)
			{
				filename = "fire.png";
				System.out.println("Hazard image fire");
			}
		}
		else if (p.getColor().equalsIgnoreCase("white"))
		{
			name = p.getClass().getSimpleName().toLowerCase();
			color = "W_";
			filename = color + name + ".png";
		}
		else if (p.getColor().equalsIgnoreCase("black"))
		{
			name = p.getClass().getSimpleName().toLowerCase();
			color = "B_";
			filename = color + name + ".png";
		}
		else if (p instanceof Powerup)
		{
			filename = "PowerUp.png";
		}
		ImageView imgView = new ImageView();
		imgView.setImage(new Image("/resources/pieces/" + filename));
		return imgView;
	}

	/**
	 * Updates the labels on the gui to show the state of the game such as check, checkmates and draws
	 */
	public void updateGui()
	{
		String oppColor = util.oppositeColor(game.getTurn());
		SGameScreen.setInfoMessage("");
		System.out.println("Opposite color: " + oppColor);
		if (game.getCheckStatus(oppColor))
		{
			System.out.println(oppColor + " is in check");
			SGameScreen.setInfoMessage(oppColor + " is in check");
		}
		if (game.getMateStatus(oppColor))
		{
			SGameScreen.setInfoMessage(game.getTurn() + " wins!");
			promoteWait = true; //just so board cant be used
			SGameScreen.showEndScreen();
			sound.turnOff();
			System.out.println(oppColor + " is in check mate");
			askForDraw.hide();
		}
		if (game.getDraw())
		{
			SGameScreen.setInfoMessage("The game ended in a draw");
			System.out.println("Is a draw");
			askForDraw.hide();
		}
	}

	/**
	 * Sets the default colour of the tile
	 *
	 * @param tile
	 *            - the tile you want the default color of
	 */
	private void setDefaultColor(Tile tile)
	{
		if ((tile.getPos().getXpos() % 2 == 1 && tile.getPos().getYpos() % 2 == 1)
				|| ((tile.getPos().getXpos() % 2 == 0) && (tile.getPos().getYpos() % 2 == 0)))
		{
			tile.setbgColor(new Image("/resources/blackTile.png"));
		}
		else
		{
			tile.setbgColor(new Image("/resources/whiteTile.png"));
		}
	}

	/**
	 * Displays the available normal chess moves for the currently selected piece
	 */
	public void showMoves()
	{
		renderGameState(game.getGamestate());
		if (selectedTile == null)
		{
			return;
		}
		validMoves = game.getEcat().validMoves(game.getPiece(selectedTile.getPos()), false, game.getGamestate(), game.getTurnNo());
		for (Tile tile : tiles)
		{
			for (Position pos : validMoves)
			{
				if (tile.getPos().equals(pos))
				{
					tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
				}
			}
		}
		selectedTile.setbgColor(new Image("/resources/selectedTile.png"));
	}

	/**
	 * Displays second available moves for a power up if it contains two parts. E.g. teleport
	 */
	public void showPowerMoves()
	{
		renderGameState(game.getGamestate());
		int powerIndex = powerUps.getSelectedIndex();
		String power = game.getPowerUps(game.getTurn()).get(powerIndex);
		ArrayList<Position> poweredMoves = powerMain.validPowerupMoves(power, game.getGamestate(), selectedTile.getPos(), false);
		for (Tile tile : tiles)
		{
			for (Position pos : poweredMoves)
			{
				if (tile.getPos().equals(pos))
				{
					tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
				}
			}
		}
		if (selectedTile != null)
		{
			selectedTile.setbgColor(new Image("/resources/selectedTile.png"));
		}
	}

	/**
	 * Displays the available powered up moves for the selected piece
	 */
	public void showInitPowerMoves()
	{
		renderGameState(game.getGamestate());
		int powerIndex = powerUps.getSelectedIndex();
		String power = game.getPowerUps(game.getTurn()).get(powerIndex);
		ArrayList<Position> poweredMoves = powerMain.initialPowerupMoves(power, game.getGamestate(), game.getTurn());
		for (Tile tile : tiles)
		{
			for (Position pos : poweredMoves)
			{
				if (tile.getPos().equals(pos))
				{
					tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
				}
			}
		}
		validMoves = null;
		selectedTile = null;
		selected = false;
	}

	/**
	 * Plays the default chess sound
	 */
	private void playNormalSound()
	{
		if (game.getBB() == 1 || (game.getTurnNo() == game.getBBlt() + 4 && !(game.getBBlt() == 0)))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isbombOn())
			{
				sound.turnOffbomb();
			}
			sound.Bomb();
			game.resetBBombsound();
		}
		else if (game.getWB() == 1 || (game.getTurnNo() == game.getWBlt() + 4 && !(game.getWBlt() == 0)))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isbombOn())
			{
				sound.turnOffbomb();
			}
			sound.Bomb();
			game.resetWBombsound();
		}
		else
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.ischessmoveOn())
			{
				sound.turnOffChessmove();
			}
			sound.chessmove();
		}
	}

	/**
	 * plays the correct powered move sound
	 */
	private void playPwSound()
	{
		// SUCCESFFUL POWERED MOVE
		//play sound effects
		if (powerUps.getSelectedStr().equalsIgnoreCase("teleport"))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isTeleportOn())
			{
				sound.turnOffTeleport();
			}
			sound.Teleport();
		}
		if (powerUps.getSelectedStr().equalsIgnoreCase("minipromote"))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isTeleportOn())
			{
				sound.turnOffTeleport();
			}
			sound.MiniPromote();
		}
		if (powerUps.getSelectedStr().equalsIgnoreCase("freecard"))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isFreeCardOn())
			{
				sound.turnOffFreeCard();
			}
			sound.FreeCard();
		}
		if (powerUps.getSelectedStr().equalsIgnoreCase("bomb"))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isSetbombOn())
			{
				sound.turnOffSetBomb();
			}
			sound.SetBomb();
		}
		if (powerUps.getSelectedStr().equalsIgnoreCase("dummypiece"))
		{
			if (SGameScreen.isMusicOn() && !SGameScreen.isDummyPieceOn())
			{
				sound.turnOffDummy();
			}
			sound.DummyPiece();
		}
		System.out.println("Successful powered up move");
		powerUps.setSelectedIndex(-1);
	}

	/**
	 * Method for PawnPromotion. add 4 buttons when click on a pawn which has reach the edge of board. r for rook, b for bishop, k for
	 * knight, q for queen
	 * 
	 * @param b
	 *            the box we use for adding buttons, we have two in SBoard, one for white and the other one for black
	 * @param p
	 *            the position of the pawn which reach the edge of board.
	 */
	public void PawnPromote(HBox b, Position p)
	{
		AbstractPiece[][] gamestateCopy = util.safeCopyGamestate(game.getGamestate());
		AbstractPiece copiedPiece = util.getPiece(p, true, gamestateCopy);
		if (copiedPiece.getColor().equalsIgnoreCase(game.getTurn()) && b.getChildren().isEmpty())
		{
			Button rook = new Button();
			rook.setText("r");
			Button Bishop = new Button();
			Bishop.setText("b");
			Button Knight = new Button();
			Knight.setText("k");
			Button Queen = new Button();
			Queen.setText("q");
			b.getChildren().addAll(rook, Bishop, Knight, Queen);
			rook.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor()))
					{
						game.setGamestate(util.placePiece(new Rook(copiedPiece.getColor(), copiedPiece.getPosition(), "Normal"), true,
								game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else
					{
						System.out.println("not your bottom");
					}
				}
			});
			Bishop.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor()))
					{
						game.setGamestate(util.placePiece(new Bishop(copiedPiece.getColor(), copiedPiece.getPosition(), "Normal"), true,
								game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else
					{
						System.out.println("not your bottom");
					}
				}
			});
			Knight.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor()))
					{
						game.setGamestate(util.placePiece(new Knight(copiedPiece.getColor(), copiedPiece.getPosition(), "Normal"), true,
								game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else
					{
						System.out.println("not your bottom");
					}
				}
			});
			Queen.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor()))
					{
						game.setGamestate(util.placePiece(new Queen(copiedPiece.getColor(), copiedPiece.getPosition(), "Normal"), true,
								game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else
					{
						System.out.println("not your bottom");
					}
				}
			});

		}
	}

	/**
	 * returns the board gridpane
	 * 
	 * @return - the gridpane of the board
	 */
	public GridPane getBoard()
	{
		return this.board;
	}

	/**
	 *
	 * @return true if tile is selected
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * Enables the AI to play
	 * 
	 * @param levels
	 *            - the difficulty level. 'easy', 'medium', 'hard'
	 */
	public void enableAI(String levels)
	{
		this.aiEnabled = true;
		if (levels == "easy")
		{
			ai.AI(game, "easy");
		}
		else if (levels == "medium")
		{
			ai.AI(game, "medium");
		}
		else
		{
			ai.AI(game, "hard");
		}
	}

	/**
	 * This method makes the move for the AI
	 */
	private void aiMove()
	{
		Thread thread = new Thread()
		{
			public void run()
			{
				aiTurn = true;
				long startTime = System.currentTimeMillis();
				AbstractPiece[][] gs = ai.AI(game);
				long endTime = System.currentTimeMillis();
				//So the ai takes at least 2 seconds to make it feel more realistic
				if ((endTime - startTime) < 1000)
				{
					System.out.println("Going to sleep");
					try
					{
						sleep(endTime - startTime);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}

				game.setGamestate(gs);

				ArrayList<String> powerUpList = game.getPowerUps(game.getTurn());
				game.changeTurn();

				//check for pawn promote
				AbstractPiece promote = game.isPawnPromote(game.getGamestate());
				if (promote != null)
				{
					game.promote(promote, "q");
				}

				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						powerUps.setPowerUps(powerUpList, util.oppositeColor(game.getTurn()));

						updateRuleChangeInfo();
						renderGameState(game.getGamestate());

						if (game.getCheckStatus(game.getTurn()))
						{
							System.out.println("Ai has checked you ");
							SGameScreen.setInfoMessage("AI has Checked You");
						}

						if (game.getMateStatus(game.getTurn()))
						{
							System.out.println("AI has check mated you");
							SGameScreen.setInfoMessage("AI Wins");
						}

						if (game.getDraw())
						{
							System.out.println("Is a draw");
							SGameScreen.setInfoMessage("The game ended in a draw");
						}
						SGameScreen.updateMoveLabel(game.getTurn());

						playNormalSound();
					}
				});
				aiTurn = false;
			}
		};
		thread.start();
	}

	/**
	 * Updates the rule change info
	 */
	public void updateRuleChangeInfo()
	{
		if (game.getBrs())
		{
			if (game.getCounter() == 1)
			{
				SGameScreen.getRCinfo().setText("Bishops & Rooks are switched! Last turn.");
			}
			else
			{
				SGameScreen.getRCinfo().setText("Bishops & Rooks are switched! Turns left: " + game.getCounter());
			}
		}
		else if (game.getPS())
		{
			if (game.getCounter() == 1)
			{
				SGameScreen.getRCinfo().setText("Pawns can go backwards! Last turn.");
			}
			else
			{
				SGameScreen.getRCinfo().setText("Pawns can go backwards! Turns left: " + game.getCounter());
			}
		}
		else if (game.getKS())
		{
			if (game.getCounter() == 1)
			{
				SGameScreen.getRCinfo().setText("Kings can move like Queens! Last turn.");
			}
			else
			{
				SGameScreen.getRCinfo().setText("Kings can move like Queens! Turns left: " + game.getCounter());
			}
		}
		else
		{
			SGameScreen.getRCinfo().setText("");
		}
	}

	/**
	 * Used to promote a piece on the board
	 * 
	 * @param newPiece
	 *            what you are promoting to. 'q','k','b','r' for queen, knight, bishop, rook respectively
	 */
	public void promte(String newPiece)
	{
		game.promote(promotePiece, newPiece);
		renderGameState(game.getGamestate());
		SGameScreen.hidePromotes();
		promoteWait = false;
		promotePiece = null;
	}

	/**
	 * set ai
	 * 
	 * @param ai
	 *            - true to enable ai. false to disable ai
	 */
	public void setAIEnabled(boolean ai)
	{
		aiEnabled = ai;
	}

	/**
	 *
	 * @return true if ai is enabled
	 */
	public boolean getAIEnabled()
	{
		return aiEnabled;
	}

	/**
	 * enables the music
	 * 
	 * @param music
	 *            - true to enable music
	 */
	public void setMusic(boolean music)
	{
		if (music)
		{
			sound.turnOn();
			musicOn = true;
		}
		else
		{
			sound.turnOff();
			musicOn = false;
		}
	}
}
