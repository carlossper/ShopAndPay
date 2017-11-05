package com.feup.ei12078.shopandpay;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andremachado on 04/11/2017.
 */

public class AppUtils {
    public static String baseUrl = "http://192.168.1.3:8000/"; //For local testing use your ipv4 address


    public static boolean isInternetAvailable(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
