package Graphics;

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
import java.util.concurrent.Semaphore;

public class GameScreenClient {

    private Scene scene;
    private StackPane root;
    private boolean selected; // shows if a tile is already selected
    private Tile startTile; //Position of tile which is clicked
    private Tile endTile; //Position of Graphics.Tile want to move piece to
    private Client client;
    private ArrayList<Tile> tiles;

    public GameScreenClient(){

        Semaphore semaphore = new Semaphore(0);
        client = new Client(semaphore,this);
        Thread thread = new Thread(client);
        thread.start();

        root = new StackPane();
        scene = new Scene(root,500,800);


        try {
            semaphore.acquire();
            initBoard(client.getPlayer());
            renderGameState(client.getCurrentGameState(),true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        board.setAlignment(Pos.CENTER);
        root.getChildren().addAll(board);
        addMoveListeners();
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

                        //Recieve Move from server
                        //GameState gs = client.reciveGameState();
                        //renderGameState(gs.getGameState());
                        /*
                        //ONLY DESELECT TILE IF MOVE WAS SUCCESSFUL
                        if(!gs.isTurn()){
                            setDefaultColor(startTile);
                            selected = false;
                        }*/

                    }
                }
            });
        }
    }

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
}
