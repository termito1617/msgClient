package com.company.GUI.main;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vlad on 01.03.2017.
 */
public class Item extends JButton {
    private int id;
    private String firstName;
    private String surname;
    private String status;
    public Item(int id, String name, String surname, String status, ImageIcon icon) {
        super("<html>" + name + " " + surname , icon);
        firstName = name;
        this.surname = surname;
        this.id = id;
        this.status = status;
        this.setHorizontalAlignment(LEFT);
        this.setPreferredSize(new Dimension(165, 50));
    }
    public void setStatus(String newStatus) {
        status = newStatus;
    }
    public int getID() {
        return id;
    }
    public String getStatus() {
        return status;
    }



    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
