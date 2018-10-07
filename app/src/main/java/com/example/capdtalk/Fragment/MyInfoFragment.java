package com.example.capdtalk.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.LoginActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.MyInfoRequest;
import com.example.capdtalk.Request.MyInfoUpdateRequest;
import com.example.capdtalk.SHA256.SHA256;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyInfoFragment extends Fragment {


    private ArrayAdapter adapter;
    private Spinner spinner;

    TextView myinfo_id; EditText myinfo_password; EditText myinfo_name; EditText myinfo_phone;
    TextView myinfo_major;
    Button myinfo_updatebtn; Button myinfo_cancelbtn;
    RadioGroup myinfo_classGroup; RadioButton myinfo_group;
    RadioButton myinfo_classA,myinfo_classB;
    String myinfo_rbtncheck;
    String pre_password;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("내 정보 수정");

        myinfo_id = (TextView) view.findViewById(R.id.myinfo_id);
        myinfo_password = (EditText) view.findViewById(R.id.myinfo_password);
        myinfo_major = (TextView) view.findViewById(R.id.myinfo_major); // 스피너에 기본 값 넣을 때만 사용함
        myinfo_name = (EditText) view.findViewById(R.id.myinfo_name);
        myinfo_phone = (EditText) view.findViewById(R.id.myinfo_phone);

        myinfo_updatebtn = (Button) view.findViewById(R.id.myinfo_updatebtn);
        myinfo_cancelbtn = (Button) view.findViewById(R.id.myinfo_cancelbtn);

        spinner = (Spinner) view.findViewById(R.id.myinfo_majorSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),R.array.major,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myinfo_classA = (RadioButton) view.findViewById(R.id.myinfo_classA);
        myinfo_classB = (RadioButton) view.findViewById(R.id.myinfo_classB);

       myinfo_classGroup = (RadioGroup) view.findViewById(R.id.myinfo_classGroup);
        /*final int classGroupID = myinfo_classGroup.getCheckedRadioButtonId();
        myinfo_rbtncheck = ( (RadioButton) view.findViewById(classGroupID) ).getText().toString();*/

        myinfo_classGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                RadioButton classButton = (RadioButton) getView().findViewById(i);
                myinfo_rbtncheck = classButton.getText().toString(); // update시 사용할 변수
            }
        });


        getMemberData();



        myinfo_updatebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                Snackbar.make(getView(), "회원정보를 수정하였습니다.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager
                                        .beginTransaction()
                                        .replace(R.id.content_main,new MainFragment())
                                        .commit();
                            } else {
                                Snackbar.make(getView(), "회원정보 수정을 실패하였습니다.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };


                String inputPass;
                // EditText의 값이 비어있다면 Edit의 Hint값에 있을걸 넣어줘서 sql쿼리에 ''값이 들어가지 않도록 방지
                if(myinfo_password.getText().toString().equals("")){
                    myinfo_password.setText(myinfo_password.getHint().toString());
                    inputPass = pre_password;
                }else{
                    inputPass = SHA256.getSHA256(myinfo_password.getText().toString());
                }


                if(myinfo_name.getText().toString().equals(""))
                    myinfo_name.setText(myinfo_name.getHint().toString());
                if(myinfo_phone.getText().toString().equals(""))
                    myinfo_phone.setText(myinfo_phone.getHint().toString());



                MyInfoUpdateRequest myInfoUpdateRequest = new MyInfoUpdateRequest(myinfo_id.getText().toString(),
                        inputPass,spinner.getSelectedItem().toString(),myinfo_rbtncheck,
                        myinfo_name.getText().toString(),myinfo_phone.getText().toString(),responseListener);

                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(myInfoUpdateRequest);
            }
        });

        myinfo_cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.content_main,new MainFragment())
                        .commit();
            }
        });

    }

    private void getMemberData(){ // async로 할랬더니 문제가 발생 = new task()실행시 안에 배열에 넣은값들이 oncreate로 안넘어옴
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    SharedPreferences pref = getActivity().getSharedPreferences("appData",LoginActivity.MODE_PRIVATE);
                    String login_id = pref.getString("ID","");
                    String m_id,m_password,m_major,m_group,m_name,m_phone;
                    while(count < jsonArray.length() ){
                        JSONObject object = jsonArray.getJSONObject(count);
                        m_id = object.getString("m_id");
                        m_password = object.getString("m_password");
                        m_major = object.getString("m_major");
                        m_group = object.getString("m_group");
                        m_name = object.getString("m_name");
                        m_phone = object.getString("m_phone");

                        if(m_id.equals(login_id)){
                            myinfo_id.setText(m_id);
                            pre_password = m_password;
                            myinfo_password.setHint("변경 시 입력");

//                            myinfo_major.setText(m_major);

                            for(int i=0;i<6;i++){  // 스피너 기본값 설정
                                if(myinfo_major.getText().equals(spinner.getItemAtPosition(i).toString())){
                                    spinner.setSelection(i);
                                    break;
                                }
                            }

                            if(m_group.equals("A반")){
                                myinfo_classA.setChecked(true);
                                myinfo_classB.setChecked(false);
                            }else if(m_group.equals("B반")){
                                myinfo_classA.setChecked(false);
                                myinfo_classB.setChecked(true);
                            }
                            myinfo_name.setHint(m_name);
                            myinfo_phone.setHint(m_phone);

                            break; // id값과 멤버디비를 검색한 값의 아이디가 같은 경우 m_변수에 값을 넣고 break
                        }
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        MyInfoRequest myInfoRequest = new MyInfoRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(myInfoRequest);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myinfo,container,false);
    }
}
