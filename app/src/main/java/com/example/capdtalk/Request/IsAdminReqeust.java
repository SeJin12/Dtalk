package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IsAdminReqeust extends StringRequest{

    final static private String URL = "http://tpwls8122.cafe24.com/IsAdminResponse.php";
    private Map<String,String> parameters;

    public IsAdminReqeust(String m_id, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("m_id",m_id);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

