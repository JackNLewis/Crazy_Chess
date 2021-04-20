package Graphics;

import java.net.URL;

import javafx.scene.media.AudioClip;

/**
 * The music class.
 * 
 * @author Yunlong
 *
 */

public class music {
	
	// create sound for chessmove
	public URL ulrchessmove;
	public AudioClip chessmove;
	
	//create sound for Bomb
	protected URL ulrBomb;
	protected AudioClip Bomb;
	protected URL ulrSetBomb;
	protected AudioClip SetBomb;
		
	//create sound for Mini-Promote
	protected URL ulrMiniPromote;
	protected AudioClip MiniPromote;
	
	//create sound for Teleport
	protected URL ulrTeleport;
	protected AudioClip Teleport;
	
	//create sound for Freecard
	protected URL ulrFreeCard;
	protected AudioClip FreeCard;
	
	//create sound for DummyPiece
	protected URL ulrDummyPiece;
	protected AudioClip DummyPiece;
	
	//create sound int
	protected double bombsound;
	protected double normalsound;
	protected double miniAndfree;
	
	
	/**
	 * Constructor for the music class.
	 * Initiates every sound effect
	 */
	
	public music() {
		ulrchessmove = this.getClass().getClassLoader().getResource("resources.music/chessmove.wav");
		chessmove	  = new AudioClip(ulrchessmove.toExternalForm());
		
		ulrBomb = this.getClass().getClassLoader().getResource("resources.music/Bomb.wav");
		Bomb = new AudioClip(ulrBomb.toExternalForm());
		ulrSetBomb = this.getClass().getClassLoader().getResource("resources.music/SetBomb.wav");
		SetBomb = new AudioClip(ulrSetBomb.toExternalForm());
		
        ulrMiniPromote  = this.getClass().getClassLoader().getResource("resources.music/Mini-Promote.wav");
		MiniPromote  = new AudioClip(ulrMiniPromote.toExternalForm());
		
		ulrTeleport = this.getClass().getClassLoader().getResource("resources.music/Teleport.wav");
		Teleport = new AudioClip(ulrTeleport.toExternalForm());
		
		ulrFreeCard = this.getClass().getClassLoader().getResource("resources.music/FreeCard.wav");
		FreeCard = new AudioClip(ulrFreeCard.toExternalForm());
		
		ulrDummyPiece = this.getClass().getClassLoader().getResource("resources.music/DummyPiece.wav");
		DummyPiece = new AudioClip(ulrDummyPiece.toExternalForm());
		
		bombsound = 0.1;
		normalsound = 1.0;
		miniAndfree = 0.6;
	}
	//turn on music
	public void turnOff() {
		this.bombsound = 0.0;
		this.normalsound = 0.0;
		this.miniAndfree = 0.0;
	}
	
	//turn on music
	public void turnOn() {
		this.bombsound = 0.1;
		this.normalsound = 1.0;
		this.miniAndfree = 0.6;
	}
	
	
	//play chessmove sound
	public void chessmove() {
		chessmove.play(normalsound);
	}
	
	//play bomb sound
	public void Bomb() {
		Bomb.play(bombsound);
	}
	public void SetBomb() {
		SetBomb.play(normalsound);
	}
	
	//play Mini-Promote sound
	public void MiniPromote() {
		MiniPromote.play(miniAndfree);
	}
	
	//play Teleport sound
	public void Teleport() {
		Teleport.play(normalsound);
	}
	
	//play FreeCard sound
	public void FreeCard() {
		FreeCard.play(miniAndfree);
	}
	
	//play DummyPiece sound
	public void DummyPiece() {
		DummyPiece.play(normalsound);
	}
	
}
