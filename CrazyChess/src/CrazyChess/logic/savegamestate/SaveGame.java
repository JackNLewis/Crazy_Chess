package CrazyChess.logic.savegamestate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardAssigner;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.pieces.*;
import Graphics.SGameScreen;
	
public class SaveGame implements ChessIO {
	
//	MainLogic board = new MainLogic();
//	AbstractPiece[][] gs = new AbstractPiece[8][8];
	public AbstractPiece p;
	public AbstractPiece originalp;
	public AbstractPiece HPiece;
//	public HazardPiece hazardPiece;
	public Hazard hazardTile;
//	AbstractPiece color;
	HazardAssigner assignH = new HazardAssigner();
	Utilities utils = new Utilities();
	ExtraChecksAndTools ecat = new ExtraChecksAndTools();
    
	public static void saveDataToFile(byte[] data, File file) {
		try {
			Files.write(Paths.get(file.toURI()), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] save(MainLogic board, AbstractPiece[][] gamestate) {

		ArrayList<AbstractPiece> blackPieces = ecat.getBlackPieces(gamestate);
		ArrayList<AbstractPiece> whitePieces = ecat.getWhitePieces(gamestate);
		ArrayList<AbstractPiece> blankPieces = ecat.getBlankArrayList(gamestate);
		ArrayList<AbstractPiece> hazardPieces = ecat.getHazardPieces(gamestate);
//		assignH.assignHazard(gamestate);
//		assignH.getActiveHazard();
//		assignH.getUntilHazard();
//		

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//create new xml output factory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = null;
		try {
			writer = factory.createXMLStreamWriter(baos);
			writer.writeStartDocument();
			writer.writeStartElement("Board");
			writer.writeAttribute("CurrentTurn", board.getTurn() + "");
			writer.writeAttribute("TurnNo", String.valueOf(board.getTurnNo()));
			if(!blackPieces.isEmpty()) {
				writePieces(blackPieces, writer);
			}
			if(!whitePieces.isEmpty()) {
				writePieces(whitePieces, writer);
			}
			writePieces(blankPieces, writer);
			if(!hazardPieces.isEmpty()) {
				writePieces(hazardPieces, writer);
//				assignH.setActiveHazard(true);
//				assignH.getHazardTurns();
//				writer.writeAttribute("activeHazard", (assignH.getActiveHazard()) + "");
//				System.out.println("active hazard: " + (assignH.getActiveHazard()));
			}
			writer.writeEndElement();
		} catch (XMLStreamException ex) {
			ex.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.flush();
					writer.close();
				} catch (XMLStreamException ex) {
					ex.printStackTrace();
				}
			}
		}
		return baos.toByteArray();
	}

	private void writePieces(ArrayList<AbstractPiece> pieces, XMLStreamWriter writer) throws XMLStreamException {
		switch (pieces.get(0).getClass().getSimpleName()) {
		case "HazardPiece":
			writer.writeStartElement("Hazard");
			break;
		default:
			writer.writeStartElement(pieces.get(0).getColor());
		}

		for (AbstractPiece piece : pieces) {
			writer.writeEmptyElement("Piece");
			switch(piece.getClass().getSimpleName()) {
			case "HazardPiece":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
			    writer.writeAttribute("Colour", piece.getColor());
			    writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			    writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			    writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
				writer.writeAttribute("HazardType", ((HazardPiece) piece).getHazard() + "");
				writer.writeAttribute("OriginalPiece", ((HazardPiece) piece).getOriginalPiece().getClass().getSimpleName() +"");
				assignH.setActiveHazard(true);
				writer.writeAttribute("activeHazard", assignH.getActiveHazard() + "");
//				assignH.setUntilHazard(input);
				assignH.untilHazard = 2;
				writer.writeAttribute("untilHazard", assignH.getUntilHazard() + "");
//				writer.writeAttribute("getUntilHazard", assignH.getUntilHazard() + "");
//				writer.writeAttribute("getActiveHazard", assignH.getActiveHazard() + "");
//				writer.writer.writeAttribute("activeHazard", ((HazardAssigner) piece).getActiveHazard() + "");
				break;
			case "Pawn":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
			    writer.writeAttribute("Colour", piece.getColor());
			    writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			    writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			    writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
				writer.writeAttribute("Doublejump", ((Pawn) piece).getDoublejump() + "");
				writer.writeAttribute("enPassant", ((Pawn) piece).getEnPassant() + "");
				break;
			case "King":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
			    writer.writeAttribute("Colour", piece.getColor());
			    writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			    writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			    writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
				writer.writeAttribute("isChecked", ((King) piece).getIsChecked() + "");
				writer.writeAttribute("wasMovedKing", ((King) piece).getWasMoved() + "");
				break;
			case "Rook":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
			    writer.writeAttribute("Colour", piece.getColor());
			    writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			    writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			    writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
			    writer.writeAttribute("wasMovedRook", ((Rook)piece).getWasMoved() + "");
			    break;
			default:
			    writer.writeAttribute("Type", piece.getClass().getSimpleName());
			    writer.writeAttribute("Colour", piece.getColor());
			    writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			    writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			    writer.writeAttribute("Powerup", piece.getPoweruptype() + "");

			}
			
			
/////////////adding all attributes needed for hazardpiece
//			writer.writeAttribute("HazardType", ((HazardPiece) piece).getHazard() + "");
//			writer.writeAttribute("StageHazard", piece.equals(HazardPiece));
		}
		writer.writeEndElement();
	}
	
	
	public static byte[] loadDataFromFile(File file) {
		try {
			return Files.readAllBytes(Paths.get(file.toURI()));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public void load(byte[] data, MainLogic board, AbstractPiece[][] gamestate) {
		// AbstractPiece p;
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		try {
			reader = factory.createXMLStreamReader(bais);
			Stack<String> stack = new Stack<>();
			while (reader.hasNext()) {
				switch (reader.getEventType()) {
				
				case XMLStreamReader.START_ELEMENT:
					stack.push(reader.getName().getLocalPart());
					switch (String.join("/", stack)) {
					   case "Board":
						            System.out.println("currentturn: " + reader.getAttributeValue("", "CurrentTurn"));
						            board.setCurrentTurnColor(reader.getAttributeValue("", "CurrentTurn"));
						            board.setCurrentTurn(Integer.parseInt(reader.getAttributeValue("", "TurnNo")));
//						            System.out.println("successfully written turn");
						            break;
					   case "Board/Black/Piece":
//						            System.out.println("black pieces");
						            loadPiece(reader, gamestate);
						            System.out.println("DONE black pieces");
						            break; 
					   case "Board/White/Piece":
//						            System.out.println("white pieces");
						            loadPiece(reader, gamestate);
						            System.out.println("DONE white pieces");
						            break;
					   case "Board/Blank/Piece":
						            loadPiece(reader, gamestate);
						            System.out.println("DONE blanks");
						            break;
					   case "Board/Hazard/Piece":
//						            loadHazardPiece(reader, gamestate);
						            loadPiece(reader, gamestate);
						            System.out.println("DONE hazard pieces");
						            break;
					   default:
						            System.out.println("Next");
					}
				    
					break;
				case XMLStreamReader.END_ELEMENT:
					stack.pop();
					break;
				}

				reader.next();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private AbstractPiece[][] loadPiece(XMLStreamReader reader, AbstractPiece[][] gamestate) throws XMLStreamException {
		String type = reader.getAttributeValue("", "Type");
		String color = reader.getAttributeValue("", "Colour");
		String powerup = reader.getAttributeValue("", "Powerup");
		int xcoord = Integer.parseInt(reader.getAttributeValue("", "XCoordinate"));
		int ycoord = Integer.parseInt(reader.getAttributeValue("", "YCoordinate"));
		Position position = new Position(xcoord, ycoord);
		//needed for hazard pieces
		String OriginalPiece = reader.getAttributeValue("", "OriginalPiece");
		String HazardType = reader.getAttributeValue("", "HazardType");
		
		Boolean KingCheck = Boolean.parseBoolean(reader.getAttributeValue("", "isChecked"));
		Boolean KingMove = Boolean.parseBoolean(reader.getAttributeValue("", "wasMovedKing"));
		Boolean RookMove = Boolean.parseBoolean(reader.getAttributeValue("", "wasMovedRook"));
//		
//		if(HazardType == "FROZEN") {
//			hazardTile = Hazard.FROZEN;
//		}
//		else if(HazardType == "BURN") {
//			hazardTile = Hazard.BURN;
//		}
		
		if(type == "HazardPiece") {
			switch(OriginalPiece) {
			case "Rook":
				originalp = new Rook(color, position, powerup);
				((Rook) originalp).setWasMoved(RookMove);
				break;
			case "Knight":
				originalp = new Knight(color, position, powerup);
				break;
			case "Bishop":
				originalp = new Bishop(color, position, powerup);
				break;
			case "Queen":
				originalp = new Queen(color, position, powerup);
				break;
			case "King":
				originalp = new King(color, position, powerup);
				((King) originalp).setIsChecked(KingCheck);
				((King) originalp).setWasMoved(KingMove);
				break;
			case "Pawn":
				originalp = new Pawn(color, position, powerup);
				int PawnJump = Integer.parseInt(reader.getAttributeValue("", "Doublejump"));
				((Pawn) originalp).setDoublejump(PawnJump);
				Boolean Passant = Boolean.parseBoolean(reader.getAttributeValue("", "enPassant"));
				((Pawn) originalp).setEnPassant(Passant);
				break;
			case "BlankPiece":
				originalp = new BlankPiece(color, position, powerup);
				break;
			}
		}
		
		//load all pieces
		switch (type) {
		case "Rook":
			p = new Rook(color, position, powerup);
			((Rook) p).setWasMoved(RookMove);
			break;
		case "Knight":
			p = new Knight(color, position, powerup);
			break;
		case "Bishop":
			p = new Bishop(color, position, powerup);
			break;
		case "Queen":
			p = new Queen(color, position, powerup);
			break;
		case "King":
			p = new King(color, position, powerup);
			((King) p).setIsChecked(KingCheck);
			((King) p).setWasMoved(KingMove);
			break;
		case "Pawn":
			p = new Pawn(color, position, powerup);
			int PawnJump = Integer.parseInt(reader.getAttributeValue("", "Doublejump"));
			((Pawn) p).setDoublejump(PawnJump);
			Boolean Passant = Boolean.parseBoolean(reader.getAttributeValue("", "enPassant"));
			((Pawn) p).setEnPassant(Passant);
			break;
		case "BlankPiece":
			p = new BlankPiece(color, position, powerup);
			break;
		case "HazardPiece":
//			Hazard hazardTile = null;
			if(HazardType == "FROZEN") {
				hazardTile = Hazard.FROZEN;
			}
			else if(HazardType == "BURN") {
				hazardTile = Hazard.BURN;
			}
			p = new HazardPiece(position, hazardTile, originalp);
			break;
		default:
			throw new IllegalArgumentException("Unknown Piece: \"" + type + "\"");
		}
		System.out.println("written piece successfully: " + p);

		AbstractPiece[][] gs = gamestate;
		gs = utils.placePiece(p, false, gamestate);
//		System.out.println("place all pieces onto gamestate successfully");
		return gs;

	}

	@Override
	public String getFileTypeDescription() {
		return "XML files (*.xml)";
	}

	@Override
	public String getFileExtension() {
		return "xml";
	}



}
