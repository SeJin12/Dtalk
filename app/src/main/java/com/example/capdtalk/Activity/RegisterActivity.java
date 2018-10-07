package com.example.capdtalk.Activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.RegisterRequest;
import com.example.capdtalk.Request.ValidateRequest;
import com.example.capdtalk.SHA256.SHA256;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private AlertDialog dialog;

    private String m_id;
    private String m_password;
    private String m_name;
    private String m_major;
    private String m_group;
    private String m_phone;
    private boolean validate =false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원 가입");

        spinner = (Spinner) findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.major,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final EditText register_id = (EditText) findViewById(R.id.register_id);
        final EditText register_password = (EditText) findViewById(R.id.register_password);
        final EditText register_name = (EditText) findViewById(R.id.register_name);
        final EditText register_phone = (EditText) findViewById(R.id.register_phone);

        final RadioGroup classGroup = (RadioGroup) findViewById(R.id.classGroup);
        final int classGroupID = classGroup.getCheckedRadioButtonId();
        m_group = ( (RadioButton) findViewById(classGroupID) ).getText().toString();

        classGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                RadioButton classButton = (RadioButton) findViewById(i);
                m_group = classButton.getText().toString();
            }
        });

        final Button validatebutton = (Button) findViewById(R.id.validatebutton);
        validatebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String m_id = register_id.getText().toString();
                if (validate) {
                    return;
                }
                if (m_id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용 가능한 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                register_id.setEnabled(false); // 중복체크 가능한 순간 더 이상 변경할 수 없음
                                validate = true;
                                return;

                                // 버튼 클릭시 색깔 바꾸기 가능하도록
                            /*idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));*/

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateReqeust = new ValidateRequest(m_id,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateReqeust);
            }
        });

        Button register_button = (Button) findViewById(R.id.register_button);
        register_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                String m_id = register_id.getText().toString();
                String m_password = register_password.getText().toString();
                String m_name = register_name.getText().toString();
                String m_major = spinner.getSelectedItem().toString();
                String m_phone = register_phone.getText().toString();

                if(!validate){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복 체크를 해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(m_id.equals("")||m_password.equals("")||m_major.equals("")||m_group.equals("")||m_name.equals("")||m_phone.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                            .setNegativeButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 가입 성공")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();*/
                                Toast.makeText(getApplicationContext(),"회원 가입 성공",Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                /*AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원 가입을 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();*/
                                Toast.makeText(getApplicationContext(),"회원 가입 실패",Toast.LENGTH_SHORT).show();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };

                String convertPass = SHA256.getSHA256(m_password);

                RegisterRequest registerRequest = new RegisterRequest(m_id,convertPass,m_major,m_group,m_name,m_phone,responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

        Button register_cancel = (Button) findViewById(R.id.register_cancel);
        register_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

}
