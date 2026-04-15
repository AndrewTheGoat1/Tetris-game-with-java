package com.soft.helper;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private static Map<String, Clip> clips = new HashMap<>();

    public static void load(String name, String path) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    SoundPlayer.class.getResource("/" + path)
            );

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clips.put(name, clip);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void play(String name) {
        try {
            Clip clip = clips.get(name);
            if (clip == null) return;

            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}