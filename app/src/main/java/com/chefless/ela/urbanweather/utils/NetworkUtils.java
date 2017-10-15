package com.chefless.ela.urbanweather.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chefless.ela.urbanweather.data.CityForecast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ela on 26/09/2017.
 */

public class NetworkUtils {

    public static final String STATIC_CITIES_FORECAST_URL = "https://samples.openweathermap.org/data/2.5/group?id=2643743,2968815,3675707,4516749,5001962,2950159,2925533,2761369,2673730,7778677&units=metric&appid=b1b15e88fa797225412429c1c50c122a1";
    public static final String STATIC_CITY_FORECAST_URL = "http://samples.openweathermap.org/data/2.5/weather?appid=b1b15e88fa797225412429c1c50c122a1&id=";
    public static final String STATIC_CITIES_FORECAST_RESPONSE_ROOT_NAME = "list";

    public static final String JSON_EXTENSION = ".json";

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(new NullHostNameVerifier());

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static CityForecast[] parseJSONToCitiesForecast(String response) {
        if(response==null || TextUtils.isEmpty(response))
            return null;

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, CityForecast[].class);
    }

    public static CityForecast parseJSONToCityForecast(String response) {
        if(response==null || TextUtils.isEmpty(response))
            return null;

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, CityForecast.class);
    }


    public static class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }
}
