package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {

    private StackPane sp;
    private Rectangle background;
    private ImageView pieceImg;
    private Position pos;
    private int tileSize;


    public Tile(Position pos, int tileSize){
        this.pos = pos;
        this.tileSize = tileSize;
        sp = new StackPane();
        background = new Rectangle(tileSize,tileSize);
        background.setArcHeight(10);
        background.setArcWidth(10);

        sp.getChildren().add(background);
    }

    /**
     * Adds the imageView of piece to the tile
     *
     */
    public void addImg(ImageView img){
        this.pieceImg = img;
        img.setFitWidth(tileSize-5);
        img.setFitHeight(tileSize-5);
        sp.getChildren().add(pieceImg);
    }

    /**
     * Removes the ImageView of the Piece from the tile
     *
     */
    public void removeImg(){
        if(pieceImg!=null){
            sp.getChildren().remove(pieceImg);
        }
    }

    /**
     * Sets the color of the tile background
     *
     */
    public void setbgColor(Color color){
        background.setFill(color);
    }

    /**
     * Returns the stackpane which contains all the nodes for a given tile
     *
     * @return the stackpane of tile
     *
     */
    public StackPane getSP(){
        return sp;
    }

    /**
     * @return returns the position of a tile
     *
     */
    public Position getPos(){
        return this.pos;
    }


}


