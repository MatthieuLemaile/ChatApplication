package com.excilys.mlemaile.application.chat.chatapplication;

import java.util.UUID;

/**
 * Created by excilys on 06/04/17.
 */

public class Message {
    private String uuid;
    private String login;
    private String message;

    public Message(){
        uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return uuid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
