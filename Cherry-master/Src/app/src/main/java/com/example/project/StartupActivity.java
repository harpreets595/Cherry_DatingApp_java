package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.project.ui.accounts.ProfileCreationActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //Signup
        Button button_signup = this.findViewById(R.id.button_signup);
        button_signup.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), ProfileCreationActivity.class);
            startActivity(intent);
        });

        //Login
        Button button_login = this.findViewById(R.id.button_login);
        button_login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });
    }
}
