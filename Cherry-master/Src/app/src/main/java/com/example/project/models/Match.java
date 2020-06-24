package com.example.project.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.project.CherryApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.List;

public class Match implements Parcelable {

    private class Links {
        private class Link {
            public URL href;
        }

        public Link self;
        public Link user1;
        public Link user2;
    }

    private class Embedded {
        public Profile user1;
        public Profile user2;
    }

    private class Matches {
        private class EmbeddedArray {
            public List<Match> matches;
        }

        public EmbeddedArray _embedded;
    }

    public long id;

    public Profile user1;
    public Profile user2;

    public List<Message> messages;

    private Links _links;
    private Embedded _embedded;

    /*
     * Json parsing
     */
    public static List<Match> parseArray (String json) {
        Gson gson = new GsonBuilder().create();
        Matches matches = gson.fromJson(json, Matches.class);

        for (Match m : matches._embedded.matches) {
            m.user1 = m._embedded.user1;
            m.user2 = m._embedded.user2;
            m.id = Long.parseLong(m._links.self.href.getFile().split("/")[2]);
        }

        return matches._embedded.matches;
    }

    public static Match parse(String json) {
        Gson gson = new GsonBuilder().create();
        Match m = gson.fromJson(json, Match.class);
        m.user1 = m._embedded.user1;
        m.user2 = m._embedded.user2;

        return m;
    }


    public Match() {}

    /*
     * Parcelable function
     */

    protected Match(Parcel in) {
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };


    private class JsonMatch {
        public String user1;
        public String user2;
    }

    public String format (CherryApplication application) {
        Gson gson = new Gson();
        JsonMatch jm = new JsonMatch();
        jm.user1 = application.host + "/user/" + user1.uuid;
        jm.user2 = application.host + "/user/" + user2.uuid;
        return gson.toJson(jm);
    }

    @Override
    public String toString() {
        return user1.toString() + " --- " + user2.toString();
    }
}