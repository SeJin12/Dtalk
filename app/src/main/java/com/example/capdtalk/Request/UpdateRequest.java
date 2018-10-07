package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/UpdateResponse.php";
    private Map<String,String> parameters;

    public UpdateRequest(String table,String set,String where, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("table",table);
        parameters.put("set",set);
        parameters.put("where",where);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
