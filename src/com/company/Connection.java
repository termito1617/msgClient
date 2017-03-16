package com.company;

import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * Created by vlad on 24.02.2017.
 */

public class Connection {
    private final String  ip   = "127.0.0.1";
    private final int     port = 8080;


    private BufferedReader  input;
    private PrintWriter     output;
    private Socket          socket;

    private static Connection connection = new Connection();
    public  static Connection getInstance() { return connection;}

    private Connection() {
        System.out.println("Creating conection");
        connect();
    }


    public boolean isConnected() {
        return socket != null;
    }
    public boolean connect() {
        if (!isConnected()) {
            try {
                socket = new Socket(ip, port);
                input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                return true;
            } catch (IOException e) {
                socket = null;
                input  = null;
                output = null;
                return false;
            }
        }
        return true;
    }

    public int     Message_Registration(User user) {
        if (!isConnected() && !connect()) {
            return -1;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"registration\"><login>" +
                user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass><name>" +
                user.getName() + "</name><surname>" + user.getLastName() + "</surname></message>");
        try {
            return Integer.parseInt(input.readLine());
        } catch (IOException e) {
            System.exit(-2);
            return -2;
        }

    }
    public int     Message_LoginIn(User user) {
        if (!isConnected() && !connect()) {
            return -1;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"login\"><login>"
                + user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass></message>");
        
        try {
            return Integer.parseInt(input.readLine());
        } catch (IOException e) {
            System.exit(-2);
            return -2;
        }
    }
    public void    Message_End() {
        if (isConnected()) {
            output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"end\"></message>");
            try {
                socket.close();
            } catch (IOException e) {
                socket = null;
            }
        }
        input  = null;
        output = null;
    }
    public User    MessageInfo() {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"info\"></message>");

        String str;
        try {
            str = input.readLine();
        } catch (IOException e) {
            return null;
        }
        SAXHandler handler;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            handler = new SAXHandler();
            parser.parse(new InputSource(new StringReader(str)), handler);
        } catch (Exception e) {
            return null;
        }
        return  new User(handler.getValue("login"), null, handler.getValue("name"),  handler.getValue("surname"));

    }
    public List<User> Message_getFriendsList() {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_friends\"></message>");

        String str;
        try {
            str = input.readLine();
        } catch (IOException e) {
            return null;
        }
        SAXHandler handler;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            handler = new SAXHandler();
            parser.parse(new InputSource(new StringReader(str)), handler);
        } catch (Exception e) {
            return null;
        }
        return handler.getUserList();
    }
    public List<User> Message_getUserList(String fName) {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_users\"><name>" +
                fName + "</name></message>");

        String str;
        try {
            str = input.readLine();
        } catch (IOException e) {
            return null;
        }
        SAXHandler handler;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            handler = new SAXHandler();
            parser.parse(new InputSource(new StringReader(str)), handler);
        } catch (Exception e) {
            return null;
        }
        return handler.getUserList();
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (isConnected())
            Message_End();
    }
}
