package CrazyChess.logic.savegamestate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import CrazyChess.logic.ExtraChecksAndTools;
import CrazyChess.logic.MainLogic;
import CrazyChess.logic.Position;
import CrazyChess.logic.Utilities;
import CrazyChess.logic.StageHazards.Hazard;
import CrazyChess.logic.StageHazards.HazardPiece;
import CrazyChess.pieces.*;

/**
 * A class that handles saving and loading the game
 *
 */
public class SaveGame
{

	public AbstractPiece	p;
	public AbstractPiece	originalp;
	public AbstractPiece	HPiece;
	public Hazard			hazardTile;
	Utilities				utils	= new Utilities();
	ExtraChecksAndTools		ecat	= new ExtraChecksAndTools();

	/**
	 * Method that saves an array of data to an inputed file
	 * 
	 * @param data
	 *            the array of data
	 * @param file
	 *            the file to write to
	 */
	public static void saveDataToFile(byte[] data, File file)
	{
		try
		{
			Files.write(Paths.get(file.toURI()), data);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method that saves the current game into a byte array
	 * 
	 * @param board
	 *            the MainLogic object of the current game
	 * @param gamestate
	 *            current gamestate
	 * @return byte array with the information about a current game
	 */

	public byte[] save(MainLogic board, AbstractPiece[][] gamestate)
	{

		ArrayList<AbstractPiece> blackPieces = ecat.getBlackPieces(gamestate);
		ArrayList<AbstractPiece> whitePieces = ecat.getWhitePieces(gamestate);
		ArrayList<AbstractPiece> blankPieces = ecat.getBlankArrayList(gamestate);
		ArrayList<AbstractPiece> hazardPieces = ecat.getHazardPieces(gamestate);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		//create new xml output factory
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = null;
		try
		{
			writer = factory.createXMLStreamWriter(baos);
			writer.writeStartDocument();
			writer.writeStartElement("Board");
			writer.writeAttribute("CurrentTurn", board.getTurn() + "");
			writer.writeAttribute("TurnNo", String.valueOf(board.getTurnNo()));
			if (!blackPieces.isEmpty())
			{
				writePieces(blackPieces, writer);
			}
			if (!whitePieces.isEmpty())
			{
				writePieces(whitePieces, writer);
			}
			writePieces(blankPieces, writer);
			if (!hazardPieces.isEmpty())
			{
				writePieces(hazardPieces, writer);
			}

			writer.writeEndElement();
		}
		catch (XMLStreamException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (writer != null)
			{
				try
				{
					writer.flush();
					writer.close();
				}
				catch (XMLStreamException ex)
				{
					ex.printStackTrace();
				}
			}
		}
		return baos.toByteArray();
	}

	/**
	 * Method to write the information about the pieces on the board
	 * 
	 * @param pieces
	 *            ArrayList of pieces to write
	 * @param writer
	 *            the XMLStreamWriter object
	 * @throws XMLStreamException
	 */
	private void writePieces(ArrayList<AbstractPiece> pieces, XMLStreamWriter writer) throws XMLStreamException
	{
		switch (pieces.get(0).getClass().getSimpleName())
		{
		case "HazardPiece":
			writer.writeStartElement("Hazard");
			break;
		default:
			writer.writeStartElement(pieces.get(0).getColor());
		}

		for (AbstractPiece piece : pieces)
		{
			writer.writeEmptyElement("Piece");
			switch (piece.getClass().getSimpleName())
			{
			case "HazardPiece":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
				writer.writeAttribute("Colour", ((HazardPiece) piece).getOriginalPiece().getColor());
				writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
				writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
				writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
				writer.writeAttribute("HazardType", ((HazardPiece) piece).getHazard() + "");
				writer.writeAttribute("OriginalPiece", ((HazardPiece) piece).getOriginalPiece().getClass().getSimpleName() + "");
				break;
			case "Pawn":
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
				writer.writeAttribute("Colour", piece.getColor());
				writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
				writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
				writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
				writer.writeAttribute("Doublejump", ((Pawn) piece).getDoublejump() + "");

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
				writer.writeAttribute("wasMovedRook", ((Rook) piece).getWasMoved() + "");
				break;
			default:
				writer.writeAttribute("Type", piece.getClass().getSimpleName());
				writer.writeAttribute("Colour", piece.getColor());
				writer.writeAttribute("XCoordinate", String.valueOf(piece.getXpos()));
				writer.writeAttribute("YCoordinate", String.valueOf(piece.getYpos()));
				writer.writeAttribute("Powerup", piece.getPoweruptype() + "");
			}
		}
		writer.writeEndElement();
	}

	/**
	 * Loads a game from a file into a byte array
	 * 
	 * @param file
	 *            file witha saved game
	 * @return an array of bytes containing information about the loaded game
	 */

	public static byte[] loadDataFromFile(File file)
	{
		try
		{
			return Files.readAllBytes(Paths.get(file.toURI()));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Method to load information from the byte array into an actual game
	 * 
	 * @param data
	 *            the byte array of data about the game
	 * @param board
	 *            a MainLogic object to be loaded into
	 * @param gamestate
	 *            gamestate to be loaded into
	 */

	public void load(byte[] data, MainLogic board, AbstractPiece[][] gamestate)
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		try
		{
			reader = factory.createXMLStreamReader(bais);
			Stack<String> stack = new Stack<>();
			while (reader.hasNext())
			{
				switch (reader.getEventType())
				{

				case XMLStreamReader.START_ELEMENT:
					stack.push(reader.getName().getLocalPart());
					switch (String.join("/", stack))
					{
					case "Board":
						board.setCurrentTurnColor(reader.getAttributeValue("", "CurrentTurn"));
						board.setCurrentTurn(Integer.parseInt(reader.getAttributeValue("", "TurnNo")));
						break;
					case "Board/Black/Piece":
						loadPiece(reader, gamestate);
						System.out.println("DONE black pieces");
						break;
					case "Board/White/Piece":
						loadPiece(reader, gamestate);
						System.out.println("DONE white pieces");
						break;
					case "Board/Blank/Piece":
						loadPiece(reader, gamestate);
						System.out.println("DONE blanks");
						break;
					case "Board/Hazard/Piece":
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
		}
		catch (XMLStreamException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (XMLStreamException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Method to load the information about the pieces
	 * 
	 * @param reader
	 *            the XMLStreamWriter object
	 * @param gamestate
	 *            gamestate to be loaded into
	 * @throws XMLStreamException
	 * @returns a gamestate with the pieces loaded into it
	 */

	private AbstractPiece[][] loadPiece(XMLStreamReader reader, AbstractPiece[][] gamestate) throws XMLStreamException
	{
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

		//load all pieces
		switch (type)
		{
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
			break;
		case "BlankPiece":
			p = new BlankPiece(color, position, powerup);
			break;
		case "HazardPiece":
			switch (OriginalPiece)
			{
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
				break;
			case "BlankPiece":
				originalp = new BlankPiece(color, position, powerup);
				break;
			}
			switch (HazardType)
			{
			case "FROZEN":
				System.out.println("original piece: " + originalp);
				p = new HazardPiece(position, Hazard.FROZEN, originalp);
				break;
			case "BURN":
				System.out.println("original piece: " + originalp);
				p = new HazardPiece(position, Hazard.BURN, originalp);
				break;
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown Piece: \"" + type + "\"");
		}
		System.out.println("written piece successfully: " + p);

		AbstractPiece[][] gs = gamestate;
		gs = utils.placePiece(p, false, gamestate);
		return gs;

	}

	/**
	 * Returns an XML file description
	 * 
	 * @return an XML file description
	 */
	public String getFileTypeDescription()
	{
		return "XML files (*.xml)";
	}

	/**
	 * Returns an XML file extension
	 * 
	 * @return an XML file extension ("xml")
	 */
	public String getFileExtension()
	{
		return "xml";
	}

}
