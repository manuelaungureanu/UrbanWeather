package com.chefless.ela.urbanweather.detailforecast;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chefless.ela.urbanweather.BasePresenter;
import com.chefless.ela.urbanweather.Injection;
import com.chefless.ela.urbanweather.R;
import com.chefless.ela.urbanweather.citiesforecast.CitiesForecastPresenter;
import com.chefless.ela.urbanweather.data.CitiesForecastLoader;
import com.chefless.ela.urbanweather.data.CitiesForecastRepository;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.chefless.ela.urbanweather.data.CityForecastLoader;
import com.chefless.ela.urbanweather.databinding.ActivityDetailBinding;
import com.chefless.ela.urbanweather.databinding.ActivityMainBinding;

import static com.google.common.base.Preconditions.checkNotNull;

public class DetailActivity extends AppCompatActivity implements CityForecastContract.View {

    public static final String EXTRA_CITY_ID = "cityId";
    BasePresenter mPresenter;

    private TextView mDetailTitle;
    private TextView mDetailDescription;
    private ProgressBar mLoadingIndicator;
    ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initUI(binding);

        String id = getIntent().getStringExtra(EXTRA_CITY_ID);

        // Create the presenter
        mPresenter = new CityForecastPresenter(
                id,
                Injection.provideCitiesForecastRepository(getApplicationContext()),
                this,
                new CityForecastLoader(id, getApplicationContext()),
                getSupportLoaderManager());
    }

    private void initUI(ActivityDetailBinding binding) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingIndicator = binding.cityForecastContentDetail.pbLoadingData;
        mDetailTitle = binding.cityForecastContentDetail.tvName;
        mDetailDescription = binding.cityForecastContentDetail.tvWeatherDesc;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    //region BaseView Implementation
    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    //endregion

    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadingIndicator.setVisibility(active?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showMissingForecast() {
        mDetailTitle.setText("");
        mDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void setModel(Object model) {
        CityForecast forecast = (CityForecast) model;
        binding.cityForecastContentDetail.setForecast(forecast);
    }
}
