package net.stemkoski.bagel;

import java.io.File;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *  Load an audio file (sound or music). 
 *  <br><br>
 *  Sound files are typically small, short audio files 
 *  that are played when discrete game events occur 
 *  (such as when an item is collected, or when two objects collide).
 *  Sound files are completely loaded into memory.
 *  <br><br>
 *  Music files are typically large, long audio files
 *  containing background music that is played continuously during the game.
 *  Music files are streamed from a file as they are played.
 */
public class Audio
{
	AudioClip sound;
	Media music;
	MediaPlayer musicPlayer;

	/**
	 *  Empty constructor for internal methods; 
	 *  use {@link #loadMusic(String)} or {@link #loadSound(String)} to create an instance.
	 */
	Audio()
    {  }
    
	/**
	 * Create an audio object from a sound file.
	 * @param fileName name of the sound file
	 * @return an Audio object
	 */
	public static Audio loadSound(String fileName)
    {
        Audio audio = new Audio();
        audio.sound = new AudioClip( new File(fileName).toURI().toString() );
        return audio;
    }
    
	/**
	 * Create an audio object from a music file.
	 * @param fileName name of the music file
	 * @return an Audio object
	 */
	public static Audio loadMusic(String fileName)
    {
        Audio audio = new Audio();
        audio.music = new Media( new File(fileName).toURI().toString() );
        audio.musicPlayer = new MediaPlayer( audio.music );
        return audio;
    }
    
	/**
	 * Set whether this audio should repeat forever or play once.
	 * @param loop whether this audio should repeat forever
	 */
	public void setLoop(boolean loop)
    {
		int repeatCount = 1;
		
		if (loop)
			repeatCount = Integer.MAX_VALUE;
		
        if (sound != null)
            sound.setCycleCount(repeatCount);
            
        if (music != null)
            musicPlayer.setCycleCount(repeatCount);
    }
    
	/**
	 * Set volume of audio playback. 
	 * @param volume value between 0.0 (silent) and 1.0 (original volume).
	 */
	public void setVolume(double volume)
    {
        if (sound != null)
            sound.setVolume(volume);
            
        if (music != null)
            musicPlayer.setVolume(volume);
    }
    
	/**
	 *  Play the loaded audio file.
	 */
	public void play()
    {
        if (sound != null)
            sound.play();
            
        if (music != null)
            musicPlayer.play();
    }
	
	/**
	 * Stop the audio from currently playing.
	 */
	public void stop()
    {
        if (sound != null)
            sound.stop();
            
        if (music != null)
            musicPlayer.stop();
    }
}
