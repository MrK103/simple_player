package com.simple.player.object;


import com.simple.player.utils.FileChooser;
import javazoom.jlgui.basicplayer.*;

import javax.swing.*;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mp3Player{
    private final BasicPlayer basicPlayer = new BasicPlayer();
    private String songName;
    private double volume;


    public void setSongValue(JSlider songValue) {
        this.songValue = songValue;
    }

    private JSlider songValue;

    public void setSongTextArea(JTextArea songTextArea) {
        this.songTextArea = songTextArea;
    }

    private JTextArea songTextArea;

    public void play(String songName){
        try {
            if (this.songName!=null && this.songName.equals(songName) && basicPlayer.getStatus() == BasicPlayer.PAUSED){
               basicPlayer.resume();
               return;
            }
            this.songName = songName;
            basicPlayer.open(new File(songName));
            basicPlayer.play();
            basicPlayer.setGain(volume);
        } catch (BasicPlayerException e) {
            Logger.getLogger(Mp3Player.class.getName()).log(Level.SEVERE, null, e);
            }
    }
    public void pause(){
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException e) {
            Logger.getLogger(Mp3Player.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void stop(){
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException e) {
            Logger.getLogger(Mp3Player.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void setVolume(int volume, int max){
        this.volume = volume;
        try {
            if (volume==0){
                basicPlayer.setGain(0);
            }else {basicPlayer.setGain(refactorVolume(volume,max));}
        } catch (BasicPlayerException e) {
            Logger.getLogger(Mp3Player.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    public double refactorVolume(int current, int max){
      volume = (double)current / (double) max;
      return volume;
    }

    public void jump(long bytes) {
        try {
            basicPlayer.seek(bytes);
            basicPlayer.setGain(volume);// устанавливаем уровень звука
        } catch (BasicPlayerException ex) {
            Logger.getLogger(Mp3Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Mp3Player(BasicPlayerListener listener) {
        basicPlayer.addBasicPlayerListener(listener);
    }
}
