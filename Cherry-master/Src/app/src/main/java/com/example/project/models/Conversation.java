package com.example.project.models;

import java.util.Date;

public class Conversation {

    public String uuid;
    public String name;
    public Date lastMessage;
    public String toUuid;

    public Conversation(String name, Date lastMessage) {
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public Conversation setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }
}
