package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class BoardCommentRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/BoardCommentResponse.php";
    private Map<String,String> parameters;

    public BoardCommentRequest(String type,String wname,String wdate,String ccontent,String cname,String cdate, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("type",type);
        parameters.put("wname",wname);
        parameters.put("wdate",wdate);
        parameters.put("ccontent",ccontent);
        parameters.put("cname",cname);
        parameters.put("cdate",cdate);
    }

    @Override
    public Map<String, String> getParams() {
        return parameters;
    }
}
