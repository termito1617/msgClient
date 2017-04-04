package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vlad on 28.03.2017.--------
 */


public class FileApplicationsToFriend {
    private String path;
    public FileApplicationsToFriend(int myId){
        path = "MyICQ\\" + myId + ".txt";
        File f = new File("MyICQ");
        if (!f.exists()) f.mkdir();
    }

    public boolean isContains(int id) {
        List<Message> list = getList();
        if (list == null) return false;
        for (Message m: list)
            if (m.getId() == id) return true;
        return false;
    }
    public void add(Message msg) {
        String str;
        BufferedWriter bw = null;
        try {

            File file  = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(msg.getId() + " " + msg.getText());
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<Message> getList() {

        BufferedReader reader;
        List<Message> msgList = new ArrayList<>();
        Message msg;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            return null;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                msg = new Message();
                if (line.charAt(0) == '\uFEFF') {
                    line = line.substring(1);
                }
                String[] strs = line.split(" ", 2);

                msg.setId(Integer.parseInt(strs[0]));
                msg.setText(strs[1]);
                msgList.add(msg);
            }
            reader.close();
            return msgList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    public void delete(int id) {
     List<Message> list = getList();
        if (list == null) return;
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Message msg: list) {
            if (msg.getId() != id) add(msg);
        }
    }
    public int getCountOfApp() {
        BufferedReader reader;
        int result = 0;
        try {
            reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            return 0;
        }

        String line;
        try {
            while ((line = reader.readLine()) != null) {
               result++;
            }
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
