package Graphics;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.BlankPiece;
import CrazyChess.pieces.Powerup;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class SinglePlayerBoard {

    private boolean selected; // shows if a tile is already selected
    private Tile startTile; //Position of tile which is clicked
    private Tile endTile; //Position of Graphics.Tile want to move piece to
    private ArrayList<Tile> tiles;
    private GridPane board;
    private double boardSize;
    private MainLogic game;
    private Tile selectedTile;
    private ExtraChecksAndTools ect;
    private Utilities util;
    private ArrayList<Position> validMoves;
    private SinglePlayer singlePlayer;
    private PowerUpMenu powerUps;
    private PowerUpMenu pwrUp;
    private PowerupMain powerMain;

    public SinglePlayerBoard(MainLogic game, SinglePlayer singlePlayer){
        initBoard("white");
        this.game = game;
        this.singlePlayer = singlePlayer;
        selected = false;
        ect = new ExtraChecksAndTools();
        util = new Utilities();
        powerUps = singlePlayer.getPwrUpMenu();
        powerMain = new PowerupMain();
    }

    public void initBoard(String player) {
        int squareSize = 50;
        boardSize = 50*8;
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
                setDefaultColor(tile);

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
        board.setHgap(5);
        board.setVgap(5);
        board.setMaxSize(boardSize,boardSize);

        addMoveListeners();
    }

    public void renderGameState(AbstractPiece[][] gamesState){
        System.out.println("Rendering");
        for(Tile tile : tiles){
            int x = tile.getPos().getXpos();
            int y = tile.getPos().getYpos();
            AbstractPiece piece = gamesState[x][y];
            setDefaultColor(tile);
            tile.removeImg();
            if(!(piece instanceof BlankPiece)){
                //System.out.println(piece.toString());
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
                    String currentColor = game.getTurn();
                    System.out.println("current turn " + currentColor);
                    String selectedColor = game.getPiece(tile.getPos()).getColor();
                    //System.out.println("Selected piece color: " + selectedColor);

                    if(!selected){

                        //Make sure you only select tiles of your colour
                        if(selectedColor.equalsIgnoreCase(currentColor)){
                            selected = true;
                            tile.setbgColor(Color.web("#EF476F"));
                            selectedTile = tile;

                            if(powerUps.getSelectedIndex() != -1){
                                showPoweredMoves();
                            }else{
                                showMoves();
                            }

                        }
                    }
                    //code for executing a move
                    else{
                        if(tile.equals(selectedTile)){
                            //deselect current tile
                            renderGameState(game.getGamestate());

                            validMoves = null;
                            selectedTile = null;
                            selected = false;
                            return;
                        }

                        //check if a power up is selected
                        if(powerUps.getSelectedIndex() != -1){
                            game.usePowerup(powerUps.getSelectedIndex(), selectedTile.getPos(), tile.getPos());

                            game.changeTurn();
                            powerUps.showPowers(game.getTurn());
                            renderGameState(game.getGamestate());
                            return;
                        }

                        boolean moved = game.moveTo(game.getPiece(selectedTile.getPos()),tile.getPos().getXpos(),tile.getPos().getYpos());
                        if(moved){
                            System.out.println("Successful move");
                            selectedTile = null;
                            validMoves = null;
                            selected = false;
                            game.printGameState();
                            String oppColor = util.oppositeColor(game.getTurn());

                            singlePlayer.setInfoMessage("");
                            System.out.println("Opposite color: " + oppColor);
                            if(game.getCheckStatus(oppColor)){
                                System.out.println(oppColor + " is in check");
                                singlePlayer.setInfoMessage(oppColor + " is in check");
                            }
                            if(game.getMateStatus(oppColor)){
                                singlePlayer.setInfoMessage(game.getTurn() + " wins!");
                                System.out.println(oppColor + " is in check mate");
                            }

                            ArrayList<String> powerUpList = game.getPowerUps(game.getTurn());
                            powerUps.setPowerUps(powerUpList,game.getTurn());

                            game.changeTurn();
                            powerUps.showPowers(game.getTurn());

                            singlePlayer.updateMoveLabel(game.getTurn());
                        }else{
                            System.out.println("Unsuccessful move");
                        }

                        renderGameState(game.getGamestate());


                    }


                }
            });
        }
    }


    public ImageView getImageView(AbstractPiece p) {
        String name = p.getClass().getSimpleName().toLowerCase();
        String color=" ";
        String filename = "";
        if(p.getColor().equalsIgnoreCase("white")) {
            color="W_";
            filename = color+name+".png";
        }else if (p.getColor().equalsIgnoreCase("black")) {
            color="B_";
            filename = color+name+".png";
        }else if(p instanceof Powerup){
            filename = "PowerUp.png";
        }
        else if(p.getColor().equalsIgnoreCase("blank")) {
            return null;
        }


        ImageView imgView = new ImageView();
        imgView.setImage(new Image("/resources/pieces/"+filename));
        return imgView;
    }


    private void setDefaultColor(Tile tile){
        if ((tile.getPos().getXpos() % 2 == 1 && tile.getPos().getYpos() % 2 == 1)
                || ((tile.getPos().getXpos() % 2 == 0) && (tile.getPos().getYpos() % 2 == 0))) {
            tile.setbgColor(Color.web("#118AB2"));
        } else {
            tile.setbgColor(Color.web("#06D6A0"));
        }
    }

    public void showMoves(){
        renderGameState(game.getGamestate());
        validMoves = ect.validMoves(game.getPiece(selectedTile.getPos()),false,game.getGamestate(),game.getTurnNo());
        for(Tile tile: tiles){
            for(Position pos: validMoves){
                if(tile.getPos().equals(pos)){
                    tile.setbgColor(Color.web("#FFD166"));
                }
            }
        }
    }

    public void showPoweredMoves(){
        renderGameState(game.getGamestate());
        int powerIndex = powerUps.getSelectedIndex();
        String power = game.getPowerUps(game.getTurn()).get(powerIndex);
        ArrayList<Position> poweredMoves = powerMain.validPowerupMoves(power,game.getGamestate(), selectedTile.getPos(), false);
        for(Tile tile: tiles){
            for(Position pos: poweredMoves){
                if(tile.getPos().equals(pos)){
                    tile.setbgColor(Color.web("#FFD166"));
                }
            }
        }
        //showMoves(powerMain.validPowerupMoves(power,game.getGamestate(), selectedTile.getPos(), false));
    }

    public GridPane getBoard(){
        return this.board;
    }

    public boolean isSelected(){
        return selected;
    }
}
