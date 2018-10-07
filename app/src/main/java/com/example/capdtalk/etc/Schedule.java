package com.example.capdtalk.etc;

import android.content.Context;
import android.util.Log;

import com.example.capdtalk.R;

/*********************************************************

 * Schedule

 * 만든이 - 안재규

 * 목적 - 강의 추가 후 시간이 겹치는 강의가 있을 때의 예외처리

 * 사용법 -

 * 작성일 - 2018-04-08

 **********************************************************/



public class Schedule {
    //각 요일의 9교시까지 배열 생성
    private String monday[] = new String[10];
    private String tuesday[] = new String[10];
    private String wednesday[] = new String[10];
    private String thursday[] = new String[10];
    private String friday[] = new String[10];

    public Schedule() {
        for(int i = 0; i < 10; i++)
        {
            monday[i] = "";
            tuesday[i] = "";
            wednesday[i] = "";
            thursday[i] = "";
            friday[i] = "";
        }
    }

    public void addSchedule(String scheduleText){
        int temp;
        //예시 데이터 : 월:[3][4][5]화:[4][5]
        if((temp = scheduleText.indexOf("월")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                }
            }
        }
        if((temp = scheduleText.indexOf("화")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                }
            }
        }
        if((temp = scheduleText.indexOf("수")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                }
            }
        }
        if((temp = scheduleText.indexOf("목")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                }
            }
        }
        if((temp = scheduleText.indexOf("금")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = "수업";
                }
            }
        }
    }

    //시간이 중복되는지 판단하는 함수
    public boolean validate(String scheduleText)
    {
        if(scheduleText.equals("")){
            return true;
        }
        int temp;
        if((temp = scheduleText.indexOf("월")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    if(!monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals(""))
                    {
                        return false;
                    }
                }
            }
        }
        if((temp = scheduleText.indexOf("화")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    if(!tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals(""))
                    {
                        return false;
                    }
                }
            }
        }
        if((temp = scheduleText.indexOf("수")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    if(!wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals(""))
                    {
                        return false;
                    }
                }
            }
        }
        if((temp = scheduleText.indexOf("목")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    if(!thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals(""))
                    {
                        return false;
                    }
                }
            }
        }
        if((temp = scheduleText.indexOf("금")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    if(!friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))].equals(""))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void addSchedule(String scheduleText, String courseTitle, String courseProfessor){
        String professor;
        if(courseProfessor.equals("")){
            professor = "";
        }
        else{
            professor = "(" + courseProfessor + ")";
        }
        int temp;
        //예시 데이터 : 월:[3][4][5]화:[4][5]
        if((temp = scheduleText.indexOf("월")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    monday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle + "\n" + professor;
                }
            }
        }
        if((temp = scheduleText.indexOf("화")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    tuesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle + "\n" + professor;
                }
            }
        }
        if((temp = scheduleText.indexOf("수")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    wednesday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle + "\n" + professor;
                }
            }
        }
        if((temp = scheduleText.indexOf("목")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    thursday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle + "\n" + professor;
                }
            }
        }
        if((temp = scheduleText.indexOf("금")) > -1)
        {
            temp += 2;
            int startPoint = temp;
            int endPoint = temp;
            for(int i = temp; i < scheduleText.length() && scheduleText.charAt(i) != ':'; i++)
            {
                if(scheduleText.charAt(i) == '[' )
                {
                    startPoint = i;
                }
                if(scheduleText.charAt(i) == ']')
                {
                    endPoint = i;
                    friday[Integer.parseInt(scheduleText.substring(startPoint + 1, endPoint))] = courseTitle + "\n" + professor;
                }
            }
        }
    }

    public void setting(AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday, AutoResizeTextView[] friday, Context context) {

        String maxText = "";
        for(int i = 1; i<10; i++){
            if(this.monday[i].length() > maxText.length()){
                maxText = this.monday[i];
            }
            if(this.tuesday[i].length() > maxText.length()){
                maxText = this.tuesday[i];
            }
            if(this.wednesday[i].length() > maxText.length()){
                maxText = this.wednesday[i];
            }
            if(this.thursday[i].length() > maxText.length()){
                maxText = this.thursday[i];
            }
            if(this.friday[i].length() > maxText.length()){
                maxText = this.friday[i];
            }
        }

        for (int i = 1; i < 10; i++)
        {
            if(!this.monday[i].equals("")){
                monday[i].setText(this.monday[i]);
                Log.d("Schedule.setting",":"+this.monday[i]);
                monday[i].setBackground(context.getResources().getDrawable(R.drawable.cell_shape_havetext));
            }
            else{
                monday[i].setText(maxText);
            }
            if(!this.tuesday[i].equals("")){
                tuesday[i].setText(this.tuesday[i]);
                Log.d("Schedule.setting",":"+this.tuesday[i]);
                tuesday[i].setBackground(context.getResources().getDrawable(R.drawable.cell_shape_havetext));
            }
            else{
                tuesday[i].setText(maxText);
            }
            if(!this.wednesday[i].equals("")){
                wednesday[i].setText(this.wednesday[i]);
                Log.d("Schedule.setting",":"+this.wednesday[i]);
                wednesday[i].setBackground(context.getResources().getDrawable(R.drawable.cell_shape_havetext));
            }
            else{
                wednesday[i].setText(maxText);
            }
            if(!this.thursday[i].equals("")){
                thursday[i].setText(this.thursday[i]);
                Log.d("Schedule.setting",":"+this.thursday[i]);
                thursday[i].setBackground(context.getResources().getDrawable(R.drawable.cell_shape_havetext));
            }
            else{
                thursday[i].setText(maxText);
            }
            if(!this.friday[i].equals("")){
                friday[i].setText(this.friday[i]);
                Log.d("Schedule.setting",":"+this.friday[i]);
                friday[i].setBackground(context.getResources().getDrawable(R.drawable.cell_shape_havetext));
            }
            else{
                friday[i].setText(maxText);
            }

        }

    }

}
