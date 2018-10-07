package com.example.capdtalk.Fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyLog;
import com.example.capdtalk.Activity.MainActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.etc.AutoResizeTextView;
import com.example.capdtalk.etc.Schedule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/*********************************************************

 * 파일명 - ScheduleFragment

 * 만든이 - 안재규

 * 목적 - login_id를 받아와서 해당 사용자의 강의목록만 불러옴

 * 사용법 - MainActivity에서 호출

 * 작성일 - 2018-04-07

 **********************************************************/


public class ScheduleFragment extends Fragment {

    private AutoResizeTextView monday[] = new AutoResizeTextView[10];
    private AutoResizeTextView tuesday[] = new AutoResizeTextView[10];
    private AutoResizeTextView wednesday[] = new AutoResizeTextView[10];
    private AutoResizeTextView thursday[] = new AutoResizeTextView[10];
    private AutoResizeTextView friday[] = new AutoResizeTextView[10];
    private Schedule schedule = new Schedule();

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        getActivity().setTitle("시간표");

        monday[0] = (AutoResizeTextView) getView().findViewById(R.id.monday0);
        monday[1] = (AutoResizeTextView) getView().findViewById(R.id.monday1);
        monday[2] = (AutoResizeTextView) getView().findViewById(R.id.monday2);
        monday[3] = (AutoResizeTextView) getView().findViewById(R.id.monday3);
        monday[4] = (AutoResizeTextView) getView().findViewById(R.id.monday4);
        monday[5] = (AutoResizeTextView) getView().findViewById(R.id.monday5);
        monday[6] = (AutoResizeTextView) getView().findViewById(R.id.monday6);
        monday[7] = (AutoResizeTextView) getView().findViewById(R.id.monday7);
        monday[8] = (AutoResizeTextView) getView().findViewById(R.id.monday8);
        monday[9] = (AutoResizeTextView) getView().findViewById(R.id.monday9);
        tuesday[0] = (AutoResizeTextView) getView().findViewById(R.id.tuesday0);
        tuesday[1] = (AutoResizeTextView) getView().findViewById(R.id.tuesday1);
        tuesday[2] = (AutoResizeTextView) getView().findViewById(R.id.tuesday2);
        tuesday[3] = (AutoResizeTextView) getView().findViewById(R.id.tuesday3);
        tuesday[4] = (AutoResizeTextView) getView().findViewById(R.id.tuesday4);
        tuesday[5] = (AutoResizeTextView) getView().findViewById(R.id.tuesday5);
        tuesday[6] = (AutoResizeTextView) getView().findViewById(R.id.tuesday6);
        tuesday[7] = (AutoResizeTextView) getView().findViewById(R.id.tuesday7);
        tuesday[8] = (AutoResizeTextView) getView().findViewById(R.id.tuesday8);
        tuesday[9] = (AutoResizeTextView) getView().findViewById(R.id.tuesday9);
        wednesday[0] = (AutoResizeTextView) getView().findViewById(R.id.wednesday0);
        wednesday[1] = (AutoResizeTextView) getView().findViewById(R.id.wednesday1);
        wednesday[2] = (AutoResizeTextView) getView().findViewById(R.id.wednesday2);
        wednesday[3] = (AutoResizeTextView) getView().findViewById(R.id.wednesday3);
        wednesday[4] = (AutoResizeTextView) getView().findViewById(R.id.wednesday4);
        wednesday[5] = (AutoResizeTextView) getView().findViewById(R.id.wednesday5);
        wednesday[6] = (AutoResizeTextView) getView().findViewById(R.id.wednesday6);
        wednesday[7] = (AutoResizeTextView) getView().findViewById(R.id.wednesday7);
        wednesday[8] = (AutoResizeTextView) getView().findViewById(R.id.wednesday8);
        wednesday[9] = (AutoResizeTextView) getView().findViewById(R.id.wednesday9);
        thursday[0] = (AutoResizeTextView) getView().findViewById(R.id.thursday0);
        thursday[1] = (AutoResizeTextView) getView().findViewById(R.id.thursday1);
        thursday[2] = (AutoResizeTextView) getView().findViewById(R.id.thursday2);
        thursday[3] = (AutoResizeTextView) getView().findViewById(R.id.thursday3);
        thursday[4] = (AutoResizeTextView) getView().findViewById(R.id.thursday4);
        thursday[5] = (AutoResizeTextView) getView().findViewById(R.id.thursday5);
        thursday[6] = (AutoResizeTextView) getView().findViewById(R.id.thursday6);
        thursday[7] = (AutoResizeTextView) getView().findViewById(R.id.thursday7);
        thursday[8] = (AutoResizeTextView) getView().findViewById(R.id.thursday8);
        thursday[9] = (AutoResizeTextView) getView().findViewById(R.id.thursday9);
        friday[0] = (AutoResizeTextView) getView().findViewById(R.id.friday0);
        friday[1] = (AutoResizeTextView) getView().findViewById(R.id.friday1);
        friday[2] = (AutoResizeTextView) getView().findViewById(R.id.friday2);
        friday[3] = (AutoResizeTextView) getView().findViewById(R.id.friday3);
        friday[4] = (AutoResizeTextView) getView().findViewById(R.id.friday4);
        friday[5] = (AutoResizeTextView) getView().findViewById(R.id.friday5);
        friday[6] = (AutoResizeTextView) getView().findViewById(R.id.friday6);
        friday[7] = (AutoResizeTextView) getView().findViewById(R.id.friday7);
        friday[8] = (AutoResizeTextView) getView().findViewById(R.id.friday8);
        friday[9] = (AutoResizeTextView) getView().findViewById(R.id.friday9);


        new BackgroundTask_Schedule().execute();
    }

    class BackgroundTask_Schedule extends AsyncTask<Void,Void,String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://tpwls8122.cafe24.com/ScheduleList.php?userID=" + URLEncoder.encode(MainActivity.login_id, "UTF-8");
                Log.d(VolleyLog.TAG, "스케줄프래그먼트아이디:"+ MainActivity.login_id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                Log.d(VolleyLog.TAG, "스케줄프래그먼트타겟주소:"+target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null)
                    stringBuilder.append(temp + "\n");

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.d(VolleyLog.TAG, "스케줄프래그먼트데이터:"+result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String courseProfessor;
                String courseTime;
                String courseTitle;
                int courseID;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseID = object.getInt("courseID");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseTitle =object.getString("courseTitle");
                    schedule.addSchedule(courseTime, courseTitle, courseProfessor);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }





}
