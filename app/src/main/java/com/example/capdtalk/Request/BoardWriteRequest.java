package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardWriteRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/BoardWriteResponse.php";
    private Map<String,String> parameters;

    public BoardWriteRequest(String title, String content, String name, String date, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("title",title);
        parameters.put("content",content);
        parameters.put("name",name);
        parameters.put("date",date);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
