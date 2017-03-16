package com.company.GUI.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vlad on 01.03.2017.
 */
public class SearchPanel extends JPanel {
    private FrendsPanel fp;
    private JPanel panelSearchOptions;
    private JRadioButton rbFriend;
    private JRadioButton rbAll;
    private JTextField tfInputForSearch;
    public SearchPanel(FrendsPanel fp) {
        this.fp = fp;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setMaximumSize(new Dimension(200, 100));
        JPanel panelSearch = new JPanel();
        panelSearch.setLayout(new FlowLayout());
        panelSearch.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        tfInputForSearch = new JTextField();
        tfInputForSearch.setPreferredSize(new Dimension(110, 25));
        JButton buttonSearch = new JButton("Искать");
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton)e.getSource();
                if (b.getText().equals("Искать")) {
                    setVisibleOptionPanel(true);
                    b.setText("Скрыть");
                    if (rbFriend.isSelected()) {
                        fp.search(tfInputForSearch.getText());
                    } else {
                        fp.searchAll(tfInputForSearch.getText());
                    }
                }
                else {
                    setVisibleOptionPanel(false);
                    b.setText("Искать");
                    tfInputForSearch.setText("");
                    //...............
                    fp.showAll();
                }

            }
        });
        buttonSearch.setFont(new Font("Arial", Font.PLAIN, 10));
        buttonSearch.setPreferredSize(new Dimension(70, 25));
        panelSearch.add(tfInputForSearch);
        panelSearch.add(buttonSearch);

        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JRadioButton rb = (JRadioButton) e.getSource();
                if (rb.getText().equals("Друзья")) {
                    fp.search(tfInputForSearch.getText());
                } else {
                    fp.searchAll(tfInputForSearch.getText());
                }
            }
        };
        panelSearchOptions = new JPanel(new FlowLayout());
        rbFriend = new JRadioButton("Друзья", true);
        rbFriend.setHorizontalAlignment(SwingConstants.LEFT);
        rbFriend.addActionListener(al);
        rbAll    = new JRadioButton("Все", false);
        rbAll.setHorizontalAlignment(SwingConstants.LEFT);
        rbAll.addActionListener(al);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbFriend);
        bg.add(rbAll);
        panelSearchOptions.add(rbFriend);
        panelSearchOptions.add(rbAll);

        panelSearchOptions.setVisible(false);


        this.add(panelSearch);
        this.add(panelSearchOptions);
    }

    public void setVisibleOptionPanel(boolean v) {

        panelSearchOptions.setVisible(v);

    }
}
