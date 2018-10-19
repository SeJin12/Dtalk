package com.example.capdtalk.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.MainActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.CheckCountRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyService extends Service {
    NotificationManager manager;
    ServiceThread thread;
    NotificationCompat.Builder builder = null;
    public static int mainCount = 0;
//    public static String boardDate = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId= "Notification-Main";
            NotificationChannel channel = new NotificationChannel(channelId,"Notification-Main",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("check notice,event");
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this,channelId);
        }else{
            builder = new NotificationCompat.Builder(this,null);
        }
        MyServiceHandler handler = new MyServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread.stopForever();
        thread = null;
    }

    class MyServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            checkMain();
        }
    }

    private void checkMain(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count;
                    JSONObject object = jsonArray.getJSONObject(0);
                    count = object.getInt("C");
                    //todo 공지 행사에 따라서 타이틀컨텐츠바꾸기 그리고 쿼리문 생각 공지행사 카운트 합으로 count하는방법생각
                    if(mainCount == 0){
                        mainCount = count;
                    }else{
                        if(mainCount < count) {
                            Intent intent = new Intent(MyService.this, MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setContentText("학과로부터 도착한 내용을 확인하세요.")
//                    .setContentTitle("학과로부터글쓴이이름")
                                    .setSmallIcon(R.drawable.primary_logo_image_on_transparent_150x150)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .build();
                            manager.notify(777, builder.build());
                        }else{
                        }
                        mainCount = count;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CheckCountRequest checkCountRequest = new CheckCountRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(checkCountRequest);
    }

}
