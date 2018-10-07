package com.example.capdtalk.etc;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.AdminActivity;
import com.example.capdtalk.Activity.MainActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.MyInfoRequest;
import com.example.capdtalk.Request.NEAddRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class NEManagerDialog extends Dialog {

    EditText dam_title,dam_content;
    TextView dam_name ,dam_date;
    Button dam_add,dam_cancel; TextView dam_datebtn;
    String bring_table;
    public int ryear,rmonth,rday;
    String y,m,d; // 달력의 선택한 날짜를 가져오기위한 변수

    private OnDismissListener onDismissListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 1.0f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_addmanager);

        dam_title = findViewById(R.id.dam_title); dam_content = findViewById(R.id.dam_content);
        dam_name = findViewById(R.id.dam_name); dam_date = findViewById(R.id.dam_date);
        dam_add = findViewById(R.id.dam_add); dam_cancel = findViewById(R.id.dam_cancel); dam_datebtn = findViewById(R.id.dam_datebtn);


        dam_datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                ryear = c.get(Calendar.YEAR);
                rmonth = c.get(Calendar.MONTH);
                rday = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        dam_date.setText( year + "-" + (month+1) + "-" + day ); // DatePicker 클릭 시 날짜 선택 버튼의 text를 바꾼다.
                        y = String.valueOf(year); m = String.valueOf(month + 1); d = String.valueOf(day);
                    }
                }, ryear, rmonth, rday);

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        dam_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getContext(),"항목이 추가되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(),AdminActivity.class);
                                getContext().startActivity(intent);
                                cancel();
                            } else {
                                Toast.makeText(getContext(),"항목 추가 실패",Toast.LENGTH_SHORT).show();
                                cancel();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                NEAddRequest neAddRequest = new NEAddRequest(bring_table,dam_title.getText().toString(),dam_content.getText().toString(),dam_name.getText().toString(),dam_date.getText().toString(),responseListener);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(neAddRequest);
            }
        });

        dam_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

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
                            dam_name.setText(m_name);
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(myInfoRequest);
    }


    public NEManagerDialog(@NonNull Context context) {
        super(context);
    }

    public NEManagerDialog(@NonNull Context tmp_context,String table) {
        super(tmp_context);
        bring_table = table;
    }


}
