package com.company;

import com.company.GUI.GuiLogin;

import javax.swing.*;
import java.io.File;


public class Main {

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String ip = (String)JOptionPane.showInputDialog(null, "Введите IP", "MySCQ", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
        Connection.getInstance().setIp(ip);
        Connection.getInstance().init();

        GuiLogin guiL = new GuiLogin();
        guiL.setVisible(true);

    }

}

