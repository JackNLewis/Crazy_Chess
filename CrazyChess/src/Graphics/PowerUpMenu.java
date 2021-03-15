package Graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PowerUpMenu {

    private VBox container;
    private ObservableList<String> whitePowers;
    private ObservableList<String> blackPowers;
    private final ListView<String> powerList;
    private SGameScreen gameScreen;
    private int selectedIndex = -1;

    public PowerUpMenu(SGameScreen gameScreen){
        this.gameScreen = gameScreen;
        container = new VBox();
        container.setAlignment(Pos.CENTER);
        powerList = new ListView<String>();
        powerList.getStyleClass().add("menu-list");
        whitePowers = FXCollections.observableArrayList();
        blackPowers = FXCollections.observableArrayList();

        powerList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int clickedIndex = powerList.getSelectionModel().getSelectedIndex();
                if(clickedIndex != selectedIndex){
                    System.out.println("clicked on " + clickedIndex);
                    System.out.println("Power: " + powerList.getItems().get(clickedIndex));
                    selectedIndex = clickedIndex;

                    if(gameScreen.getBoard().isSelected()){
                        gameScreen.getBoard().showPoweredMoves();
                    }
                }else{
                    System.out.println("Deselecting index " + clickedIndex);
                    powerList.getSelectionModel().clearSelection();
                    selectedIndex = -1;

                    if(gameScreen.getBoard().isSelected()){
                        gameScreen.getBoard().showMoves();
                    }
                }

            }
        });
        Label title = new Label("Powers Ups");
        title.getStyleClass().add("menu-text");
        container.getChildren().addAll(title,powerList);
    }

    public void setPowerUps(ArrayList<String> powers,String player){
        if(player.equalsIgnoreCase("black")){
            blackPowers.clear();
            blackPowers.addAll(powers);
            powerList.setItems(whitePowers);
        }else{
            powerList.setItems(blackPowers);
            whitePowers.clear();
            whitePowers.addAll(powers);
        }
    }

    public VBox getPowerUpMenu(){
        return container;
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }


}

