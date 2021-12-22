package com.simple.player.utils;

import com.simple.player.object.MP3;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileChooser extends JFrame
{
    private static final long serialVersionUID = 1L;
    private  JMenuItem      menuSave;
    private  JButton      buttonRemove ;
    private  JButton      buttonAdd;
    private  JFileChooser fileChooser;
    private File selectedFile[];
    private DefaultListModel<MP3> mp3List;
    private JList songList;
    private ListSong listSong;


    public DefaultListModel<MP3> getMp3List() {return mp3List;} //getter


    public FileChooser( JButton buttonAdd , JButton buttonRemove, JMenuItem menuSave, JList songList , DefaultListModel<MP3> mp3List) {
        this.mp3List = mp3List;
        this.menuSave =menuSave;
        this.buttonAdd = buttonAdd;
        this.buttonRemove = buttonRemove;
        this.songList = songList;
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        // Определяем фильтры типов файлов
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3", "mp3", "txt");
        fileChooser.setFileFilter(filter);
        //
        fileChooser.setMultiSelectionEnabled(true);
        listSong = new ListSong();

        // Подключение слушателей к кнопкам
        addFileChooserListeners();


    }




    private void addFileChooserListeners() {

        buttonRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] index = songList.getSelectedIndices();
                if (index.length > 0){
                    List<MP3> listForRemove = new ArrayList<>();
                    for (int i = 0; i < index.length;i++){
                        listForRemove.add(mp3List.getElementAt(index[i]));
                    }
                    for (MP3 mp3 : listForRemove){
                        mp3List.removeElement(mp3);
                    }
                    //выделение выше стоящей песни после удаления
                    songList.setSelectedIndex(index[0]-1);
                    listSong.saveList(mp3List);

                    //songList.setModel(mp3List);
                }

            }
        });

        buttonAdd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                fileChooser.setDialogTitle("Выберите файл");
                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooser.this);
                // Если файл выбран, покажем его в сообщении
                if (result == JFileChooser.APPROVE_OPTION )
                    selectedFile = fileChooser.getSelectedFiles();
                for (File f : selectedFile){
                    MP3 mp3 = new MP3(f.getName(),f.getAbsolutePath());
                    if (checkRepeat(mp3List,mp3)){
                        mp3List.addElement(mp3);
                    }else {
                        JOptionPane.showMessageDialog(fileChooser,"Эта песня уже есть в плей листе");
                    }
                }
                listSong.saveList(mp3List);
                songList.setModel(mp3List);
            }
        });
        menuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Сохранение файла");
                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(FileChooser.this);
                // Если файл выбран, то представим его в сообщении
                if (result == JFileChooser.APPROVE_OPTION ){
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    listSong = new ListSong();
                    listSong.saveList(mp3List);
                    }
                    JOptionPane.showMessageDialog(FileChooser.this, "Файл '" + fileChooser.getSelectedFile() + " ) сохранен");
            }
        });
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
                    check = false;;
                    songList.setSelectedIndex(i);
                }

            }return check;
        }

}
