package com.ThoriqJmartDR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.ThoriqJmartDR.model.Product;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity class is used to manage tha activities occurred in the main page which involves searching a product
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */
public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    private static final Gson gson = new Gson();

    int page;
    MyRecyclerViewAdapter adapter;

    //CardView Variables
    private CardView cv_product;
    private CardView cv_filter;
    private EditText et_productName;
    private EditText et_lowestPrice;
    private EditText et_highestPrice;
    private CheckBox cb_new;
    private CheckBox cb_used;
    private Spinner spinner_filterCategory;
    private Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout mainTabLayout = findViewById(R.id.mainTabLayout);
        cv_product = findViewById(R.id.cv_product);
        cv_filter = findViewById(R.id.cv_filter);

        RequestQueue queue = Volley.newRequestQueue(this);

        /*
          This two block of code is used to manage the card view based on the user's selection
          either tab products and tab filter
         */
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        cv_product.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        cv_filter.setVisibility(View.INVISIBLE);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab){}
        });

        /*
        This block of code is used to get the product list from product tab into the xml display
         */
        Button btnPrev = findViewById(R.id.btnPrev);
        Button btnNext = findViewById(R.id.btnNext);
        Button btnGo = findViewById(R.id.btnGo);
        EditText et_page = findViewById(R.id.et_page);
        List<Product> productNames = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.rv_Products);
        adapter = new MyRecyclerViewAdapter(this, productNames);

        page = 0;
        et_page.setText(Integer.toString(page));

        fetchProduct(productNames, page, queue, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // This block of code is used to control the pagination of the main page
        btnPrev.setOnClickListener(v -> {
            if(page > 0){
                page--;
                et_page.setText(Integer.toString(page));
            }
        });
        btnNext.setOnClickListener(v -> {
            page++;
            et_page.setText(Integer.toString(page));
        });
        btnGo.setOnClickListener(v -> {
            page = Integer.parseInt(et_page.getText().toString());
            fetchProduct(productNames, page, queue, true);
        });

        et_productName = findViewById(R.id.et_productName);
        et_lowestPrice = findViewById(R.id.et_lowestPrice);
        et_highestPrice = findViewById(R.id.et_highestPrice);
        spinner_filterCategory = findViewById(R.id.spinner_filterCategory);
        cb_new = findViewById(R.id.cb_new);
        cb_used = findViewById(R.id.cb_used);
        btnApply = findViewById(R.id.btnApply);

        String checkCondition = null;
        if(cb_new.isChecked()){
            checkCondition = "true";
        }
        else if(cb_used.isChecked()){
            checkCondition = "false";
        }
//        else {
//            checkCondition = "";
//        }

        // What to do if the apply button in filter is clicked
//        final String check = checkCondition;
        btnApply.setOnClickListener(v -> {
            String productName = et_productName.getText().toString();
            String lowestPrice= et_lowestPrice.getText().toString();
            String highestPrice = et_highestPrice.getText().toString();
            String category = spinner_filterCategory.getSelectedItem().toString();
            StringRequest filterRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:5050/product/getFiltered?pageSize=10&accountId="+LoginActivity.getLoggedAccount().id+"&search="+productName+"&minPrice="+lowestPrice+"&maxPrice="+highestPrice+"&category="+category/*+"&conditionUsed="+check*/, response -> {
                JsonReader reader = new JsonReader(new StringReader(response));
                try {
                    productNames.clear();
                    reader.beginArray();
                    while(reader.hasNext()){
                        productNames.add(gson.fromJson(reader, Product.class));
                    }
                    adapter.refresh(productNames);
                    reader.close();
                    Toast.makeText(getApplicationContext(), "Filter Applied", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Filter product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
                }
            }, error -> Toast.makeText(getApplicationContext(), "Filtering error, check again.", Toast.LENGTH_LONG).show());
            queue.add(filterRequest);
        });

        // What to do if the clear button is clicked
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Clear Filter", Toast.LENGTH_SHORT).show();
            et_productName.setText("");
            et_lowestPrice.setText("");
            et_highestPrice.setText("");
            cb_new.setChecked(false);
            cb_used.setChecked(false);
            spinner_filterCategory.setSelection(0);
        });
    }

    /**
     * This method is used to get the list of product store in the json file into the xml list display
     *
     * @param productNames is the list that is going to be used to store list of product from json file
     * @param page is the argument that navigate the page of list view
     * @param queue is the queue that is used to arrange the current page requests
     * @param refreshAdapter is the true and false condition whether the method should refresh or not
     */
    public void fetchProduct(List<Product> productNames, int page, RequestQueue queue, boolean refreshAdapter){
        StringRequest fetchProductsRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:5050/product/page?page="+page+"&pageSize=10", response -> {
            JsonReader reader = new JsonReader(new StringReader(response));
            try {
                productNames.clear();
                reader.beginArray();
                while(reader.hasNext()){
                    productNames.add(gson.fromJson(reader, Product.class));
                }
                if(refreshAdapter){
                    adapter.refresh(productNames);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Fetch product unsuccessful, error occurred", Toast.LENGTH_LONG).show();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Fetching Product Unsuccessful!", Toast.LENGTH_LONG).show());
        queue.add(fetchProductsRequest);
    }

    /**
     * This method is used to manage the responds when one of the item get clicked
     *
     * @param view
     * @param position states the position of the item relative to the product list
     */
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getApplicationContext(), "Product is Clicked", Toast.LENGTH_LONG).show();
    }

    /**
     * this method is used to hide or don't hide the add products menu based on the user's store possession
     *
     * @param menu
     * @return boolean which states the add product icon should be hide or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * This method is used to evaluate which page the user wanted to go after visiting the main page
     *
     * @param item use to store the evaluated item, in this case it will be menu_main
     * @return boolean which states the options of toolbar are selected or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
//                Search feature is not done yet, we need to use the filterable syntax
                return true;
            case R.id.menu_add:
                startActivity(new Intent(this, CreateProductActivity.class));
                return true;
            case R.id.menu_aboutme:
                startActivity(new Intent(MainActivity.this, AboutMeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}