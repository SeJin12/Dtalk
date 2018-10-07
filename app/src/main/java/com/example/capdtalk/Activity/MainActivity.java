package com.example.capdtalk.Activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Fragment.AdminModeFragment;
import com.example.capdtalk.Fragment.BoardFragment;
import com.example.capdtalk.Fragment.CourseFragment;
import com.example.capdtalk.Fragment.MainFragment;
import com.example.capdtalk.Fragment.MyInfoFragment;
import com.example.capdtalk.Fragment.ScheduleFragment;
import com.example.capdtalk.Notification.MyService;
import com.example.capdtalk.Notification.MyServiceBoard;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.IsAdminReqeust;
import com.example.capdtalk.Request.MyInfoRequest;
import com.example.capdtalk.etc.CustomWeatherDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager = getFragmentManager();
    SharedPreferences pref;
    public static String login_id;
    private MenuItem menuItem_admin;
    TextView nav_header_id,nav_header_major,nav_header_group,nav_header_name;
    private CustomWeatherDialog dialog;
    FloatingActionButton fab;
    public static String member_name; // 로그인한 멤버의 이름을 전역으로 쓰기 위해서

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dtalk 메인");
        fragmentManager.beginTransaction().replace(R.id.content_main,new MainFragment()).commit();
        pref = getSharedPreferences("appData",LoginActivity.MODE_PRIVATE);
        login_id = pref.getString("ID","");

        if(login_id == null){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            MainActivity.this.startActivity(intent);
            overridePendingTransition(0,0);
        }

        // Toolbar 객체 :   나중에 ToolBar를 애니메이션을 적용시켜서 좀 더 부드러운 어플리케이션을 만들 수 있습니다

        //툴바를 가져온후
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setSupportActionBar() 메서드는 인자로 받은 툴바를 액티비티의 액션바로 대체하는 역할을 합니다. 기본으로 제공되는 액션바 외에 별도로 툴바를 사용하고 싶다면 이 메서드를 호출하지 않고 툴바만 단독으로 사용하는 것도 가능합니다.

        // FloatingActionButton 객체 : 우하단에 위치하고 있는 버튼, FloatingActionButton 에 관한 것
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,BoardWriteActivity.class);
                MainActivity.this.startActivity(intent);
                overridePendingTransition(0,0);
            }
        });


        // DrawerLayout 객체 : Drawer 기능을 이용하기 위해서 밑에 까는 레이아웃
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // ActionBarDrawerToggle 객체 : ActionBar 에서 좌상단에 위치한 버튼인데, NavigationView를 부를때와 안부를때 모양이 바뀌는 것을 하는 듯
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView 객체 : 왼쪽에서 드래그해서 볼 수 있는 전광 View
        //출처: http://ljs93kr.tistory.com/3 [건프의 소소한 개발이야기]
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View nav_header_view = navigationView.getHeaderView(0);
        nav_header_id = (TextView) nav_header_view.findViewById(R.id.nav_header_id);
        nav_header_major = (TextView) nav_header_view.findViewById(R.id.nav_header_major);
        nav_header_group = (TextView) nav_header_view.findViewById(R.id.nav_header_group);
        nav_header_name = (TextView) nav_header_view.findViewById(R.id.nav_header_name);

        Intent intent = new Intent(MainActivity.this,MyService.class);
        Intent intentBoard = new Intent(MainActivity.this,MyServiceBoard.class);
        startService(intent);
        startService(intentBoard);


    }
    private long lastTimeBackPressed;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MainActivity.this,MyService.class);
        Intent intentBoard = new Intent(MainActivity.this,MyServiceBoard.class);
        stopService(intent);
        stopService(intentBoard);
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        //2초 이내에 뒤로가기 버튼을 재 클릭 시 앱 종료
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000)
        {
            finish();
            return;
        }
        //'뒤로' 버튼 한번 클릭 시 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
        //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록
        lastTimeBackPressed = System.currentTimeMillis();

    }


    @Override
    public void onWindowFocusChanged(boolean b) {
        super.onWindowFocusChanged(b);
        if(b){ // 화면이 다시 포커싱될때 재실행 - 왜썼냐면 앱실행시 관리자모드하면 메뉴가 보이는데 로그아웃하고 다시 관리자로 로그인하면 안떠서 이거씀
            pref = getSharedPreferences("appData",LoginActivity.MODE_PRIVATE);
            String login_id = pref.getString("ID","");
            IsAdmin(login_id);
        }

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(this).attach(this).commit(); detach(this)문제발생 프래그먼트 refresh하는거

        getMemberData(); // db 값을 끌어와 nav_header 부분에 Member의 정보로 TextView에 setText함

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        menuItem_admin = menu.findItem(R.id.admin);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin) {
            fab.setVisibility(View.GONE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main,new AdminModeFragment())
                    .commit();
            return true;
        } else if( id == R.id.weather ){
            dialog = new CustomWeatherDialog(MainActivity.this);
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {//메인
            fab.setVisibility(View.GONE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main,new MainFragment())
                    .commit();
        } else if (id == R.id.nav_class) {
            fab.setVisibility(View.GONE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main,new CourseFragment())
                    .commit();
        } else if (id == R.id.nav_schedule) {
            fab.setVisibility(View.GONE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main, new ScheduleFragment())
                    .commit();
        } else if (id == R.id.nav_board) {
            fab.setVisibility(View.VISIBLE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main, new BoardFragment())
                    .commit();
        } else if (id == R.id.nav_myinfo) {
            fab.setVisibility(View.GONE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_main,new MyInfoFragment())
                    .commit();
        } else if (id == R.id.nav_logout) {
            // 로그아웃 클릭시 SharedPreferences 지우고 맨 처음 로그인액티비티로 넘어감
            SharedPreferences appData = getSharedPreferences("appData",LoginActivity.MODE_PRIVATE);
            LoginActivity.editor = appData.edit();
            LoginActivity.editor.clear();
            LoginActivity.editor.commit();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void IsAdmin(final String id){  // 로그인 후 메인 액티비티 넘어올때 관리자id 면 관리자메뉴버튼이 보이게 하는 함수
                                                // 이 함수의 정의는 onCreateOptionsMenu 함수정의보다 늦게 정의되어야함

        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String m_id; String m_admin;

                    while(count < jsonArray.length() ) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        m_id = object.getString("m_id");
                        m_admin = object.getString("m_admin");

                        if(m_id.equals(id) && m_admin.equals("1")){
                            menuItem_admin.setVisible(true);
                            break;
                        }
                        count++;
                        /*Log.i("id가 같냐? ","" + m_id.equals(id));
                        Log.i("admin이 같냐? ","" + m_admin.equals("1"));
                        */

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        IsAdminReqeust isAdminReqeust = new IsAdminReqeust(id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(isAdminReqeust);
    }

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

                        if(m_id.equals(login_id)){
                            nav_header_id.setText("( " + m_id + " )");
                            nav_header_major.setText(m_major);
                            nav_header_group.setText(m_group);
                            nav_header_name.setText(m_name);
                            member_name = m_name;
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

    private void checkNotification(){
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

                        if(m_id.equals(login_id)){
                            nav_header_id.setText("( " + m_id + " )");
                            nav_header_major.setText(m_major);
                            nav_header_group.setText(m_group);
                            nav_header_name.setText(m_name);
                            member_name = m_name;
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
