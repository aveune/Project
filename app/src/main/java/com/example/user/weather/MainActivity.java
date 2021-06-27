package com.example.user.weather;



import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapsInitializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {


    TextView name,date, monday, tusday, wedday, thuday, friday, satday, sunday, montmp, tustmp, wedtmp, thutmp, fritmp, sattmp, suntmp;
    ImageView monimg, tusimg, wedimg, thuimg, friimg, satimg, sunimg;
    Button btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        monday = findViewById(R.id.monday);
        tusday = findViewById(R.id.tusday);
        wedday = findViewById(R.id.wedday);
        thuday = findViewById(R.id.thuday);
        friday = findViewById(R.id.friday);
        satday = findViewById(R.id.satday);
        sunday = findViewById(R.id.sunday);
        montmp = findViewById(R.id.montmp);
        tustmp = findViewById(R.id.tustmp);
        wedtmp = findViewById(R.id.wedtmp);
        thutmp = findViewById(R.id.thutmp);
        fritmp = findViewById(R.id.fritmp);
        sattmp = findViewById(R.id.sattmp);
        suntmp = findViewById(R.id.suntmp);
        monimg = findViewById(R.id.monimg);
        tusimg = findViewById(R.id.tusimg);
        wedimg = findViewById(R.id.wedimg);
        thuimg = findViewById(R.id.thuimg);
        friimg = findViewById(R.id.friimg);
        satimg = findViewById(R.id.satimg);
        sunimg = findViewById(R.id.sunimg);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    0 );
        }
        else{
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();


            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon=" + longitude + "&exclude=current&lang=kr&appid=5cdafc40bf3f63d109a0f0b695a98a85";

            // AsyncTask를 통해 HttpURLConnection 수행.
            NetworkTask networkTask = new NetworkTask(url, null);
            networkTask.execute();
        }


    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
            return result;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DateUtil dateUtil = new DateUtil();
            int index = 0;
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            JsonParser parser = new JsonParser();
            JsonObject obj = (JsonObject) parser.parse(s); // 이거로 정보를 빼내올 수 있게 해버림
            // 그럼 이제 여기서 정보를 꺼내와서 사용한다  daily 부분임
            JsonArray daily = obj.getAsJsonArray("daily");
            // daily만 가져왔음 다시 요일별로 분리를 해야함


            for(JsonElement e : daily)
            {
                // 이 for문은 daily라는 JsonArray에서 하나하나씩 뽑아와서 그 Array안에 있는 수만큼 반복
                //  JsonElemet e 라는 애는 처음에는 월요일 정보
                String dt = e.getAsJsonObject().get("dt").getAsString(); //  String으로 받아올거고 e를 JsonObject로 가져와서 dt만 가져온거
                dt = dateUtil.cvzTimeToDate(dt);
                //daily안에 8개가 {}로 묶인 8개 JsonObject getAsJsonObject dt만 가져오고  String으로 가져오겠다
                String temp = e.getAsJsonObject().getAsJsonObject("temp").get("day").getAsString(); // 보면 데일리의 첫번째를 가져와서 거기서 temp를 가져오고 거기서 day를 String 형태로 가져온다
                String day = dateUtil.getDayOfweek(dt);
                double realTemp = Double.parseDouble(temp) - 273.15; // 섭씨 구하는거임 켈빈으로 켈빈 : 미국 온도 이걸 이제 온도에 세팅

             
                if(index == 0)
                {
                    date.setText(dt);
                    monday.setText(day);
                    montmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }
                else if(index == 1)
                {
                    tusday.setText(day);
                    tustmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }
                else if(index == 2){
                    wedday.setText(day);
                    wedtmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                } else if (index ==3){
                    thuday.setText(day);
                    thutmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }else if (index == 4){
                    friday.setText(day);
                    fritmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }else if (index == 5){
                    satday.setText(day);
                    sattmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }else if (index == 6){
                    sunday.setText(day);
                    suntmp.setText(Integer.toString((int) Math.round(realTemp))+"°C");
                }

                index++;
            }
            // 자 이제 timezone을 가져와야함
            String location = obj.get("timezone").getAsString().split("/")[1];
            name.setText(location);

        }
    }





    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {


        }


        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };



}








