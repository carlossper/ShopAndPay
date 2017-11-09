package com.feup.ei12078.shopandpay.data_classes;

import android.util.Log;

/**
 * Created by andremachado on 09/11/2017.
 */

public class Invoice {
    private String price;
    private String id;
    private String rawDate;

    private String year;
    private String month;
    private String day;

    private String hour;
    private String minute;

    public Invoice(String price, String id, String rawDate){
        this.price = price;
        this.id = id;
        this.rawDate = rawDate;

        parseDate(rawDate);

    }

    private void parseDate(String rawDate) {
        //TODO get a decent regex for this shit. This is very time and memory consuming


        String[] t1 = rawDate.split("T");
        String ymdS = t1[0];
        String temp = t1[1];

        String[] temp2 = temp.split("\\.");
        String mhsS = temp2[0];

        String[] ymd = ymdS.split("-");
        String[] mhs = mhsS.split(":");

        this.year = ymd[0];
        this.month = ymd[1];
        this.day = ymd[2];

        Log.i("TAG",this.month);
        Log.i("TAG",this.day);


        this.hour = mhs[0];
        this.minute = mhs[1];


    }

    public String getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
