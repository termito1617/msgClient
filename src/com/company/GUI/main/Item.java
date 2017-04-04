package com.company.GUI.main;

import com.company.Connection;
import com.company.FileMessages;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vlad on 01.03.2017.
 */
public class Item extends JButton {
    private int id;
    private String firstName;
    private String visibleText;
    private String surname;
    private boolean status;
    private int counterOfNewMessages;

    public Item(int id, String name, String surname, boolean status, ImageIcon icon) {
        super();
        int k;
        visibleText = name + " " + surname;
        counterOfNewMessages = 0;
        FontMetrics fm = this.getFontMetrics(this.getFont());
        k = fm.stringWidth(visibleText);
        String newVisibleText = "";
        if (k > 130) {
            for (int i = 0; i < visibleText.length(); i++) {
                if (fm.stringWidth(newVisibleText + visibleText.charAt(i)) < 131) {
                    newVisibleText += visibleText.charAt(i);
                } else {
                    newVisibleText += "...";
                    this.setToolTipText(visibleText);
                    visibleText = newVisibleText;
                    break;
                }
            }
        }

        this.setText("<html>" + visibleText);
        this.setIcon(icon);
        firstName = name;
        this.surname = surname;
        this.id = id;
        setStatus(status);
        this.setHorizontalAlignment(LEFT);
        this.setPreferredSize(new Dimension(165, 50));

    }
    public void setStatus(boolean newStatus) {
        status = newStatus;
        if (!status) {
            setBackground(Color.gray);
        } else {
            setBackground(Color.LIGHT_GRAY);
        }
    }

    public boolean isOnline() { return status; }
    public int getID() {
        return id;
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

    public int getCounterOfNewMessages() {
        return counterOfNewMessages;
    }

    public void setCounterOfNewMessages(int counterOfNewMessages) {
        this.counterOfNewMessages = counterOfNewMessages;
        if (counterOfNewMessages == 0) {
            this.setText("<html>" + visibleText);
        } else {
            this.setText("<html>[" + counterOfNewMessages + "]<br>" + visibleText);
        }
    }
}
