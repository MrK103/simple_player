package com.simple.player.utils;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SkinUtil {

    public SkinUtil(){

    }

    public static void changeSkin(Component component, LookAndFeel laf){
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            Logger.getLogger(SkinUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        SwingUtilities.updateComponentTreeUI(component);
        UIManager.put( "Button.arc", 25 );

    }

    public static void changeSkin(Component component, String laf){

        try {
            UIManager.setLookAndFeel(laf);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            Logger.getLogger(SkinUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Black", "@mrk", "MrK107");
        SwingUtilities.updateComponentTreeUI(component);
    }
}
