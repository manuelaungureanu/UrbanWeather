package com.chefless.ela.urbanweather.citiesforecast;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chefless.ela.urbanweather.Injection;
import com.chefless.ela.urbanweather.R;
import com.chefless.ela.urbanweather.data.CitiesForecastLoader;
import com.chefless.ela.urbanweather.data.CitiesForecastRepository;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.chefless.ela.urbanweather.databinding.ActivityMainBinding;
import com.chefless.ela.urbanweather.detailforecast.DetailActivity;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainActivity extends AppCompatActivity implements CitiesForecastContract.View,
        CitiesForecastAdapter.CitiesForecastAdapterOnClickHandler {

    private CitiesForecastContract.Presenter mPresenter;

    private CitiesForecastAdapter mAdapter;

    private View mNoForecastsView;

    private TextView mNoForecastsMainView;

    private LinearLayout mForecastsView;

    private RecyclerView mForecastsRV;

    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUI(binding);

        // Create the presenter
        CitiesForecastRepository repository = Injection.provideCitiesForecastRepository(getApplicationContext());
        CitiesForecastLoader forecastsLoader = new CitiesForecastLoader(getApplicationContext(), repository);

        mPresenter = new CitiesForecastPresenter(
                forecastsLoader,
                getSupportLoaderManager(),
                repository,
                this
        );
    }

    private void initUI(ActivityMainBinding binding) {
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        mForecastsRV = binding.forecastsMainContent.forecastsRv;
        mForecastsView = binding.forecastsMainContent.forecastsLL;

        mNoForecastsView = binding.forecastsMainContent.noForecasts;
        mNoForecastsMainView = binding.forecastsMainContent.noForecastsMain;
        mLoadingIndicator = binding.forecastsMainContent.pbLoadingData;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mForecastsRV.setLayoutManager(layoutManager);
        mForecastsRV.setHasFixedSize(true);
        mAdapter = new CitiesForecastAdapter(this);
        mForecastsRV.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mAdapter.setData(null);
                mPresenter.loadForecasts(true);
                break;
        }
        return true;
    }


    //region CitiesForecastContract.View implementation
    @Override
    public void setLoadingIndicator(boolean active) {
        mLoadingIndicator.setVisibility(active?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public void showForecasts(List<CityForecast> data) {
        mAdapter.setData(data);

        mForecastsView.setVisibility(View.VISIBLE);
        mNoForecastsView.setVisibility(View.GONE);
    }

    @Override
    public void showForecastDetailsUi(String id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_CITY_ID, id);
        startActivity(intent);
    }

    @Override
    public void showLoadingForecastsError() {
        showMessage(getString(R.string.loading_forecasts_error));
    }

    private void showMessage(String message) {
        Snackbar.make(mForecastsView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoForecasts() {
        showNoForecastsViews(getResources().getString(R.string.no_forecasts_all));
    }

    private void showNoForecastsViews(String mainText) {
        mForecastsView.setVisibility(View.GONE);
        mNoForecastsView.setVisibility(View.VISIBLE);
        mNoForecastsMainView.setText(mainText);
    }
    //endregion


    //region BaseView implementation
    @Override
    public void setPresenter(CitiesForecastContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
    //endregion

    //region CitiesForecastAdapter.CitiesForecastAdapterOnClickHandler implementation
    @Override
    public void onClick(CityForecast forecast) {
        mPresenter.openForecastDetails(forecast);
    }
    //endregion
}
