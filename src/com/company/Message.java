package com.company;

/**
 * Created by vlad on 19.03.2017.
 */
public class Message {
    private String text;
    private int type;
    private int id;

    public Message() {
        text = "";
        type = 0;
        id = 1;
    }
    public Message(String text, int type, int id) {
        this.text = text;
        setType(type);
        setId(id);
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if (type < 0 || type > 1) type = 0;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 1) id = 1;
        this.id = id;
    }
}
