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
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.capdtalk.Activity.BoardActivity;
import com.example.capdtalk.R;
import com.example.capdtalk.Request.CheckBoardRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyServiceBoard extends Service {
    NotificationManager manager;
    ServiceThreadBoard thread;
    NotificationCompat.Builder builder = null;
    public static String boardDate = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId= "Notification-Board";
            NotificationChannel channel = new NotificationChannel(channelId,"Notification-Board",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("check board");
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this,channelId);
        }else{
            builder = new NotificationCompat.Builder(this,null);
        }
        MyServiceHandler handler = new MyServiceHandler();
        thread = new ServiceThreadBoard(handler);
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
            checkBoard();
        }
    }

    private void checkBoard(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    String[][] arr = new String[jsonArray.length()][4];
                    JSONObject object = jsonArray.getJSONObject(count);

                    if( !(object.length() == 0) ){
                        arr[0][0] = object.getString("board_title");
                        arr[0][1] = object.getString("board_content");
                        arr[0][2] = object.getString("board_name");
                        arr[0][3] = object.getString("board_date");

                        Log.i("asd : ",boardDate + "  " + arr[0][3]);

                        if(boardDate == null){
                            boardDate = arr[0][3];
                        }else if(!boardDate.equals(arr[0][3])){
                            Intent intent = new Intent(MyServiceBoard.this, BoardActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(MyServiceBoard.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                            builder.setContentText("["+arr[0][2]+"]  "+ arr[0][0])
                                    .setContentTitle("새 글이 작성되었습니다.")
                                    .setSmallIcon(R.drawable.primary_logo_image_on_transparent_150x150)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .build();
                            manager.notify(123, builder.build());

                            boardDate = arr[0][3];
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        CheckBoardRequest checkBoardRequest = new CheckBoardRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(checkBoardRequest);
    }

}
