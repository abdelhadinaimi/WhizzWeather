package com.whizzmirray.whizzweather;

import com.whizzmirray.whizzweather.http.HTTPClient;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void rename() throws Exception {
        File folder = new File("C:\\Users\\Whizz Mirray\\Documents\\weather_icons");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                File fileWithNewName = new File(listOfFiles[i].getParent(), listOfFiles[i].getName().replace('-','_'));
                boolean success = listOfFiles[i].renameTo(fileWithNewName);
            }
        }

    }

    @Test
    public void httpRequest(){
        String lublinStart = "{\"coord\":{\"lon\":22.57,\"lat\":51.25}";
        System.out.println(HTTPClient.getWeatherJSON("51.25","22.67"));
        assertTrue("Network Error",HTTPClient.getWeatherJSON("lublin,poland").startsWith(lublinStart));
        assertTrue("Network Error",HTTPClient.getWeatherJSON("51.25","22.57").startsWith(lublinStart));
    }


}