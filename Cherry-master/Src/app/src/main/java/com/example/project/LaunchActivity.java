package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.project.models.Match;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;

/*
 * This activity decides which activity to launch:
 *    Signup/Login or Main activity
 */
public class LaunchActivity extends AppCompatActivity {

    private CherryApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        application = (CherryApplication) getApplication();
        if (application.currentUserUuid == null || application.currentUserUuid == "") {
            Intent intent = new Intent(this, StartupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        } else {
            HttpRequest req = new HttpRequest(application.host + "/user/" + application.currentUserUuid, HttpRequest.Method.GET);
            HttpRequestTask task = new HttpRequestTask();
            task.setOnErrorListener(error -> {
                Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
            });
            task.setOnResponseListener(response -> {
                HttpRequest match1Req = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches1", HttpRequest.Method.GET);
                HttpRequestTask match1Task = new HttpRequestTask();
                match1Task.setOnResponseListener(matchResponse -> {
                    application.matches.addAll(Match.parseArray(matchResponse.getResponseBody()));

                    HttpRequest match2Req = new HttpRequest(application.host + "/user/" + application.currentUserUuid + "/matches2", HttpRequest.Method.GET);
                    HttpRequestTask match2Task = new HttpRequestTask();
                    match2Task.setOnResponseListener(match2Response -> {
                        application.matches.addAll(Match.parseArray(match2Response.getResponseBody()));

                        application.currentUser = Profile.parse(response.getResponseBody());
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    });
                    match2Task.execute(match2Req);
                });
                match1Task.execute(match1Req);
            });
            task.execute(req);
        }
    }
}
