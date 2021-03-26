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
	
	protected URL ulrFreeCard;
	protected AudioClip FreeCard;
	
	protected URL ulrDummyPiece;
	protected AudioClip DummyPiece;
	
	
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
	}

	
	//play chessmove sound
	public void chessmove() {
		chessmove.play();
	}
	
	//play bomb sound
	public void Bomb() {
		Bomb.play(0.1);
	}
	public void SetBomb() {
		SetBomb.play();
	}
	
	//play Mini-Promote sound
	public void MiniPromote() {
		MiniPromote.play(0.6);
	}
	
	//play Teleport sound
	public void Teleport() {
		Teleport.play();
	}
	
	//play FreeCard sound
	public void FreeCard() {
		FreeCard.play(0.6);
	}
	
	//play DummyPiece sound
	public void DummyPiece() {
		DummyPiece.play();
	}
	
}
