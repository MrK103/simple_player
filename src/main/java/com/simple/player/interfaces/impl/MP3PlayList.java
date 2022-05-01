package com.simple.player.interfaces.impl;

import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;

import com.simple.player.interfaces.PlayList;
import com.simple.player.interfaces.Player;

import com.simple.player.object.MP3;
import com.simple.player.utils.FileChooser;
import com.simple.player.utils.SafeList;

// плейлист на основе компонента JList
public class MP3PlayList implements PlayList {

    private static final String EMPTY_STRING = "";

    private final FileChooser fileChooser;
    private final Player player;
    private SafeList safeList;
    private final JList jList;
    private final DefaultListModel<MP3> model = new DefaultListModel<>();

    public MP3PlayList(JList jList, Player player) {
        this.jList = jList;
        this.player = player;
        initPlayList();
        fileChooser = new FileChooser(jList, model);
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
        if (name == null || name.trim().equals(EMPTY_STRING)) {
            return false;
        }
        ArrayList<Integer> mp3FoundedIndexes = new ArrayList<>();

        // проходим по коллекции и ищем соответствия имен песен со строкой поиска
        for (int i = 0; i < model.getSize(); i++) {
            MP3 mp3 = model.getElementAt(i);
            // поиск вхождения строки в название песни без учета регистра букв
            if (mp3.getName().toUpperCase().contains(name.toUpperCase())) {
                mp3FoundedIndexes.add(i);// найденный индексы добавляем в коллекцию
            }
        }
        int[] selectIndexes = new int[mp3FoundedIndexes.size()];

        // если не найдено ни одной песни
        if (selectIndexes.length == 0) {
            return false;
        }
        // преобразовать коллекцию в массив, т.к. метод для выделения строк в JList работает только с массивом
        for (int i = 0; i < selectIndexes.length; i++) {
            selectIndexes[i] = mp3FoundedIndexes.get(i);
        }
        // выделить в плелисте найдные песни по массиву индексов, найденных ранее
        jList.setSelectedIndices(selectIndexes);
        return true;
    }

    @Override
    public void addSong() {
        fileChooser.addSong();
        safeList.saveList(model);
    }

    @Override
    public void removeSong() {
        fileChooser.removeSong();
        safeList.saveList(model);
    }

    @Override
    public void playFile() {
        int[] index = jList.getSelectedIndices();
        if (index.length > 0) {
            MP3 mp3 = model.getElementAt(index[0]);
            player.play(mp3.getPath());
        }
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
        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // если нажали левую кнопку мыши 2 раза
                if (evt.getModifiers() == InputEvent.BUTTON1_MASK && evt.getClickCount() == 2) {
                    playFile();
                }
            }
        });
        //слушатель ввода с клавиатуры
        jList.addKeyListener(new KeyAdapter() {
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
