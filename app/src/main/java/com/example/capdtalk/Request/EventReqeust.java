package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EventReqeust extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/EventList.php";
    private Map<String,String> parameters;

    public EventReqeust(Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }

}
