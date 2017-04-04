package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vlad on 19.03.2017.
 */
public class FileMessages {
    private String path;
    public FileMessages(int myId){
        path = "MyICQ\\Messages\\" + myId;
        File f = new File("MyICQ");
        if (!f.exists()) f.mkdir();
        f = new File("MyICQ\\Messages");
        if (!f.exists()) f.mkdir();
    }

    public void addMessage(Message msg) {
        File fPath = new File(path);
        String str;
        BufferedWriter bw = null;
        try {
            if (!fPath.exists()) {
                fPath.mkdir();
            }
            File file  = new File(fPath, msg.getId() + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            str = msg.getType() + " " + msg.getText();
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(str);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Message> getMessage(int id) {

        BufferedReader reader;
        List<Message> msgList = new ArrayList<>();
        Message msg;
        try {
            reader = new BufferedReader(new FileReader(path + "\\" + id + ".txt"));
        } catch (FileNotFoundException e) {
            return null;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                msg = new Message();

                msg.setType(  Integer.parseInt( line.substring(0, 1) )  );
                msg.setText(line.substring(1));
                msgList.add(msg);
            }
            return msgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
