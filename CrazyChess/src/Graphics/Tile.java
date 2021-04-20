package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Tile {

    private StackPane sp;
    private Rectangle background;
    private ArrayList<ImageView> imgs;
    private Position pos;
    private int tileSize;


    public Tile(Position pos, int tileSize){
        this.pos = pos;
        this.tileSize = tileSize;
        sp = new StackPane();
        background = new Rectangle(tileSize,tileSize);
        background.setArcHeight(10);
        background.setArcWidth(10);
        imgs = new ArrayList<>();
        sp.getChildren().add(background);
    }

    /**
     * Adds the imageView of piece to the tile
     *
     */
    public void addImg(ImageView img){
        imgs.add(img);
        img.setFitWidth(tileSize-5);
        img.setFitHeight(tileSize-5);
        sp.getChildren().add(img);
    }

    /**
     * Removes the ImageView of the Piece from the tile
     *
     */
    public void removeImg(){
        for(ImageView img: imgs){
            sp.getChildren().remove(img);
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


