package Graphics;

import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;

public class Tile {

    private StackPane sp;
    private Rectangle background;
    private ArrayList<ImageView> imgs;
    private Position pos;
    private int tileSize;


    /**
     * Constructor for a Tile
     * @param pos
     * @param tileSize
     */
    public Tile(Position pos, int tileSize){
        this.pos = pos;
        this.tileSize = tileSize;
        sp = new StackPane();
        background = new Rectangle(tileSize,tileSize);
//        background.setArcHeight(10);
//        background.setArcWidth(10);
        imgs = new ArrayList<>();
        sp.getChildren().add(background);
    }

    /**
     * Adds the imageView of piece to the tile
     *
     */
    public void addImg(ImageView img){
        imgs.add(img);
        if(img.getImage().getWidth() == 64) {
        	img.setFitWidth(tileSize-2);
            img.setFitHeight(tileSize-2);
        }else {
        	img.setFitWidth(tileSize);
            img.setFitHeight(tileSize);
        }
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
    public void setbgColor(Image img){
        background.setFill(new ImagePattern(img));
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


