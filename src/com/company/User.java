package com.company;




/**
 * Created by vlad on 21.02.2017.
 */
public class User {
    private int id;
    private String login;
    private String passHash;
    private String name;
    private String lastName;
    //public String toString() {
     //   return new String(login + " " + passHash + " " + name + " " + lastName);
    //}

    public void setLogin(String login) {
        this.login = login;
    }
    public void setPassHash(String Password) {
        passHash = Password;
    }
    public void setName(String name) {
        this.name= name;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //public User(String s)  {
   ////     String []user = s.split(" ");
    //    if (user.length != 4) {
     //       login = "default";
     //       passHash = "default";
     //       name = "default";
      //      lastName = "default";
      //  }
    //}
    public User() {
        login    = "";
        passHash = "";
        name     = "";
        lastName = "";
    }
    public User(String login, String passHash, String name, String lastName) {
        this.login = login;
        this.passHash = passHash;
        this.name = name;
        this.lastName = lastName;
    }
    public String getLogin() {
        return login;
    }
    public String getPassHash() {
        return passHash;
    }
    public String getName() {
        return name;
    }
    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
