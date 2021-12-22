package com.simple.player.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FilterSong extends FileFilter {

    private final String fileExtension;
    private final String fileDescription;

    public FilterSong(String fileExtension, String fileDescription){
        this.fileExtension = fileExtension;
        this.fileDescription = fileDescription;
    }

    @Override
    public boolean accept(File f) {
        return f.isDirectory() ||  f.getAbsolutePath().endsWith(fileExtension);
    }

    @Override
    public String getDescription() {
        return fileDescription + " (*." + fileExtension + ")";
    }
}
