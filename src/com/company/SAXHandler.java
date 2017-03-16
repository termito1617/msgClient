package com.company;

/**
 * Created by vlad on 02.03.2017.
 */


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vlad on 23.02.2017.
 */
public class SAXHandler extends DefaultHandler {

    private String curTag;
    private HashMap<String, String> hm;
    private List<User> users;
    private String type;
    private boolean isList;
    private User curUser;
    public SAXHandler() {
        isList = false;
        curUser = new User();
        hm = new HashMap<>();
        users = new ArrayList<>();
    }

    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        curTag = qName;
        if (qName.equals("message")) {
            type = atts.getValue("type");
            if (type.equals("friends") || type.equals("users")) isList = true;
        }

    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);

        if (!isList) {
            hm.put(curTag, value);

        } else {
           switch (curTag) {
               case "id": {
                   curUser = new User();
                   curUser.setId(Integer.parseInt(value));
                   break;
               }
               case "name": {
                   curUser.setName(value);
                   break;
               }
               case "lastName": {
                   curUser.setLastName(value);

                   users.add(curUser);
                   break;
               }
           }
        }
    }





    public String getType() {
        return type;
    }

    String getValue(String key) {
        return hm.get(key);
    }
    List<User> getUserList() {
        return users;
    }
}