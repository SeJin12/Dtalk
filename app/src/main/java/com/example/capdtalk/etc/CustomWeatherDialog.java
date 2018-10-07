package com.example.capdtalk.etc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.capdtalk.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CustomWeatherDialog extends Dialog{

    TextView pubDate,temp,hour,sky,pty,pop,reh; // 발표시간, 시간, 하늘상태코드, 강수상태코드,강수확률,습도
    String pubDate1,temp1,hour1,sky1,pty1,pop1,reh1;
    Button weathercancelbtn;

    private Handler weatherHandler;
    private String urlStr = "http://www.weather.go.kr/wid/queryDFSRSS.jsp?zone=4817053000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 1.0f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.custom_weather_dialog);

        weatherHandler = new Handler();

        pubDate = (TextView) findViewById(R.id.pubDate);  temp = (TextView) findViewById(R.id.temp);
        hour = (TextView) findViewById(R.id.hour);
        sky = (TextView) findViewById(R.id.sky);  pty = (TextView) findViewById(R.id.pty);
        pop = (TextView) findViewById(R.id.pop);  reh = (TextView) findViewById(R.id.reh);
        weathercancelbtn = (Button) findViewById(R.id.weathercancelbtn);

        RequestThread thread = new RequestThread();
        thread.start();

        weathercancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        }

    public CustomWeatherDialog(@NonNull Context context) {
        super(context);
    }

    class RequestThread extends Thread {
        public void run(){
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();

                URL url = new URL(urlStr);
                InputStream inputStream = url.openStream();

                Document document = builder.parse(inputStream);
                boolean isProcessed = processDocument(document);

                if (isProcessed) {
                    Log.d("MyTownWeather", "Weather process succeeded.");
                } else {
                    Log.d("MyTownWeather", "Weather process failed.");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean processDocument(Document doc){
        try {
            Element docEle = doc.getDocumentElement();
            Element item = (Element) docEle.getElementsByTagName("item").item(0);
            Element data = (Element) docEle.getElementsByTagName("data").item(0);

            pubDate1 = docEle.getElementsByTagName("pubDate").item(0).getFirstChild().getNodeValue();
            temp1 = docEle.getElementsByTagName("temp").item(0).getFirstChild().getNodeValue();
            hour1 = docEle.getElementsByTagName("hour").item(0).getFirstChild().getNodeValue();
            sky1 = docEle.getElementsByTagName("sky").item(0).getFirstChild().getNodeValue();
            pty1 = docEle.getElementsByTagName("pty").item(0).getFirstChild().getNodeValue();
            pop1 = docEle.getElementsByTagName("pop").item(0).getFirstChild().getNodeValue();
            reh1 = docEle.getElementsByTagName("reh").item(0).getFirstChild().getNodeValue();

            weatherHandler.post(new Runnable() {
                public void run() {

                    // sky 1:맑음 2:구름조금 3:구름많음 4:흐림
                    switch (sky1){
                        case "1":
                            sky.setText("맑음");
                            break;
                        case "2":
                            sky.setText("구름 조금");
                            break;
                        case "3":
                            sky.setText("구름 많음");
                            break;
                        case "4":
                            sky.setText("흐림");
                            break;
                    }
                    // pty 0:없음 1:비 2:비/눈 3:눈/비 4:눈
                    switch (pty1){
                        case "0":
                            pty.setText("없음");
                            break;
                        case "1":
                            pty.setText("비");
                            break;
                        case "2":
                            pty.setText("비/눈");
                            break;
                        case "3":
                            pty.setText("눈/비");
                            break;
                        case "4":
                            pty.setText("눈");
                            break;
                    }

                    pubDate.setText(pubDate1);
                    temp.setText(temp1 + "'C");
                    hour.setText(hour1 + "시");
                    pop.setText(pop1 + "%");
                    reh.setText(reh1 + "%");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
