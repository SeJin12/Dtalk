package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyInfoUpdateRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/MyInfoUpdateResponse.php";
    private Map<String,String> parameters;

    public MyInfoUpdateRequest(String id,String password,String major,String group,String name,String phone, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("id",id);
        parameters.put("password",password);
        parameters.put("major",major);
        parameters.put("group",group);
        parameters.put("name",name);
        parameters.put("phone",phone);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}

