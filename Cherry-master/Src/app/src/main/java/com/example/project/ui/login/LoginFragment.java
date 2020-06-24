package com.example.project.ui.login;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.MainActivity;
import com.example.project.R;



/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_login, container, false);

        loginButton = root.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        usernameEditText = root.findViewById(R.id.username_EditText);
        passwordEditText = root.findViewById(R.id.password_EditText);

        return root;
    }

    private void login() {
        LoginManager loginManager = ((CherryApplication) getActivity().getApplication()).getLoginManager();
        loginManager.setOnLoginListener(new LoginManager.OnLoginListener() {
            @Override
            public void onLogin() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().startActivityForResult(intent, 1);
            }

            @Override
            public void onLoginError(String message) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Problem with login.")
                        .setMessage(message)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
        loginManager.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
    }
}
