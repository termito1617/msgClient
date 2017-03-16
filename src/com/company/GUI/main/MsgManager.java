package com.company.GUI.main;

import com.company.Connection;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Created by vlad on 14.03.2017.
 */
public class MsgManager extends JFrame {
    private JTabbedPane jtb;
    private JTextField jtfInputMsg;
    private int curIndex;
    private class ActionClose extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            setVisible(false);
        }
    }

    public MsgManager() {
        super();
        curIndex = -1;
        addWindowListener(new ActionClose());
        setBounds(235, 10, 500, 530);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        jtb = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        jtb.setPreferredSize(new Dimension(500, 500));


        JPanel jpMsg = new JPanel();
        jpMsg.setLayout(new BoxLayout(jpMsg, BoxLayout.X_AXIS));

        jtfInputMsg = new JTextField();

        JButton b = new JButton("Отправить");

        //------------ Делаем из JLabel l кнопку-картинку
        JLabel l = new JLabel(new ImageIcon("E:\\closeUp.jpg"));
        l.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        l.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JLabel)e.getSource()).setIcon(new ImageIcon("E:\\closeDown.jpg"));
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                ((JLabel)e.getSource()).setIcon(new ImageIcon("E:\\closeUp.jpg"));
                jtb.remove(jtb.getSelectedIndex());
                if (jtb.getTabCount() == 0) setVisible(false);

            }
        });


        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        jpMsg.add(jtfInputMsg);
        jpMsg.add(b);
        jpMsg.add(l);
        mainPanel.add(jtb);
        mainPanel.add(jpMsg);
        setContentPane(mainPanel);
    }
    public void addTab(int id, String name, String surname) {

        int index = -1;
        for (int i = 0; i < jtb.getTabCount(); i++) {

            JScrollPane       jsp  = (JScrollPane)       jtb.getComponentAt(i);
            JPanelForMessages jpfm = (JPanelForMessages) jsp.getViewport().getView();

            if (jpfm.getId() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            jtb.setSelectedIndex(index);

            return;
        }
        JPanelForMessages jpm = new JPanelForMessages(id);
        JScrollPane scrPane = new JScrollPane(jpm, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        //


        jtb.addTab(name + " " + surname, scrPane);
        jtb.setSelectedIndex(jtb.getTabCount() - 1);
        setVisible(true);
        addMsg("Класс java.util.Random представляет собой генератор псевдослучайных чисел.", 0, true, id);
        addMsg("Класс представлен двумя конструкторами", 1, true, id);
        addMsg("Random() - создаёт генератор чисел, использующий уникальное начальное число", 1, true, id);
        addMsg("Random(long seed) - позволяет указать начальное число вручную", 0, true, id);
        addMsg("Так как класс создаёт псевдослучайное число, то задав начальное " +
                "число, вы определяете начальную точку случайной последовательности.", 1, true, id);
        addMsg("И будете получать одинаковые случайные последовательности.", 0, true, id);
        addMsg("Чтобы избежать такого совпадения, обычно используют второй конструктор " +
                "с использованием текущего времени в качестве инициирующего значения.", 1, true, id);
        addMsg("Методы:", 0, true, id);
        addMsg("boolean nextBoolean() - возвращает следующее случайное значение типа boolean", 1, true, id);
        addMsg("void nextBytes(byte[] buf) - заполняет массив случайно созданными значениями", 0, true, id);
        addMsg("double nextDouble() - возвращает следующее случайное значение типа double", 0, true, id);
        addMsg("float nextFloat() - возвращает следующее случайное значение типа float", 0, true, id);
        addMsg("synchronized double nextGaussian() - возвращает следующее случайное значение гауссова случайного числа, т.е. " +
                "значения, центрированное по 0.0 со стандартным отклонением в 1.0 (кривая нормального распределения)", 1, true, id);


        showM(id);

    }
    public void addMsg(String text, int type, boolean t, int id) {
        JScrollPane sp = null;
        JPanelForMessages pfm = null;
        for (int i = 0; i < jtb.getTabCount(); i++) {

            JScrollPane jsp = (JScrollPane) jtb.getComponentAt(i);
            JPanelForMessages jpfm = (JPanelForMessages) jsp.getViewport().getView();

            if (jpfm.getId() == id) {
                sp = jsp;
                pfm = jpfm;
            }
        }
        if (sp == null || pfm == null) return;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = pfm.getCount();
        gbc.gridx = type == 0 ? 0 : 1;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = type == 0? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gbc.weightx = 10;
        JButton l = new JButton();
        l.setBackground(Color.WHITE);

        if (t) l.setVisible(false);
        pfm.add(l, gbc);
        pfm.incCount();



        StringBuffer str = new StringBuffer(text.length() + text.length() / 30 * 4 + 10);

        str.append("<html>");
        int space = 0;
        int pLine = 6;
        Graphics g = l.getGraphics();
        FontMetrics fm;
        fm = g.getFontMetrics();
        for (int i= 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (fm.stringWidth(str.substring(pLine) + ch) > 180) {
                if (ch == ' ') {
                    str.append("<br>");
                    pLine = str.length();
                    space = 0;
                } else {
                    if (space == 0) {
                        int last = str.length() - 1;
                        char lastCh = str.charAt(last);
                        str.deleteCharAt(last);
                        str.append("-<br>");
                        pLine = str.length();
                        str.append(lastCh);
                        str.append(ch);
                    } else {

                        str.deleteCharAt(space);
                        str.insert(space, "<br>");
                        pLine = str.lastIndexOf("<br>")+ 4;
                        str.append(ch);
                    }
                }
            } else {
                str.append(ch);
                if (ch == ' ') space = str.length() - 1;
            }
        }

        l.setText(str.toString());
        if (!t) {
            final JScrollPane fsp = sp;
            SwingUtilities.invokeLater(() -> {
                fsp.getVerticalScrollBar().setValue(fsp.getVerticalScrollBar().getMaximum()
                        - fsp.getVerticalScrollBar().getVisibleAmount());
            });
        }

    }
    public void showM(int id) {
        JScrollPane sp = null;
        JPanelForMessages pfm = null;
        for (int i = 0; i < jtb.getTabCount(); i++) {

            JScrollPane jsp = (JScrollPane) jtb.getComponentAt(i);
            JPanelForMessages jpfm = (JPanelForMessages) jsp.getViewport().getView();

            if (jpfm.getId() == id) {
                sp = jsp;
                pfm = jpfm;
            }
        }
        if (sp == null || pfm == null) return;
        pfm.setVisible(false);
        for (Component c: pfm.getComponents()){
            c.setVisible(true);
        }


        final JScrollPane scrPane = sp;
        SwingUtilities.invokeLater(() -> {
            scrPane.getVerticalScrollBar().setValue(scrPane.getVerticalScrollBar().getMaximum()
                    - scrPane.getVerticalScrollBar().getVisibleAmount());
        });
        pfm.setVisible(true);
    }
}
