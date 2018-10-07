package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/*********************************************************

 * AddRequest

 * 만든이 - 안재규

 * 목적 - CourseAdd.php 에 파라미터(sc_userID, sc_courseID)값 전달=>Schedule 테이블에 저장

 * 사용법 - CourseListAdapter 에서 호출

 * 작성일 - 2018-04-04

 **********************************************************/

public class CourseAddRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/CourseAdd.php";
    private Map<String,String> parameters;

    public CourseAddRequest(String m_id, String courseID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("sc_userID",m_id);
        parameters.put("sc_courseID", courseID);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }

}
