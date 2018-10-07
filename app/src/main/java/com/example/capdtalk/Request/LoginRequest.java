package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/MemberLogin.php";
    private Map<String,String> parameters;

    public LoginRequest(String m_id, String m_password, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("m_id",m_id);
        parameters.put("m_password",m_password);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
