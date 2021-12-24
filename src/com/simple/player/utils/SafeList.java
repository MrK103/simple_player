package com.simple.player.utils;

import com.simple.player.object.MP3;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;

public class SafeList {

    private JAXBContext context;
    private BufferedReader bfr;
    private BufferedWriter bfw;
    private MP3 mp3;
    private ArrayList temp;

    public void saveList(DefaultListModel<MP3> mp3List) {
        try {
            mp3 = new MP3();
            temp = new ArrayList();
            mp3.setMp3ArrayList(temp);
            for (int i = 0; i < mp3List.getSize(); i++) {
                mp3.getMp3ArrayList().add(mp3List.getElementAt(i));
            }
            context = JAXBContext.newInstance(MP3.class);
            File f1 = new File("res");
            File f = new File(f1,"songList.xml");
            bfw = new BufferedWriter(new FileWriter(f));
            StringWriter writer = new StringWriter();
            //создание объекта Marshaller, который выполняет сериализацию
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // сама сериализация
            marshaller.marshal(mp3, writer);
            String results = writer.toString();
            bfw.write(results);
        } catch (JAXBException | IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bfw != null) bfw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    public void getSongList(DefaultListModel<MP3> mp3List , JList songList){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MP3.class);
            Unmarshaller un = jaxbContext.createUnmarshaller();
            File f1 = new File("res");
            File f = new File(f1,"songList.xml");
            if (!f.exists()){ f.createNewFile();}
            mp3 = new MP3();
            mp3 = (MP3) un.unmarshal(f);
            mp3List.clear();
            if (mp3.getMp3ArrayList() != null){
                for (MP3 mp33 : mp3.getMp3ArrayList()){
                    mp33.setName(mp33.getName().replace(".mp3", ""));
                    mp3List.addElement(mp33);
                }
            } else mp3.setMp3ArrayList(new ArrayList<>());
            songList.setModel(mp3List);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }


}
