package Graphics;

import CrazyChess.logic.*;
import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.logic.powerups.PowerupMain;
import CrazyChess.pieces.AbstractPiece;
import CrazyChess.pieces.Bishop;
import CrazyChess.pieces.BlankPiece;
import CrazyChess.pieces.Knight;
import CrazyChess.pieces.Pawn;
import CrazyChess.pieces.Powerup;
import CrazyChess.pieces.Queen;
import CrazyChess.pieces.Rook;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import Graphics.music;
import java.util.ArrayList;

/**
 * This class is for the actual game screen. It conects different components of the game screen such as board, power up menu.
 *
 */
public class SBoard {

    //Main screen window connects other components
    private SGameScreen SGameScreen;

    //Used for checking
    private MainLogic game;
  //  private ExtraChecksAndTools ect;
    private Utilities util;

    //board logic
    private boolean selected; // shows if a tile is already selected
    private ArrayList<Tile> tiles;
    private GridPane board;
    private double boardSize;
    private Tile selectedTile;

    private AskForDraw askForDraw;
    
    //to do with power up
    private ArrayList<Position> validMoves;
    private PowerUpMenu powerUps;
    private PowerupMain powerMain; // Used to see valid powered moves
    
    private music sound; // Used to play sound
    
    //to add pawn promotion button
    private HBox Wpawnpormote;
    private HBox Bpawnpormote;
    
    private boolean aiEnabled = false;
    private AI ai = new AI();
    
//    private HazardPiece hazardPiece;

    public SBoard(MainLogic game, SGameScreen SGameScreen){
        initBoard("white");
        this.game = game;
        this.SGameScreen = SGameScreen;
        selected = false;
     //   ect = new ExtraChecksAndTools();
        util = new Utilities();
        askForDraw = new AskForDraw(SGameScreen, game);
        powerUps = SGameScreen.getPwrUpMenu();
        powerMain = new PowerupMain();
        sound = new music();
        
    }

    public void initBoard(String player) {
        int squareSize = 66;
        boardSize = 50*8;
        board = new GridPane();
        tiles = new ArrayList<Tile>();

        Wpawnpormote = new HBox(4);
        board.add(Wpawnpormote, 0 , 10);
        Bpawnpormote = new HBox(4);
        board.add(Bpawnpormote, 5 , 10);
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
//        board.setHgap(1);
//        board.setVgap(1);
        board.setMaxSize(boardSize,boardSize);

        addMoveListeners();
    }

    public void renderGameState(AbstractPiece[][] gamesState){
        for(Tile tile : tiles){
            int x = tile.getPos().getXpos();
            int y = tile.getPos().getYpos();
            AbstractPiece piece = gamesState[x][y];
            setDefaultColor(tile);
            tile.removeImg();
            if(!(piece instanceof BlankPiece)){
                ImageView img = getImageView(piece);
                tile.addImg(img);
                if(piece instanceof HazardPiece){
                    ImageView imgOrig = getImageView(((HazardPiece) piece).getOriginalPiece());
                    tile.addImg(imgOrig);
                }
            }
        }
    }

    public void addMoveListeners(){
        for(Tile tile: tiles){
            tile.getSP().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String currentColor = game.getTurn();
//                    System.out.println("current turn " + currentColor);
//                    System.out.println("current gamestate ");
                    //util.printGameState(game.getGamestate());
                    String selectedColor = game.getPiece(tile.getPos()).getColor();

                    boolean success = false;

                    
                    //check pawn promotion is or is not valid
                    if (game.getPiece(tile.getPos()) instanceof Pawn && (game.getPiece(tile.getPos()).getColor().equalsIgnoreCase("White") || game.getPiece(tile.getPos()).getColor().equalsIgnoreCase("Black"))
                    		 && (tile.getPos().getYpos() == 0||tile.getPos().getYpos() == 7)) {
                    	if(game.getPiece(tile.getPos()).getColor().equalsIgnoreCase("White")) {
                    		PawnPromote(getWBox(), tile.getPos());
                    	}
                    	if(game.getPiece(tile.getPos()).getColor().equalsIgnoreCase("Black")) {
                    		PawnPromote(getBBox(), tile.getPos());
                    	}
                     	
                     	return;
                     	
                   }
                    

                    //If tile not selected
                    if(!selected){
                    	if(game.getDrawAsked() || game.getDraw()){
                    		return;
                    	}
                        //Make sure you only select tiles of your colour
                        if(selectedColor.equalsIgnoreCase(currentColor)){
                            selected = true;
                            selectedTile = tile;
                            // check if power up selected
                            if(powerUps.getSelectedIndex() != -1){
                                if(powerUps.getSelectedStr().equalsIgnoreCase("bomb")){
                                    System.out.println("place bomb and change turn");
                                    boolean poweredMove = game.usePowerup(powerUps.getSelectedIndex(), tile.getPos(),null);
                                    if(poweredMove){
                                        playPwSound();
                                        System.out.println("Successful bomb place");
                                        success = true;
                                    }
                                }
                                else{
                                    showPowerMoves();
                                }
                            }else {
                                showMoves();
                            }
                        }
                    }
                    //Tile is selected so execute a move
                    else{
                        if(tile.equals(selectedTile) || game.getDrawAsked() || game.getDraw()){
                            //deselect current tile
                            renderGameState(game.getGamestate());
                            validMoves = null;
                            selectedTile = null;
                            selected = false;
                            if(powerUps.getSelectedIndex()!=-1){
                                showInitPowerMoves();
                            }
                            return;
                        }
                        

                        //Execute a Move
                        //check if a power up is selected
                        if(powerUps.getSelectedIndex() != -1){
                            boolean poweredMove = game.usePowerup(powerUps.getSelectedIndex(), selectedTile.getPos(),tile.getPos());
                            if(poweredMove){
                                // SUCCESFFUL POWERED MOVE
                            	//play sound effects
                            	playPwSound();
                                System.out.println("Successful powered up move");
                                success = true;
                            }else{
                                System.out.println("Unsucessful powered move");
                                return;
                            }
                        }
                        
                        
                        //NORMAL MOVE if no power up selected
                        else{
                            boolean normalMove = game.moveTo(game.getPiece(selectedTile.getPos()),tile.getPos().getXpos(),tile.getPos().getYpos());
                            //Normal move was successful
                            if(normalMove){
                                System.out.println("Successful move");
                                updateGui();
				playNormalSound();
                                success = true;
                            }
                            //Normal move was unsuccessful
                            else{
                                System.out.println("Unsuccessful move");
                                return;
                            }
                        }
                    }
                    if(success){
                        ArrayList<String> powerUpList = game.getPowerUps(game.getTurn());
                        powerUps.setPowerUps(powerUpList,game.getTurn());
                        game.changeTurn();
                        //powerUps.showPowers(game.getTurn());
                        SGameScreen.updateMoveLabel(game.getTurn());
                      /*  SGameScreen.*/updateRuleChangeInfo();
                        selectedTile = null;
                        validMoves = null;
                        selected = false;
                        powerUps.setSelectedIndex(-1);
                        renderGameState(game.getGamestate());

                        //If ai is enabled make the ai move
                        if(aiEnabled){
                            aiMove();
                        }
                    }

                }
            });
        }
    }


    public ImageView getImageView(AbstractPiece p) {
        String filename = "";
        String name;
        String color;
        if(p == null){
            System.out.println("p is null in getImage");
        }
        
        else if(p instanceof HazardPiece){
            System.out.println("is hazard piece");
//        	filename = "fire.png";
        	HazardPiece hazardPiece = (HazardPiece) p;
            if(hazardPiece.getHazard() == Hazard.FROZEN) {
            	filename = "ice.png";
            	System.out.println("frozen");

            }
            else if(hazardPiece.getHazard() == Hazard.BURN){
            	filename = "fire.png";
                System.out.println("Hazard image fire");
            }
        }
        else if(p.getColor().equalsIgnoreCase("white")) {
            name = p.getClass().getSimpleName().toLowerCase();
            color="W_";
            filename = color+name+".png";
        }else if (p.getColor().equalsIgnoreCase("black")) {
            name = p.getClass().getSimpleName().toLowerCase();
            color="B_";
            filename = color+name+".png";
        }else if(p instanceof Powerup){
            filename = "PowerUp.png";
        }
        ImageView imgView = new ImageView();
        imgView.setImage(new Image("/resources/pieces/"+filename));
        return imgView;
    }

    public void updateGui(){
        String oppColor = util.oppositeColor(game.getTurn());
        SGameScreen.setInfoMessage("");
        System.out.println("Opposite color: " + oppColor);
        if(game.getCheckStatus(oppColor)){
            System.out.println(oppColor + " is in check");
            SGameScreen.setInfoMessage(oppColor + " is in check");
        }
        if(game.getMateStatus(oppColor)){
            SGameScreen.setInfoMessage(game.getTurn() + " wins!");
            System.out.println(oppColor + " is in check mate");
            askForDraw.hide();
        }
        if(game.getDraw()){
            SGameScreen.setInfoMessage("The game ended in a draw");
            System.out.println("Is a draw");
            askForDraw.hide();
        }
    }
    private void setDefaultColor(Tile tile){
        if ((tile.getPos().getXpos() % 2 == 1 && tile.getPos().getYpos() % 2 == 1)
                || ((tile.getPos().getXpos() % 2 == 0) && (tile.getPos().getYpos() % 2 == 0))) {
            tile.setbgColor(new Image("/resources/blackTile.png"));
        } else {
            tile.setbgColor(new Image("/resources/whiteTile.png"));
        }
    }

    public void showMoves(){
        renderGameState(game.getGamestate());
        if(selectedTile ==null){
            return;
        }
      //  validMoves = ect.validMoves(game.getPiece(selectedTile.getPos()),false,game.getGamestate(),game.getTurnNo());
        validMoves = game.getEcat().validMoves(game.getPiece(selectedTile.getPos()),false,game.getGamestate(),game.getTurnNo());
        for(Tile tile: tiles){
            for(Position pos: validMoves){
                if(tile.getPos().equals(pos)){
                    tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
                }
            }
        }
        selectedTile.setbgColor(new Image("/resources/selectedTile.png"));
    }

    public void showPowerMoves(){
        renderGameState(game.getGamestate());
        int powerIndex = powerUps.getSelectedIndex();
        String power = game.getPowerUps(game.getTurn()).get(powerIndex);
        ArrayList<Position> poweredMoves = powerMain.validPowerupMoves(power,game.getGamestate(),selectedTile.getPos(),false);
        for(Tile tile: tiles){
            for(Position pos: poweredMoves){
                if(tile.getPos().equals(pos)){
                	tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
                }
            }
        }
        if(selectedTile!=null){
            selectedTile.setbgColor(new Image("/resources/selectedTile.png"));
        }
    }

    public void showInitPowerMoves(){
        renderGameState(game.getGamestate());
        int powerIndex = powerUps.getSelectedIndex();
        String power = game.getPowerUps(game.getTurn()).get(powerIndex);
        ArrayList<Position> poweredMoves = powerMain.initialPowerupMoves(power,game.getGamestate(),game.getTurn());
        //ArrayList<Position> poweredMoves = powerMain.validPowerupMoves(power,game.getGamestate(), selectedTile.getPos(), false);
        for(Tile tile: tiles){
            for(Position pos: poweredMoves){
                if(tile.getPos().equals(pos)){
                    tile.addImg(new ImageView(new Image("/resources/PossibleMove.png")));
                }
            }
        }
        validMoves = null;
        selectedTile = null;
        selected = false;
    }

    //to play chessmove and Bomb sound
    private void playNormalSound(){
    	if(SGameScreen.isMusicOn()) {
    		sound.turnOn();
    	}
    	else {
    		sound.turnOff();
    	}
        if(game.getBB() == 1 || (game.getTurnNo() == game.getBBlt() + 4 &&!(game.getBBlt() == 0))) {
        	if(SGameScreen.isMusicOn() && !SGameScreen.isbombOn()) {
    			sound.turnOffbomb();
    		}
        	sound.Bomb();
    		game.resetBBombsound();
    	}
    	else if(game.getWB() == 1|| (game.getTurnNo() == game.getWBlt() + 4 &&!(game.getWBlt() == 0))) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isbombOn()) {
    			sound.turnOffbomb();
    		}
    		sound.Bomb();
    		game.resetWBombsound();
    	}
    	else {
    		if(SGameScreen.isMusicOn() && !SGameScreen.ischessmoveOn()) {
    			sound.turnOffChessmove();
    		}
    		sound.chessmove();
    	}
    }
    
    //to play powerups sound
    private void playPwSound(){
        // SUCCESFFUL POWERED MOVE
        //play sound effects
    	//check sound menu
    	if(SGameScreen.isMusicOn()) {
    		sound.turnOn();
    	}
    	else {
    		sound.turnOff();
    	}
    	//play sound effects
    	if(powerUps.getSelectedStr().equalsIgnoreCase("teleport")) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isTeleportOn()) {
    			sound.turnOffTeleport();
    		}
    		sound.Teleport();
    	}
    	if(powerUps.getSelectedStr().equalsIgnoreCase("minipromote")) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isTeleportOn()) {
    			sound.turnOffTeleport();
    		}
    		sound.MiniPromote();
    	}
    	if(powerUps.getSelectedStr().equalsIgnoreCase("freecard")) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isFreeCardOn()) {
    			sound.turnOffFreeCard();
    		}
    		sound.FreeCard();
    	}
    	if(powerUps.getSelectedStr().equalsIgnoreCase("bomb")) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isSetbombOn()) {
    			sound.turnOffSetBomb();
    		}
    		sound.SetBomb();
    	}
    	if(powerUps.getSelectedStr().equalsIgnoreCase("dummypiece")) {
    		if(SGameScreen.isMusicOn() && !SGameScreen.isDummyPieceOn()) {
    			sound.turnOffDummy();
    		}
    		sound.DummyPiece();
    	}
        System.out.println("Successful powered up move");
        powerUps.setSelectedIndex(-1);
    }
    
    /**
	 * Method for PawnPromotion. add 4 buttons when click on a pawn which has reach the edge of board.
	 * r for rook, b for bishop, k for knight, q for queen
	 * @param b the box we use for adding buttons, we have two in SBoard, one for white and the other one for black
	 * @param p the position of the pawn which reach the edge of board.
	 */
	
	public void PawnPromote(HBox b,Position p) {
		AbstractPiece[][] gamestateCopy = util.safeCopyGamestate(game.getGamestate());
		AbstractPiece copiedPiece = util.getPiece(p, true, gamestateCopy);
		if(copiedPiece.getColor().equalsIgnoreCase(game.getTurn())&& b.getChildren().isEmpty()) {
			Button rook = new Button();
			rook.setText("r");
			Button Bishop = new Button();
			Bishop.setText("b");
			Button Knight = new Button();
			Knight.setText("k");
			Button Queen = new Button();
			Queen.setText("q");
			b.getChildren().addAll(rook,Bishop,Knight,Queen);
			rook.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor())) {
						game.setGamestate(util.placePiece(new Rook(copiedPiece.getColor(),copiedPiece.getPosition(),"Normal"), true, game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else{
						System.out.println("not your bottom");
					}
				}
			});
			Bishop.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor())) {
						game.setGamestate(util.placePiece(new Bishop(copiedPiece.getColor(),copiedPiece.getPosition(),"Normal"), true, game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else{
						System.out.println("not your bottom");
					}
				}
			});
			Knight.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor())) {
						game.setGamestate(util.placePiece(new Knight(copiedPiece.getColor(),copiedPiece.getPosition(),"Normal"), true, game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else{
						System.out.println("not your bottom");
					}
				}
			});
			Queen.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					if (game.getTurn().equalsIgnoreCase(copiedPiece.getColor())) {
						game.setGamestate(util.placePiece(new Queen(copiedPiece.getColor(),copiedPiece.getPosition(),"Normal"), true, game.getGamestate()));
						b.getChildren().remove(rook);
						b.getChildren().remove(Bishop);
						b.getChildren().remove(Knight);
						b.getChildren().remove(Queen);
						game.changeTurn();
						renderGameState(game.getGamestate());
					}
					else{
						System.out.println("not your bottom");
					}
				}
			});
			
		}
	}

    public GridPane getBoard(){
        return this.board;
    }
    
    public HBox getWBox() {
    	return Wpawnpormote;
    }
    public HBox getBBox() {
    	return Bpawnpormote;
    }

    public boolean isSelected(){
        return selected;
    }

    public void enableAI(String levels){
        this.aiEnabled = true;
        if(levels == "easy") {
        	ai.AI(game, "easy");
        }
        else if(levels == "medium") {
        	ai.AI(game, "medium"); 
        }
        else {
        	ai.AI(game, "hard");
        }
    }

    private void aiMove(){
        AbstractPiece[][] gs = this.ai.AI(game);
        game.setGamestate(gs);
        renderGameState(game.getGamestate());

        String oppColor = (game.getTurn().equalsIgnoreCase("white")) ? "black" : "white";

        ArrayList<String> powerUpList = game.getPowerUps(game.getTurn());
        powerUps.setPowerUps(powerUpList,game.getTurn());
        game.changeTurn();
        updateRuleChangeInfo();

        if(game.getCheckStatus(game.getTurn())){
            System.out.println("Ai has checked you ");
            SGameScreen.setInfoMessage("AI has Checked You");
        }

        if(game.getMateStatus(game.getTurn())){
            System.out.println("AI has check mated you");
            SGameScreen.setInfoMessage("AI Wins");
        }

        if(game.getDraw()){
            System.out.println("Is a draw");
            SGameScreen.setInfoMessage("The game ended in a draw");
        }
        SGameScreen.updateMoveLabel(game.getTurn());
    }
    
    public void updateRuleChangeInfo(){
   // 	System.out.println("qqqqqqqqqq");
        if(game.getBrs()){
        	if(game.getCounter() == 1) {
        		SGameScreen.getRCinfo().setText("Bishops & Rooks are switched! Last turn.");
			} else {
				SGameScreen.getRCinfo().setText("Bishops & Rooks are switched! Turns left: " + game.getCounter());
			}
        }
        else if (game.getPS()){
        	if(game.getCounter() == 1) {
        		SGameScreen.getRCinfo().setText("Pawns can go backwards! Last turn.");
			} else {
				SGameScreen.getRCinfo().setText("Pawns can go backwards! Turns left: " + game.getCounter());
			}
        }
        else if (game.getKS()){
        	if(game.getCounter() == 1) {
        		SGameScreen.getRCinfo().setText("Kings can move like Queens! Last turn.");
			} else {
				SGameScreen.getRCinfo().setText("Kings can move like Queens! Turns left: " + game.getCounter());
			}
        }
        else{
        	SGameScreen.getRCinfo().setText("");
        }
    }
}
