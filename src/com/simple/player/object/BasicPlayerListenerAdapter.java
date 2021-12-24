package com.simple.player.object;

import java.util.Map;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


// класс-адаптер, чтобы в анонимных классах переопределять только нужные методы
public class BasicPlayerListenerAdapter implements BasicPlayerListener{

    @Override
    public void opened(Object o, Map map) {
        
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {
        
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
      
    }

    @Override
    public void setController(BasicController bc) {
        
    }

}
