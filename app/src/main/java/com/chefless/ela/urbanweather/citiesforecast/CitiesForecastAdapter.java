package com.chefless.ela.urbanweather.citiesforecast;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chefless.ela.urbanweather.data.CityForecast;
import com.chefless.ela.urbanweather.databinding.ForecastListItemBinding;

import java.util.List;

public class CitiesForecastAdapter extends RecyclerView.Adapter<CitiesForecastAdapter.CitiesForecastAdapterViewHolder>
{
    private List<CityForecast> mData;

    /** An on click handler added for the activity for interact with the recycler viw*/
    private final CitiesForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface CitiesForecastAdapterOnClickHandler {
        void onClick(CityForecast forecast);
    }

    public CitiesForecastAdapter(CitiesForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public CitiesForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ForecastListItemBinding binding = ForecastListItemBinding.inflate(inflater, parent, false);
        return new CitiesForecastAdapterViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(CitiesForecastAdapterViewHolder holder, int position) {
        CityForecast item = mData.get(position);
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }

    public void setData(List<CityForecast> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class CitiesForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ForecastListItemBinding binding;

        public CitiesForecastAdapterViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            CityForecast forecast = mData.get(position);
            mClickHandler.onClick(forecast);
        }

        // We'll use this field to showcase matching the holder from the test.
        private boolean mIsInTheMiddle = false;

        public boolean getIsInTheMiddle() {
            return mIsInTheMiddle;
        }
        public void setIsInTheMiddle(boolean isInTheMiddle) {
            mIsInTheMiddle = isInTheMiddle;
        }
    }
}
