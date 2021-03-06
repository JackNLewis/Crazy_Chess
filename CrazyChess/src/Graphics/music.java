package Graphics;

import java.net.URL;

import javafx.scene.control.RadioMenuItem;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * The music class.
 * 
 * @author Yunlong
 *
 */

public class music
{

	// create sound for chessmove
	public URL				ulrchessmove;
	public AudioClip		chessmove;

	//create sound for Bomb
	protected URL			ulrBomb;
	protected AudioClip		Bomb;
	protected URL			ulrSetBomb;
	protected AudioClip		SetBomb;

	//create sound for Mini-Promote
	protected URL			ulrMiniPromote;
	protected AudioClip		MiniPromote;

	//create sound for Teleport
	protected URL			ulrTeleport;
	protected AudioClip		Teleport;

	//create sound for Freecard
	protected URL			ulrFreeCard;
	protected AudioClip		FreeCard;

	//create sound for DummyPiece
	protected URL			ulrDummyPiece;
	protected AudioClip		DummyPiece;

	//create sound for DummyPiece
	protected MediaPlayer	mediaPlayer;

	//create sound int
	////////////////////////////////
	protected double		bombsound;
	protected double		chessmovesound;
	protected double		SetBombsound;
	protected double		Minisound;
	protected double		Teleportsound;
	protected double		FreeCardsound;
	protected double		DummyPiecesound;
	/////////////////////////////////

	/**
	 * Constructor for the music class. Initiates every sound effect
	 */

	public music()
	{
		//starts the background music
		Media h = new Media(this.getClass().getClassLoader().getResource("resources.music/gameMusic.wav").toString());
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.setVolume(0.1);
		if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
		{
			mediaPlayer.stop();
		}
		mediaPlayer.play();

		ulrchessmove = this.getClass().getClassLoader().getResource("resources.music/chessmove.wav");
		chessmove = new AudioClip(ulrchessmove.toExternalForm());

		ulrBomb = this.getClass().getClassLoader().getResource("resources.music/Bomb.wav");
		Bomb = new AudioClip(ulrBomb.toExternalForm());
		ulrSetBomb = this.getClass().getClassLoader().getResource("resources.music/SetBomb.wav");
		SetBomb = new AudioClip(ulrSetBomb.toExternalForm());

		ulrMiniPromote = this.getClass().getClassLoader().getResource("resources.music/Mini-Promote.wav");
		MiniPromote = new AudioClip(ulrMiniPromote.toExternalForm());

		ulrTeleport = this.getClass().getClassLoader().getResource("resources.music/Teleport.wav");
		Teleport = new AudioClip(ulrTeleport.toExternalForm());

		ulrFreeCard = this.getClass().getClassLoader().getResource("resources.music/FreeCard.wav");
		FreeCard = new AudioClip(ulrFreeCard.toExternalForm());

		ulrDummyPiece = this.getClass().getClassLoader().getResource("resources.music/DummyPiece.wav");
		DummyPiece = new AudioClip(ulrDummyPiece.toExternalForm());

		/////////////////////////////////
		bombsound = 0.1;
		chessmovesound = 1.0;
		SetBombsound = 1.0;
		Minisound = 0.6;
		Teleportsound = 1.0;
		FreeCardsound = 0.6;
		DummyPiecesound = 1.0;
		/////////////////////////////////
	}

	////////////////////////////////////
	/**
	 * Turns the music off in the game
	 */
	public void turnOff()
	{
		this.mediaPlayer.setVolume(0.0);
	}

	/**
	 * Turns the music on in the game
	 */
	public void turnOn()
	{
		this.mediaPlayer.setVolume(0.1);
	}

	/**
	 * Turns off the sound for moving pieces
	 */
	public void turnOffChessmove()
	{
		this.chessmovesound = 0.0;
	}

	/**
	 * Turns off the sound for bombs exploding
	 */
	public void turnOffbomb()
	{
		this.bombsound = 0.0;
	}

	/**
	 * Turns off sound the sound setting up a bomb
	 */
	public void turnOffSetBomb()
	{
		this.SetBombsound = 0.0;
	}

	/**
	 * Turns off sound for mini promotions
	 */
	public void turnOffMini()
	{
		this.Minisound = 0.0;
	}

	/**
	 * Turns off the sounds for teleports
	 */
	public void turnOffTeleport()
	{
		this.Teleportsound = 0.0;
	}

	/**
	 * Turns off the sounds for free cards
	 */
	public void turnOffFreeCard()
	{
		this.FreeCardsound = 0.0;
	}

	/**
	 * Turns off the sounds for dummy pieces
	 */
	public void turnOffDummy()
	{
		this.DummyPiecesound = 0.0;
	}

	/**
	 * Plays the sound for moving pieces
	 */
	public void chessmove()
	{
		chessmove.play(chessmovesound);
	}

	/**
	 * Plays the sound for bombs exploding
	 */
	public void Bomb()
	{
		Bomb.play(bombsound);
	}

	/**
	 * Plays the sound for setting up bombs
	 */
	public void SetBomb()
	{
		SetBomb.play(SetBombsound);
	}

	/**
	 * Plays the sound for mini promotions
	 */
	public void MiniPromote()
	{
		MiniPromote.play(Minisound);
	}

	/**
	 * Plays the sound for teleports
	 */
	public void Teleport()
	{
		Teleport.play(Teleportsound);
	}

	/**
	 * Plays the sound for free card
	 */
	public void FreeCard()
	{
		FreeCard.play(FreeCardsound);
	}

	/**
	 * Plays the sound for dummy pieces
	 */
	public void DummyPiece()
	{
		DummyPiece.play(DummyPiecesound);
	}

	///////////////////////////////////////////
	/**
	 * Getter for mediaPlayer
	 * 
	 * @return mediaPlayer
	 */
	public MediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}
}
