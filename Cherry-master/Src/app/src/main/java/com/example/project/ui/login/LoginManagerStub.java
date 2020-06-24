package com.example.project.ui.login;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * A login manager stub for the prototype.
 *
 * - Mimics login using hardcoded data.
 * - Stores login information in preference files to keep the user logged next time the app is run.
 */
public class LoginManagerStub implements LoginManager {

    public static final String PREFERENCES_NAME = "login-manager";
    private static Map<String, Tuple<String, String>> fakeUserAccounts;

    static  {
        fakeUserAccounts = new HashMap<>();
        fakeUserAccounts.put("ian", Tuple.tuple("emacs", "7bdba0fe-fe95-4b1c-8247-f2479ee6e380"));
        fakeUserAccounts.put("jim", Tuple.tuple("vim", "97489bce-1c85-4ff2-b457-ba53589d12cc"));
        fakeUserAccounts.put("sandy", Tuple.tuple("perl","2c77dafe-1545-432f-b5b1-3a0011cf7036"));
    }

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
    public LoginManagerStub(Context context) {
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

        // use hard-coded data to mimic login.
        if(fakeUserAccounts.containsKey(username)) {
            String expected = hashPassword(fakeUserAccounts.get(username).getFirst());
            if(hashPassword(password).equals(expected)) {
                isLoggedIn = true;
                uuid = fakeUserAccounts.get(username).getSecond();

                // shared preferences
                context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString("username", username)
                    .putString("uuid", uuid)
                    .apply();

                // login event
                if(onLoginListener != null)
                    onLoginListener.onLogin();
            }
            else {
                isLoggedIn = false;
                uuid = null; // just in case

                // login error event
                if(onLoginListener != null)
                    onLoginListener.onLoginError("Incorrect password.");
            }
        }
        else {
            // login error event.
            if(onLoginListener != null)
                onLoginListener.onLoginError("Unknown user.");
        }
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
