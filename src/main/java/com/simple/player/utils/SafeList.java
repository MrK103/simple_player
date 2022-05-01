package com.simple.player.utils;

import com.simple.player.object.MP3;

import javax.swing.*;
import javax.xml.bind.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class SafeList {

    private static File songListXML;

    static {
        try {
            String tempURL = SafeList.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            if (tempURL.contains(".jar")) tempURL = tempURL.substring(0, tempURL.length() - 4);
            else tempURL += "simple_player";

            File tempFolder = new File(tempURL);
            if (!tempFolder.exists()) tempFolder.mkdirs();
            songListXML = new File(tempFolder, "SongList.xml");
            if (!songListXML.exists()) songListXML.createNewFile();

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedWriter bfw;
    private MP3 mp3;

    public void saveList(DefaultListModel<MP3> mp3List) {
        try {
            mp3 = new MP3();
            ArrayList<MP3> temp = new ArrayList<>();
            mp3.setMp3ArrayList(temp);
            for (int i = 0; i < mp3List.getSize(); i++) {
                mp3.getMp3ArrayList().add(mp3List.getElementAt(i));
            }
            JAXBContext context = JAXBContext.newInstance(MP3.class);
            bfw = new BufferedWriter(new FileWriter(songListXML));
            StringWriter writer = new StringWriter();
            //creating a Marshaller object that does the serialization
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // serialization
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

    public void getSongList(DefaultListModel<MP3> mp3List, JList songList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MP3.class);
            Unmarshaller un = jaxbContext.createUnmarshaller();
            mp3 = new MP3();

            try {
                mp3 = (MP3) un.unmarshal(songListXML);
            } catch (UnmarshalException ex) {
                System.err.println("Corrupt \"SongList.xml\" file, on first run it's ok");
            }

            mp3List.clear();
            if (mp3.getMp3ArrayList() != null) {
                for (MP3 song : mp3.getMp3ArrayList()) {
                    song.setName(song.getName().replace(".mp3", ""));
                    mp3List.addElement(song);
                }
            } else mp3.setMp3ArrayList(new ArrayList<>());
            songList.setModel(mp3List);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
