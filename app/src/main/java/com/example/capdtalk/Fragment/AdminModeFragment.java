package com.example.capdtalk.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.AdminActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.EventReqeust;
import com.example.capdtalk.Request.InsertRequest;
import com.example.capdtalk.Request.NoticeRequest;
import com.example.capdtalk.Request.UpdateRequest;
import com.example.capdtalk.etc.Event;
import com.example.capdtalk.etc.EventListAdapter;
import com.example.capdtalk.etc.EventManagerDialog;
import com.example.capdtalk.etc.NEManagerDialog;
import com.example.capdtalk.etc.Notice;
import com.example.capdtalk.etc.NoticeListAdapter;
import com.example.capdtalk.etc.NoticeManagerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminModeFragment extends Fragment {

    private ListView noticeListView;
    private ListView eventListView; // activity_main 에 있는 리스트뷰의 id를 가져와 생성?
    private NoticeListAdapter nAdapter;
    private EventListAdapter eAdapter; // 어탭터 생성
    public List<Notice> noticeList;
    public List<Event> eventList;
    private NoticeManagerDialog noticeManagerDialog;
    private EventManagerDialog eventManagerDialog;
    private NEManagerDialog neManagerDialog;

    String[][] narray;
    String[][] earray;
    int count;

    EditText ad_insert_table, ad_insert_value, ad_update_table, ad_update_set, ad_update_where, ad_delete_table, ad_delete_where;
    Button ad_insert_btn, ad_update_btn, ad_delete_btn;
    Button fa_noticebtn,fa_eventbtn,fa_sqlbtn;
    LinearLayout fa_linearNotice,fa_linearEvent,fa_linearSql;
    TextView fa_noticeaddbtn, fa_eventaddbtn;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("관리자 모드");

        ad_insert_table = (EditText) view.findViewById(R.id.ad_insert_table); ad_insert_value = (EditText) view.findViewById(R.id.ad_insert_value); ad_insert_btn = (Button) view.findViewById(R.id.ad_insert_btn);
        ad_update_table = (EditText) view.findViewById(R.id.ad_update_table); ad_update_set = (EditText) view.findViewById(R.id.ad_update_set); ad_update_where = (EditText) view.findViewById(R.id.ad_update_where); ad_update_btn = (Button) view.findViewById(R.id.ad_update_btn);
        ad_delete_table = (EditText) view.findViewById(R.id.ad_delete_table); ad_delete_where = (EditText) view.findViewById(R.id.ad_delete_where); ad_delete_btn = (Button) view.findViewById(R.id.ad_delete_btn);

        fa_noticebtn = view.findViewById(R.id.fa_noticebtn); fa_eventbtn = view.findViewById(R.id.fa_eventbtn); fa_sqlbtn = view.findViewById(R.id.fa_sqlbtn);
        fa_linearNotice = view.findViewById(R.id.fa_linearNotice); fa_linearEvent = view.findViewById(R.id.fa_linearEvent); fa_linearSql = view.findViewById(R.id.fa_linearSql);

        fa_noticeaddbtn = view.findViewById(R.id.fa_noticeaddbtn);  fa_eventaddbtn = view.findViewById(R.id.fa_eventaddbtn);

        fa_noticeaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neManagerDialog = new NEManagerDialog(getActivity(),"Notice");
                neManagerDialog.show();
            }
        });

        fa_eventaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neManagerDialog = new NEManagerDialog(getActivity(),"Event");
                neManagerDialog.show();
            }
        });

        noticeListView = (ListView) view.findViewById(R.id.admin_noticeListView);
        noticeList = new ArrayList<Notice>();
        nAdapter = new NoticeListAdapter(getActivity().getApplicationContext(),noticeList);
        noticeListView.setAdapter(nAdapter);

        eventListView = (ListView) view.findViewById(R.id.admin_eventListView);
        eventList = new ArrayList<Event>();
        eAdapter = new EventListAdapter(getActivity().getApplicationContext(),eventList);
        eventListView.setAdapter(eAdapter);

        fa_noticebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa_linearNotice.setVisibility(View.VISIBLE);
                fa_linearEvent.setVisibility(View.GONE);
                fa_linearSql.setVisibility(View.GONE);
            }
        });

        fa_eventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa_linearNotice.setVisibility(View.GONE);
                fa_linearEvent.setVisibility(View.VISIBLE);
                fa_linearSql.setVisibility(View.GONE);
            }
        });

        fa_sqlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fa_linearNotice.setVisibility(View.GONE);
                fa_linearEvent.setVisibility(View.GONE);
                fa_linearSql.setVisibility(View.VISIBLE);
            }
        });

        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title,content,name,date;
                title = noticeList.get(position).getTitle();
                content = noticeList.get(position).getContent();
                name = noticeList.get(position).getName();
                date = noticeList.get(position).getDate();

                noticeManagerDialog = new NoticeManagerDialog(getActivity(),title,content,name,date);
                noticeManagerDialog.show();
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title,content,name,date;
                title = eventList.get(position).getTitle();
                content = eventList.get(position).getContent();
                name = eventList.get(position).getName();
                date = eventList.get(position).getDate();

                eventManagerDialog = new EventManagerDialog(getActivity(),title,content,name,date);
                eventManagerDialog.show();

            }
        });


        setNoticeListView();
        setEventListView();


        ad_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 추가하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),AdminActivity.class);
                                getActivity().startActivity(intent);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 추가에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                String table = ad_insert_table.getText().toString();
                String value = ad_insert_value.getText().toString();

                InsertRequest insertRequest = new InsertRequest(table,value,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(insertRequest);
            }
        });

        ad_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 수정하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),AdminActivity.class);
                                getActivity().startActivity(intent);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 수정에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                String table = ad_update_table.getText().toString();
                String set = ad_update_set.getText().toString();
                String where = ad_update_where.getText().toString();

                UpdateRequest updateRequest = new UpdateRequest(table,set,where,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(updateRequest);
            }
        });

        ad_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 삭제하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),AdminActivity.class);
                                getActivity().startActivity(intent);
                            } else {
                                Toast.makeText(getActivity().getApplicationContext(),"데이터를 삭제에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                String table = ad_delete_table.getText().toString();
                String where = ad_delete_where.getText().toString();

                InsertRequest insertRequest = new InsertRequest(table,where,responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(insertRequest);
            }
        });

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
        return inflater.inflate(R.layout.fragment_admin,container,false);
    }
}
