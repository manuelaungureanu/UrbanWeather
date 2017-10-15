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
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Custom {@link android.content.Loader} for a list of {@link CityForecast}, using the
 * {@link CitiesForecastRepository} as its source. This Loader is a {@link AsyncTaskLoader} so it queries
 * the data asynchronously.
 */
public class CitiesForecastLoader extends AsyncTaskLoader<List<CityForecast>>
        implements CitiesForecastRepository.CitiesForecastRepositoryObserver{

    private CitiesForecastRepository mRepository;

    public CitiesForecastLoader(Context context, @NonNull CitiesForecastRepository repository) {
        super(context);
        checkNotNull(repository);
        mRepository = repository;
    }

    @Override
    public List<CityForecast> loadInBackground() {
        return mRepository.getCitiesForecast();
    }

    @Override
    public void deliverResult(List<CityForecast> data) {
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
            deliverResult(mRepository.getCachedCitiesForecast());
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
    protected void onStopLoading() {
        cancelLoad();
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
