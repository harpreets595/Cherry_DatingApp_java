package com.example.project.ui.accounts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.project.CherryApplication;
import com.example.project.models.Picture;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import com.example.project.R;

import static com.example.project.ui.accounts.MyProfileActivityFragment.REQUEST_IMAGE_CAPTURE;

public class MyProfileActivity extends AppCompatActivity {

    CherryApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        application = (CherryApplication) getApplication();

        FloatingActionButton fab = findViewById(R.id.newImageFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
