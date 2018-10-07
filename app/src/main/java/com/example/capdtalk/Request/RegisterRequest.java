package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest  extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/MemberRegister.php";
    private Map<String,String> parameters;

    public RegisterRequest(String m_id,String m_password,String m_major,String m_group,String m_name,String m_phone,Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("m_id",m_id);
        parameters.put("m_password",m_password);
        parameters.put("m_major",m_major);
        parameters.put("m_group",m_group);
        parameters.put("m_name",m_name);
        parameters.put("m_phone",m_phone);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }

}
