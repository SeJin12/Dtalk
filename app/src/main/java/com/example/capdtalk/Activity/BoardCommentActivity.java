package com.example.capdtalk.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.BoardCommentRequest;
import com.example.capdtalk.Request.CommentRequest;
import com.example.capdtalk.Request.MyInfoRequest;
import com.example.capdtalk.etc.Comment;
import com.example.capdtalk.etc.CommentListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoardCommentActivity extends AppCompatActivity {

    TextView bcw_name, bcw_date, bcw_title, bcw_content;
    EditText bcw_comment; Button bcw_btn; ListView bcw_listview;
    String title,content,name,date;
    private MenuItem menuItem_delete;
    private CommentListAdapter cAdapter;
    public List<Comment> commentList;
    int count; String[][] carray;
    String commentName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardcomment);

        if(MainActivity.login_id == null){
            Intent intent = new Intent(BoardCommentActivity.this,LoginActivity.class);
            BoardCommentActivity.this.startActivity(intent);
            overridePendingTransition(0,0);
        }

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
                            setTitle(date);
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

        bcw_name = findViewById(R.id.bcw_name); bcw_date = findViewById(R.id.bcw_date); bcw_title = findViewById(R.id.bcw_title);
        bcw_content = findViewById(R.id.bcw_content); bcw_comment = findViewById(R.id.bcw_comment); bcw_btn = findViewById(R.id.bcw_btn);
        bcw_listview = findViewById(R.id.bcw_listview);

        commentList = new ArrayList<Comment>();
        cAdapter = new CommentListAdapter(getApplicationContext(),commentList);
        bcw_listview.setAdapter(cAdapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Intent intent = getIntent();
        title = intent.getStringExtra("title"); content = intent.getStringExtra("content");
        name = intent.getStringExtra("name"); date = intent.getStringExtra("date");
        bcw_title.setText(title); bcw_content.setText(content);
        bcw_name.setText(name); bcw_date.setText(date);

        setCommentList();
        getMemberData();

        bcw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment("insert");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu2, menu);
        menuItem_delete = menu.findItem(R.id.board_delete);
        if (MainActivity.member_name.equals(bcw_name.getText().toString())){
            menuItem_delete.setVisible(true);
        }
        return true;
    }

    // ActionBar 의 버튼 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 뒤로 가기
            case android.R.id.home:
                overridePendingTransition(0,0);
                finish();
                return true;
            case R.id.board_delete:
                delBoard("delete");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCommentList(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    count = 0;
                    carray = new String[jsonArray.length()][5];
                    cAdapter.notifyDataSetChanged();
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        carray[count][0] = object.getString("wname");
                        carray[count][1] = object.getString("wdate");
                        carray[count][2] = object.getString("ccontent");
                        carray[count][3] = object.getString("cname");
                        carray[count][4] = object.getString("cdate");
                        count++;
                    }
                    for(int i=0;i<count;i++){
                        if(name.equals(carray[i][0]) && date.equals(carray[i][1])){
                            Comment comment  = new Comment(carray[i][0],carray[i][1],carray[i][2],carray[i][3],carray[i][4]);
                            commentList.add(comment);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CommentRequest commentRequest = new CommentRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(commentRequest);
    }

    private void delBoard(String type){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast.makeText(getApplicationContext(),"해당 글을 삭제하였습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),BoardActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"글을 지울 수 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };



        BoardCommentRequest boardCommentRequest = new BoardCommentRequest(type,bcw_name.getText().toString(),bcw_date.getText().toString(),"","","",responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(boardCommentRequest);

    }

    private void comment(String type){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Intent intent = new Intent(getApplicationContext(),BoardCommentActivity.class);
                        intent.putExtra("title",title);
                        intent.putExtra("content",content);
                        intent.putExtra("name",name);
                        intent.putExtra("date",date);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),"댓글 작성 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        long now = System.currentTimeMillis();
        Date da = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String cdate = dateFormat.format(da);

        /*Log.i("[값들] ",type+" 글작성자 : "+bcw_name.getText().toString()+" 글작성시간 : "+bcw_date.getText().toString()+" 댓글쓸거 :  "+
                bcw_comment.getText().toString()+" 댓작성자 : "+commentName+" 댓작성시간 : "+cdate);*/
        BoardCommentRequest boardCommentRequest = new BoardCommentRequest(type,bcw_name.getText().toString(),bcw_date.getText().toString(),
                bcw_comment.getText().toString(),commentName,cdate,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(boardCommentRequest);

    }

// 안쓰고 있음
    private void getMemberData(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    String m_id,m_password,m_major,m_group,m_name,m_phone;
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        m_id = object.getString("m_id");
                        //m_password = object.getString("m_password");
                        m_major = object.getString("m_major");
                        m_group = object.getString("m_group");
                        m_name = object.getString("m_name");
                        //m_phone = object.getString("m_phone");

                        if(m_id.equals(MainActivity.login_id)){
                            commentName = m_name;
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


}
