package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import Networking.Client;
import Networking.Move;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class GameScreen {

    private Scene scene;
    private BorderPane root;
    private boolean selected; // shows if a tile is already selected
    private Tile startTile; //Position of tile which is clicked
    private Tile endTile; //Position of Graphics.Tile want to move piece to
    private Client client;
    private ArrayList<Tile> tiles;
    private Label playerLabel;


    public GameScreen(Stage stage, Client client){

        this.client = client;
        root = new BorderPane();
        scene = new Scene(root,500,800);

        playerLabel = new Label("White");
        root.setTop(playerLabel);

        initBoard(client.getPlayer());
        renderGameState(client.getCurrentGameState(),true);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                client.close();
            }
        });
    }

    /**
     * Initializes the board variable as a GridPane and
     * fills it with empty squares. Then waits for mouseclicks
     * on the board
     *
     */
    public void initBoard(String player) {


        int squareSize = 60;
        GridPane board = new GridPane();
        tiles = new ArrayList<Tile>();

        for (int i=0; i<8; i++) {
                board.getColumnConstraints().add(new ColumnConstraints(squareSize));
                board.getRowConstraints().add(new RowConstraints(squareSize));
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile tile = new Tile(new Position(i,j), squareSize);
                tiles.add(tile);
                StackPane sp = tile.getSP();
                if ((i % 2 == 1 && j % 2 == 1) || ((i % 2 == 0) && (j % 2 == 0))) {
                    tile.setbgColor(Color.WHITE);
                } else {
                    tile.setbgColor(Color.GREY);
                }

                if(player.equalsIgnoreCase("white")){
                    board.add(sp,i,7-j);
                }else if(player.equalsIgnoreCase("black")){
                    board.add(sp,i,j);
                }
                else{
                    System.out.println("doesnt recognise player for init");
                }
            }
        }

        board.setMaxSize(60*8,60*8);
        root.setCenter(board);
        addMoveListeners();

        renderGameState(client.getCurrentGameState(),true);
    }

    /**
     * Method that renders a gamestate to the board
     *
     */
    public void renderGameState(AbstractPiece[][] gamesState,boolean successMove){
        for(Tile tile : tiles){
            int x = tile.getPos().getXpos();
            int y = tile.getPos().getYpos();
            AbstractPiece piece = gamesState[x][y];
            tile.removeImg();
            if(!(piece instanceof BlankPiece)){
                ImageView img = getImageView(piece);
                tile.addImg(img);
            }
            if (successMove){
                setDefaultColor(tile);
                selected = false;
            }
        }
    }

    /**
     * Method that adds the mouse listeners to the tiles on the board
     *
     */
    public void addMoveListeners(){
        for(Tile tile: tiles){
            tile.getSP().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    //Doesn't let you select if its not your turn
                    if(!client.isTurn()){
                        System.out.println("Not your turn");
                        return;
                    }
                    //If it is correct turn
                    if(!selected){
                        int x = tile.getPos().getXpos();
                        int y = tile.getPos().getYpos();
                        String pieceColor = client.getCurrentGameState()[x][y].getColor();
                        String playerColor = client.getPlayer();

                        //Checks that player chooses a piece that belongs to them
                        if(!pieceColor.equalsIgnoreCase(playerColor)){
                            System.out.println("You cannot choose that tile");
                            return;
                        }

                        //client.getCurrentGameState()[tile.getPos().getXpos()][tile.getPos().getYpos()];
                        tile.setbgColor(Color.GREEN);
                        startTile = tile;
                        selected = true;
                    }else{
                        //Deselect
                        if(startTile.equals(tile)){
                            setDefaultColor(tile);
                            selected = false;
                            return;
                        }

                        //Send move to server
                        endTile = tile;
                        client.sendMove(new Move(startTile.getPos(),endTile.getPos()));

                    }
                }
            });
        }
    }

    /**
     * Method that sets a tile to its deafult colour
     *
     */
    private void setDefaultColor(Tile tile){
        if ((tile.getPos().getXpos() % 2 == 1 && tile.getPos().getYpos() % 2 == 1)
                || ((tile.getPos().getXpos() % 2 == 0) && (tile.getPos().getYpos() % 2 == 0))) {
            tile.setbgColor(Color.WHITE);
        } else {
            tile.setbgColor(Color.GREY);
        }
    }

    /**
     * Method that returns an image view of the inputed piece
     * @param p   piece to be inputed
     * @return    ImageView of the piece
     */
    public ImageView getImageView(AbstractPiece p) {
        String name = p.getClass().getSimpleName().toLowerCase();
        String color=" ";

        if(p.getColor().equalsIgnoreCase("white")) {
            color="W_";
        }else if (p.getColor().equalsIgnoreCase("black")) {
            color="B_";
        }else if(p.getColor().equalsIgnoreCase("blank")) {
            /*if(game.isDebug()) {
                System.out.println("Can't get an image for an empty square");
            }*/
            return null;
        }
        String filename = color+name+".png";

        ImageView imgView = new ImageView();
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        imgView.setImage(new Image("/resources/pieces/"+filename));
        return imgView;
    }

    public Scene getScene(){
        return this.scene;
    }

    public void updateMoveLabel(){
        if(playerLabel.getText().equalsIgnoreCase("White")){
            playerLabel.setText("Black");
        }else{
            playerLabel.setText("White");
        }
    }


}
