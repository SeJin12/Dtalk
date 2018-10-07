package com.example.capdtalk.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.LoginRequest;
import com.example.capdtalk.SHA256.SHA256;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static String id;
    public static String pwd;
    public static boolean saveLoginData;
    private AlertDialog dialog;
    public static SharedPreferences appData;
    public static SharedPreferences.Editor editor;
    CheckBox logincheck;
    EditText login_id;
    EditText login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Dtalk!");

        // 설정값 불러오기
        appData = getSharedPreferences("appData",MODE_PRIVATE);
        load();

        if(saveLoginData){ // 전에 로그인유지버튼을 체크 했다면 기록이 남아있어 바로 Main액티비티로 바로 넘어간다.
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            LoginActivity.this.startActivity(intent);
        }

        Button registerbutton = (Button) findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        login_id = (EditText) findViewById(R.id.login_id);
        login_password = (EditText) findViewById(R.id.login_password);
        final Button loginbutton = (Button) findViewById(R.id.loginbutton);
        logincheck = (CheckBox) findViewById(R.id.logincheck);



        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String m_id = login_id.getText().toString();
                String m_password = login_password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                save(); // 로그인 성공시 SharedPreferences 저장 처리
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|intent.FLAG_ACTIVITY_CLEAR_TASK);
                                LoginActivity.this.startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                String convertPass = SHA256.getSHA256(m_password);
                LoginRequest loginRequest = new LoginRequest(m_id,convertPass,responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null ){
            dialog.dismiss();  //
            dialog = null;
        }
    }

    // 설정 값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체 만으론 저장 불가능 Editor 사용
        editor = appData.edit();

        // 에디터객체 .put타입 ( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA",logincheck.isChecked());
        editor.putString("ID",login_id.getText().toString().trim());
        editor.putString("PWD",login_password.getText().toString().trim());
        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.commit();
    }

    private void load() {
        // SharedPreferences 객체 .get타입 ( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA",false);
        id = appData.getString("ID","");
        pwd = appData.getString("PWD","");
    }

}

