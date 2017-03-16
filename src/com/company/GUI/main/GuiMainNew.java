package com.company.GUI.main;

import com.company.Connection;
import com.company.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.util.List;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GuiMainNew extends JFrame {
    private MsgManager msgManager;

    private class ActionClose extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            Connection.getInstance().Message_End();
            System.exit(0);
        }
    }
    public GuiMainNew(String login, String name, String surname, ImageIcon icon) {
        super("MyICQ");
        msgManager = new MsgManager();
        ImageIcon defaultIcon = new ImageIcon("E:\\img.jpg");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(10, 10, 200, 530);
        this.setResizable(false);
        addWindowListener(new ActionClose());
        setLayout(new FlowLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));



        PanelAccountInfo pAccountInfo = new PanelAccountInfo(login, name, surname, icon == null ? defaultIcon : icon);
        FrendsPanel panelFriends = new FrendsPanel(msgManager);
        SearchPanel searchPanel = new SearchPanel(panelFriends);


        List<User> FriendsList = Connection.getInstance().Message_getFriendsList();
        for (User u : FriendsList) {
            Item item = new Item(u.getId(), u.getName(), u.getLastName(), "online", defaultIcon);
            panelFriends.addItem(item);
        }

        JScrollPane scrollPaneFriends = new JScrollPane(panelFriends, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelFriends.setSp(scrollPaneFriends);
        scrollPaneFriends.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Друзья"));

        mainPanel.add(pAccountInfo);
        mainPanel.add(searchPanel);
        mainPanel.add(scrollPaneFriends);
        setContentPane(mainPanel);
    }
}
