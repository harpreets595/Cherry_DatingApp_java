package com.example.project.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.GetChars;
import android.widget.Toast;

import com.example.project.CherryApplication;
import com.example.project.models.Profile;
import com.example.project.networking.HttpRequest;
import com.example.project.networking.HttpRequestTask;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



import com.example.project.networking.HttpResponse;
import com.example.project.networking.OnErrorListener;
import com.example.project.networking.OnResponseListener;

/**
 * A login manager for the implementation.
 *
 * - perform server login
 * - Stores login information in preference files to keep the user logged next time the app is run.
 */
public class LoginManagerServer implements LoginManager {

    public static final String PREFERENCES_NAME = "login-manager";
    private final Context context;

    private OnLoginListener onLoginListener;
    private OnLogoutListener onLogoutListener;
    private String uuid;
    private String username;
    private boolean isLoggedIn;

    /**
     * Construct the login manager.
     * @param context
     */
    public LoginManagerServer(Context context) {
        this.context = context;

        // check the preferences file in case of previous login.
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if(preferences.contains("username")) {
            isLoggedIn = true;
            username = preferences.getString("username", "");
            uuid = preferences.getString("uuid", "");
        }
        else
            isLoggedIn = false;
    }


    @Override
    public void login(String username, String password) {
        this.username = username;

        CherryApplication application = (CherryApplication) context.getApplicationContext();
        String url = String.format("%s/userLogin?username=%s&password=%s", application.getUrlPrefix(), username, hashPassword(password));

        HttpRequest request = new HttpRequest(url, HttpRequest.Method.GET);
        HttpRequestTask task = new HttpRequestTask()
                .setOnResponseListener(new OnResponseListener<HttpResponse>() {
                    @Override
                    public void onResponse(HttpResponse data) {

                        if(data.getCode() == 200) {

                            isLoggedIn = true;
                            String path = data.getHeaders().get("Path").get(0);
                            uuid = path.replace("/user/", "");
                            application.currentUserUuid = uuid;



                            // shared preferences
                            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("username", username)
                                    .putString("uuid", uuid)
                                    .apply();

                            HttpRequest userRequest = new HttpRequest(application.host + "/user/" + uuid, HttpRequest.Method.GET);
                            HttpRequestTask userRequestTask = new HttpRequestTask();
                            userRequestTask.setOnResponseListener(userResponse -> {
                                application.currentUser = Profile.parse(userResponse.getResponseBody());
                                // login event
                                if (onLoginListener != null)
                                    onLoginListener.onLogin();
                            });
                            userRequestTask.execute(userRequest);


                        }
                        else {
                            isLoggedIn = false;
                            uuid = null; // just in case

                            // login error event
                            if(onLoginListener != null)
                                onLoginListener.onLoginError("Login error: either username doesn't exist of password is incorrect.");
                        }
                    }
                })
                .setOnErrorListener(new OnErrorListener() {
                    @Override
                    public void onError(Exception error) {

                        isLoggedIn = false;
                        uuid = null; // just in case

                        // login error event
                        if(onLoginListener != null)
                            onLoginListener.onLoginError(error.getMessage());
                    }
                });
        task.execute(request);

    }

    @Override
    public void logout() {

        // wipe the preferences file.
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove("username")
                .remove("uuid")
                .apply();

        isLoggedIn = false;

        // logout event
        if(onLogoutListener != null)
            onLogoutListener.onLogout();
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public String getUsername() {
        if(!isLoggedIn)
            throw new IllegalStateException("Not logged in.");
        return username;
    }

    @Override
    public String getUuid() {
        if(!isLoggedIn)
            throw new IllegalStateException("Not logged in.");
        return uuid;
    }

    @Override
    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    @Override
    public void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        this.onLogoutListener = onLogoutListener;
    }

    // Used to hash passwords.
    private String hashPassword(String password) {

        // http://stackoverflow.com/questions/3103652/hash-string-via-sha-256-in-java
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return String.format("%040x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e1) {
            // should not happen
        }
        return null;
    }
}
