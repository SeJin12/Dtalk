package com.example.capdtalk.Notification;

import android.os.Handler;

public class ServiceThreadBoard extends Thread {
    Handler handler;
    boolean isRun = true;

    public ServiceThreadBoard(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this){
            this.isRun = false;
        }
    }

    public void run(){
        while(isRun){
            handler.sendEmptyMessage(0);
            try{
                Thread.sleep(20000);
            }catch (Exception e){}
        }
    }
}
