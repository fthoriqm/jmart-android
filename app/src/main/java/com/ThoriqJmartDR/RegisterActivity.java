package com.ThoriqJmartDR;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ThoriqJmartDR.request.RegisterRequest;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatActivity;

/**
 * RegisterActivity class is used to manage the application page of registering a new account
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */

public class RegisterActivity extends AppCompatActivity {
    EditText nameRegisterEntry;
    EditText emailRegisterEntry;
    EditText passwordRegisterEntry;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameRegisterEntry = findViewById(R.id.nameRegisterEntry);
        emailRegisterEntry = findViewById(R.id.emailRegisterEntry);
        passwordRegisterEntry = findViewById(R.id.passwordRegisterEntry);
        registerButton = findViewById(R.id.registerButton);

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        // What to to if the register button is clicked
        registerButton.setOnClickListener(v -> {
            Response.Listener<String> listener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null) {
                        Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException err){
                    Toast.makeText(RegisterActivity.this, "Register Unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorListener = response -> Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();

            String name = nameRegisterEntry.getText().toString();
            String email = emailRegisterEntry.getText().toString();
            String password = passwordRegisterEntry.getText().toString();

            RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
            requestQueue.add(registerRequest);
        });
    }
}