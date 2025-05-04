package letterlinkodyssey;

import javafx.scene.media.AudioClip;

import java.io.File;

public class SoundManager {
    private static final String BGM_PATH = "src/assets/bgm/";

    private static AudioClip currentBGM = null;
    private static double bgmVolume = 0.5;

    public static void playBGM(String filename, boolean loop) {
        stopBGM(); // stop previous
        File file = new File(BGM_PATH + filename);
        if (file.exists()) {
            currentBGM = new AudioClip(file.toURI().toString());
            currentBGM.setCycleCount(loop ? AudioClip.INDEFINITE : 1);
            currentBGM.setVolume(bgmVolume);
            currentBGM.play();
        } else {
            System.err.println("BGM not found: " + filename);
        }
    }
    
    public static void playSFX(String filename) {
        File file = new File("src\\assets\\sfx\\" + filename);
        if (file.exists()) {
            AudioClip clip = new AudioClip(file.toURI().toString());
            clip.play();
        } else {
            System.err.println("SFX file not found: " + filename);
        }
    }

    public static void stopBGM() {
        if (currentBGM != null) {
            currentBGM.stop();
        }
    }

    public static void setBGMVolume(double volume) {
        bgmVolume = volume;
        if (currentBGM != null) {
            currentBGM.setVolume(volume);
        }
    }
}
