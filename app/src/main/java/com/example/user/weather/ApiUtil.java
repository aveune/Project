package com.example.user.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ApiUtil {

    public String callApi(String url){
        String result = null;
        try {
            URL restApi = new URL(url);
            HttpURLConnection con = (HttpURLConnection) restApi.openConnection(); // 여기서 URL SET하고 보냄 쏘고
            con.setRequestMethod("GET");
            StringBuilder sb = new StringBuilder(); //여기서 응답을 읽을 버퍼를 만듬
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8")); // 인풋으로 읽음
                String line;
                while((line = br.readLine()) != null){ // 한줄씩
                    sb.append(line).append("\n");
                }
                br.close();
                result = sb.toString();
            }else{
                System.out.println(con.getResponseMessage());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result; // 받아온 값 return
    }
}
