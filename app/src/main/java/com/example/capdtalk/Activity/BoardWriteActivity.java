package com.example.capdtalk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.BoardWriteRequest;
import com.example.capdtalk.Request.MyInfoRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BoardWriteActivity extends AppCompatActivity {

    EditText bw_title, bw_content;
    TextView bw_name, bw_date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardwrite);
        setTitle("글 작성");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        if(MainActivity.login_id == null){
            Intent intent = new Intent(BoardWriteActivity.this,LoginActivity.class);
            BoardWriteActivity.this.startActivity(intent);
            overridePendingTransition(0,0);
        }

        bw_title = findViewById(R.id.bw_title); bw_content = findViewById(R.id.bw_content);
        bw_name = findViewById(R.id.bw_name); bw_date = findViewById(R.id.bw_date);

        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long now = System.currentTimeMillis();
                            Date da = new Date(now);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
                            String date = dateFormat.format(da);
                            bw_date.setText(date);
                        }
                    });
                    try {
                        Thread.sleep(1000); // 1000 ms = 1초
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } // while
            } // run()
        }; // new Thread() { };

        thread.start();

        getMemberData();
    }

    private void getMemberData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    String m_id,m_name;
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        m_id = object.getString("m_id");
                        m_name = object.getString("m_name");

                        if(m_id.equals(MainActivity.login_id)){
                            bw_name.setText(m_name);
                            break;
                        }
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        MyInfoRequest myInfoRequest = new MyInfoRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(myInfoRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    // ActionBar 의 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 뒤로 가기
            case android.R.id.home:
                Toast.makeText(BoardWriteActivity.this,"글 작성을 취소하였습니다", Toast.LENGTH_SHORT).show();
                overridePendingTransition(0,0);
                finish();
                return true;
            case R.id.board_save:
                Write();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Write(){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(getApplicationContext(),"글 작성 완료하였습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BoardWriteActivity.this,BoardActivity.class);
                        BoardWriteActivity.this.startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"글 작성에 실패했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        long now = System.currentTimeMillis();
        Date da = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA); // 날짜 표현 포맷 설정
        String date = dateFormat.format(da);

        BoardWriteRequest boardWriteRequest = new BoardWriteRequest(bw_title.getText().toString(),bw_content.getText().toString(),bw_name.getText().toString(),date,responseListener);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(boardWriteRequest);
    }


}
