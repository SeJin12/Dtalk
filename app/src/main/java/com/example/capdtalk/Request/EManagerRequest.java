package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EManagerRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/EManagerResponse.php";
    private Map<String,String> parameters;

    public EManagerRequest(String title, String content, String name, String date, String update_title, String update_content, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("title",title);
        parameters.put("content",content);
        parameters.put("name",name);
        parameters.put("date",date);
        parameters.put("u_title",update_title);
        parameters.put("u_content",update_content);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
