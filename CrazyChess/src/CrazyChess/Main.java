package CrazyChess;

import Graphics.MenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class of the whole project
 *
 */
public class Main extends Application
{

	public void start(Stage primaryStage)
	{

		MenuScreen menu = new MenuScreen(primaryStage);
		Scene currentScene = menu.getScene();
		primaryStage.setScene(currentScene);

		primaryStage.setTitle("Crazy Chess");
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
