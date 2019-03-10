package com.whizzmirray.whizzweather.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.whizzmirray.whizzweather.MainActivity.TAG;

public class HTTPClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static String BASE_FORCAST_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static String API_KEY = "66023a3e3cd2e93da80f061172424003";

    public static String getWeatherJSON(String lat,String lon) throws Exception {
        return getWeatherData("?lat=" + lat + "&lon=" + lon,false);
    }
    public static String getWeatherJSON(String loc) throws Exception {
        return getWeatherData("?q="+loc,false);
    }

    public static String getForcast(String lat,String lon) throws Exception {
        return getWeatherData("?lat=" + lat + "&lon=" + lon,true);
    }

    private static String getWeatherData(String args,boolean forecast) throws Exception{
        HttpURLConnection con = null;
        InputStream in = null;

            con = (HttpURLConnection) ( new URL((forecast ? BASE_FORCAST_URL : BASE_URL) + args +"&APPID=" + API_KEY + "&units=metric")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            StringBuilder buffer = new StringBuilder();
            in = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line).append("\r\n");

            in.close();
            con.disconnect();
            Log.d(TAG, "getWeatherData: Finished Fetching Data");

            return buffer.toString();
    }
    static public boolean isConnected() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -i .5 -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}