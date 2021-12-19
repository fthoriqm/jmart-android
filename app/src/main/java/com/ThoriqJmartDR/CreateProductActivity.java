package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.ThoriqJmartDR.request.CreateProductRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * CreateProductActivity Class manages how the user that posses a store to make a new product for sale
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */
public class CreateProductActivity extends AppCompatActivity {
    private EditText etCreateProductName;
    private EditText etCreateProductPrice;
    private EditText etCreateProductWeight;
    private EditText etCreateProductDiscount;
    private boolean newProductCondition = true;
    private Spinner spinner_createCategory, spinner_createShipment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        RequestQueue queue = Volley.newRequestQueue(this);

        etCreateProductName = findViewById(R.id.etCreateProductName);
        etCreateProductPrice = findViewById(R.id.etCreateProductPrice);
        etCreateProductWeight = findViewById(R.id.etCreateProductWeight);
        etCreateProductDiscount = findViewById(R.id.etCreateProductDiscount);
        spinner_createCategory = findViewById(R.id.spinner_createCategory);
        spinner_createShipment = findViewById(R.id.spinner_createShipment);
        RadioGroup radio_conditionList = findViewById(R.id.radio_conditionList);
        Button btnCreateProduct = findViewById(R.id.btnCreateProduct);
        Button btnCancelProduct = findViewById(R.id.btnCancelProduct);

        // This part manage the selection of radio button new and used
        radio_conditionList.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.radio_conditionNew:
                    newProductCondition = true;
                    break;
                case R.id.radio_conditionUsed:
                    newProductCondition = false;
                    break;
            }
        });

        // This block of code describes what to do when the create product button is clicked
        btnCreateProduct.setOnClickListener(v -> {
            String accountId = String.valueOf(LoginActivity.getLoggedAccount().id);
            String productName = etCreateProductName.getText().toString();
            String productPrice = etCreateProductPrice.getText().toString();
            String productWeight = etCreateProductWeight.getText().toString();
            String productDiscount = etCreateProductDiscount.getText().toString();
            String productCategory = spinner_createCategory.getSelectedItem().toString();
            String productShipment = spinner_createShipment.getSelectedItem().toString();

            // This code block shows the process of converting enum values into byte flags
            switch (productShipment) {
                case "INSTANT":
                    productShipment = String.valueOf(0);
                    break;
                case "SAME DAY":
                    productShipment = String.valueOf(1);
                    break;
                case "NEXT DAY":
                    productShipment = String.valueOf(2);
                    break;
                case "REGULAR":
                    productShipment = String.valueOf(3);
                    break;
                case "KARGO":
                    productShipment = String.valueOf(4);
                    break;
            }
            System.out.println(productCategory + "  " + productShipment);
            CreateProductRequest createProductRequest = new CreateProductRequest(accountId, productName, productWeight,
                    String.valueOf(newProductCondition), productPrice, productDiscount, productCategory, productShipment,
                    response -> {
                        try {
                            Toast.makeText(getApplicationContext(), "Create product successful", Toast.LENGTH_LONG).show();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Create Product Unsuccessful! Error Occured", Toast.LENGTH_LONG).show();
                        }
                    }, error -> Toast.makeText(getApplicationContext(), "Create Product Unsuccessful!", Toast.LENGTH_LONG).show());
            queue.add(createProductRequest);
        });
        btnCancelProduct.setOnClickListener(v -> finish());
    }
}