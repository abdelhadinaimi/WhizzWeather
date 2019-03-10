package com.whizzmirray.whizzweather;


import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.whizzmirray.whizzweather.http.AsyncHTTPClient;
import com.whizzmirray.whizzweather.http.HTTPClient;
import com.whizzmirray.whizzweather.http.JSONParser;
import com.whizzmirray.whizzweather.models.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static org.junit.Assert.assertTrue;

public class HTTPClientTest  {

    @Test
    public void httpRequest(){
        String lublinStart = "{\"coord\":{\"lon\":22.57,\"lat\":51.25}";
        System.out.println(HTTPClient.getWeatherJSON("51.25","22.67"));
        assertTrue("Network Error",HTTPClient.getWeatherJSON("lublin,poland").startsWith(lublinStart));
        assertTrue("Network Error",HTTPClient.getWeatherJSON("51.25","22.57").startsWith(lublinStart));
    }

    @Test
    public void asyncHTTP()  {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("lon","22.57");
        paramMap.put("lat","51.25");
        AsyncHTTPClient.post("weather", new RequestParams(paramMap), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    System.out.println(response.get("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }
        });

    }

}