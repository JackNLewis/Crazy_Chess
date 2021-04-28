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
	public HazardPiece hazardPiece;
	public Hazard hazardTile;
	AbstractPiece color;
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
//		AbstractPiece[][] gamestate;
//		ArrayList<AbstractPiece> pieces = ecat.gamestateToPieceArrayList(gamestate);
//		AbstractPiece[][] finalgs = assignH.assignHazard(gamestate);
//		assignH.Hazard
		
		ArrayList<AbstractPiece> blackPieces = ecat.getBlackPieces(gamestate);
		ArrayList<AbstractPiece> whitePieces = ecat.getWhitePieces(gamestate);
		ArrayList<AbstractPiece> blankPieces = ecat.getBlankArrayList(gamestate);
		ArrayList<AbstractPiece> hazardPieces = ecat.getHazardPieces(gamestate);
		

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
			writer.writeAttribute("HazardAssigned", assignH + "");
//			writer.writeAttribute("StageHazard", gamestate.);
//			writeHazardPieces(hazardPieces, writer);
			writePieces(blackPieces, writer);
			writePieces(whitePieces, writer);
			writePieces(blankPieces, writer);
			writeHazardPieces(hazardPieces, writer);
			

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
		writer.writeStartElement(pieces.get(0).getColor());
		for (AbstractPiece piece : pieces) {
			writer.writeEmptyElement("Piece");
			writer.writeAttribute("Type", piece.getClass().getSimpleName());
			writer.writeAttribute("Colour", piece.getColor());
			writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
//			writer.writeAttribute("Position", piece.getPosition() + "");
			writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
//			writer.writeAttribute("StageHazard", piece.equals(HazardPiece));
		}
		writer.writeEndElement();
	}
	
	private void writeHazardPieces(ArrayList<AbstractPiece> pieces, XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartElement("Hazard");
		for (AbstractPiece piece : pieces) {
			writer.writeEmptyElement("Piece");
			writer.writeAttribute("Type", ((HazardPiece) piece).getOriginalPiece().getClass().getSimpleName() +"");
			writer.writeAttribute("OriginalPiece", ((HazardPiece) piece).getOriginalPiece() +"");
			writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
			writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
			writer.writeAttribute("HazardType", ((HazardPiece) piece).getHazard() + "");
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
						            loadHazardPiece(reader, gamestate);
						            System.out.println("DONE hazard pieces");
						            break;
					   default:
						            System.out.println("Next");
						            break;
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
		int xcoord = Integer.parseInt(reader.getAttributeValue("", "XCoordinate"));
		int ycoord = Integer.parseInt(reader.getAttributeValue("", "YCoordinate"));
		String powerup = reader.getAttributeValue("", "Powerup");
		AbstractPiece[][] gs = gamestate;

		switch (type) {
			case "Rook":
				p = new Rook(color, xcoord, ycoord, powerup);
				break;
			case "Knight":
				p = new Knight(color, xcoord, ycoord, powerup);
				break;
			case "Bishop":
				p = new Bishop(color, xcoord, ycoord, powerup);
				break;
			case "Queen":
				p = new Queen(color, xcoord, ycoord, powerup);
				break;
			case "King":
				p = new King(color, xcoord, ycoord, powerup);
				break;
			case "Pawn":
				p = new Pawn(color, xcoord, ycoord, powerup);
				break;
			case "BlankPiece":
			    p = new BlankPiece(color, xcoord, ycoord, powerup);	
			    break;
			default:
				throw new IllegalArgumentException("Unknown Piece: \"" + type + "\"");
		}
		System.out.println("written piece successfully: " + p);
		
		gs = utils.placePiece(p, false, gamestate);
		System.out.println("place piece onto gamestate successfully");
		return gs;

	}
	private AbstractPiece[][] loadHazardPiece(XMLStreamReader reader, AbstractPiece[][] gamestate) throws XMLStreamException {
		String type = reader.getAttributeValue("", "Type");
		String OriginalPiece = reader.getAttributeValue("", "OriginalPiece");
		String HazardType = reader.getAttributeValue("", "HazardType");
		String powerup = reader.getAttributeValue("", "Powerup");
        
		int xcoord = Integer.parseInt(reader.getAttributeValue("", "XCoordinate"));
		int ycoord = Integer.parseInt(reader.getAttributeValue("", "YCoordinate"));
		Position position = new Position(xcoord, ycoord);
		
//		Hazard hazardTile;
		if(HazardType == "FROZEN") {
			hazardTile = Hazard.FROZEN;
		}
		else if(HazardType == "BURN") {
			hazardTile = Hazard.BURN;
		}
		
		String color = null;
		if(OriginalPiece.startsWith("White")) {
			color = "White";
		}
		else if(OriginalPiece.startsWith("Black")) {
			color = "Black";
		}
		else if(OriginalPiece.startsWith("Blank")) {
			color = "Blank";
		}
		
		AbstractPiece original;
		switch (type) {
		case "Rook":
			original = new Rook(color, position, powerup);
			break;
		case "Knight":
			original = new Knight(color, position, powerup);
			break;
		case "Bishop":
			original = new Bishop(color, position, powerup);
			break;
		case "Queen":
			original = new Queen(color, position, powerup);
			break;
		case "King":
			original = new King(color, position, powerup);
			break;
		case "Pawn":
			original = new Pawn(color, position, powerup);
			break;
		case "BlankPiece":
			original = new BlankPiece(color, position, powerup);
			break;
		default:
			throw new IllegalArgumentException("Unknown Piece: \"" + type + "\"");
	    }

		AbstractPiece[][] gs = gamestate;
		hazardPiece  = new HazardPiece(position, hazardTile, original);
		gs = utils.placePiece(hazardPiece, false, gamestate);
//	    HazardAssigner assignH = new 
		AbstractPiece[][] finalGameState = assignH.assignHazard(gs);
		System.out.println("place HazardPiece onto gamestate successfully");
		return finalGameState;

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
