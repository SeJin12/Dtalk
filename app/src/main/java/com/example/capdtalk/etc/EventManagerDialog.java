package com.example.capdtalk.etc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.AdminActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.EManagerRequest;
import com.example.capdtalk.Request.EventDeleteRequest;

import org.json.JSONObject;

public class EventManagerDialog extends Dialog {

    EditText dlm_title,dlm_content;
    TextView dlm_name,dlm_date;
    Button dlm_update,dlm_delete,dlm_cancel;
    String bring_title,bring_content,bring_name,bring_date;

    private OnDismissListener onDismissListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 1.0f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_listmanager);

        dlm_title = findViewById(R.id.dlm_title); dlm_content = findViewById(R.id.dlm_content);
        dlm_name = findViewById(R.id.dlm_name); dlm_date = findViewById(R.id.dlm_date);
        dlm_update = findViewById(R.id.dlm_update); dlm_delete = findViewById(R.id.dlm_delete); dlm_cancel = findViewById(R.id.dlm_cancel);

        dlm_title.setHint(bring_title);
        dlm_content.setHint(bring_content);
        dlm_name.setText(bring_name);
        dlm_date.setText(bring_date);

        dlm_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getContext(),"항목이 수정되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(),AdminActivity.class);
                                getContext().startActivity(intent);
                                cancel();
                            } else {
                                Toast.makeText(getContext(),"항목 수정 실패",Toast.LENGTH_SHORT).show();
                                cancel();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                String update_title, update_content;

                // EditText의 값이 비어있다면 Edit의 Hint값에 있을걸 넣어줘서 sql쿼리에 ''값이 들어가지 않도록 방지
                if(dlm_title.getText().toString().equals(""))
                    update_title = dlm_title.getHint().toString();
                else
                    update_title = dlm_title.getText().toString();

                if(dlm_content.getText().toString().equals(""))
                    update_content = dlm_content.getHint().toString();
                else
                    update_content = dlm_content.getText().toString();

                EManagerRequest eManagerRequest = new EManagerRequest(dlm_title.getHint().toString(),dlm_content.getHint().toString(),
                        dlm_name.getText().toString(),dlm_date.getText().toString(),update_title,update_content,responseListener);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(eManagerRequest);
            }
        });

        dlm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Toast.makeText(getContext(),"항목이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(),AdminActivity.class);
                                getContext().startActivity(intent);
                                cancel();
                            } else {
                                Toast.makeText(getContext(),"항목 삭제 실패",Toast.LENGTH_SHORT).show();
                                cancel();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                // EditText의 값이 비어있다면 Edit의 Hint값에 있을걸 넣어줘서 sql쿼리에 ''값이 들어가지 않도록 방지
                if(dlm_title.getText().toString().equals(""))
                    dlm_title.setText(dlm_title.getHint().toString());
                if(dlm_content.getText().toString().equals(""))
                    dlm_content.setText(dlm_content.getHint().toString());

                EventDeleteRequest eventDeleteRequest = new EventDeleteRequest(dlm_title.getText().toString(),dlm_content.getText().toString(),
                        dlm_name.getText().toString(),dlm_date.getText().toString(),responseListener);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(eventDeleteRequest);

            }
        });

        dlm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

    }


    public EventManagerDialog(@NonNull Context context) {
        super(context);
    }

    public EventManagerDialog(@NonNull Context tmp_context, String title, String content, String name, String date) {
        super(tmp_context);
        bring_title = title;
        bring_content = content;
        bring_name = name;
        bring_date = date;
    }


}
