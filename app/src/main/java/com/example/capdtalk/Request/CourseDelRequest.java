package com.example.capdtalk.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/*********************************************************

 * AddRequest

 * 만든이 - 안재규

 * 목적 - CourseDelete.php 에 파라미터(sc_userID, sc_courseID)값 전달=>Schedule 테이블에서 삭제

 * 사용법 - MyCourseListAdapter 에서 호출

 * 작성일 - 2018-05-22

 **********************************************************/

public class CourseDelRequest extends StringRequest {

    final static private String URL = "http://tpwls8122.cafe24.com/CourseDelete.php";
    private Map<String,String> parameters;

    public CourseDelRequest(String m_id, String courseID, Response.Listener<String> listener) {
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
