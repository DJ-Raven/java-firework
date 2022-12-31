package firework;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

    private final URL url_effect;
    private final URL url_firework;

    public Sound() {
        url_effect = this.getClass().getClassLoader().getResource("firework/effect.wav");
        url_firework = this.getClass().getClassLoader().getResource("firework/firework.wav");
    }

    public void soundEffect() {
        play(url_effect);
    }

    public void soundFirework() {
        play(url_firework);
    }

    private void play(URL url) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent le) {
                    if (le.getType() == LineEvent.Type.STOP) {
                        clip.close();

                    }
                }
            });
            audioIn.close();
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println(e);
        }
    }
}
