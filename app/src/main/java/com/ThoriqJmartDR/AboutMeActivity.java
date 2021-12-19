package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ThoriqJmartDR.request.RegisterStoreRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

/**
 * AboutMeActivity class is used to manage the activities that are happening in the Profile Page
 * which includes top up transaction and registering store
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */
public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView tv_userName = findViewById(R.id.tv_userName);
        TextView tv_userEmail = findViewById(R.id.tv_userEmail);
        TextView tv_userBalance = findViewById(R.id.tv_userBalance);
        EditText et_topUpAmount = findViewById(R.id.et_topUpAmount);
        Button btnTopUp = findViewById(R.id.btnTopUp);

        tv_userName.setText(LoginActivity.getLoggedAccount().name);
        tv_userEmail.setText(LoginActivity.getLoggedAccount().email);
        tv_userBalance.setText(String.valueOf(LoginActivity.getLoggedAccount().balance));

        RequestQueue queue = Volley.newRequestQueue(this);

        // What to do if the top up button is clicked
        btnTopUp.setOnClickListener(view -> {
            String balance = et_topUpAmount.getText().toString();
            String URL = "http://10.0.2.2:5050/account/" + (LoginActivity.getLoggedAccount().id) + "/topUp";
            StringRequest topUpRequest = new StringRequest(Request.Method.POST, URL, response -> {
                try {
                    LoginActivity.reloadLoggedAccount(response);
                    Toast.makeText(getApplicationContext(), "Top Up Successful! for Account " + LoginActivity.getLoggedAccount().name, Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Top Up unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }, error -> Toast.makeText(getApplicationContext(), "Top Up Failed!", Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("balance", balance);
                    return params;
                }
            };
            queue.add(topUpRequest);
        });

        // Register new Store Mechanism
        CardView cv_registerStore = findViewById(R.id.cv_registerStore);
        CardView cv_storeExists = findViewById(R.id.cv_storeExists);

        Button btnRegisterStore = findViewById(R.id.btnRegisterStore);
        EditText et_storeName = findViewById(R.id.et_storeName);
        EditText et_storeAddress = findViewById(R.id.et_storeAddress);
        EditText et_storePhoneNumber = findViewById(R.id.et_storePhoneNumber);
        Button btnRegisterStoreConfirm = findViewById(R.id.btnRegisterStoreConfirm);
        Button btnRegisterStoreCancel = findViewById(R.id.btnRegisterStoreCancel);

        if (LoginActivity.getLoggedAccount().store != null) {
            btnRegisterStore.setVisibility(View.GONE);
            cv_storeExists.setVisibility(View.VISIBLE);

            //Show the User's Store Information
            TextView tv_storeNameF = findViewById(R.id.tv_storeNameF);
            TextView tv_storeAddressF = findViewById(R.id.tv_storeAddressF);
            TextView tv_storePhoneNumberF = findViewById(R.id.tv_storePhoneNumberF);
            tv_storeNameF.setText(LoginActivity.getLoggedAccount().store.name);
            tv_storeAddressF.setText(LoginActivity.getLoggedAccount().store.address);
            tv_storePhoneNumberF.setText(LoginActivity.getLoggedAccount().store.phoneNumber);
        }

        btnRegisterStore.setOnClickListener(v -> {
            btnRegisterStore.setVisibility(View.INVISIBLE);
            cv_registerStore.setVisibility(View.VISIBLE);
        });
        btnRegisterStoreCancel.setOnClickListener(v -> {
            btnRegisterStore.setVisibility(View.VISIBLE);
            cv_registerStore.setVisibility(View.INVISIBLE);
        });

        btnRegisterStoreConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_storeName.getText().toString();
                String address = et_storeAddress.getText().toString();
                String phoneNumber = et_storePhoneNumber.getText().toString();

                if (name.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Store Entries Cannot be Empty!", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    int number = Integer.parseInt(phoneNumber);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Phone Number Must be Digit", Toast.LENGTH_LONG).show();
                    return;
                }

                RegisterStoreRequest registerStoreRequest = new RegisterStoreRequest(LoginActivity.getLoggedAccount().id, name, address, phoneNumber, response -> {
                    try {
                        LoginActivity.insertLoggedAccountStore(response);
                        Toast.makeText(getApplicationContext(), "Register Store Successful", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Register Store Unsuccessful!", Toast.LENGTH_LONG).show();
                    }
                }, error -> Toast.makeText(getApplicationContext(), "Register Store Failed!", Toast.LENGTH_LONG).show());

                queue.add(registerStoreRequest);
            }
        });
    }
}
