package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ThoriqJmartDR.model.Account;
import com.ThoriqJmartDR.request.LoginRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{
    private static final Gson gson = new Gson();
    private static final Account loggedAccount = null;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEntryLogin = findViewById(R.id.emailEntryLogin);
        EditText passwordEntryLogin = findViewById(R.id.passwordEntryLogin);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button tvRegisterNow = (Button) findViewById(R.id.registerNow);

        loginButton.setOnClickListener(v -> {
            Response.Listener<String> listener = response -> {
                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                loggedAccount = gson.fromJson();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            };
            Response.ErrorListener errorListener = error -> Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            String email = emailEntryLogin.getText().toString();
            String password = passwordEntryLogin.getText().toString();
            LoginRequest loginRequest = new LoginRequest(email, password, listener, errorListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        });
        tvRegisterNow.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorResponse(VolleyError error) {
    }
    @Override
    public void onResponse(String response) {
    }
}