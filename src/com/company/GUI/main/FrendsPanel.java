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
public class FrendsPanel extends JPanel {
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

                switch (e.getActionCommand()) {
                    case "Открыть диалог": {
                        msgManager.addTab(selected.getID(), selected.getFirstName(), selected.getSurname());
                        selected.setCounterOfNewMessages(0);
                        break;
                    }
                    case "Удалить из друзей": {
                        int answer = JOptionPane.showConfirmDialog(null,
                                "Вы действительно хотите удалить пользователя " + selected.getFirstName() + " " +
                                        selected.getSurname() + " из друзей?",
                                "Потвердите действие", JOptionPane.YES_NO_OPTION);
                        if (answer == JOptionPane.YES_OPTION) {
                            deleteItem(selected.getID());
                            Connection.getInstance().Message_deleteFriend(selected.getID());
                        }
                        break;
                    }
                    case "Добавить в друзья": {
                        User u = Connection.getInstance().MessageInfo();
                        String ans = (String) JOptionPane.showInputDialog(null, "Введите сообщение пользователю которое будет приклеплено к заявке", "Заявка в друзья",
                                JOptionPane.QUESTION_MESSAGE, null, null,
                                "Пользователь " + u.getName() + " " + u.getLastName() + " хочет добавить вас в друзья");
                        if (ans == null) break;
                        Connection.getInstance().Message_newFriend(ans, selected.getID());
                        break;
                    }
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

        JMenuItem msg = new JMenuItem("Открыть диалог");
        msg.addActionListener(aeForContext);
        JMenuItem delete = new JMenuItem("Удалить из друзей");
        delete.addActionListener(aeForContext);
        jpuFriends.add(msg);
        jpuFriends.add(delete);


        jpuUsers = new JPopupMenu();
        JMenuItem newFriend = new JMenuItem("Добавить в друзья");
        newFriend.addActionListener(aeForContext);
        jpuUsers.add(newFriend);
    }

    public void setSp(JScrollPane s) {
        sp = s;
    }
    public void addItem(Item item) {
        item.addActionListener(ae);
        fPanel.add(item);
    }
    public void deleteItem(int id) {
        Component[] c = fPanel.getComponents();

        for (Component aC : c) {
            if (((Item) aC).getID() == id) {
                fPanel.remove(aC);
                fPanel.revalidate();
                fPanel.repaint();
                return;
            }
        }

    }
    public void setCountOfNewMessages(int id, int count, boolean isAdd) {
        Component[] c = fPanel.getComponents();

        for (Component aC : c) {
            Item item = (Item)aC;
            if (item.getID() == id) {
                if (isAdd) {
                    item.setCounterOfNewMessages(count + item.getCounterOfNewMessages());
                } else {
                    item.setCounterOfNewMessages(count);
                }
            }
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
                Item item = new Item(user.getId(), user.getName(), user.getLastName(), false, new ImageIcon("E:\\img.jpg"));
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
    public void setStatuses(int id[], boolean status) {
        if (id == null) return;
        Component[] c = fPanel.getComponents();

        for (Component aC : c) {
            for (int cId: id) {
                if (cId == ((Item)aC).getID())
                    ((Item)aC).setStatus(status);
            }
        }
    }
}
