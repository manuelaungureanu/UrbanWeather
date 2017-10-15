/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chefless.ela.urbanweather.detailforecast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chefless.ela.urbanweather.BasePresenter;
import com.chefless.ela.urbanweather.data.CitiesForecastRepository;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.chefless.ela.urbanweather.data.CityForecastLoader;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkNotNull;

public class CityForecastPresenter implements
        LoaderManager.LoaderCallbacks<CityForecast>, BasePresenter {

    private static final int CITY_FORECAST_QUERY = 3;

    private CitiesForecastRepository mRepository;

    private CityForecastContract.View mDetailView;

    private CityForecastLoader mLoader;

    private LoaderManager mLoaderManager;

    @Nullable
    private String mId;

    public CityForecastPresenter(@Nullable String forecastId,
                                 @NonNull CitiesForecastRepository repository,
                                 @NonNull CityForecastContract.View detailView,
                                 @NonNull CityForecastLoader loader,
                                 @NonNull LoaderManager loaderManager) {
        mId = forecastId;
        mRepository = checkNotNull(repository, "Repository cannot be null!");
        mDetailView = checkNotNull(detailView, "DetailView cannot be null!");
        mLoader = checkNotNull(loader, "Loader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loaderManager cannot be null!");

        mDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        mLoaderManager.initLoader(CITY_FORECAST_QUERY, null, this);
    }

    private void showCityForecast(@NonNull CityForecast forecast) {
        String title = forecast.getWeather()[0].getMain();
        String description = forecast.getWeather()[0].getDescription();
        //String rain = String.valueOf(forecast.getRain().get3h());

        mDetailView.setModel(forecast);

//        if (Strings.isNullOrEmpty(title)) {
//            mDetailView.hideTitle();
//        } else {
//            mDetailView.showTitle(title);
//        }
//
//        if (Strings.isNullOrEmpty(description)) {
//            mDetailView.hideDescription();
//        } else {
//            mDetailView.showDescription(description);
//        }

        mDetailView.setLoadingIndicator(false);
    }

    @Override
    public Loader<CityForecast> onCreateLoader(int id, Bundle args) {
        if (mId == null) {
            return null;
        }
        mDetailView.setLoadingIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<CityForecast> loader, CityForecast data) {
        if (data != null) {
            showCityForecast(data);
        } else {
            mDetailView.showMissingForecast();
        }
    }

    @Override
    public void onLoaderReset(Loader<CityForecast> loader) {
        // no-op
    }

}
