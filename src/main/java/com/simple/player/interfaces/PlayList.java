package com.simple.player.interfaces;

import java.io.File;

public interface PlayList {

    void removeSong();
    
    void clear();

    boolean search(String name);

    void addSong();
    
    void playFile();

    void down();

    void up();
}
