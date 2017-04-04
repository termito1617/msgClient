package com.company.GUI;

import com.company.Connection;
import com.company.GUI.main.GuiMainNew;
import com.company.User;
import com.company.md5Algo;

import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;


public class GuiLogin extends JFrame {
    private JLabel          errorL          = new JLabel("");
    private JTextField      loginTF         = new JTextField(15);
    private JPasswordField  passPF          = new JPasswordField(15);

    private class ActionBRegisterPress implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            setVisible(false);
            GuiRegisteration GuiR = new GuiRegisteration(loginTF.getText(), passPF.getText());
            GuiR.setVisible(true);
        }
    }
    private class ActionBLoginInPress implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (loginTF.getText().isEmpty() || passPF.getPassword().length == 0) {
                errorL.setText("Необходимо заполнить все поля!");
                return;
            }

            Connection con = Connection.getInstance();
            User user = new User();
            user.setLogin(loginTF.getText());
            try {
                user.setPassHash(md5Algo.md5(passPF.getText() + "Qmc78Fwo1"));
            } catch (NoSuchAlgorithmException e) {
                errorL.setText("Ошибка хеширования пароля.");
                return;
            }
            
            int result = con.Message_LoginIn(user);
            if (result == -1) {
                errorL.setText("Отсутствует подключение к сети.");
                return;
            }
            if (result == 0) {
                errorL.setText("Логин и пароль не верны.");
                return;
            }
            user = con.Message_Info(0);
            setVisible(false);
            GuiMainNew gm = new GuiMainNew(user.getLogin(), user.getName(), user.getLastName(), null);
            gm.setVisible(true);
        }
    }
    private class ActionClose extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            Connection.getInstance().Message_End();
            System.exit(0);
        }
    }
    public GuiLogin() {
        super("MyICQ: Login");
        JButton         logInB          = new JButton("Login in");
        JButton         registrationB   = new JButton("Registration");
        JLabel          loginL          = new JLabel("Login:");
        JLabel          passwordL       = new JLabel("Password:");
        loginTF.setText("termito1617@gmail.com");
        passPF.setText("WicKedqwe15");
        errorL.setForeground(Color.red);

        logInB.addActionListener(new ActionBLoginInPress());
        registrationB.addActionListener(new ActionBRegisterPress());
        this.addWindowListener(new ActionClose());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(100, 100, 400, 180);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS ));
        container.add(errorL);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.add(loginL, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(4, 2, 2, 2), 0, 0));

        panel.add(passwordL, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));

        panel.add(logInB, new GridBagConstraints(0, 2, 1, 1, 1, 1,
               GridBagConstraints.NORTHWEST,
               GridBagConstraints.HORIZONTAL,
               new Insets(40, 5, 2, 2), 0, 0));

        panel.add(registrationB, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(40, 2, 2, 5), 0, 0));

        panel.add(loginTF, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL,
                new Insets(4, 2, 2, 2), 0, 0));

        panel.add(passPF, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0));
        container.add(panel);
    }
}
