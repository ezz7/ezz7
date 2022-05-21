package com.example.android.newsfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {
    /** URL for earthquake data from the USGS dataset */
    //I used uri builder to build the whole link
//    private static final String GURDIAN_NEWS_URL =
//            "https://content.guardianapis.com/search?page=2&q=debate&api-key=test";

    private NewsItemAdapter mAdapter;
    TextView empty_status_text;
    View progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the list view
        ListView newsItemsList = findViewById(R.id.list);

        //Get new adapter
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());

        //Setting the adapter to listItem views
        newsItemsList.setAdapter(mAdapter);

        //Get the Empty status Text View
        empty_status_text = findViewById(R.id.empty_status_text);

        //Checking if there is no internet connection
        if(!isNetworkConnected()){
            empty_status_text.setText(R.string.no_internet_connection);
            //Removing the progress bar after data is shown
            progress_bar = findViewById(R.id.loading_indicator);
            progress_bar.setVisibility(View.GONE);
        }
        else{
            //***Getting Support manager and initializing the Loader***
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }


    //***Start of methods related to loader manager***
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        //Here Using uri builder to build the whole path of url
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("page", "2")
                .appendQueryParameter("q", "debate")
                .appendQueryParameter("api-key", "test");
        String myUrl = builder.build().toString();
        return new NewsItemLoader(this, myUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        //Removing the progress bar after data is shown
        progress_bar = findViewById(R.id.loading_indicator);
        progress_bar.setVisibility(View.GONE);
        if(data == null || data.isEmpty()){
            empty_status_text.setText(R.string.no_news_found);
        }

        mAdapter.clear();

        if(data != null && !data.isEmpty()){
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());
    }
    //***End of methods related to loader manager

    //check if the internet is connected
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}