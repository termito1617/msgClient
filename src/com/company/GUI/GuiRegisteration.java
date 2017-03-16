package com.company.GUI;

import com.company.Connection;
import com.company.GUI.main.GuiMainNew;
import com.company.User;
import com.company.md5Algo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.NoSuchAlgorithmException;


public class GuiRegisteration extends JFrame {
    private JLabel          errorL          = new JLabel("");
    private JTextField      loginTF         = new JTextField(15);
    private JTextField      nameTF          = new JTextField(15);
    private JTextField      lastNameTF      = new JTextField(15);
    private JPasswordField  passwordPF      = new JPasswordField(15);


    private class ActionBToRegisterPress implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            if (loginTF.getText().isEmpty() || passwordPF.getPassword().length == 0 ||
                    nameTF.getText().isEmpty() || lastNameTF.getText().isEmpty()) {
                errorL.setText("Необходимо заполнить все поля!");
                return;
            }

            if (!Validator.checkLength(loginTF.getText(), 5, 30)) {
                errorL.setText("Используйте логин длиной от 5 до 30 знаков!");
                return;
            }
            if (!Validator.checkLength(passwordPF.getText(), 8, 30)) {
                errorL.setText("Используйте пароль длиной от 8 до 30 знаков!");
                return;
            }
            if ( !Validator.checkString(loginTF.getText(), "^[a-zA-Z0-9_@.]+") ) {
                errorL.setText("Некорректный логин. Используйте a-Z, цифры и символ '_'");
                return;
            }
            if ( !Validator.checkString(passwordPF.getText(), "^[a-zA-Z0-9_@.]+") ) {
                errorL.setText("Некорректный пароль. Используйте a-Z, цифры и символ '_'");
                return;
            }

            if (!Validator.checkString(nameTF.getText(), "^[A-ZА-Я]?[a-zа-я]+")) {
                errorL.setText("Некорректное имя. Используйте a-Z");
                return;
            }
            if (!Validator.checkString(lastNameTF.getText(), "^[A-ZА-Я]?[a-zа-я]+") ) {
                errorL.setText("Некорректная фамилия. Используйте a-Z");
                return;
            }


            User user;
            try {
                user = new User(loginTF.getText(), md5Algo.md5(passwordPF.getText() + "Qmc78Fwo1"),
                        nameTF.getText(), lastNameTF.getText());
            } catch (NoSuchAlgorithmException e) {
                errorL.setText("Ошибка хеширования пароля");
                return;
            }
            /////////////
            Connection con = Connection.getInstance();
            int result = con.Message_Registration(user);
            if (result == 0) {
                errorL.setText(" Логин занят. Используйте другой.");
                return;
            }
            if (result == -1) {
                errorL.setText("Отсутствует подключение к сети.");
                return;
            }
            setVisible(false);
            GuiMainNew gm = new GuiMainNew(user.getLogin(), user.getName(), user.getLastName(), null);
            gm.setVisible(true);




            /////////////
        }
    }
    private class ActionClose extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            Connection.getInstance().Message_End();
            System.exit(0);
        }
    }

    public GuiRegisteration(String login, String pass) {
        super("MyICQ: Registration");
        JLabel          loginL          = new JLabel("Login:");
        JLabel          passwordL       = new JLabel("Password:");
        JLabel          nameL           = new JLabel("Name:");
        JLabel          lastnameL       = new JLabel("Surname:");
        JButton         RegisterB       = new JButton("to Register");
        loginTF.setText(login);
        passwordPF.setText(pass);
        errorL.setForeground(Color.red);
        RegisterB.addActionListener(new ActionBToRegisterPress());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new ActionClose());
        this.setBounds(100, 100, 400, 250);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        Container container = this.getContentPane();

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS ));
        container.add(errorL);
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        panel.add(loginL, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(passwordL, new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(nameL, new GridBagConstraints(0, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(lastnameL, new GridBagConstraints(0, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(loginTF, new GridBagConstraints(1, 0, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(passwordPF, new GridBagConstraints(1, 1, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(nameTF, new GridBagConstraints(1, 2, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(lastNameTF, new GridBagConstraints(1, 3, 1, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2),
                0, 0));
        panel.add(RegisterB, new GridBagConstraints(0, 4, 2, 1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(40, 2, 2, 2),
                0, 0));
        container.add(panel);
    }


}
