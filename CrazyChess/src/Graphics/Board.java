package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import Networking.Client;
import Networking.Move;
import Networking.Server;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Board {

    private boolean selected; // shows if a tile is already selected
    private Tile startTile; //Position of tile which is clicked
    private Tile endTile; //Position of Graphics.Tile want to move piece to
    private ArrayList<Tile> tiles;
    private GridPane board;
    private Client client;

    public Board(Client client){
        this.client = client;
        initBoard(client.getPlayer());

    }

    public void initBoard(String player) {
        int squareSize = 60;
        board = new GridPane();
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
        addMoveListeners();

        renderGameState(client.getCurrentGameState(),true);
    }

    public void renderGameState(AbstractPiece[][] gamesState, boolean successMove){
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


    public ImageView getImageView(AbstractPiece p) {
        String name = p.getClass().getSimpleName().toLowerCase();
        String color=" ";

        if(p.getColor().equalsIgnoreCase("white")) {
            color="W_";
        }else if (p.getColor().equalsIgnoreCase("black")) {
            color="B_";
        }else if(p.getColor().equalsIgnoreCase("blank")) {
            return null;
        }
        String filename = color+name+".png";

        ImageView imgView = new ImageView();
        imgView.setFitWidth(60);
        imgView.setFitHeight(60);
        imgView.setImage(new Image("/resources/pieces/"+filename));
        return imgView;
    }


    private void setDefaultColor(Tile tile){
        if ((tile.getPos().getXpos() % 2 == 1 && tile.getPos().getYpos() % 2 == 1)
                || ((tile.getPos().getXpos() % 2 == 0) && (tile.getPos().getYpos() % 2 == 0))) {
            tile.setbgColor(Color.WHITE);
        } else {
            tile.setbgColor(Color.GREY);
        }
    }

    public GridPane getBoard(){
        return this.board;
    }
}
