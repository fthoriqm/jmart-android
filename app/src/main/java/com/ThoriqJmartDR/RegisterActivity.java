package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText nameEntry = findViewById(R.id.nameEntry);
        EditText emailEntry = findViewById(R.id.emailEntry);
        EditText passwordEntry = findViewById(R.id.passwordEntry);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            Response.Listener<String> listener = response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null) {
                        Toast.makeText(RegisterActivity.this, "Register Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                } catch (JSONException err){
                    Toast.makeText(RegisterActivity.this, err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            Response.ErrorListener errorListener = response -> Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
            String name = nameEntry.getText().toString();
            String email = emailEntry.getText().toString();
            String password = passwordEntry.getText().toString();
            RegisterRequest registerRequest = new RegisterRequest(name, email, password, listener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(registerRequest);
        });
    }
}