
        package com.company;

        import com.company.GUI.main.GuiMainNew;
        import com.company.GUI.main.Item;
        import com.company.GUI.main.MsgManager;
        import org.xml.sax.InputSource;

        import javax.swing.*;
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
    private String  ip = "127.0.0.1";
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
		//test comment
    }
    public void init() {
        connect();
        this.start();
    }
    public void run() {
        while (true) {

            try {
                String str = input.readLine();
                if (socket == null || !socket.isConnected() || str == null) {
                    JOptionPane.showMessageDialog(null, "Соединение пропало", "Error", JOptionPane.ERROR_MESSAGE);
                    System.out.println("SDf");
                    System.exit(-1);
                }

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
                    case "removeFromFriend": {
                        int id = Integer.parseInt(handler.getValue("id"));
                        guiMainNew.getPanelFriends().deleteItem(id);
                        JOptionPane.showMessageDialog(null,
                                "Пользователь " + handler.getValue("name") + " " + handler.getValue("surname") + " удалил вас из друзей",
                                "MyICQ", JOptionPane.YES_OPTION);
                        break;
                    }
                    case "confimFriend": {
                        Item item = new Item(Integer.parseInt(handler.getValue("id")),
                                handler.getValue("name"), handler.getValue("surname"), true, guiMainNew.getDefaultIcon());
                        guiMainNew.getPanelFriends().addItem(item);
                        guiMainNew.getPanelFriends().revalidate();
                        guiMainNew.getPanelFriends().repaint();
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
                        User u = new User();
                        u.setName(handler.getValue("name"));
                        u.setLastName(handler.getValue("surname"));
                        u.setId(msg.getId());
                        guiMainNew.getGnf().add(msg.getText(), u);
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
                JOptionPane.showMessageDialog(null, "Соединение пропало", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
    }

    public void connect() {
            try {
                socket = new Socket(ip, port);
                input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Соединение отсутствует", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
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
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"msg\">" +
                "<to>" + msg.getId() + "</to>" +
                "<text>" + msg.getText() + "</text>" +
                "</message>");
    }
    public int     Message_Registration(User user) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"registration\"><login>" +
                user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass><name>" +
                user.getName() + "</name><surname>" + user.getLastName() + "</surname></message>");
        int result = Integer.parseInt(getHandler().getValue("result"));
        h = null;
        myId = result;
        return result;
    }
    public int     Message_LoginIn(User user) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"login\"><login>"
                + user.getLogin() + "</login>" +
                "<pass>" + user.getPassHash() + "</pass></message>");

        int result = Integer.parseInt(getHandler().getValue("result"));
        h = null;
        myId = result;
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
        if (socket.isConnected()) {
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
    public User    Message_Info(int id) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"info\">" +
                    "<id>" + id + "</id>" +
                "</message>");
        User user = new User(getHandler().getValue("login"), null, getHandler().getValue("name"), getHandler().getValue("surname"));
        user.setId(Integer.parseInt(getHandler().getValue("id")));
        h = null;
        return user;
    }
    public void    Message_deleteFriend(int id) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"delete\">" +
                "<id>" + id + "</id>" +
                "</message>");
    }
    public void    Message_answerForNewFriend(boolean b, int id) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"answerForNewFriend\">" +
                "<answer>" + (b ? 1 : 0) + "</answer>" +
                "<id>" + id + "</id>" +
                "</message>");
    }

    public List<User>      Message_getFriendsList() {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_friends\"></message>");

        List<User> list = getHandler().getUserList();
        h = null;
        return list;
    }
    public List<User>      Message_getUserList(String fName) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"get_list_of_users\"><name>" +
                fName + "</name></message>");


        List<User> list = getHandler().getUserList();
        h = null;
        return list;
    }
    public List<Message>   Message_getNewMessages() {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"get_new_messages\">" +
                "</message>");


        List<Message> msgList = getHandler().getMessages();

        h = null;
        return msgList;
    }
    public List<Message>   Message_getNewAppToFriends() {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<message type=\"getNewApp\">" +
                "</message>");

        List<Message> list = getHandler().getMessages();
        h = null;
        return list;
    }
    public void            Message_newFriend(String msg, int id) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><message type=\"newFriend\">" +
                "<text>" + msg + "</text>" +
                "<id>" + id + "</id>" +
                "</message>");
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (socket.isConnected())
            Message_End();
    }

    public int getMyId() { return myId; }
            public void setIp(String ip) {
                this.ip = ip;
            }
        }
