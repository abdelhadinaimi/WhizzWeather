package com.whizzmirray.whizzweather.http;

import com.whizzmirray.whizzweather.models.Location;
import com.whizzmirray.whizzweather.models.Temperature;
import com.whizzmirray.whizzweather.models.Weather;
import com.whizzmirray.whizzweather.models.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


public class JSONParser {

    public static Weather[] parseForcastJSON(String forcastJson,String currJson) throws JSONException{
        JSONObject jObj = new JSONObject(forcastJson);

        JSONObject cityJo = getObject("city",jObj);
        JSONObject coordJo = getObject("coord",cityJo);

        int cnt = getInt("cnt",jObj) + 1;
        Weather[] weathers = new Weather[cnt];
        weathers[cnt-1] = parseJSON(currJson);
        Location location = new Location();
        location.setCity(getString("name",cityJo));
        location.setCountry(getString("country",cityJo));
        location.setLon(getFloat("lon",coordJo));
        location.setLat(getFloat("lat",coordJo));

        JSONArray list = jObj.getJSONArray("list");
        for(int i = 0; i < cnt-1; i++){

            JSONObject wJo = list.getJSONObject(i);
            JSONObject mainJo = getObject("main",wJo);
            JSONObject weatherJo = wJo.getJSONArray("weather").getJSONObject(0);
            JSONObject windJo = getObject("wind",wJo);

            Temperature temperature = new Temperature();
            temperature.setTempCurrent(getFloat("temp",mainJo));
            temperature.setTempMax(getFloat("temp_max",mainJo));
            temperature.setTempMin(getFloat("temp_min",mainJo));

            Wind wind = new Wind();
            wind.setSpeed(getFloat("speed",windJo));
            //wind.setDeg(getFloat("deg",windJo));
            Weather weather = new Weather();
            weather.setHumidity(getFloat("humidity",mainJo));
            weather.setTemperature(temperature);
            weather.setWind(wind);

            weather.setDt(new Date(Long.parseLong(String.valueOf(getLong("dt",wJo))) * 1000));

            weather.setId(getInt("id",weatherJo));
            weather.setDescription(getString("description",weatherJo));
            weather.setIcon(getString("icon",weatherJo));
            weather.setCondition(getString("main",weatherJo));
            weather.setLocation(location);
            weathers[i] = weather;
        }

        return weathers;
    }
    public static Weather parseJSON(String json) throws JSONException{
        JSONObject jObj = new JSONObject(json);

        JSONObject mainJo = getObject("main", jObj);
        JSONObject weatherJO = jObj.getJSONArray("weather").getJSONObject(0);
        JSONObject sysJo = getObject("sys",jObj);
        JSONObject coordJo = getObject("coord",jObj);
        JSONObject windJo = getObject("wind",jObj);

        Temperature temperature = new Temperature();
        temperature.setTempCurrent(getFloat("temp",mainJo));
        temperature.setTempMax(getFloat("temp_max",mainJo));
        temperature.setTempMin(getFloat("temp_min",mainJo));

        Location location = new Location();
        location.setCity(getString("name",jObj));
        location.setCountry(getString("country",sysJo));
        location.setLon(getFloat("lon",coordJo));
        location.setLat(getFloat("lat",coordJo));

        Wind wind = new Wind();
        wind.setSpeed(getFloat("speed",windJo));
        //wind.setDeg(getFloat("deg",windJo));

        Weather weather = new Weather();
        weather.setHumidity(getFloat("humidity",mainJo));
        weather.setLocation(location);
        weather.setTemperature(temperature);
        weather.setWind(wind);

        weather.setDt(new Date(Long.parseLong(String.valueOf(getLong("dt",jObj))) * 1000));

        weather.setId(getInt("id",weatherJO));
        weather.setDescription(getString("description",weatherJO));
        weather.setIcon(getString("icon",weatherJO));
        weather.setCondition(getString("main",weatherJO));

        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    private static long  getLong(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getLong(tagName);
    }

}