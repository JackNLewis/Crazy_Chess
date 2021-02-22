package Graphics;

import Networking.Server;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuScreen {

    private VBox buttons;
    private Scene scene;
    private Stage stage;

    public MenuScreen(Stage stage){
        buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        scene = new Scene(buttons,500,800);
        this.stage = stage;
        addButtons(buttons);

    }

    public void addButtons(VBox root){
        //Add New Game
        Button newButton = new Button("Local Game");


        //Add Multiplayer Mode
        Button multiplayer = new Button("Multiplayer");
        enableMultiplayer(multiplayer);
        //Add vs AI
        Button VsAI = new Button("Player Vs AI");

        root.getChildren().addAll(newButton,multiplayer,VsAI);
    }

    public void enableMultiplayer(Button button){
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                buttons.getChildren().clear();
                Button connect = new Button("Connect");
                connect.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        stage.setScene(new GameScreenClient().getScene());
                        stage.show();
                    }
                });

                Button host = new Button("Host");
                host.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Server server = new Server();
                        Thread thread = new Thread(server);
                        thread.start();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                buttons.getChildren().add(new Label("Server started"));
                            }
                        });
                    }
                });

                buttons.getChildren().addAll(connect,host);
            }
        });
    }

    public Scene getScene(){
        return this.scene;
    }
}
