package Graphics;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import CrazyChess.logic.MainLogic;

/**
 * A class that handles asking for a draw in game
 *
 */
public class AskForDraw
{
	private VBox		container;
	private SGameScreen	sGameScreen;
	private MainLogic	game;

	private Label		title	= new Label("");
	private Button		draw	= new Button("Ask for draw");
	private Button		yes		= new Button("yes");
	private Button		no		= new Button("no");

	/**
	 * Adds an "Ask for draw" button, then a "Do you accept?" option for the other player in solo mode.
	 * 
	 * @param sGameScreen
	 *            game screen
	 * @param game
	 *            game logic
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
				System.out.println(game.getTurn() + " asked for draw");
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
						System.out.println(game.getTurn() + " accepted the draw, the game ended.");
					}
				});

				no.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						System.out.println(game.getTurn() + " refused the draw");
						game.refuseDraw();
						game.switchTurn();
						sGameScreen.updateMoveLabel(game.getTurn());
						title.setText("");
						draw.setVisible(true);
						yes.setVisible(false);
						no.setVisible(false);
					}
				});
			}
		});
	}

	/**
	 * Getter for the Ask for draw button.
	 * 
	 * @return container Ask for draw button
	 */

	public VBox getAskForDraw()
	{
		return container;
	}

	/**
	 * Hides the ask for draw
	 */
	public void hide()
	{
		container.getChildren().clear();
	}
}