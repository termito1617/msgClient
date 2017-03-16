package com.company.GUI.main;

import com.company.Connection;
import com.company.User;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by vlad on 01.03.2017.
 */
 class FrendsPanel extends JPanel {
    private Item selected;
    private ActionListener ae;
    private ActionListener aeForContext;
    private JPanel fPanel;
    private JPanel sPanel;
    private JScrollPane sp;
    private JPopupMenu jpuFriends;
    private JPopupMenu jpuUsers;
    private MsgManager msgManager;

    public FrendsPanel(MsgManager m) {
        super();
        msgManager = m;
        sPanel = new JPanel();
        fPanel = new JPanel();
        ae = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fPanel.isVisible()) {
                    jpuFriends.show((Item) e.getSource(), 40, 20);
                } else {
                    jpuUsers.show((Item) e.getSource(), 40, 20);
                }
                selected = (Item) e.getSource();
            }
        };
        aeForContext = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Написать сообщение")) {
                    msgManager.addTab(selected.getID(), selected.getFirstName(), selected.getSurname());

                }
            }
        };
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        sPanel.setVisible(false);
        sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.Y_AXIS));
        fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.Y_AXIS));
        add(fPanel);
        add(sPanel);

        jpuFriends = new JPopupMenu();

        JMenuItem msg = new JMenuItem("Написать сообщение");
        msg.addActionListener(aeForContext);
        JMenuItem delete = new JMenuItem("Удалить из друзей");
        delete.addActionListener(aeForContext);
        jpuFriends.add(msg);
        jpuFriends.add(delete);


        jpuUsers = new JPopupMenu();
        jpuUsers.add("Добавить в друзья");
    }

    public void setSp(JScrollPane s) {
        sp = s;
    }
    public void addItem(Item item) {
        item.addActionListener(ae);
        fPanel.add(item);
    }
    public void deleteItem(int id) {
        Component[] c = getComponents();

        for (Component aC : c) {
            if (((Item) aC).getID() == id)
                this.remove(aC);

        }
    }

    public void search(String name) {
        if (name.equals("")) return;
        showAll();
        Component[] c = fPanel.getComponents();

        for (Component aC : c) {
            if ( !(((Item) aC).getFirstName() + " " + ((Item) aC).getSurname()).contains(name)) {
                aC.setVisible(false);
            }
        }

    }
    public void searchAll(String name) {
        if (name.equals("")) return;
        List<User> users = Connection.getInstance().Message_getUserList(name);
        if (users != null) {
            for (User user : users) {
                Item item = new Item(user.getId(), user.getName(), user.getLastName(), "online", new ImageIcon("E:\\img.jpg"));
                item.addActionListener(ae);
                sPanel.add(item);
            }
        }
        sp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Результат поиска:"));
        fPanel.setVisible(false);
        sPanel.setVisible(true);


    }
    public void showAll() {
        Component[] c = fPanel.getComponents();

        for (Component aC : c)
            aC.setVisible(true);

        fPanel.setVisible(true);
        sPanel.setVisible(false);
        sPanel.removeAll();

        sp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Друзья"));

    }
}
