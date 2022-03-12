package com.simple.player.interfaces;

public interface Player {

    void play(String fileName);

    void stop();

    void pause();

    void setVolume(int volume, int max);

    void jump(double controlPosition);
    
}
