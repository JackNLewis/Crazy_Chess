package Networking;

import CrazyChess.logic.Position;

import java.io.Serializable;

public class Move implements Serializable {

    private Position start;
    private Position end;

    public Move(Position start,Position end){
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

}
