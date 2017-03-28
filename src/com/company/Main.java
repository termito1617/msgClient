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


        GuiLogin guiL = new GuiLogin();
        guiL.setVisible(true);

    }

}

