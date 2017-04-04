package com.company.GUI.main;

import com.company.Connection;
import com.company.FileApplicationsToFriend;
import com.company.Message;
import com.company.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by vlad on 29.03.2017.
 */
public class GuiNewFriends extends JFrame {
    private GuiMainNew gmn;

    ActionListener al = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = ((JButton)e.getSource()).getName();
            String strs[] = str.split(" ", 2);
            int id = Integer.parseInt(strs[0]);
            delete(id);
            gmn.setCountOfApp(-1, true);
            FileApplicationsToFriend fapt = new FileApplicationsToFriend(Connection.getInstance().getMyId());
            fapt.delete(id);
            Connection.getInstance().Message_answerForNewFriend(strs[1].equals("П"), id);
            if (strs[1].equals("П")) {
                User u = Connection.getInstance().Message_Info(id);
                Item item = new Item(id, u.getName(), u.getLastName(),
                        Connection.getInstance().Message_isOnline(id) == 1, gmn.getDefaultIcon());

                gmn.getPanelFriends().addItem(item);

            }
        }
    };
    private  JPanel main;

    public GuiNewFriends() {
        super("Запросы в друзья");
        setBounds(100, 100, 450, 300);
        setResizable(false);

        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        JScrollPane jspMain = new JScrollPane(main, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        FileApplicationsToFriend fatf = new FileApplicationsToFriend(Connection.getInstance().getMyId());
        List<Message> list = fatf.getList();
        if (list != null) {
            for (Message m: list) {
                User user = Connection.getInstance().Message_Info(m.getId());
                if (user == null) {
                    fatf.delete(m.getId());
                    continue;
                }

                add(m.getText(), user);
            }
        }

        main.setAlignmentX(LEFT_ALIGNMENT);
        setContentPane(jspMain);

    }

    public void add(String str, User user) {
        JPanel jpNew = new JPanel();
        jpNew.setName("" + user.getId());
        jpNew.setBorder(BorderFactory.createEtchedBorder());
        jpNew.setLayout(new BoxLayout(jpNew, BoxLayout.Y_AXIS));
        JPanel j1 = new JPanel();
        j1.setLayout(new BoxLayout(j1, BoxLayout.X_AXIS));
        JLabel fulname = new JLabel("<html>" + user.getName() + " " + user.getLastName());
        JButton bConfim = new JButton("Принять");
        bConfim.setName(user.getId() + " П");
        bConfim.addActionListener(al);
        JButton bCancel = new JButton("Отклонить");
        bCancel.setName(user.getId() + " О");
        bCancel.addActionListener(al);
        JTextArea jta = new JTextArea(str);
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.setEditable(false);
        jta.setFocusable(false);
        jta.setRows(4);
        jta.setColumns(20);
        JScrollPane jsp = new JScrollPane(jta, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        j1.add(fulname);
        j1.add(bConfim);
        j1.add(bCancel);
        jpNew.add(j1);
        jpNew.add(jsp);
        main.add(jpNew);
        JLabel spacer = new JLabel("  ");

        main.add(spacer);
    }
    public void delete(int id) {
        for (int i = 0; i < main.getComponentCount(); i++) {
            if (main.getComponent(i) instanceof JPanel && main.getComponent(i).getName().equals("" + id)) {
                main.remove(i);
                main.revalidate();
                main.repaint();
                return;
            }
        }
    }

    public void setGmn(GuiMainNew gmn) {
        this.gmn = gmn;
    }
}
