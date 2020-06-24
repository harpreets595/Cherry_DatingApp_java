package com.example.project;

import android.app.Application;
import android.content.Intent;

import com.example.project.models.Match;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.example.project.ui.login.LoginManager;
import com.example.project.ui.login.LoginManagerServer;
import com.example.project.ui.login.LoginManagerStub;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CherryApplication extends Application {

    //sandy: 2c77dafe-1545-432f-b5b1-3a0011cf7036
    //Ian: 7bdba0fe-fe95-4b1c-8247-f2479ee6e380
    //Jim: 97489bce-1c85-4ff2-b457-ba53589d12cc
    public Profile currentUser;
    public String currentUserUuid;

    public static final boolean DEBUG = false;
    private LoginManager loginManager;
    public String host;
    public URL url;

    public List<Match> matches;

    @Override
    public void onCreate() {
        super.onCreate();

        host = "http://10.0.2.2:9999";
        loginManager = DEBUG ? new LoginManagerStub(this) : new LoginManagerServer(this);

        currentUserUuid = "";
        matches = new ArrayList<>();
    }
    public LoginManager getLoginManager() {

        return loginManager;
    }

    public Object getUrlPrefix() {
        return this.host;
    }
}
