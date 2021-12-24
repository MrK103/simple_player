package com.simple.player.utils;

import com.simple.player.object.MP3;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileChooser extends JFrame
{

    private final JFileChooser fileChooser;
    private File[] selectedFile;
    private final DefaultListModel<MP3> mp3List;
    private final JList songList;


    public DefaultListModel<MP3> getMp3List() {return mp3List;} //getter


    public FileChooser(JList songList , DefaultListModel<MP3> mp3List) {
        this.mp3List = mp3List;
        this.songList = songList;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        // Определяем фильтры типов файлов
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3", "mp3", "txt");
        fileChooser.setFileFilter(filter);
        //
        fileChooser.setMultiSelectionEnabled(true);

    }


    public void addSong(){
        fileChooser.setDialogTitle("Выберите файл");
        // Определение режима - только файл
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(FileChooser.this);
        // Если файл выбран, покажем его в сообщении
        if (result == JFileChooser.APPROVE_OPTION )
          selectedFile = fileChooser.getSelectedFiles();
        for (File f : selectedFile){
            try {
                AudioFileFormat add = AudioSystem.getAudioFileFormat(new File(f.getAbsolutePath()));
                MP3 mp3 = new MP3(add.properties().get("title").toString(),f.getAbsolutePath());
                if (checkRepeat(mp3List,mp3)){
                    mp3List.addElement(mp3);
                }else {
                    JOptionPane.showMessageDialog(fileChooser,"Эта песня уже есть в плей листе");
            }
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
        songList.setModel(mp3List);
    }

    public void removeSong(){
        int[] index = songList.getSelectedIndices();
        if (index.length > 0){
            List<MP3> listForRemove = new ArrayList<>();
            for (int j : index) {
            listForRemove.add(mp3List.getElementAt(j));
        }
        for (MP3 mp3 : listForRemove){
            mp3List.removeElement(mp3);
        }
        //выделение выше стоящей песни после удаления
        songList.setSelectedIndex(index[0]-1);
        }
    }

    public void start()
    {
        // Локализация компонентов окна JFileChooser
        UIManager.put("FileChooser.saveButtonText"      , "Сохранить"             );
        UIManager.put("FileChooser.openButtonText"      , "Открыть"               );
        UIManager.put("FileChooser.cancelButtonText"    , "Отмена"                );
        UIManager.put("FileChooser.fileNameLabelText"   , "Наименование файла"    );
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов"           );
        UIManager.put("FileChooser.lookInLabelText"     , "Директория"            );
        UIManager.put("FileChooser.saveInLabelText"     , "Сохранить в директории");
        UIManager.put("FileChooser.folderNameLabelText" , "Путь директории"       );
    }
    public boolean checkRepeat(DefaultListModel mp3List, MP3 mp3){
            boolean check = true;
            for (int i = 0; i < mp3List.getSize(); i++){
                MP3 mp31 = (MP3) mp3List.getElementAt(i);
                if (mp31.getName().equals(mp3.getName()) && mp31.getPath().equals(mp3.getPath())){
                    check = false;
                    songList.setSelectedIndex(i);
                }

            }return check;
        }

}
