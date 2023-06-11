/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksu.mailnoti.util;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Hamdi
 */
public class SoundPlayer {
    
public static synchronized void playSound(final String url) {
  new Thread(new Runnable() {
  // The wrapper thread is unnecessary, unless it blocks on the
  // Clip finishing; see comments.
    public void run() {
      try {
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream( new File (url));
        clip.open(inputStream);
        clip.start(); 
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
  }).start();
}    
    
}
