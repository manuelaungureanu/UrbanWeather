package com.chefless.ela.urbanweather.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chefless.ela.urbanweather.data.CitiesForecastDataSource;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.chefless.ela.urbanweather.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.chefless.ela.urbanweather.utils.NetworkUtils.STATIC_CITIES_FORECAST_RESPONSE_ROOT_NAME;

/**
 * Created by ela on 12/10/2017.
 */

public class CitiesForecastRemoteDataSource implements CitiesForecastDataSource {

    private static CitiesForecastRemoteDataSource INSTANCE = null;

    public static CitiesForecastRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CitiesForecastRemoteDataSource();
        }
        return INSTANCE;
    }

    private CitiesForecastRemoteDataSource() {}

    @Nullable
    @Override
    public List<CityForecast> getCitiesForecast() {
        try {
            URL articlesUrl = new URL(NetworkUtils.STATIC_CITIES_FORECAST_URL);
            String response = NetworkUtils.getResponseFromHttpUrl(articlesUrl);
            JSONObject jsonRootObject = new JSONObject(response);
            String articlesJsonAsString = jsonRootObject.getString(STATIC_CITIES_FORECAST_RESPONSE_ROOT_NAME);

            CityForecast[] arrayResult =  NetworkUtils.parseJSONToCitiesForecast(articlesJsonAsString);

            if(arrayResult==null || arrayResult.length==0)
                return new ArrayList<>();

            return new ArrayList<>(Arrays.asList(arrayResult));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    @Override
    public CityForecast getCityForecast(@NonNull String cityId) {

        if(TextUtils.isEmpty(cityId))
            return null;

        try {
            String url = NetworkUtils.STATIC_CITY_FORECAST_URL + cityId;
            URL articlesUrl = new URL(url);
            String response = NetworkUtils.getResponseFromHttpUrl(articlesUrl);
            return NetworkUtils.parseJSONToCityForecast(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void refreshCitiesForecast() {

    }
}
