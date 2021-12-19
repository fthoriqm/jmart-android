package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import com.ThoriqJmartDR.model.Account;
import com.ThoriqJmartDR.model.Store;
import com.ThoriqJmartDR.request.LoginRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * LoginActivity class is used to manage the application page of logging into an available account
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */
public class LoginActivity extends AppCompatActivity{
    private static final Gson gson = new Gson();
    private static Account loggedAccount = null;

    /**
     * This method is used to get the logged account
     *
     * @return Account which defines the account that successfully logged in
     */
    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailLoginEntry = findViewById(R.id.emailLoginEntry);
        EditText passwordLoginEntry = findViewById(R.id.passwordLoginEntry);
        Button loginButton = findViewById(R.id.loginButton);
        TextView textRegisterNow = findViewById(R.id.textRegisterNow);

        // For Developing Purposes
        emailLoginEntry.setText("fadlurrahman.thoriq@gmail.com");
        passwordLoginEntry.setText("Hello123*");

        RequestQueue queue = Volley.newRequestQueue(this);

        // What to do if the login button is clicked
        loginButton.setOnClickListener(view -> {
            String email = emailLoginEntry.getText().toString();
            String password = passwordLoginEntry.getText().toString();
            LoginRequest loginRequest = new LoginRequest(email, password, response -> {
                try {
                    loggedAccount = gson.fromJson(response, Account.class);
                    Toast.makeText(getApplicationContext(), "Login Successful, Hello " + loggedAccount.name, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error! Login Unsuccessful", Toast.LENGTH_LONG).show();
                }
            }, error -> Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_LONG).show());
            queue.add(loginRequest);
        });

        // What to do if the register now text is clicked
        textRegisterNow.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
    }

    /**
     * This method is used to reload the account from json file
     *
     * @param response is used to reload the account based on certain parameter
     */
    public static void reloadLoggedAccount(String response){
        loggedAccount = gson.fromJson(response, Account.class);
    }

    /**
     * This method is used to insert the store data into a specific account
     *
     * @param response is used to insert into the parameter specified account
     */
    public static void insertLoggedAccountStore(String response){
        loggedAccount.store = gson.fromJson(response, Store.class);
    }
}
