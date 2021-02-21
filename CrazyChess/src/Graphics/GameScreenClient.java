package Graphics;


import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;

import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import Networking.Client;
import Networking.Move;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class GameScreenClient {

    private Scene scene;
    private StackPane root;
    ArrayList<Tile> tiles; // All the tiles on board
    MainLogic game;
    boolean selected; // shows if a tile is already selected
    Position startPosition; //Position of tile which is clicked
    Position endPosition; //Position of Tile want to move piece to
    Client client;

    public GameScreenClient(){
        game = new MainLogic();
        game.resetBoard();

        root = new StackPane();
        scene = new Scene(root,800,800);
        initBoard();

        renderGameState(tiles, game.getGamestate());
        client = new Client();
        Thread thread = new Thread(client);
        thread.start();
    }

    /**
     * Initializes the board variable as a GridPane and
     * fills it with empty squares. Then waits for mouseclicks
     * on the board
     *
     */
    public void initBoard() {
        int squareSize = 60;
        GridPane board = new GridPane();
        board.setGridLinesVisible(true);
        tiles = new ArrayList<>();

        for (int i=0; i<8; i++) {
                board.getColumnConstraints().add(new ColumnConstraints(squareSize));
                board.getRowConstraints().add(new RowConstraints(squareSize));
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Tile tile = new Tile(new Position(i,j), squareSize);
                StackPane sp = tile.getSP();
                if ((i % 2 == 1 && j % 2 == 1) || ((i % 2 == 0) && (j % 2 == 0))) {
                    tile.setbgColor(Color.WHITE);
                } else {
                    tile.setbgColor(Color.GREY);
                }
                board.add(sp,i,7-j);
                tiles.add(tile);
            }
        }

        board.setMaxSize(60*8,60*8);
        board.setAlignment(Pos.CENTER);
        root.getChildren().addAll(board);
        addMoveListeners();
    }

    /**
     * Method that renders a gamestate to the board
     *
     */
    public void renderGameState(ArrayList<Tile> tiles,AbstractPiece[][] gamesState){
        for(Tile tile : tiles){
            int x = tile.getPos().getXpos();
            int y = tile.getPos().getYpos();

            System.out.println("X: " + x + "Y: " + y);
            AbstractPiece piece = gamesState[x][y];
            if(!(piece instanceof BlankPiece)){
                ImageView img = getImageView(piece);
                tile.addImg(img);
            }
        }
    }

    public void addMoveListeners(){
        for(Tile tile: tiles){
            tile.getSP().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(!selected){
                        tile.setbgColor(Color.GREEN);
                        startPosition = tile.getPos();
                        selected = true;
                    }else{
                        endPosition = tile.getPos();
                        client.sendMove(new Move(startPosition,endPosition));
                        System.out.println("Move from: " + startPosition.toString() + " to : " + endPosition.toString());
                        //Server will get reply
                        selected = false;
                    }
                }
            });
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
            if(game.isDebug()) {
                System.out.println("Can't get an image for an empty square");
            }
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
}
