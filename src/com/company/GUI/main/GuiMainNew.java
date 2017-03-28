package com.company.GUI.main;

import com.company.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class GuiMainNew extends JFrame {
    private MsgManager  msgManager;
    private FrendsPanel panelFriends;
    private JButton     newFriends;
    private int         countApplicatonsToFriends;
    private class ActionClose extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            Connection.getInstance().Message_End();
            System.exit(0);
        }
    }

    public GuiMainNew(String login, String name, String surname, ImageIcon icon) {
        super("MyICQ");
        msgManager = new MsgManager();
        countApplicatonsToFriends = 0;
        ImageIcon defaultIcon = new ImageIcon("E:\\img.jpg");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(10, 10, 230, 530);
        this.setResizable(false);
        addWindowListener(new ActionClose());
        setLayout(new FlowLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        PanelAccountInfo pAccountInfo = new PanelAccountInfo(login, name, surname, icon == null ? defaultIcon : icon);

        panelFriends = new FrendsPanel(msgManager);
        SearchPanel searchPanel = new SearchPanel(panelFriends);



        List<User> FriendsList = Connection.getInstance().Message_getFriendsList();
        for (User u : FriendsList) {

            Item item = new Item(u.getId(),
                    u.getName(),
                    u.getLastName(),
                    Connection.getInstance().Message_isOnline(u.getId()) == 1 ,
                    defaultIcon);

            panelFriends.addItem(item);
        }

        List<Message> msgList = Connection.getInstance().Message_getNewMessages();

        FileMessages fileMessages = new FileMessages(Connection.getInstance().getMyId());
        if (msgList != null) {
            for (Message m : msgList) {
                panelFriends.setCountOfNewMessages(m.getId(), 1, true);
                fileMessages.addMessage(m);
            }
        }

        JScrollPane scrollPaneFriends = new JScrollPane(panelFriends, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelFriends.setSp(scrollPaneFriends);
        scrollPaneFriends.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Друзья"));


        List<Message> messageList = Connection.getInstance().Message_getNewAppToFriends();
        FileApplicationsToFriend fatf = new FileApplicationsToFriend(Connection.getInstance().getMyId());
        if (messageList != null) {
            for (Message m: messageList) {
                fatf.add(m);
            }
        }

        JPanel j = new JPanel();
        newFriends = new JButton();
        newFriends.setVisible(false);
        newFriends.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        newFriends.setHorizontalAlignment(SwingConstants.LEFT);
        j.add(newFriends);
        setCountOfApp(fatf.getCountOfApp(), false);


        mainPanel.add(pAccountInfo);
        mainPanel.add(searchPanel);
        mainPanel.add(scrollPaneFriends);
        mainPanel.add(j);


        setContentPane(mainPanel);
        Connection.getInstance().setGuiMainNew(this);
    }

    public void setCountOfApp(int n, boolean isAdd) {
        if (isAdd) countApplicatonsToFriends += n;
        else countApplicatonsToFriends = n;

        if (countApplicatonsToFriends > 0) {
            newFriends.setText("У вас " + n + " заявки в друзья");
            if (!newFriends.isVisible()) newFriends.setVisible(true);
        }
    }
    public MsgManager getMsgManager() { return msgManager; }
    public FrendsPanel getPanelFriends() {
        return panelFriends;
    }
    public void addMessage(Message m, boolean t) {
        msgManager.addMsg(m, t);
        if (!msgManager.isSelectedTab(m.getId())) {
            panelFriends.setCountOfNewMessages(m.getId(), 1, true);
        }
    }


}
