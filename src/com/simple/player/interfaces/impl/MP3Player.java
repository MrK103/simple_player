package com.simple.player.interfaces.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.simple.player.interfaces.PlayControlListener;
import com.simple.player.interfaces.Player;
import com.simple.player.object.BasicPlayerListenerAdapter;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MP3Player implements Player {

    private JSlider songSlider;
    private JTextArea titleSong;

    //private boolean movingFromJump;
    private boolean moveAutomatic = true; //автоматическое перемещение ползунка песни
    private long duration; // длительность песни в секундах
    private int bytesLen; // размер песни в байтах
    private BasicPlayer basicPlayer = new BasicPlayer(); // библиотека для проигрывания мп3
    private String currentFilePath;// текущая песня
    private double currentVolume;
    //private long secondsAmount; // сколько секунд прошло с начала проигрывания
    private final PlayControlListener playControlListener;
    private JTextArea songDuration;


    public MP3Player(PlayControlListener playControlListener, JSlider songSlider, JTextArea titleSong, JTextArea songDuration) {
        this.playControlListener = playControlListener;
        this.songSlider = songSlider;
        this.titleSong = titleSong;
        this.songDuration = songDuration;
        //слушатель bpl
        basicPlayer.addBasicPlayerListener(new BasicPlayerListenerAdapter() {
           
            @Override
            public void progress(int bytesRead, long microseconds, byte[] pcmdata, Map properties) {
                    float progress = -1.0f;
                    if ((bytesRead > 0) && ((duration > 0))) {
                        progress = bytesRead * 1.0f / bytesLen * 1.0f;
                    }
                    int secAmount = (int) (duration * progress);
                    if (duration != 0) {
                        if (moveAutomatic == true) {
                            songSlider.setValue(secAmount);
                            songDuration.setText( String.valueOf(secAmount) + "/" + String.valueOf(duration));
                            System.out.println(secAmount);
                            //System.out.println(Math.round(secAmount * 1000 / duration));
                        }
                    }
                }


            @Override
            public void opened(Object o, Map map) {
                duration = Math.round((((Long) map.get("duration")).longValue()) / 1000000);
                songSlider.setMaximum((int) duration);
                bytesLen = Math.round((((Integer) map.get("mp3.length.bytes")).intValue()));
                String songName = map.get("title") != null ? map.get("title").toString() : (new File(o.toString()).toString());
                if (songName.length() > 12) {
                    songName = songName.substring(0, 12) + "...";
                }
                titleSong.setText(songName);
                //songDuration.setText(String.valueOf(duration));

            }

            @Override
            public void stateUpdated(BasicPlayerEvent bpe) {
                int state = bpe.getCode();

                if (state == BasicPlayerEvent.EOM) {
                    MP3Player.this.playControlListener.playFinished();
                }

            }

        });

        songSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                moveAutomatic = false;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!moveAutomatic) {
                    jump(songSlider.getValue());
                }
                moveAutomatic = true;
            }
        });

        songSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               /* if (songSlider.getValueIsAdjusting() == false) {
                    if (moveAutomatic == true) {
                        moveAutomatic = false;
                        System.out.println(songSlider.getValue());
                        double posValue = (double) songSlider.getValue() / duration;
                        System.out.println(posValue);
                        processSeek(posValue);
                    }
                } else {
                    moveAutomatic = true;
                    movingFromJump = true;
                }*/
            }
        });
    }


    @Override
    public void play(String filePath) {

        try {
            // если включают ту же самую песню, которая была на паузе
            if (currentFilePath != null && currentFilePath.equals(filePath) && basicPlayer.getStatus() == BasicPlayer.PAUSED) {
                basicPlayer.resume();
                return;
            }

            File mp3File = new File(filePath);

            currentFilePath = filePath;
            basicPlayer.open(mp3File);
            basicPlayer.play();
            System.out.println(basicPlayer.getGainValue());
            basicPlayer.setGain(currentVolume);

            System.out.println(basicPlayer.getGainValue());


        } catch (BasicPlayerException ex) {
            Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
        }

    }



    @Override
    public void stop() {
        try {
            basicPlayer.stop();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void pause() {
        try {
            basicPlayer.pause();
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // регулирует звук при проигрывании песни
    @Override
    public void setVolume(int volume, int max){
        this.currentVolume = volume;
        try {
            if (volume==0){
                basicPlayer.setGain(0);
            }else {basicPlayer.setGain(refactorVolume(volume,max));}
        } catch (BasicPlayerException e) {
            Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, e);

        }
    }
    public double refactorVolume(int current, int max){
        currentVolume = (double)current / (double) max;
        return currentVolume;
    }
    @Override
    public void jump(double controlPosition) {
        try {
            controlPosition = controlPosition/duration;
            controlPosition = bytesLen * controlPosition;
            basicPlayer.seek((long) controlPosition);
            basicPlayer.setGain(currentVolume);
        } catch (BasicPlayerException ex) {
            Logger.getLogger(MP3Player.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
