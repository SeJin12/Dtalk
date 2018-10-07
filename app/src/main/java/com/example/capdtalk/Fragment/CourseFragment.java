package com.example.capdtalk.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.VolleyLog;
import com.example.capdtalk.Activity.LoginActivity;
import com.example.capdtalk.Activity.MainActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.etc.Course;
import com.example.capdtalk.etc.CourseListAdapter;
import com.example.capdtalk.etc.MyCourseListAdapter;

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

 * 파일명 - CourseFragment

 * 만든이 - 안재규

 * 목적 - 수업 관리를 위한 프래그먼트

 * 사용법 - 네비게이션 버튼 수업 클릭시 프래그먼트 호출

 * 작성일 - 2018-04-07

 **********************************************************/

public class CourseFragment extends Fragment {

    private ArrayAdapter yearAdapter;
    private Spinner yearSpinner;
    private ArrayAdapter termAdapter;
    private Spinner termSpinner;
    private ArrayAdapter areaAdapter;
    private Spinner areaSpinner;
    private ArrayAdapter majorAdapter;
    private Spinner majorSpinner;

    private String courseUniversity = "";

    private ListView courseListView;
    private CourseListAdapter adapter;
    private MyCourseListAdapter myadapter;
    private List<Course> courseList;


    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        getActivity().setTitle("수업 관리");


        final RadioGroup courseUniversityGroup = (RadioGroup) getView().findViewById(R.id.courseUniversityGroup);
        yearSpinner = (Spinner) getView().findViewById(R.id.yearSpinner);
        termSpinner = (Spinner) getView().findViewById(R.id.termSpinner);
        areaSpinner = (Spinner) getView().findViewById(R.id.areaSpinner);
        majorSpinner = (Spinner) getView().findViewById(R.id.majorSpinner);

        //라디오버튼 학부,대학원 선택 시 각 스피너에 초기값 부여
        courseUniversityGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton courseButton = (RadioButton) getView().findViewById(checkedId);
                courseUniversity = courseButton.getText().toString();

                yearAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.year, android.R.layout.simple_spinner_dropdown_item);
                yearSpinner.setAdapter(yearAdapter);

                termAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.term, android.R.layout.simple_spinner_dropdown_item);
                termSpinner.setAdapter(termAdapter);

                if (courseUniversity.equals("학부")) {
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                } else if (courseUniversity.equals("대학원")) {
                    areaAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateArea, android.R.layout.simple_spinner_dropdown_item);
                    areaSpinner.setAdapter(areaAdapter);
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }

            }
        });

        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (areaSpinner.getSelectedItem().equals("교양및기타")) {
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityRefinementMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                if (areaSpinner.getSelectedItem().equals("전공")) {
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.universityMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
                if (areaSpinner.getSelectedItem().equals("일반대학원")) {
                    majorAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.graduateMajor, android.R.layout.simple_spinner_dropdown_item);
                    majorSpinner.setAdapter(majorAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        courseListView = (ListView) getView().findViewById(R.id.courseListView);
        courseList = new ArrayList<Course>();
        adapter = new CourseListAdapter(getContext(), courseList); //todo 빨간줄 뜨는데 되긴함 getActivity().getApplicationContext() 으로고치니 안되고
        courseListView.setAdapter(adapter);

        Button searchButton = (Button) getView().findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(courseUniversity!="") {
                    adapter = new CourseListAdapter(getContext(), courseList);
                    courseListView.setAdapter(adapter);
                    new BackgroundTask().execute();
                }else{
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("학부/대학원을 선택해주세요.")
                            .setPositiveButton("다시시도",null)
                            .create();
                    dialog.show();
                }
            }
        });

        Button myCourseButton = (Button) getView().findViewById(R.id.myCourseButton);
        myCourseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                myadapter = new MyCourseListAdapter(getContext(), courseList);
                courseListView.setAdapter(myadapter);
                new BackgroundTask_MyCourse().execute();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course, container, false);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://tpwls8122.cafe24.com/CourseList.php?courseUniversity=" + URLEncoder.encode(courseUniversity, "UTF-8") +
                        "&courseYear=" + URLEncoder.encode(yearSpinner.getSelectedItem().toString().substring(0, 4), "UTF-8") + "&courseTerm=" + URLEncoder.encode(termSpinner.getSelectedItem().toString(), "UTF-8") +
                        "&courseArea=" + URLEncoder.encode(areaSpinner.getSelectedItem().toString(), "UTF-8") + "&courseMajor=" + URLEncoder.encode(majorSpinner.getSelectedItem().toString(), "UTF-8");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        //@doInBackground : 입력된 스트링을 반환
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
                {
                    stringBuilder.append(temp + "\n");
                }
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
        public void onProgressUpdate(Void... values) { super.onProgressUpdate(); }

        @Override
        public  void onPostExecute(String result){
            try {
                courseList.clear();
                JSONObject jsonObject = new JSONObject(result);
                Log.d(VolleyLog.TAG, result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                int courseID; //강의 고유번호(자동부여)
                String courseUniversity; //강의 구분(학부|대학원)
                int courseYear; //해당 년도
                String courseTerm; //해당 학기
                String courseArea; //강의 구분(전공|교양및기타)
                String courseMajor; //해당 학과
                String courseGrade; //해당 학년
                String courseTitle; //강의 제목
                int courseCredit; //강의 학점
                int courseDivide; //강의 분반
                int coursePersonnel; //강의 제한인원
                String courseProfessor; //강의 교수
                String courseTime; //강의시간대
                String courseRoom; //강의실
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    courseID = object.getInt("courseID");
                    courseUniversity = object.getString("courseUniversity");
                    courseYear = object.getInt("courseYear");
                    courseTerm = object.getString("courseTerm");
                    courseArea = object.getString("courseArea");
                    courseMajor = object.getString("courseMajor");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseRoom = object.getString("courseRoom");
                    Course course = new Course(courseID, courseUniversity, courseYear, courseTerm, courseArea, courseMajor, courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime, courseRoom);
                    courseList.add(course);
                    count++;
                }
                if(count == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("조회된 강의가 없습니다.\n날짜를 확인하세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }
                adapter.notifyDataSetChanged();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask_MyCourse extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            try {
                target = "http://tpwls8122.cafe24.com/MyCourseList.php?userID=" + URLEncoder.encode(MainActivity.login_id, "UTF-8");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        //@doInBackground : 입력된 스트링을 반환
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                Log.d("target:",target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while ((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
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
        public void onProgressUpdate(Void... values) { super.onProgressUpdate(); }

        @Override
        public  void onPostExecute(String result){
            try {
                courseList.clear();
                JSONObject jsonObject = new JSONObject(result);
                Log.d(VolleyLog.TAG, result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int mycount = 0;
                int courseID; //강의 고유번호(자동부여)
                String courseUniversity; //강의 구분(학부|대학원)
                int courseYear; //해당 년도
                String courseTerm; //해당 학기
                String courseArea; //강의 구분(전공|교양및기타)
                String courseMajor; //해당 학과
                String courseGrade; //해당 학년
                String courseTitle; //강의 제목
                int courseCredit; //강의 학점
                int courseDivide; //강의 분반
                int coursePersonnel; //강의 제한인원
                String courseProfessor; //강의 교수
                String courseTime; //강의시간대
                String courseRoom; //강의실
                while(mycount < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(mycount);
                    courseID = object.getInt("courseID");
                    courseUniversity = object.getString("courseUniversity");
                    courseYear = object.getInt("courseYear");
                    courseTerm = object.getString("courseTerm");
                    courseArea = object.getString("courseArea");
                    courseMajor = object.getString("courseMajor");
                    courseGrade = object.getString("courseGrade");
                    courseTitle = object.getString("courseTitle");
                    courseCredit = object.getInt("courseCredit");
                    courseDivide = object.getInt("courseDivide");
                    coursePersonnel = object.getInt("coursePersonnel");
                    courseProfessor = object.getString("courseProfessor");
                    courseTime = object.getString("courseTime");
                    courseRoom = object.getString("courseRoom");
                    Course course = new Course(courseID, courseUniversity, courseYear, courseTerm, courseArea, courseMajor, courseGrade, courseTitle, courseCredit, courseDivide, coursePersonnel, courseProfessor, courseTime, courseRoom);
                    courseList.add(course);
                    mycount++;
                }
                if(mycount == 0)
                {
                    AlertDialog dialog;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    dialog = builder.setMessage("조회된 강의가 없습니다. \n강의를 추가하세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                }
                myadapter.notifyDataSetChanged();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
