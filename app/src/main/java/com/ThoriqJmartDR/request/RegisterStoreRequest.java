package com.ThoriqJmartDR.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RegisterStoreRequest class is used to integrate the android studio into the postman rest api
 * in terms of registering a new store based on the account's id
 *
 * @author Fadlurrahman Thoriq Musyaffa
 */
public class RegisterStoreRequest extends StringRequest{
    private final Map<String, String> params;

    public RegisterStoreRequest(int id, String name, String address, String phoneNumber, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, "http://10.0.2.2:5050/account/" + id + "/registerStore", listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("address", address);
        params.put("phoneNumber", phoneNumber);
    }

    public Map<String, String> getParams(){
        return params;
    }
}