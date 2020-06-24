package com.example.project.models;

import com.example.project.CherryApplication;
import com.google.gson.*;

import java.util.Date;
import java.util.List;

public class Message {

    private class Messages {
        private class EmbeddedArray {
            public List<Message> messages;
        }
        public EmbeddedArray _embedded;
    }

    private class JsonMessage {
        private String match;
        private String fromuser;
        private String message;
        private Date timesent;
    }

    public class Embbeded {
        public Profile fromuser;
        public Match _match;
    }

    public Profile fromuser;
    public String message;
    public Date timesent;
    public Match match;

    private Embbeded _embedded;

    public static List<Message> parseArray(String json) {
        Gson gson = new GsonBuilder().create();
        Messages messages = gson.fromJson(json, Messages.class);

        for (Message m : messages._embedded.messages) {
            m.fromuser = m._embedded.fromuser;
            m.match = m._embedded._match;
        }

        return messages._embedded.messages;
    }

    public String format(CherryApplication application) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        JsonMessage msg = new JsonMessage();
        msg.fromuser = application.host + "/user/" + fromuser.uuid;
        msg.match = application.host + "/match/" + match.id;
        msg.message = message;
        msg.timesent = timesent;
        return gson.toJson(msg);
    }

    public Match getMatch() {
        return match;
    }

    public Message setMatch(Match match) {


        this.match = match;
        return this;
    }

    public Profile getFromuser() {
        return fromuser;
    }

    public Message setFromuser(Profile fromuser) {
        this.fromuser = fromuser;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getTimesent() {
        return timesent;
    }

    public Message setTimesent(Date timesent) {
        this.timesent = timesent;
        return this;
    }
}
