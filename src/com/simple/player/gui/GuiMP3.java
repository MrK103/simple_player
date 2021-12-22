package com.simple.player.gui;

import com.formdev.flatlaf.FlatLightLaf;
import com.simple.player.object.MP3;
import com.simple.player.object.Mp3Player;
import com.simple.player.utils.FileChooser;
import com.simple.player.utils.ListSong;
import com.simple.player.utils.SkinUtil;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class GuiMP3 implements BasicPlayerListener {
    private JPanel contentPane;
    private JButton searhButton;
    private JButton downButton;
    private JTextField searhField;
    private JButton buttonAdd;
    private JButton butonRemove;
    private JButton upButton;
    private JRadioButton muteButton;
    private JSlider sliderVolume;
    private JButton leftSkipButton;
    private JButton rightSkipButton;
    private JButton pauseButton;
    private JButton playButton;
    private JButton stopButton;
    private JList list1;
    private JToolBar toolBar1;
    private JPanel panel1;
    private JMenuBar menuBar1;
    private JButton buttonOK;
    private DefaultListModel<MP3> mp3List;
    private JMenu jm;
    private JMenuItem jmiMenu;
    private JMenuItem jmiSkin1;
    private JMenuItem jmiSkin2;
    private JMenu jmService;
    private Mp3Player mp3Player;
    private JFrame jFrame;
    private JPanel panel2;
    private JTextArea titleSong;
    private JPanel statusSong;
    private JSlider songSlider;
    private FileChooser fc;
    private SkinUtil sk;
    private int duration;
    private int bytesLen;
    private boolean movingFromJump = false;
    private boolean moveAutomatic = false;

    public GuiMP3() {
        $$$setupUI$$$();
        mp3Player = new Mp3Player(this);
        mp3Player.setSongValue(songSlider);
        mp3Player.setSongTextArea(titleSong);
        mp3List = new DefaultListModel();
        ListSong listSong = new ListSong();
        listSong.getSongList(mp3List, list1);
        fc = new FileChooser(buttonAdd, butonRemove, jmiMenu, list1, mp3List);
        sk = new SkinUtil();


        //Слушатель2
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() == sliderVolume) {
                    mp3Player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
                    if (sliderVolume.getValue() == 0) {
                        muteButton.setSelected(true);
                    } else muteButton.setSelected(false);
                }
            }
        };
        //Слушатель1
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == upButton) {
                    up();
                } else if (e.getSource() == downButton) {
                    down();
                } else if (e.getSource() == playButton) {
                    play();
                } else if (e.getSource() == pauseButton) {
                    mp3Player.pause();
                } else if (e.getSource() == stopButton) {
                    mp3Player.stop();
                } else if (e.getSource() == leftSkipButton) {
                    up();
                    play();
                } else if (e.getSource() == rightSkipButton) {
                    down();
                    play();
                } else if (e.getSource() == muteButton) {
                    if (muteButton.isSelected()) {
                        mp3Player.setVolume(0, 0);
                    } else mp3Player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());

                } else if (e.getSource() == jmiSkin1) {
                    SkinUtil.changeSkin(jFrame, new FlatLightLaf());
                } else if (e.getSource() == jmiSkin2) {
                    SkinUtil.changeSkin(jFrame, UIManager.getSystemLookAndFeelClassName());
                } else if (e.getSource() == searhButton) {
                    String foundSong = searhField.getText().toLowerCase(Locale.ROOT);
                    ArrayList<Integer> arr = new ArrayList<Integer>();
                    if (!foundSong.isEmpty() && mp3List.getSize() != 0) {
                        for (int i = 0; i < mp3List.getSize(); i++) {
                            if (mp3List.getElementAt(i).getName().toLowerCase(Locale.ROOT).contains(foundSong)) {
                                arr.add(i);
                            }
                        }
                    }
                    if (arr.size() > 0) {
                        int[] arrInt = new int[arr.size()];
                        for (int i = 0; i < arrInt.length; i++) {
                            arrInt[i] = arr.get(i);
                        }
                        list1.setSelectedIndices(arrInt);
                    } else {
                        searhField.requestFocus();
                        searhField.selectAll();
                    }
                }
            }
        };


        searhButton.addActionListener(listener);
        jmiSkin2.addActionListener(listener);
        jmiSkin1.addActionListener(listener);
        downButton.addActionListener(listener);
        upButton.addActionListener(listener);
        playButton.addActionListener(listener);
        stopButton.addActionListener(listener);
        pauseButton.addActionListener(listener);
        leftSkipButton.addActionListener(listener);
        rightSkipButton.addActionListener(listener);
        sliderVolume.addChangeListener(changeListener);
        muteButton.addActionListener(listener);
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {

            }
        });


        songSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (songSlider.getValueIsAdjusting() == false) {
                    if (moveAutomatic == true) {
                        moveAutomatic = false;
                        double posValue = (double) songSlider.getValue() / duration;
                        processSeek(posValue);
                    }
                } else {
                    moveAutomatic = true;
                    movingFromJump = true;
                }
            }
        });

    }

    public void processSeek(double bytes) {
        long skipBytes = (long) (bytesLen * bytes);
        mp3Player.jump(skipBytes);
    }

    public void up() {
        int index = list1.getSelectedIndex() - 1;
        if (index >= 0) {
            list1.setSelectedIndex(index);
        }
    }

    public void down() {
        int index = list1.getSelectedIndex() + 1;
        if (index <= list1.getLastVisibleIndex()) {
            list1.setSelectedIndex(index);
        }
    }

    public void play() {
        int[] index = list1.getSelectedIndices();
        if (index.length > 0) {
            MP3 mp3 = mp3List.getElementAt(index[0]);
            mp3Player.play(mp3.getPath());
            mp3Player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
        }
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        panel2 = new JPanel();
        //contentPane = new JPanel();
        jFrame = new JFrame();
        jFrame.setLayout(new FlowLayout());
        //jFrame.add(panel2);
        jFrame.add($$$getRootComponent$$$());
        jFrame.setSize(400, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setTitle("Simple player");

        menuBar1 = new JMenuBar();
        jm = new JMenu("menu");
        jmiMenu = new JMenuItem("save");
        jmService = new JMenu("service");
        jmiSkin1 = new JMenuItem("skin1");
        jmiSkin2 = new JMenuItem("skin2");
        JMenu skins = new JMenu("skins");
        skins.add(jmiSkin1);
        skins.add(jmiSkin2);
        jmService.add(skins);
        jm.add(jmiMenu);
        menuBar1.add(jm);
        menuBar1.add(jmService);
        titleSong = new JTextArea();
        titleSong.setTabSize(10);
        titleSong.setEnabled(false);

    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(350, 450), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searhButton = new JButton();
        searhButton.setIcon(new ImageIcon(getClass().getResource("/images/search_16.png")));
        searhButton.setText("");
        panel3.add(searhButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downButton = new JButton();
        downButton.setIcon(new ImageIcon(getClass().getResource("/images/arrow-down-icon.png")));
        downButton.setText("");
        panel3.add(downButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searhField = new JTextField();
        searhField.setText("Поиск");
        searhField.setToolTipText("Поиск песен по названию");
        panel3.add(searhField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonAdd = new JButton();
        buttonAdd.setHideActionText(true);
        buttonAdd.setIcon(new ImageIcon(getClass().getResource("/images/plus_16.png")));
        buttonAdd.setText("");
        panel3.add(buttonAdd, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        butonRemove = new JButton();
        butonRemove.setIcon(new ImageIcon(getClass().getResource("/images/remove_icon.png")));
        butonRemove.setText("");
        panel3.add(butonRemove, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upButton = new JButton();
        upButton.setIcon(new ImageIcon(getClass().getResource("/images/arrow-up-icon.png")));
        upButton.setText("");
        panel3.add(upButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel4.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftSkipButton = new JButton();
        leftSkipButton.setIcon(new ImageIcon(getClass().getResource("/images/prev-icon.png")));
        leftSkipButton.setText("");
        panel5.add(leftSkipButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightSkipButton = new JButton();
        rightSkipButton.setIcon(new ImageIcon(getClass().getResource("/images/next-icon.png")));
        rightSkipButton.setText("");
        panel5.add(rightSkipButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon(getClass().getResource("/images/Pause-icon.png")));
        pauseButton.setText("");
        panel5.add(pauseButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playButton = new JButton();
        playButton.setIcon(new ImageIcon(getClass().getResource("/images/Play.png")));
        playButton.setText("");
        panel5.add(playButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setIcon(new ImageIcon(getClass().getResource("/images/stop-red-icon.png")));
        stopButton.setText("");
        panel5.add(stopButton, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        muteButton = new JRadioButton();
        muteButton.setSelected(false);
        muteButton.setText("mute");
        panel4.add(muteButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        sliderVolume = new JSlider();
        sliderVolume.setMaximum(200);
        sliderVolume.setMinorTickSpacing(5);
        sliderVolume.setValue(45);
        panel4.add(sliderVolume, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        list1 = new JList();
        list1.setDropMode(DropMode.USE_SELECTION);
        list1.setForeground(new Color(-4473925));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        list1.setModel(defaultListModel1);
        panel1.add(list1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        panel1.add(menuBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        statusSong = new JPanel();
        statusSong.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 5, 5, 5), -1, -1));
        panel1.add(statusSong, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titleSong.setEditable(false);
        titleSong.setEnabled(true);
        titleSong.setForeground(new Color(-3398028));
        titleSong.setText("Имя песни");
        statusSong.add(titleSong, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        songSlider = new JSlider();
        songSlider.setInverted(false);
        songSlider.putClientProperty("Slider.paintThumbArrowShape", Boolean.FALSE);
        statusSong.add(songSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel2;
    }

    @Override
    public void opened(Object o, Map map) {
        duration = Math.round((((Long) map.get("duration")).longValue()) / 1000000);
        songSlider.setMaximum((int) duration);
        bytesLen = Math.round((((Integer) map.get("mp3.length.bytes")).intValue()));
        String songName = map.get("title") != null ? map.get("title").toString() : (new File(o.toString()).toString());
        if (songName.length() > 23) {
            songName = songName.substring(0, 23) + "...";
        }
        titleSong.setText(songName);
    }

    @Override
    public void progress(int bytesRead, long microsec, byte[] bytes, Map map) {

        float progress = -1.0f;
        if ((bytesRead > 0) && ((duration > 0))) {
            progress = bytesRead * 1.0f / bytesLen * 1.0f;
        }
        int secAmount = (int) (duration * progress);
        if (duration != 0) {
            if (movingFromJump == false) {
                songSlider.setValue(secAmount);
                //System.out.println(Math.round(secAmount * 1000 / duration));
            }
        }
    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {
        int state = basicPlayerEvent.getCode();
        if (state == BasicPlayerEvent.PLAYING) {
            movingFromJump = false;
        } else if (state == BasicPlayerEvent.SEEKED) {
            movingFromJump = true;
        } else if (state == BasicPlayerEvent.EOM) {
            down();
            play();
        }

    }

    @Override
    public void setController(BasicController basicController) {

    }
}


