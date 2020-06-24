package com.example.project.ui.login;

/**
 * Login Manager
 *
 * - API definition for login.
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public interface LoginManager {

    /**
     * Listener for login events.
     */
    interface OnLoginListener {
        void onLogin();
        void onLoginError(String message);
    }

    /**
     * Listener for logout events.
     */
    interface OnLogoutListener {
        void onLogout();
    }

    /**
     * Attempt a login.
     * @param username username for login.
     * @param password username's password.
     */
    void login(String username, String password);

    /**
     * Logout.
     */
    void logout();

    /**
     * Detemine if a user is logged in.
     * @return
     */
    boolean isLoggedIn();

    /**
     * Get the username.
     * @return
     */
    String getUsername();

    /**
     * Get the user's UUID.
     * @return
     */
    String getUuid();

    /**
     * Register a login handler.
     * @param onLoginListener
     */
    void setOnLoginListener(OnLoginListener onLoginListener);

    /**
     * Register a logout handler.
     * @param onLogoutListener
     */
    void setOnLogoutListener(OnLogoutListener onLogoutListener);
}
