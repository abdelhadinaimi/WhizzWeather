package com.whizzmirray.whizzweather.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Whizz Mirray on 02/26/2018.
 */

public class AsyncHTTPClient {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static String BASE_FORCAST_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static String API_KEY = "66023a3e3cd2e93da80f061172424003";

    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        params.add("APPID",API_KEY);
        params.add("units","metric");
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
