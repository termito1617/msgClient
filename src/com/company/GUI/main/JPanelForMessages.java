package com.company.GUI.main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vlad on 14.03.2017.
 */
public class JPanelForMessages extends JPanel {
    private int count;
    private int id;

    public JPanelForMessages(int id) {
        super(new GridBagLayout());
        count = 0;
        this.id = id;
    }

    public void incCount() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
