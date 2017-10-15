package com.chefless.ela.urbanweather.citiesforecast;

import android.support.v4.app.LoaderManager;

import com.chefless.ela.urbanweather.data.CitiesForecastLoader;
import com.chefless.ela.urbanweather.data.CitiesForecastRepository;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link CitiesForecastPresenter}
 */
public class CitiesForecastPresenterTest {

    private static List<CityForecast> FORECASTS = Lists.newArrayList(new CityForecast("123", "London"),
            new CityForecast("345", "Paris"));

    private static List<CityForecast> EMPTY_FORECASTS = new ArrayList<>(0);

    @Mock
    private CitiesForecastRepository mRepository;

    @Mock
    private CitiesForecastContract.View mView;

    @Mock
    private CitiesForecastLoader mLoader;

    @Mock
    private LoaderManager mLoaderManager;

    private CitiesForecastPresenter mPresenter;
    private List<CitiesForecastRepository.CitiesForecastRepositoryObserver> mObservers = new ArrayList<>();

    @Before
    public void setupPresenter() {
        MockitoAnnotations.initMocks(this);
        // Get a reference to the class under test
        mPresenter = new CitiesForecastPresenter(mLoader, mLoaderManager, mRepository, mView);
    }

    @Test
    public void loadForecastsFromRepository() {

        // Given an initialized Presenter with initialized forecasts
        // show forecasts in UI
        mPresenter.processForecasts(FORECASTS);
        verify(mView).showForecasts(FORECASTS);
    }

    @Test
    public void clickOnForecast_ShowsDetailUi() {
        // Given a stubbed forecast
        CityForecast requestedForecast = new CityForecast("1", "London");

        // When opening details is requested
        mPresenter.openForecastDetails(requestedForecast);

        // Then forecast detail UI is shown
        verify(mView).showForecastDetailsUi(any(String.class));
    }
}
