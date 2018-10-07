package com.example.capdtalk.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.EventReqeust;
import com.example.capdtalk.Request.NoticeRequest;
import com.example.capdtalk.etc.Event;
import com.example.capdtalk.etc.EventListAdapter;
import com.example.capdtalk.etc.Notice;
import com.example.capdtalk.etc.NoticeListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {

    private ListView noticeListView;
    private ListView eventListView; // activity_main 에 있는 리스트뷰의 id를 가져와 생성?
    private NoticeListAdapter nAdapter;
    private EventListAdapter eAdapter; // 어탭터 생성
    public List<Notice> noticeList;
    public List<Event> eventList;

    String[][] narray;
    String[][] earray;
    int count;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Welcome");


        noticeListView = (ListView) view.findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        nAdapter = new NoticeListAdapter(getActivity().getApplicationContext(),noticeList);
        noticeListView.setAdapter(nAdapter);

        eventListView = (ListView) view.findViewById(R.id.eventListView);
        eventList = new ArrayList<Event>();
        eAdapter = new EventListAdapter(getActivity().getApplicationContext(),eventList);
        eventListView.setAdapter(eAdapter);

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo 리스트 눌렀을 때 간단한 다이얼로그로 볼수만 있게
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //todo 이벤트 눌럿을때 글정보 표현하기
            }
        });

        setNoticeListView();
        setEventListView();
    }



    public void setNoticeListView(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    count = 0;
                    String notice_title,notice_content,notice_name,notice_date;
                    narray = new String[jsonArray.length()][4];
                    nAdapter.notifyDataSetChanged();
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        narray[count][0] = notice_title = object.getString("notice_title");
                        narray[count][1] = notice_content = object.getString("notice_content");
                        narray[count][2] = notice_name = object.getString("notice_name");
                        narray[count][3] = notice_date = object.getString("notice_date");
                        count++;
                    }
                    for(int i=0;i<count;i++){
                        Notice notice  = new Notice(narray[i][0],narray[i][1],narray[i][2],narray[i][3]);
                        noticeList.add(notice);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        NoticeRequest noticeReqeust = new NoticeRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(noticeReqeust);
    }

    public void setEventListView(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    count = 0;
                    String event_title,event_content,event_name,event_date;
                    earray = new String[jsonArray.length()][4];
                    eAdapter.notifyDataSetChanged();
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        earray[count][0] = event_title = object.getString("event_title");
                        earray[count][1] = event_content = object.getString("event_content");
                        earray[count][2] = event_name = object.getString("event_name");
                        earray[count][3] = event_date = object.getString("event_date");
                        count++;
                    }
                    for(int i=0;i<count;i++){
                        Event event  = new Event(earray[i][0],earray[i][1],earray[i][2],earray[i][3]);
                        eventList.add(event);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        EventReqeust eventReqeust = new EventReqeust(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(eventReqeust);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main,container,false);
    }
}
