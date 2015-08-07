package alexym.com.sunshineapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.TextView;

import alexym.com.sunshineapp.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    final String TAG = "DetailActivityFragment";
    private static final String FORECAST_SHARE_HASHTAG = "#SushineApp";
    private static final int FORECAST_LOADER = 0;
    TextView forecastText;

    private ShareActionProvider mShareActionProvider;
    private String mForecast;
    Uri uri;


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


        return V;
    }

    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        //shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            forecastText = (TextView) getView().findViewById(R.id.textview_info);
            uri = intent.getData();
            //Log.i(TAG,"es "+mForecastStr);
            getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        }

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";

        return new CursorLoader(getActivity(),
                uri,
                ForecastFragment.FORECAST_COLUMNS,
                null,
                null,
                sortOrder);
    }//HOLA SOY CODIGO MALICIOSO STRING 'BORRAR TODO'; ME LA PELAN MUAJAJAJAJAJAJAJAAJAJAJAJAJAJAJA

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
