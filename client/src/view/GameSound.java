package view;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GameSound  {

	private Clip clip ;
	private String filePath;
	
	
	public GameSound(String filePath) {
		this.filePath=filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void play(){
	    File soundFile = new File("sources/"+filePath);
	    AudioInputStream sound;
		try {
			sound = AudioSystem.getAudioInputStream(soundFile);
			 // load the sound into memory (a Clip)
		    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		    clip = (Clip) AudioSystem.getLine(info);
			clip.open(sound);
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	   

	    // play the sound clip
	    clip.start();
	    /*
	    try {
			//Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	public void stop()
	{
		clip.stop();
	}

	
}
