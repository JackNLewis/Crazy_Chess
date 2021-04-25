package Graphics;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import CrazyChess.logic.MainLogic;
import Graphics.multiplayer.GameScreen;
import Networking.Client;

public class AskForDraw
{

	private VBox		container;
	private SGameScreen	sGameScreen;
	private MainLogic	game;
	private GameScreen	gameScreen;
	private Client		client;
	private Client		client2;

	private Label		title	= new Label("Draw");
	private Button		draw	= new Button("Ask for draw");
	private Button		yes		= new Button("yes");
	private Button		no		= new Button("no");

	/**
	 * Adds an "Ask for draw" button, then a "Do you accept?" option for the other player in solo mode.
	 * 
	 * @param sGameScreen
	 * @param game
	 * @return
	 */

	public AskForDraw(SGameScreen sGameScreen, MainLogic game)
	{
		this.sGameScreen = sGameScreen;
		this.game = game;
		container = new VBox();
		container.setAlignment(Pos.TOP_CENTER);
		container.getStylesheets().add("/Graphics/css/menu.css");
		title.getStyleClass().add("menu-text");
		container.getChildren().add(title);
		container.getChildren().addAll(draw, yes, no);
		yes.setVisible(false);
		no.setVisible(false);

		draw.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				game.switchTurn();
				game.setDrawAsked();
				sGameScreen.updateMoveLabel(game.getTurn());
				title.setText("Do you accept?");
				draw.setVisible(false);
				yes.setVisible(true);
				no.setVisible(true);

				yes.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						title.setText("It's a draw!");
						sGameScreen.setInfoMessage("It's a draw!");
						game.setDraw();
						yes.setVisible(false);
						no.setVisible(false);
					}
				});

				no.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						game.refuseDraw();
						game.switchTurn();
						sGameScreen.updateMoveLabel(game.getTurn());
						title.setText("Draw");
						draw.setVisible(true);
						yes.setVisible(false);
						no.setVisible(false);
					}
				});
			}
		});
	}

	/**
	 * Adds an "Ask for draw" button, then a "Do you accept?" option for the other player in multiplayer mode.
	 * 
	 * @param gameScreen
	 * @param game
	 * @return
	 */

/*	public AskForDraw(GameScreen gameScreen, MainLogic game, Client client, Client client2)
	{
		this.gameScreen = gameScreen;
		this.game = game;
		this.client = client;
		this.client2 = client2;
		container = new VBox();
		container.setAlignment(Pos.TOP_CENTER);
		container.getStylesheets().add("/Graphics/css/menu.css");
		title.getStyleClass().add("menu-text");
		container.getChildren().add(title);
		container.getChildren().addAll(draw, yes, no);
		yes.setVisible(false);
		no.setVisible(false);
		if (client.isTurn() == false)
		{
			draw.setVisible(false);
			//	title.setText("Waiting");
		}

		draw.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				//game.switchTurn();
				draw.setVisible(false);
				client.setTurn(false);
			//	client2.
				game.setDrawAsked();
				gameScreen.updateMoveLabel();
				title.setText("Waiting");
			//	yes.setVisible(true);
			//	no.setVisible(true);

				yes.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						title.setText("It's a draw!");
						gameScreen.setInfoMessage("It's a draw!");
						game.setDraw();
						yes.setVisible(false);
						no.setVisible(false);
					}
				});

				no.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						game.refuseDraw();
						game.switchTurn();
						gameScreen.updateMoveLabel();
						title.setText("Draw");
						draw.setVisible(true);
						yes.setVisible(false);
						no.setVisible(false);
					}
				});
			}
		});
	}*/

	public VBox getAskForDraw()
	{
		return container;
	}

	public void setHide()
	{
		container.getChildren().clear();
	}

	public void updateTurn()
	{
		if (client.isTurn())
		{
			draw.setVisible(true);
			//	title.setText("Waiting");
		}
		else
		{
			draw.setVisible(false);
		}
	}
}