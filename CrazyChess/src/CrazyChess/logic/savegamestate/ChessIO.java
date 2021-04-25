package CrazyChess.logic.savegamestate;

import CrazyChess.logic.MainLogic;
import CrazyChess.pieces.AbstractPiece;

public interface ChessIO {
	
//	void load(byte[] data, MainLogic board);

//	byte[] save(MainLogic board);
	
	void load(byte[] data, MainLogic board, AbstractPiece[][] gamestate);
	
	byte[] save(MainLogic board, AbstractPiece[][] gamestate);

	String getFileTypeDescription();

	String getFileExtension();
	
}
