package com.chefless.ela.urbanweather.citiesforecast;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.chefless.ela.urbanweather.data.CitiesForecastLoader;
import com.chefless.ela.urbanweather.data.CitiesForecastRepository;
import com.chefless.ela.urbanweather.data.CityForecast;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ela on 12/10/2017.
 */

public class CitiesForecastPresenter implements CitiesForecastContract.Presenter,
        LoaderManager.LoaderCallbacks<List<CityForecast>>  {


    private final static int FORECASTS_QUERY = 1;

    private final CitiesForecastRepository mRepository;

    private final CitiesForecastContract.View mView;

    private final CitiesForecastLoader mLoader;

    private final LoaderManager mLoaderManager;

    private List<CityForecast> mCurrentForecasts;

    private boolean mFirstLoad;

    public CitiesForecastPresenter(@NonNull CitiesForecastLoader loader, @NonNull LoaderManager loaderManager,
                          @NonNull CitiesForecastRepository repository, @NonNull CitiesForecastContract.View forecastsView) {
        mLoader = checkNotNull(loader, "loader cannot be null!");
        mLoaderManager = checkNotNull(loaderManager, "loader manager cannot be null");
        mRepository = checkNotNull(repository, "repository cannot be null");
        mView = checkNotNull(forecastsView, "forecastsView cannot be null!");

        mView.setPresenter(this);
    }

    //region Loader Callbacks methods
    @Override
    public Loader<List<CityForecast>> onCreateLoader(int id, Bundle args) {
        mView.setLoadingIndicator(true);
        return mLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<CityForecast>> loader, List<CityForecast> data) {
        mView.setLoadingIndicator(false);

        mCurrentForecasts = data;
        if (mCurrentForecasts == null) {
            mView.showLoadingForecastsError();
        } else {
            processForecasts(mCurrentForecasts);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<CityForecast>> loader) {
    }
    //endregion

    //region CitiesForecastContract.Presenter Implementation

    @Override
    public void loadForecasts(boolean forceUpdate) {

        if (forceUpdate || mFirstLoad) {
            mFirstLoad = false;
            mRepository.refreshCitiesForecast();
        } else {
            processForecasts(mCurrentForecasts);
        }

    }

    @Override
    public void openForecastDetails(@NonNull CityForecast requestedForecast) {

        checkNotNull(requestedForecast, "requestedForecast cannot be null!");
        mView.showForecastDetailsUi(requestedForecast.getId());
    }
    //endregion

    //region BasePresenter implementation
    @Override
    public void start() {
        mLoaderManager.initLoader(FORECASTS_QUERY, null, this);
    }
    //endregion

    //region Custom Methods
    public void processForecasts(List<CityForecast> forecasts) {
        if (forecasts.isEmpty()) {
            // Show a message indicating there are no forecasts
            mView.showNoForecasts();
        } else {
            // Show the list of forecasts
            mView.showForecasts(forecasts);
        }
    }
    //endregion
}
