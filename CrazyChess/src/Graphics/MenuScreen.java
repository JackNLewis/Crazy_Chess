package Graphics;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.concurrent.Semaphore;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.savegamestate.*;
import CrazyChess.pieces.AbstractPiece;

public class MenuScreen {

    private VBox buttons;
    private Scene scene;
    private Stage stage;
    

//    MainLogic game = new MainLogic();
//    private AbstractPiece[][] gamestate;

    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    
    private boolean isSelected1;
    private boolean isSelected2;
    private boolean isSelected3;


    public MenuScreen(Stage stage){
        buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        scene = new Scene(buttons,500,600); // make 500,600
        scene.getStylesheets().add("/Graphics/css/menu.css");
        this.stage = stage;

        //Add Banner Image
        ImageView img = new ImageView();
        img.setImage(new Image("/resources/menu_text.png"));
        img.setFitWidth(400);
        img.setPreserveRatio(true);
        buttons.getChildren().add(img);
        addButtons(buttons);
        stage.setResizable(false);
    }

    public void addButtons(VBox root){

        //Add New Game
        Button newButton = new Button("Local Game");
        newButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            	buttons.getChildren().clear();
            	
            	Button load = new Button("Load Game");
            	load.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {

                        File file = new File("saved.xml");
					    SaveGame loadState = new SaveGame();
					   
					    byte[] bytes = loadState.loadDataFromFile(file);
					    MainLogic game = new MainLogic();
					    
						try {
							loadState.load(bytes, game, game.getGamestate());
							game.setGamestate(game.getGamestate());
							
			                SGameScreen sp = new SGameScreen(game, stage);
			                stage.setScene(sp.getScene());
							System.out.println("Loaded successfully");


                            //booleans for rule changes
                            isSelected1 = cb1.isSelected();
                            isSelected2 = cb2.isSelected();
                            isSelected3 = cb3.isSelected();

                            //SGameScreen sp = new SGameScreen(newgame,stage);

                            //setting rule changes
                            sp.setRC1(isSelected1);
                            sp.setRC2(isSelected2);
                            sp.setRC3(isSelected3);
                            System.out.println("rulechange 1 selected " + isSelected1);
                            System.out.println("rulechange 2 selected " + isSelected2);
                            System.out.println("rulechange 3 selected " + isSelected3);

//						sp.loadLogic(newgame);
                            stage.setScene(sp.getScene());


						} catch (Exception exc) {
							System.out.println("Couldn't load: " + exc.getMessage());
						}
					}
            	});
            	
            	Button newGame = new Button("New Game");
            	newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MainLogic newgame = new MainLogic();
						newgame.resetBoard();

                        //booleans for rule changes
                        isSelected1 = cb1.isSelected();
                        isSelected2 = cb2.isSelected();
                        isSelected3 = cb3.isSelected();

                        SGameScreen sp = new SGameScreen(newgame,stage);

                        //setting rule changes
                        sp.setRC1(isSelected1);
                        sp.setRC2(isSelected2);
                        sp.setRC3(isSelected3);
                        System.out.println("rulechange 1 selected " + isSelected1);
                        System.out.println("rulechange 2 selected " + isSelected2);
                        System.out.println("rulechange 3 selected " + isSelected3);

                        stage.setScene(sp.getScene());
					}
            	});

            	buttons.getChildren().addAll(load,newGame);

            };
        });


        //Add vs AI
        Button VsAI = new Button("Player Vs AI");
        VsAI.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	@Override
            public void handle(MouseEvent event) {

            	buttons.getChildren().clear();
            	
				Button easy = new Button("Easy");
				easy.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MainLogic newgame = new MainLogic();
						newgame.resetBoard();
						SGameScreen sp = new SGameScreen(newgame, stage);

						isSelected1 = cb1.isSelected();
						isSelected2 = cb2.isSelected();
						isSelected3 = cb3.isSelected();

						sp.setRC1(isSelected1);
						sp.setRC2(isSelected2);
						sp.setRC3(isSelected3);
						System.out.println("rulechange 1 selected " + isSelected1);
						System.out.println("rulechange 2 selected " + isSelected2);
						System.out.println("rulechange 3 selected " + isSelected3);

						sp.getBoard().enableAI("easy");
						stage.setScene(sp.getScene());
					}
				});
            	
            	Button medium = new Button("Medium");
            	medium.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MainLogic newgame = new MainLogic();
						newgame.resetBoard();

                        //booleans for rule changes
                        isSelected1 = cb1.isSelected();
                        isSelected2 = cb2.isSelected();
                        isSelected3 = cb3.isSelected();

                        SGameScreen sp = new SGameScreen(newgame,stage);

                        //setting rule changes
                        sp.setRC1(isSelected1);
                        sp.setRC2(isSelected2);
                        sp.setRC3(isSelected3);
                        System.out.println("rulechange 1 selected " + isSelected1);
                        System.out.println("rulechange 2 selected " + isSelected2);
                        System.out.println("rulechange 3 selected " + isSelected3);
                        
                        sp.getBoard().enableAI("medium");
                        stage.setScene(sp.getScene());
					}
            	});
            	
				Button hard = new Button("Hard");
				hard.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						MainLogic newgame = new MainLogic();
						newgame.resetBoard();

						// booleans for rule changes
						isSelected1 = cb1.isSelected();
						isSelected2 = cb2.isSelected();
						isSelected3 = cb3.isSelected();

						SGameScreen sp = new SGameScreen(newgame, stage);

						// setting rule changes
						sp.setRC1(isSelected1);
						sp.setRC2(isSelected2);
						sp.setRC3(isSelected3);
						System.out.println("rulechange 1 selected " + isSelected1);
						System.out.println("rulechange 2 selected " + isSelected2);
						System.out.println("rulechange 3 selected " + isSelected3);
						
						sp.getBoard().enableAI("hard");
						stage.setScene(sp.getScene());
					}
				});

            	buttons.getChildren().addAll(easy, medium, hard);
            };
        });
        
        Label title = new Label("Which rule changes do you want to play with?");
        
        //checkboxes for rule changes
        cb1 = new CheckBox("Bishop-Rook switch");
        cb2 = new CheckBox("Pawns can go backwards");
        cb3 = new CheckBox("Kings move like Queens");

        cb1.setSelected(true);
        cb2.setSelected(true);
        cb3.setSelected(true);
        
        root.getChildren().addAll(newButton,VsAI,title,cb1,cb2,cb3);
    }



    public Scene getScene(){
        return this.scene;
    }
}
