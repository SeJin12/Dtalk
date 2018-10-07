package com.example.capdtalk.etc;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.MainActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.CourseDelRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/*********************************************************

 * MyCourseListAdapter

 * 만든이 - 안재규

 * 목적 - 사용자의 수업 목록을 courseList 에서 받아와 출력 및 삭제버튼으로 사용자의 수업삭제

 * 사용법 - CourseLayout 에서 호출

 * 작성일 - 2018-05-22

 **********************************************************/

public class MyCourseListAdapter extends BaseAdapter {

    private Context context;
    private List<Course> courseList;  //강의 목록
    private String m_id = MainActivity.login_id;  //todo LoginActivity.id 에서 메인액티비티.login_id 참조
    private Schedule schedule = new Schedule();
    private List<Integer> courseIDList;
    private Button delButton;

    public MyCourseListAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
        schedule = new Schedule();
        courseIDList = new ArrayList<Integer>();

        new BackgroundTask_Schedule().execute();

    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        for(int ii = 0;ii<courseIDList.size();ii++){
            Log.d("코스아이디리스트체크", ""+courseIDList.get(ii));}
        View v = View.inflate(context, R.layout.mycourse,null);
        TextView courseGrade = (TextView) v.findViewById(R.id.courseGrade);
        TextView courseTitle = (TextView) v.findViewById(R.id.courseTitle);
        TextView courseCredit = (TextView) v.findViewById(R.id.courseCredit);
        TextView courseDivide = (TextView) v.findViewById(R.id.courseDivide);
        TextView coursePersonnel = (TextView) v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor = (TextView) v.findViewById(R.id.courseProfessor);
        TextView courseTime = (TextView) v.findViewById(R.id.courseTime);

        if(courseList.get(i).getCourseGrade().equals("제한 없음") || courseList.get(i).getCourseGrade().equals(""))
        {
            courseGrade.setText("모든 학년");
        }
        else
        {
            courseGrade.setText(courseList.get(i).getCourseGrade() + "학년");
        }

        courseTitle.setText(courseList.get(i).getCourseTitle());
        courseCredit.setText(courseList.get(i).getCourseCredit() + "학점");
        courseDivide.setText(courseList.get(i).getCourseDivide() + "분반");
        if(courseList.get(i).getCoursePersonnel() == 0)
        {
            coursePersonnel.setText("인원 제한 없음");
        }
        else
        {
            coursePersonnel.setText("제한 인원 : " + courseList.get(i).getCoursePersonnel() + "명");
        }

        if(courseList.get(i).getCourseProfessor().equals(""))
        {
            courseProfessor.setText("개인 연구");
        }else
        {
            courseProfessor.setText(courseList.get(i).getCourseProfessor() + "교수님");
        }
        courseTime.setText(courseList.get(i).getCourseTime() + "");

        v.setTag(courseList.get(i).getCourseID());




        delButton = (Button) v.findViewById(R.id.delButton);
        //'삭제'버튼
        delButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int ii = 0;ii<courseIDList.size();ii++){
                    Log.d("코스아이디리스트체크", ""+courseIDList.get(ii));}
                if(alreadyIn(courseIDList, courseList.get(i).getCourseID()))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    AlertDialog dialog = builder.setMessage("강의가 존재하지 않습니다.")
                            .setNegativeButton("다시 시도", null)
                            .create();
                    dialog.show();
                }else if(!alreadyIn(courseIDList, courseList.get(i).getCourseID())){

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    AlertDialog dialog = builder.setMessage("강의가 삭제되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    courseIDList.remove(courseIDList.indexOf(courseList.get(i).getCourseID()));
                                    for(int ii = 0;ii<courseIDList.size();ii++){
                                    Log.d("코스아이디리스트체크", ""+courseIDList.get(ii));}

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    AlertDialog dialog = builder.setMessage("강의 삭제에 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    };
                    CourseDelRequest courseDelRequest = new CourseDelRequest(m_id, String.valueOf(courseList.get(i).getCourseID()), responseListener); //todo String값 변환후 바로 넣음 원래는 + "" 이걸로 형변환해서 넣었음 삭제안되면 여기
                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(courseDelRequest);
                }
            }

        });

        return v;
    }

    class BackgroundTask_Schedule extends AsyncTask<Void,Void,String> {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://tpwls8122.cafe24.com/ScheduleList.php?userID=" + URLEncoder.encode(m_id, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
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
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String courseProfessor;
                    String courseTime;
                    int courseID;
                    courseIDList.clear();
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                    courseID = object.getInt("courseID");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseIDList.add(courseID);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public boolean alreadyIn(List<Integer>courseIDList, int item)
    {
        for(int i = 0; i < courseIDList.size(); i++)
        {
            if(courseIDList.get(i) == item)
            {
                return false;
            }
        }
        return true;
    }
}
