
        package com.company;

        import com.company.GUI.main.GuiMainNew;
        import com.company.GUI.main.MsgManager;
        import org.xml.sax.InputSource;

        import javax.xml.parsers.SAXParser;
        import javax.xml.parsers.SAXParserFactory;
        import java.io.*;
        import java.net.InetAddress;
        import java.net.Socket;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Queue;
        import java.util.stream.Stream;

        /**
 * Created by vlad on 24.02.2017.
 */

public class Connection extends Thread {
    private final String  ip   = "127.0.0.1";
    private final int     port = 8080;

    private BufferedReader  input;
    private PrintWriter     output;
    private Socket          socket;
    private SAXHandler      h;
    private int             myId;
    private GuiMainNew      guiMainNew;


    private static Connection connection = new Connection();
    public  static Connection getInstance() { return connection;}

    private Connection() {
        System.out.println("Creating conection");
        connect();
        this.start();
    }

    public void run() {
        while (true) {
            if (!isConnected()) {
                try {
                    synchronized (this) {
                        this.wait();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            try {
                String str = input.readLine();
                SAXHandler handler;
                try {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    handler = new SAXHandler();
                    parser.parse(new InputSource(new StringReader(str)), handler);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                switch (handler.getType()) {
                    case "msg": {
                        Message msg = new Message(handler.getValue("text"), 0, Integer.parseInt(handler.getValue("from")));
                        FileMessages fm = new FileMessages(Connection.getInstance().getMyId());
                        fm.addMessage(msg);
                        guiMainNew.addMessage(msg, false);
                        break;
                    }
                    case "setOnline": {
                        int id[] = {Integer.parseInt(handler.getValue("id"))};
                        guiMainNew.getPanelFriends().setStatuses(id,
                                Integer.parseInt(handler.getValue("status")) == 1);
                        break;
                    }
                    case "newFriend": {
                        FileApplicationsToFriend fapt = new FileApplicationsToFriend(Connection.getInstance().getMyId());
                        Message msg = new Message();
                        msg.setText(handler.getValue("text"));
                        msg.setId(Integer.parseInt(handler.getValue("id")));
                        fapt.add(msg);
                        guiMainNew.setCountOfApp(1, true);
                        break;
                    }
                    default: {
                        while (h != null) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        h = handler;
                    }
                }


            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {}
                socket = null;
            }
        }
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
                synchronized (this) {
                    this.notify();
                }
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

    public void setGuiMainNew(GuiMainNew guiMainNew) {
        this.guiMainNew = guiMainNew;
    }
    private SAXHandler getHandler() {
        while (h == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return h;
    }

    public void    Message_SendMsg(Message msg) {
        if (!isConnected() && !connect()) {
            return;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"msg\">" +
                "<to>" + msg.getId() + "</to>" +
                "<text>" + msg.getText() + "</text>" +
                "</message>");
    }
    public int     Message_Registration(User user) {
        if (!isConnected() && !connect()) {
            return -1;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"registration\"><login>" +
                user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass><name>" +
                user.getName() + "</name><surname>" + user.getLastName() + "</surname></message>");
        int result = Integer.parseInt(getHandler().getValue("result"));
        h = null;
        return result;
    }
    public int     Message_LoginIn(User user) {
        if (!isConnected() && !connect()) {
            return -1;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"login\"><login>"
                + user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass></message>");

        int result = Integer.parseInt(getHandler().getValue("result"));
        h = null;
        return result;
    }
    public int     Message_isOnline(int id) {

        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"is_online\">" +
                "<id>" + id + "</id>" +
                "</message>");

        int result = Integer.parseInt(getHandler().getValue("result"));
        h = null;
        return result;
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
        User user = new User(getHandler().getValue("login"), null, getHandler().getValue("name"), getHandler().getValue("surname"));
        myId = Integer.parseInt(getHandler().getValue("id"));
        h = null;
        return user;
    }
    public void    Message_deleteFriend(int id) {
        if (!isConnected() && !connect()) {
            return;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"delete\">" +
                "<id>" + id + "</id>" +
                "</message>");
    }


    public List<User>      Message_getFriendsList() {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_friends\"></message>");

        List<User> list = getHandler().getUserList();
        h = null;
        return list;
    }
    public List<User>      Message_getUserList(String fName) {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_users\"><name>" +
                fName + "</name></message>");


        List<User> list = getHandler().getUserList();
        h = null;
        return list;
    }
    public List<Message>   Message_getNewMessages() {

        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"get_new_messages\">" +
                "</message>");


        List<Message> msgList = getHandler().getMessages();

        h = null;
        return msgList;
    }
    public List<Message>   Message_getNewAppToFriends() {
        if (!isConnected() && !connect()) {
            return null;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"getNewApp\">" +
                "</message>");

        List<Message> list = getHandler().getMessages();
        h = null;
        return list;
    }
    public void            Message_newFriend(String msg, int id) {
        if (!isConnected() && !connect()) {
            return;
        }
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"newFriend\">" +
                "<text>" + msg + "</text>" +
                "<id>" + id + "</id>" +
                "</message>");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (isConnected())
            Message_End();
    }

    public int getMyId() { return myId; }


}
