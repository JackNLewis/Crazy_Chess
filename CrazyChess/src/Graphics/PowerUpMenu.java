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

/**
 * Class for the power up menu where players can select the power ups they want to use.
 */
public class PowerUpMenu {

    private VBox container;
    private ObservableList<String> whitePowers;
    private ObservableList<String> blackPowers;
    private final ListView<String> powerList;
    private SGameScreen gameScreen;
    private int selectedIndex = -1;
    private String selectedstr;

    /**
     * Constructor for power up menu
     * @param gameScreen the SGameScreen object
     */
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
                    selectedstr = powerList.getItems().get(selectedIndex);
                    gameScreen.getBoard().showInitPowerMoves();

                }else{
                    System.out.println("Deselecting index " + clickedIndex);
                    powerList.getSelectionModel().clearSelection();
                    selectedIndex = -1;
                    selectedstr = null;
                    gameScreen.getBoard().showMoves();

                }

            }
        });
        Label title = new Label("Power Ups");
        title.getStyleClass().add("menu-text");
        container.getChildren().addAll(title,powerList);
    }

    /**
     * Sets the observable power up
     *
     * @param powers the arraylist of powers you are setting it to
     * @param player the player whos power ups your setting
     */
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

    /**
     *
     * @return the power up menu container
     */
    public VBox getPowerUpMenu(){
        return container;
    }

    /**
     *
     * @return the index of the selected power up
     */
    public int getSelectedIndex(){
        return selectedIndex;
    }

    /**
     * sets the selected index
     *
     * @param index the index of the selectd power up
     */
    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    /**
     *
     * @return the string of the selected power up
     */
    public String getSelectedStr() {
    	return selectedstr;
    }



}

