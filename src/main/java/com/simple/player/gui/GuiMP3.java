package com.simple.player.gui;

import com.simple.player.interfaces.PlayControlListener;
import com.simple.player.interfaces.PlayList;
import com.simple.player.interfaces.Player;
import com.simple.player.interfaces.impl.MP3PlayList;
import com.simple.player.interfaces.impl.MP3Player;
import com.simple.player.utils.SkinUtil;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;


public class GuiMP3 implements PlayControlListener {

    private final Player player;
    private final PlayList playList;

    private JButton searchButton;
    private JButton downButton;
    private JTextField searchField;
    private JButton buttonAdd;
    private JButton buttonRemove;
    private JButton upButton;
    private JRadioButton muteButton;
    private JSlider sliderVolume;
    private JButton leftSkipButton;
    private JButton rightSkipButton;
    private JButton pauseButton;
    private JButton playButton;
    private JButton stopButton;
    private JList jList;
    private JPanel panel1;
    private JMenuBar menuBar1;
    private JMenu jm;
    private JMenuItem jmiMenu;
    private JMenuItem jmiSkin1;
    private JMenuItem jmiSkin2;
    private JMenu jmService;
    private JFrame jFrame;
    private JPanel panel2;
    private JTextArea titleSong;
    private JPanel statusSong;
    private JSlider songSlider;
    private JTextArea songDuration;

    private void searchSong() {
        String name = searchField.getText().trim();
        if (!playList.search(name)) {
            JOptionPane.showMessageDialog(null, "Поиск по строке '" + name + "' не дал результатов");
            searchField.requestFocus();
            searchField.selectAll();
        }
    }

    public GuiMP3() {
        $$$setupUI$$$();
        player = new MP3Player(this, songSlider, titleSong, songDuration);
        playList = new MP3PlayList(jList, player);
        player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());

        //Change listener for volume slider
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() == sliderVolume) {
                    player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());
                    muteButton.setSelected(sliderVolume.getValue() == 0);
                }
            }
        };
        //Action listener for buttons
        ActionListener listener = e -> {
            if (e.getSource() == buttonAdd) {
                playList.addSong();
            } else if (e.getSource() == buttonRemove) {
                playList.removeSong();
            } else if (e.getSource() == upButton) {
                playList.up();
            } else if (e.getSource() == downButton) {
                playList.down();
            } else if (e.getSource() == playButton) {
                playList.playFile();
            } else if (e.getSource() == pauseButton) {
                player.pause();
            } else if (e.getSource() == stopButton) {
                player.stop();
            } else if (e.getSource() == leftSkipButton) {
                playList.up();
                playList.playFile();
            } else if (e.getSource() == rightSkipButton) {
                playList.down();
                playList.playFile();
            } else if (e.getSource() == muteButton) {
                if (muteButton.isSelected()) {
                    player.setVolume(0, 0);
                } else player.setVolume(sliderVolume.getValue(), sliderVolume.getMaximum());

            } else if (e.getSource() == jmiSkin2) {
                SkinUtil.changeSkin(jFrame, UIManager.getSystemLookAndFeelClassName());
            } else if (e.getSource() == jmiSkin1) {
                SkinUtil.changeSkin(jFrame, "com.jtattoo.plaf.acryl.AcrylLookAndFeel");
            } else if (e.getSource() == searchButton) {
                searchSong();
            }
        };

        buttonAdd.addActionListener(listener);
        buttonRemove.addActionListener(listener);
        searchButton.addActionListener(listener);
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
        jList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent arg0) {

            }
        });

    }


    private void createUIComponents() {

        // TODO: place custom component creation code here
        panel2 = new JPanel();
        jFrame = new JFrame();
        jFrame.setLayout(new FlowLayout());
        jFrame.add($$$getRootComponent$$$());
        jFrame.setSize(395, 500);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setTitle("Simple player");

        InputStream stream = getClass().getResourceAsStream("/images/icon.jpg");
        try {
            jFrame.setIconImage(new ImageIcon(ImageIO.read(stream)).getImage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        menuBar1 = new JMenuBar();
        jm = new JMenu("menu");
        jmiMenu = new JMenuItem("save");
        jmService = new JMenu("service");
        jmiSkin1 = new JMenuItem("JTatoo");
        jmiSkin2 = new JMenuItem("Windows");
        JMenu skins = new JMenu("skins");
        skins.add(jmiSkin2);
        skins.add(jmiSkin1);
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
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(350, 450), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setIcon(new ImageIcon(getClass().getResource("/images/search_16.png")));
        searchButton.setText("");
        panel3.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downButton = new JButton();
        downButton.setIcon(new ImageIcon(getClass().getResource("/images/arrow-down-icon.png")));
        downButton.setText("");
        panel3.add(downButton, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchField = new JTextField();
        searchField.setText("Поиск");
        searchField.setToolTipText("Поиск песен по названию");
        panel3.add(searchField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonAdd = new JButton();
        buttonAdd.setHideActionText(true);
        buttonAdd.setIcon(new ImageIcon(getClass().getResource("/images/plus_16.png")));
        buttonAdd.setText("");
        panel3.add(buttonAdd, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRemove = new JButton();
        buttonRemove.setIcon(new ImageIcon(getClass().getResource("/images/remove_icon.png")));
        buttonRemove.setText("");
        panel3.add(buttonRemove, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        upButton = new JButton();
        upButton.setIcon(new ImageIcon(getClass().getResource("/images/arrow-up-icon.png")));
        upButton.setText("");
        panel3.add(upButton, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(5, 5, 5, 5), -1, -1));
        panel1.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        sliderVolume.setMaximum(100);
        sliderVolume.setMinimum(0);
        sliderVolume.setMinorTickSpacing(1);
        sliderVolume.setValue(50);
        panel4.add(sliderVolume, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(menuBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        statusSong = new JPanel();
        statusSong.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 5, 5, 5), -1, -1));
        panel1.add(statusSong, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        titleSong.setBackground(new Color(-1118482));
        titleSong.setEditable(false);
        titleSong.setEnabled(true);
        titleSong.setForeground(new Color(-3398028));
        titleSong.setText("Имя песни");
        statusSong.add(titleSong, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(90, -1), new Dimension(90, -1), 0, false));
        songSlider = new JSlider();
        songSlider.setInverted(false);
        songSlider.putClientProperty("Slider.paintThumbArrowShape", Boolean.FALSE);
        statusSong.add(songSlider, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        songDuration = new JTextArea();
        songDuration.setBackground(new Color(-1118482));
        songDuration.setEditable(false);
        songDuration.setEnabled(false);
        songDuration.setForeground(new Color(-3398028));
        songDuration.setLineWrap(false);
        songDuration.setText("Время");
        statusSong.add(songDuration, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(12, 10), null, 0, false));
        jList = new JList();
        jList.setDropMode(DropMode.USE_SELECTION);
        jList.setForeground(new Color(-4473925));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        jList.setModel(defaultListModel1);
        panel1.add(jList, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel2;
    }

    @Override
    public void playFinished() {
        playList.down();
        playList.playFile();
    }
}


