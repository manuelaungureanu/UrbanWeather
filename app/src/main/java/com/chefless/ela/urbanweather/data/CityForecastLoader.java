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

package com.chefless.ela.urbanweather.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.chefless.ela.urbanweather.Injection;

/**
 * Custom {@link android.content.Loader} for a {@link CityForecast}, using the
 * {@link CitiesForecastDataSource} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class CityForecastLoader extends AsyncTaskLoader<CityForecast>
        implements CitiesForecastRepository.CitiesForecastRepositoryObserver{

    private final String mCityForecastId;
    private CitiesForecastRepository mRepository;

    public CityForecastLoader(String forecastId, Context context) {
        super(context);
        this.mCityForecastId = forecastId;
        mRepository = Injection.provideCitiesForecastRepository(context);
    }

    @Override
    public CityForecast loadInBackground() {
        return mRepository.getCityForecast(mCityForecastId);
    }

    @Override
    public void deliverResult(CityForecast data) {
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(data);
        }

    }

    @Override
    protected void onStartLoading() {

        // Deliver any previously loaded data immediately if available.
        if (mRepository.cachedCitiesForecastAvailable()) {
            deliverResult(mRepository.getCachedCityForecast(mCityForecastId));
        }

        // Begin monitoring the underlying data source.
        mRepository.addContentObserver(this);

        if (takeContentChanged() || !mRepository.cachedCitiesForecastAvailable()) {
            // When a change has  been delivered or the repository cache isn't available, we force
            // a load.
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mRepository.removeContentObserver(this);
    }

    @Override
    public void onCitiesForecastChanged() {
        if (isStarted()) {
            forceLoad();
        }
    }
}
