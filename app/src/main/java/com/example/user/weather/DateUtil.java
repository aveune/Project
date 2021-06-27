package com.example.user.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    //

    public String cvzTimeToDate(String time) {
        long cvzTime = Long.parseLong(time);
        Date date = new Date(cvzTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", new Locale("en", "US"));
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
        String result = sdf.format(date);
        return result;
    }


    public String getDayOfweek(String date){
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy", new Locale("en","US"));
        // 이걸 이용해서 format으로 만듬
        String[] week = {"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"};
        Calendar cal = Calendar.getInstance();
        // Calendar 객체를 만듬
        Date getDate;
        // Date 객체를 만듬
        int w = 0;
        try
        {
            getDate = format.parse(date); // date 이건 그 dd MMM yyyy 임이걸 파싱을 함
            cal.setTime(getDate); // calendar 객체에 넣음
            w = cal.get(Calendar.DAY_OF_WEEK)-1; // 생성한 cal객체를 이용해서 입력한 date 값이 어떤 요일인지를 알아낼수있음 일요일이면
            // w가 0이고 이렇게해서 0 ~6 인덱스 값을 받아올 수 있음
        } catch (ParseException e)
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return week[w];
    }


}
