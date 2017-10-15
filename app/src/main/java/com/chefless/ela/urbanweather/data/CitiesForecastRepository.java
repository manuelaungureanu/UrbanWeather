package com.chefless.ela.urbanweather.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ela on 12/10/2017.
 */

public class CitiesForecastRepository implements CitiesForecastDataSource {

    private static CitiesForecastRepository INSTANCE = null;

    private final CitiesForecastDataSource mCitiesForecastRemoteDataSource;

    private List<CitiesForecastRepositoryObserver> mObservers = new ArrayList<>();

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    Map<String, CityForecast> mCachedCitiesForecast;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty;

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param remoteDataSource the backend data source
     * @return the {@link CitiesForecastRepository} instance
     */
    public static CitiesForecastRepository getInstance(CitiesForecastDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesForecastRepository(remoteDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(CitiesForecastDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }

    // Prevent direct instantiation.
    private CitiesForecastRepository(@NonNull CitiesForecastDataSource remoteDataSource) {
        mCitiesForecastRemoteDataSource = checkNotNull(remoteDataSource);
    }

    public void addContentObserver(CitiesForecastRepositoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void removeContentObserver(CitiesForecastRepositoryObserver observer) {
        if (mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    private void notifyContentObserver() {
        for (CitiesForecastRepositoryObserver observer : mObservers) {
            observer.onCitiesForecastChanged();
        }
    }

    @Nullable
    @Override
    public List<CityForecast> getCitiesForecast() {
        List<CityForecast> forecasts;

        if (!mCacheIsDirty) {
            // Respond immediately with cache if available and not dirty
            if (mCachedCitiesForecast != null) {
                forecasts = getCachedCitiesForecast();
                return forecasts;
            }
        }

        // Grab remote data if cache is dirty
        forecasts = mCitiesForecastRemoteDataSource.getCitiesForecast();

        processLoadedCitiesForecast(forecasts);
        return getCachedCitiesForecast();

    }

    public boolean cachedCitiesForecastAvailable() {
        return mCachedCitiesForecast != null && !mCacheIsDirty;
    }

    public List<CityForecast> getCachedCitiesForecast() {
        return mCachedCitiesForecast == null ? null : new ArrayList<>(mCachedCitiesForecast.values());
    }

    public CityForecast getCachedCityForecast(String cityId) {
        return mCachedCitiesForecast.get(cityId);
    }

    private void processLoadedCitiesForecast(List<CityForecast> forecasts) {
        if (forecasts == null) {
            mCachedCitiesForecast = null;
            mCacheIsDirty = false;
            return;
        }
        if (mCachedCitiesForecast == null) {
            mCachedCitiesForecast = new LinkedHashMap<>();
        }
        mCachedCitiesForecast.clear();

        for (CityForecast forecast : forecasts) {
            mCachedCitiesForecast.put(forecast.getId(), forecast);
        }
        mCacheIsDirty = false;
    }

    @Override
    public CityForecast getCityForecast(@NonNull final String cityId) {

        checkNotNull(cityId);

        CityForecast cachedForecast = getCityForecastWithId(cityId);

        // Respond immediately with cache if we have one
        if (cachedForecast != null) {
            return cachedForecast;
        }

        CityForecast forecast = mCitiesForecastRemoteDataSource.getCityForecast(cityId);
        return forecast;
    }

    @Nullable
    private CityForecast getCityForecastWithId(@NonNull String id) {
        checkNotNull(id);
        if (mCachedCitiesForecast == null || mCachedCitiesForecast.isEmpty()) {
            return null;
        } else {
            return mCachedCitiesForecast.get(id);
        }
    }

    @Override
    public void refreshCitiesForecast() {
        mCacheIsDirty = true;
        notifyContentObserver();
    }

    public interface CitiesForecastRepositoryObserver {

        void onCitiesForecastChanged();

    }

}
