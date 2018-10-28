package com.newsapp.npmain.newsapptwo;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsReportLoader extends AsyncTaskLoader<List<NewsReport>>
{
    private final String url;
    private final Context context;

    public NewsReportLoader(Context context, String url)
    {
        super(context);
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onStartLoading()
    {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<NewsReport> loadInBackground()
    {
       if (url == null)
       {
           return null;
       }
       List<NewsReport> newsReports = NewsAppUtils.extractNewsReports(context,url);
       return newsReports;
    }
}
