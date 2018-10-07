package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/InsertResponse.php";
    private Map<String,String> parameters;

    public InsertRequest(String table,String value, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("table",table);
        parameters.put("value",value);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
