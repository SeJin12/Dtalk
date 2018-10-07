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
                Thread.sleep(30000);
            }catch (Exception e){}
        }
    }
}
/*
* 인력개발센터 뫼입사지원 경진대회 안내
* 대회기간 : 2018.9.17(월) ~ 10.15(월)
* 취업을 준비하고 있는 학생들이 작성한 자기소개서를 평가하여 장학금을 지급합니다.
* 10월 5일 10시에 졸업사진 찍는다
* */