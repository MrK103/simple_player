package com.simple.player;

import com.formdev.flatlaf.FlatLightLaf;
import com.simple.player.gui.GuiMP3;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GuiMP3 guiMP3 = new GuiMP3();
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }
}
