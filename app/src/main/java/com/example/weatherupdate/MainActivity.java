package com.example.weatherupdate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView cityName;
    Button searchButton;
    TextView result;
    Button mButton;
    class Weather extends AsyncTask<String,Void,String>{

       @Override
       protected String doInBackground(String... address) {
           try {
               URL url=new URL(address[0]);
               HttpURLConnection connection=(HttpURLConnection) url.openConnection();
               connection.connect();
               InputStream is=connection.getInputStream();
               InputStreamReader isr=new InputStreamReader(is);

               int data=isr.read();
               String content="";
               char ch;
               while (data!=-1){
                   ch=(char) data;
                   content=content+ch;
                   data=isr.read();
               }
               return  content;


           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;

       }
   }
    public void search(View view) {
     cityName =findViewById(R.id.cityName);
     searchButton=findViewById(R.id.searchButton);
     result=findViewById(R.id.resut);
     String cName=cityName.getText().toString();


        String content;
        Weather weather=new Weather();
        try {
            content=weather.execute("https://openweathermap.org/data/2.5/weather?q="+cName+"&appid=b6907d289e10d714a6e88b30761fae22").get();

            Log.i("content",content);

            JSONObject jsonObject=new JSONObject(content);
            String weatherData=jsonObject.getString("weather");
            String mainTemperature=jsonObject.getString("main");
            String windspeed=jsonObject.getString("wind");
            double visibility;
            Log.i("weatherData",weatherData);

            JSONArray array=new JSONArray(weatherData);
            String main="";
            String description="";
            String temperature="";
            String speed="";
            String maxtemp="";
            String mintemp="";
            for (int i=0;i<array.length();i++)
            {
                JSONObject weatherPart=array.getJSONObject(i);
                main=weatherPart.getString("main");
                description=weatherPart.getString("description");
            }
            JSONObject mainPart=new JSONObject(mainTemperature);
            JSONObject windPart=new JSONObject(windspeed);

            temperature=mainPart.getString("temp");
            maxtemp=mainPart.getString("temp_max");
            mintemp=mainPart.getString("temp_min");
            speed=windPart.getString("speed");
            visibility=Double.parseDouble(jsonObject.getString("visibility"));
            int visibilityInKilometer;
            visibilityInKilometer = (int) visibility/1000;
            Log.i("Tempeature",temperature);

            Log.i("main",main);
            Log.i("description",description);
            String resultText="1.Main : "+main+"\n2.Description :"+description+"\n3.Temperature(in celcius):"+temperature+"\n4.visibility (in KM) :"+visibilityInKilometer+"\n5.WindSpeed: "+speed+"\n6.Max Temp(in celcius):"+maxtemp+"\n7.Min Temp(in celcius):"+mintemp;
            result.setText(resultText);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    mButton=(Button)findViewById(R.id.aboutUs);
    mButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this,AboutUs.class);
            startActivity(intent);
        }
    });

    }

}
