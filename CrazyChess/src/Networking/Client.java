package Networking;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{
    Socket socket;
    ObjectOutputStream output;
    String address = "localhost";
    int port = 5000;


    public Client(){
    }

    @Override
    public void run() {
        System.out.println("Created Client");
        try {
            socket = new Socket(address, port);

            //Set up output stream
            output = new ObjectOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMove(Move move){
        try {
            output.writeObject(move);
            System.out.println("Send Move: \n" +
                    "From " + move.getStart() +
                    "\nTo " + move.getEnd());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
