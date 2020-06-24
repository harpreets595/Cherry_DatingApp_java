package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.ui.login.LoginManager;

public class Login extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = this.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        usernameEditText = this.findViewById(R.id.username_EditText);
        passwordEditText = this.findViewById(R.id.password_EditText);


    }
    private void login() {
        LoginManager loginManager = ((CherryApplication) this.getApplication()).getLoginManager();
        loginManager.setOnLoginListener(new LoginManager.OnLoginListener() {
            @Override
            public void onLogin() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //TODO
                CherryApplication app = (CherryApplication) getApplication();

                startActivityForResult(intent, 1);
            }

            @Override
            public void onLoginError(String message) {
                Toast.makeText(getApplicationContext(), "The username and password entered do not match.", Toast.LENGTH_SHORT).show();
            }
        });
        loginManager.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }
}


