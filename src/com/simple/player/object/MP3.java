package com.simple.player.object;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;

@XmlType(name = "MP3")
@XmlRootElement
public class MP3 {

    public MP3(){}

    @XmlElement()
    private ArrayList<MP3> mp3ArrayList;
    @XmlElement()
    private String name;
    @XmlElement()
    private String path;

    @XmlTransient
    public ArrayList<MP3> getMp3ArrayList() {return mp3ArrayList;}
    public void setMp3ArrayList(ArrayList<MP3> mp3ArrayList) {this.mp3ArrayList = mp3ArrayList;}
    @XmlTransient
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    @XmlTransient
    public String getPath() {return path;}
    public void setPath(String path) {this.path = path;}

    public MP3(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String toString(){
        return name;
    }


}
