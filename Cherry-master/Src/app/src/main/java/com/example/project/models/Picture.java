package com.example.project.models;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.project.CherryApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

public class Picture {

    private class Pictures {
        private class EmbeddedArray {
            public List<Picture> pictures;
        }

        public EmbeddedArray _embedded;
    }

    public class Embbeded {
        public Profile user;
    }

    private class JsonMessage {
        private long id;
        private String user;
        private String picture;
        private Date uploadTime;
    }

    private long id;
    private Profile user;
    private String picture;
    private Date uploadTime;
    private Embbeded _embedded;


    public Picture(Profile user, Bitmap pic) {
        this.user = user;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        this.picture = encoded;
        uploadTime = new Date();
    }

    public static List<Picture> parseArray(String json) {
        Gson gson = new GsonBuilder().create();
        Pictures pictures = gson.fromJson(json, Pictures.class);

        for (Picture p : pictures._embedded.pictures) {
            p.user = p._embedded.user;
        }

        return pictures._embedded.pictures;
    }



    public String getUsername() {
        return user.username;
    }

    public Profile getUser() {
        return user;
    }

    public void setUser(Profile user) {
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getId() {
        return id;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String format(CherryApplication application) {
        
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
        JsonMessage pic = new JsonMessage();
        pic.user = application.host + "/user/" + user.uuid;
        pic.id = id;
        pic.picture = picture;
        pic.uploadTime = uploadTime;
        return gson.toJson(pic);
    }
}
