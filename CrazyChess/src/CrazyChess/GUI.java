package CrazyChess;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import javafx.stage.Stage;

import java.util.ArrayList;

import CrazyChess.logic.*;
import CrazyChess.pieces.*;


/**
 * The GUI class. Will probably get heavily edited 
 * to implement new features.
 * 
 * @author Darius
 *
 */


public class GUI extends Application {
	
	private BorderPane borderPane;
	private GridPane board;

	//private int turnNumber; 
	
	private Position selectedSquare;
	private boolean lookingForMove;
	
	private MainLogic chess; //Object for the "server". All chess logic computation should happen in here,
							//and the purpose of the GUI is mainly to reflect the game state produced by this
							//and to pass mouseclicks on the GUI board to it.
	private AI ai = new AI();
	
	
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * This method gets executed when GUI starts.
     * Values needed for the program to run are initialized here.
     * 
     * If we need to initialize new values in the future, do it here.
     * 
     */
    @Override
	public void init(){
		//initialize the chess API
		chess = new MainLogic();
		chess.resetBoard();

		if(chess.isDebug())
			System.out.println("Pre-initialization complete.");
	}
    
    /**
	 * Method that actually starts the GUI. 
	 * Ideally should mostly deal only with stage construction.
	 */
	@Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("CrazyChess");
		borderPane = new BorderPane();
		initBoard();
	    borderPane.setCenter(board);
	    primaryStage.setScene(new Scene(borderPane, 550,550));
	    updateBoard();

	    primaryStage.show();

	    updateBoard();
	}

	/**
	 * Translates a click somewhere on the board GridePane into 
	 * a Position object. Checks if click is
	 * inside the boundaries, returns null if not on a square.
	 *
	 * @param eventX   the event.getX() input
	 * @param eventY   the event.getY() input
	 * @return         adjusted position if valid, null if invalid
	 */
	public Position getClickPos(double eventX, double eventY){
		int padding = 38;
		if(eventX < padding || eventY < padding)
			return null;
		else if(eventX > 518 || eventY > 518)
			return null;
		int x = ((int) (eventX - padding))/60;
		int y = ((int) (eventY - padding))/60;
		return new Position(x, 7-y);
	}
	
	/**
	 * This method should be called whenever a square on the board
	 * is clicked. It checks if the square clicked is valid.
	 * Then, depending on if the player is selecting a piece to
	 * move or searching for a move currently, performs different actions.
	 * If the player is selecting a piece it highlights it.
	 * If the player has already selected a piece and is now currently
	 * trying to do a move, it tries to do the move and update the GUI.
	 *
	 * @param pos   the position of the square clicked
	 */
	public void squareClicked(Position pos){
		if(chess.isDebug())
			System.out.println("Looking for move is " + lookingForMove);

		if(pos == null)
			return;
		if(pos.getXpos() > 7 || pos.getXpos() < 0 || pos.getYpos() > 7 || pos.getYpos() < 0)
			return;
		if(chess.getPiece(pos).getColor().equalsIgnoreCase("Blank") && !lookingForMove){
			if(chess.isDebug())
				System.out.println("Blank space selected without movement");
			return;
		}
		if(chess.getPiece(pos).getColor().equalsIgnoreCase("Powerup") && !lookingForMove){
			if(chess.isDebug())
				System.out.println("You can't move powerups");
			return;
		}
		if(selectedSquare.equals(pos)){
			if(chess.isDebug())
				System.out.println("Deselecting...");
			updateBoard();
			selectedSquare.setXpos(-1);
			selectedSquare.setYpos(-1);
			lookingForMove = false;
			return;
		}
		if(chess.getTurn().equalsIgnoreCase("white") && chess.getPiece(pos).getColor().equalsIgnoreCase("black") && !lookingForMove){
			if(chess.isDebug())
				System.out.println("It is white's turn");
			return;
		}
		else if(chess.getTurn().equalsIgnoreCase("black") && chess.getPiece(pos).getColor().equalsIgnoreCase("white") && !lookingForMove){
			if(chess.isDebug())
				System.out.println("It is black's turn");
			return;
		}
		if(lookingForMove){
			//Do move!
			//first, get piece that is piece of prior selected square
			AbstractPiece currentPiece = chess.getPiece(selectedSquare);
			System.out.println(currentPiece.toString());
			//Now the actual move check
			System.out.println(currentPiece);
			System.out.println(chess.getPiece(pos));
			if(!chess.moveTo(currentPiece, pos.getXpos(), pos.getYpos())){
				if(chess.isDebug())
					System.out.print("Move not successful.");
				chess.printGameState();
				return;
			}
			
			updateBoard();
			lookingForMove = false;
			selectedSquare.setXpos(-1);
			selectedSquare.setYpos(-1);
			chess.changeTurn();

			AbstractPiece[][] aiMove = ai.AI(chess);
			
			
			//chess.setGamestate(aiMove);
			//updateBoard();
			//chess.changeTurn();
			return;
		}
		else if(!lookingForMove){//Nothing selected, needs move
			selectedSquare = pos;
			lookingForMove = true;
			setSquareSelectedColor(selectedSquare);
			return;
		}
	}
	
	
	
	
	
	
	/**
	 * A method that changes the color of a square selected
	 * It first updates the board, as to clear any other
	 * selection colors. 
	 *
	 * @param pos   position to set to select color
	 */
	public void setSquareSelectedColor(Position pos){
		updateBoard();
		Rectangle rect = new Rectangle();
		rect.setWidth(60);
		rect.setHeight(60);
		rect.setStroke(Color.GRAY);
		rect.setStrokeWidth(3);
		rect.setFill(Color.LIGHTGRAY);
		board.add(rect, pos.getXpos()+1, 8-pos.getYpos());
		board.add(getImageView(chess.getPiece(pos)) ,pos.getXpos()+1 , 8-pos.getYpos());
	}

	/**
	 * Initializes the board variable as a GridPane and 
	 * fills it with empty squares. Then waits for mouseclicks
	 * on the board
	 * 
	 */
	public void initBoard(){
		int squareSize = 60;
		int padding = 35;
		board = new GridPane();
		board.getRowConstraints().add(new RowConstraints(padding));
		board.getColumnConstraints().add(new ColumnConstraints(padding));
		for(int i = 0; i < 8; i++){
			board.getColumnConstraints().add(new ColumnConstraints(squareSize));
			board.getRowConstraints().add(new RowConstraints(squareSize));
		}
		for(int i = 1; i < 9; i++){
			for(int j = 1; j < 9; j++){
				Rectangle rect = new Rectangle();
				rect.setWidth(squareSize);
				rect.setHeight(squareSize);
				rect.setStroke(Color.GREY);
				rect.setStrokeWidth(3);
				if((i%2==1&&j%2==1)||(i%2==0&&j%2==0)){
					rect.setFill(Color.GREY);
				}
				else {
					rect.setFill(Color.WHITE);
				}
				board.add(rect, i, j);
			}
		}

		board.setOnMousePressed(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event) {
				squareClicked(getClickPos(event.getX(), event.getY()));
			}

			//SOMEWHERE AROUND HERE IS WHERE YOU WOULD ADD THE AI MOVE
			
			
			
			//This makes it so when a square is clicked (ignoring objects
		});//inside, it returns a proper chess coordinate as a Position object

		selectedSquare = new Position(-1, -1);
		lookingForMove = false;
	}

	/**
	 * Takes the data from the existing game state and places it on the
	 * GUI
	 */
	public void updateBoard(){
		board.getChildren().clear();
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				Rectangle rect = new Rectangle();
				rect.setWidth(60);
				rect.setHeight(60);
				rect.setStroke(Color.GREY);
				rect.setStrokeWidth(3);
				if(((i+1)%2==1&&(j+1)%2==1)||((i+1)%2==0&&(j+1)%2==0)){
					rect.setFill(Color.GREY);
				}
				else {
					rect.setFill(Color.WHITE);
				}
				
				board.add(rect, i+1, 8-j);
				AbstractPiece p = chess.getPiece(new Position(i,j));
				//if not blank
				if(!p.getColor().equalsIgnoreCase("blank")) {
				board.add(getImageView(p) ,p.getXpos()+1 , 8-p.getYpos());}
			}
		}
		
		//System.out.println("Update Complete");
	}
	
	/**
	 * Resets game state to starting position, then updates graphical board to reflect the change
	 */
	public void resetBoardUI(){
		chess.resetBoard();
		board.getChildren().clear();
		updateBoard();
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
		}else if (p.getColor().equalsIgnoreCase("powerup")) {
			color="P_";
		}else if(p.getColor().equalsIgnoreCase("blank")) {
			if(chess.isDebug()) {
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
	
	
	/**
	 * A safe method of stopping. 
	 * Does not need to be called manually
	 */
	@Override
	public void stop(){
		if(chess.isDebug())
			System.out.println("Safely stopping.");
	}
}
