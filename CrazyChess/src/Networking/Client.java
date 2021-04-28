package Networking;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.Position;
import CrazyChess.pieces.AbstractPiece;
import Graphics.multiplayer.GameScreen;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Client implements Runnable{

    Socket socket;
    String address = "localhost";
    int port = 5000;
    Semaphore semaphore;
    ObjectOutputStream output;
    ClientReciever clientReciever;
    int turnNo;
    String player;
    boolean isTurn;
    boolean isConnected = false;
    AbstractPiece[][] currrentGameState;
    private ExtraChecksAndTools ect;
    GameScreen gameScreen;
    
    public Client(Semaphore semaphore){
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        //System.out.println("Created Client");
        try {
            socket = new Socket(address, port);
            isConnected = true;
            ect = new ExtraChecksAndTools();
            //Set up output stream
            output = new ObjectOutputStream(socket.getOutputStream());
            init();
        } catch (ConnectException e) {
            isConnected = false;
            semaphore.release();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void init() throws IOException, ClassNotFoundException {
        //Set up input stream
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        //Get initial game state from server
        GameState initialGameState = (GameState) input.readObject();
        this.player = initialGameState.getPlayer();
        this.isTurn = initialGameState.isTurn();
        currrentGameState = initialGameState.getGameState();

        //System.out.println("Initilaized Client");

        //Creates a client reader thread to recieve gamestates
        clientReciever = new ClientReciever(input,this);
        Thread thread = new Thread(clientReciever);
        thread.start();

        //Enable GameScreen to be created
        semaphore.release();
    }

    public void sendMove(Move move){
        try {
            output.writeObject(move);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setGameScreen(GameScreen gameScreen) {
        clientReciever.setGameScreen(gameScreen);
        this.gameScreen = gameScreen;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn){
        this.isTurn = turn;
    }

    public ArrayList<Position> getAvilableMoves(Position pos){
        AbstractPiece piece = currrentGameState[pos.getXpos()][pos.getYpos()];
        System.out.println(piece.toString());

        ArrayList<Position> validMoves = new ArrayList<Position>(
                ect.validMoves(
                        piece,
                        false,
                        currrentGameState,
                        turnNo
                ).keySet()
        );
        return validMoves;
    }

    public AbstractPiece[][] getCurrentGameState() {
        return this.currrentGameState;
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean isConnected(){
        return isConnected;
    }

    public void setCurrrentGameState(AbstractPiece[][] currrentGameState) {
        this.currrentGameState = currrentGameState;
    }

    public void setTurnNo(int turnNo){
        this.turnNo = turnNo;
    }


}
