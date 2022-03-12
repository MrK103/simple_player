package com.simple.player.interfaces.impl;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

import com.simple.player.interfaces.PlayList;
import com.simple.player.interfaces.Player;

import com.simple.player.object.MP3;
import com.simple.player.utils.FileChooser;
import com.simple.player.utils.SafeList;
//import ru.javabegin.training.fastjava.mp3.utils.FileUtils;

// плейлист на основе компонента JList
public class MP3PlayList implements PlayList {

   // public static final String PLAYLIST_FILE_EXTENSION = "pls";
   // public static final String PLAYLIST_FILE_DESCRIPTION = "Файлы плейлиста";

    private static final String EMPTY_STRING = "";

    private final FileChooser fileChooser;
    private final Player player;
    private SafeList safeList;
    private final JList jList;
    private final DefaultListModel model = new DefaultListModel();

    public MP3PlayList(JList jList, Player player) {
        this.jList = jList;
        this.player = player;
        initPlayList();
        fileChooser = new FileChooser(jList,model);
        safeList = new SafeList();

    }
    @Override
    public void up() {
        int index = jList.getSelectedIndex() - 1;
        if (index >= 0) {
            jList.setSelectedIndex(index);
        }
    }
    @Override
    public void down() {
        int index = jList.getSelectedIndex() + 1;
        if (index <= jList.getLastVisibleIndex()) {
            jList.setSelectedIndex(index);
        }
    }

    @Override
    public boolean search(String name) {

        // если в поиске ничего не ввели - выйти из метода и не производить поиск
        if (name == null || name.trim().equals(EMPTY_STRING)) {
            return false;
        }

        // все индексы объектов, найденных по поиску, будут храниться в коллекции
        ArrayList<Integer> mp3FindedIndexes = new ArrayList<Integer>();

        // проходим по коллекции и ищем соответствия имен песен со строкой поиска
        for (int i = 0; i < model.getSize(); i++) {
            MP3 mp3 = (MP3) model.getElementAt(i);
            // поиск вхождения строки в название песни без учета регистра букв
            if (mp3.getName().toUpperCase().contains(name.toUpperCase())) {
                mp3FindedIndexes.add(i);// найденный индексы добавляем в коллекцию
            }
        }

        // коллекцию индексов сохраняем в массив
        int[] selectIndexes = new int[mp3FindedIndexes.size()];

        if (selectIndexes.length == 0) {// если не найдено ни одной песни, удовлетворяющей условию поиска
            return false;
        }
        // преобразовать коллекцию в массив, т.к. метод для выделения строк в JList работает только с массивом
        for (int i = 0; i < selectIndexes.length; i++) {
            selectIndexes[i] = mp3FindedIndexes.get(i).intValue();
        }

        // выделить в плелисте найдные песни по массиву индексов, найденных ранее
        jList.setSelectedIndices(selectIndexes);

        return true;
    }
    @Override
    public void addSong(){
       fileChooser.addSong();
       safeList.saveList(model);
    }
    @Override
    public void removeSong(){
        fileChooser.removeSong();
        safeList.saveList(model);
    }

    //@Override
    public boolean openFiles(File[] files) {
        boolean status = false;
        for (File file : files) {
            MP3 mp3 = new MP3(file.getName(), file.getPath());
            // если эта песня уже есть в списке - не добавлять ее
            if (!model.contains(mp3)) {
                model.addElement(mp3);
                status = true;
            }
        }
        return status;
    }

    @Override
    public void playFile() {
        int[] index = jList.getSelectedIndices();
        if (index.length > 0) {
            MP3 mp3 = (MP3) model.getElementAt(index[0]);
            player.play(mp3.getPath());
           // player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
        }
    }


   // @Override
    public boolean openPlayList(File file) {
        try {
         /*   DefaultListModel mp3ListModel = (DefaultListModel) FileUtils.deserialize(file.getPath());
            this.model = mp3ListModel;
            playlist.setModel(mp3ListModel);*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void clear() {
        model.clear();
    }

    private void initPlayList() {
        safeList = new SafeList();
        safeList.getSongList(model, jList);
        jList.setModel(model);
        jList.setToolTipText("Список песен");

        jList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                // если нажали левую кнопку мыши 2 раза
                if (evt.getModifiers() == InputEvent.BUTTON1_MASK && evt.getClickCount() == 2) {
                    playFile();
                }
            }
        });

        jList.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    playFile();
                }
            }
        });
    }

}
