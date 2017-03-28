package com.company.GUI.main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vlad on 01.03.2017.
 */
public class PanelAccountInfo extends JPanel {

    public PanelAccountInfo(String login, String name, String surname, ImageIcon icon) {
        JLabel labelAccountInfo = new JLabel("<html>" + login + "<br>" + name + " " + surname, icon, SwingConstants.LEFT);
        labelAccountInfo.setVerticalTextPosition(SwingConstants.TOP);
        labelAccountInfo.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(labelAccountInfo);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Вы вошли как:"));

       setMaximumSize(new Dimension(300, 100));
    }
}
