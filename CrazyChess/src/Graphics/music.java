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
	
	//create sound for Mini-Promote
	protected URL ulrMiniPromote;
	protected AudioClip MiniPromote;
	
	//create sound for Teleport
	protected URL ulrTeleport;
	protected AudioClip Teleport;
	
	
	/**
	 * Constructor for the music class.
	 * Initiates every sound effect
	 */
	
	public music() {
		ulrchessmove = this.getClass().getClassLoader().getResource("resources.music/chessmove.wav");
		chessmove	  = new AudioClip(ulrchessmove.toExternalForm());
		
		ulrBomb = this.getClass().getClassLoader().getResource("resources.music/Bomb.wav");
		Bomb = new AudioClip(ulrBomb.toExternalForm());
		
        ulrMiniPromote  = this.getClass().getClassLoader().getResource("resources.music/Mini-Promote.wav");
		MiniPromote  = new AudioClip(ulrMiniPromote.toExternalForm());
		
		ulrTeleport = this.getClass().getClassLoader().getResource("resources.music/Teleport.wav");
		Teleport = new AudioClip(ulrTeleport.toExternalForm());
	}

	
	//play chessmove sound
	public void chessmove() {
		chessmove.play();
	}
	
	//play bomb sound
	public void Bomb() {
		Bomb.play(0.1);
	}
	
	//play Mini-Promote sound
	public void MiniPromote() {
		MiniPromote.play();
	}
	
	//play Teleport sound
	public void Teleport() {
		Teleport.play();
	}
	
}
