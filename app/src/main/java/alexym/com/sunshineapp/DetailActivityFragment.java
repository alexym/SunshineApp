package alexym.com.sunshineapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import alexym.com.sunshineapp.data.WeatherContract.WeatherEntry;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    final String TAG = "DetailActivityFragment";
    private static final String FORECAST_SHARE_HASHTAG = "#SushineApp";
    private static final int DETAIL_LOADER = 0;
    TextView forecastText;

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    TextView dateTextView,dayTextView,highTextView,lowTextView,detailTextView,humidityTextView,windTextView,pressureTextView;
    ImageView iconImageView;

    private static final String[] FORECAST_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_WIND_SPEED = 6;
    private static final int COL_WEATHER_PRESSURE = 7;
    private static final int COL_WEATHER_DEGREES = 8;
    private static final int COL_WEATHER_ID_ICON = 9;



    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }else{
            Log.i(TAG,"Shared actionprovier is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.fragment_detail, container, false);
        dateTextView = (TextView) V.findViewById(R.id.detail_item_date_textview);
        dayTextView = (TextView) V.findViewById(R.id.detail_item_day_textview);
        highTextView = (TextView) V.findViewById(R.id.detail_item_max_textview);
        lowTextView = (TextView) V.findViewById(R.id.detail_item_min_textview);
        detailTextView = (TextView) V.findViewById(R.id.detail_item_forecast_textview);
        humidityTextView = (TextView) V.findViewById(R.id.detail_item_humidity_textview);
        windTextView = (TextView) V.findViewById(R.id.detail_item_wind_textview);
        pressureTextView = (TextView) V.findViewById(R.id.detail_item_pressure_textview);
        iconImageView = (ImageView) V.findViewById(R.id.detail_item_icon);


        return V;
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();

        if (intent == null) {
            return null;

        }


        return new CursorLoader(
                getActivity(),
                intent.getData(),
                FORECAST_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(TAG, "In onLoadFinished");
        if (!data.moveToFirst()) { return; }

//        String dateString = Utility.formatDate(
//                );
        iconImageView.setImageResource(Utility.getArtResourceForWeatherCondition(data.getInt(COL_WEATHER_ID_ICON)));
        dateTextView.setText(Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE)));
        dayTextView.setText(Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_WEATHER_DATE)));
        boolean isMetric = Utility.isMetric(getActivity());
        String high = Utility.formatTemperature(getActivity(),
                data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
        highTextView.setText(high);
        String low = Utility.formatTemperature(getActivity(),
                data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);
        lowTextView.setText(low);
        String weatherDescription =
                data.getString(COL_WEATHER_DESC);
        detailTextView.setText(weatherDescription);
        humidityTextView.setText(String.format(getActivity().getString(R.string.format_humidity), data.getFloat(COL_WEATHER_HUMIDITY)));
        windTextView.setText(Utility.getFormattedWind(
                getActivity(),
                data.getFloat(COL_WEATHER_WIND_SPEED),
                data.getFloat(COL_WEATHER_DEGREES)));
        pressureTextView.setText(String.format(getActivity().getString(R.string.format_pressure), data.getFloat(COL_WEATHER_PRESSURE)));

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
