package com.example.android.newsfeed;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {
    //****Global Variables****
    private String myUrl;

    public NewsItemLoader(@NonNull Context context, String url) {
        super(context);
        myUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<NewsItem> loadInBackground() {
        List<NewsItem> newsItemList = QueryData.fetchEarthquakeData(myUrl);
        return newsItemList;
    }
}
