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
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.pieces.*;
	
//	private List<AbstractPiece> loadData(String configFile) {
//		List<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
//		try {
//			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//			
//			InputStream in = new FileInputStream(configFile);
//			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
//			
//			AbstractPiece piece = null;
//			
//			while(eventReader.hasNext()) {
//				XMLEvent event = eventReader.nextEvent();
//				
//				if()
//			}
//		}
//		return pieces;
//	}

//}

public class SaveGame implements ChessIO {
	
	AbstractPiece color;
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
		
		ArrayList<AbstractPiece> blackPieces = ecat.getBlackPieces(gamestate);
		ArrayList<AbstractPiece> whitePieces = ecat.getWhitePieces(gamestate);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		//create new xml output factory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = null;
		try {
			writer = factory.createXMLStreamWriter(baos);
			writer.writeStartDocument();
			writer.writeStartElement("Board");
			writer.writeAttribute("CurrentTurn", board.getTurn() + "");
//			writer.writeAttribute("StageHazard", gamestate.);
			writePieces(blackPieces, writer);
			writePieces(whitePieces, writer);

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
//			if(piece instanceof HazardPiece) {
//				
//			}
			writer.writeAttribute("Type", piece.getClass().getSimpleName());
			writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
			writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
//			writer.writeAttribute("Position", piece.getPosition() + "");
			writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
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
	
	public static byte[] loadDataFromResource(String resource) {
		URI uri = null;
		try {
			URL url = ChessIO.class.getClassLoader().getResource(resource);
			if (url != null) {
				uri = url.toURI();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (uri == null) {
			return null;
		}
		try {
			return Files.readAllBytes(Paths.get(uri));
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	
	public void load(byte[] data, MainLogic board, AbstractPiece[][] gamestate) {
//		AbstractPiece p;
		String color;
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
								board.setCurrentTurn(reader.getAttributeValue("", "CurrentTurn"));
								break;
							case "Board/Black/Piece":
//								color = "Black";
								loadPiece(reader, board, gamestate);
								break;
							case "Board/White/Piece":
//								color = "White";
								loadPiece(reader, board, gamestate);
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

	private void loadPiece(XMLStreamReader reader, MainLogic board, AbstractPiece[][] gamestate) throws XMLStreamException {
//		int firstTurn = Integer.parseInt(reader.getAttributeValue("", "FirstTurn"));
//		AbstractPiece[][] gamestate = null;
		AbstractPiece piece = null;
		String color = reader.getElementText();
		String type = reader.getAttributeValue("", "Type");
		int xcoord = Integer.parseInt(reader.getAttributeValue("", "XCoordinate"));
		int ycoord = Integer.parseInt(reader.getAttributeValue("", "YCoordinate"));
		String powerup = reader.getAttributeValue("", "Powerup");
		if(type == "Pawn") {
			piece = new Pawn(color, xcoord, ycoord, powerup);
		}
		else if(type == "Rook") {
			piece = new Rook(color, xcoord, ycoord, powerup);
		}
		else if(type == "Bishop") {
			piece = new Bishop(color, xcoord, ycoord, powerup);
		}
		else if(type == "Knight") {
			piece = new Knight(color, xcoord, ycoord, powerup);
		}
		else if(type == "King") {
			piece = new King(color, xcoord, ycoord, powerup);
		}
		else if(type == "Queen") {
			piece = new Queen(color, xcoord, ycoord, powerup);
		}
		else if(type == "Powerup") {
			piece = new Powerup(xcoord, ycoord, powerup);
		}
		AbstractPiece[][] gs;
		
//		AbstractPiece piece = new AbstractPiece(color, position, powerup);
//		AbstractPiece piece = Utilities.createPiece(color, type, position);
		gs = utils.placePiece(piece, false, gamestate);
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
