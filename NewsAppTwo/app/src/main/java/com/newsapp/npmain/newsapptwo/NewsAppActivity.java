package com.newsapp.npmain.newsapptwo;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsAppActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsReport>>
{
    private static final int PAGE_SIZE = 16;
    private static final String NUM_PAGES = "1";
    private static final int NEWSREPORT_LOADER_ID = 1;
    private TextView emptyTextView;
    private ProgressBar newsReportProgressBar;
    private ListView newsReportsListView;
    private NewsReportAdapter newsReportAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_app);
        final Context context = getApplicationContext();
        newsReportsListView = findViewById(R.id.list);
        emptyTextView = findViewById(R.id.empty_view);
        newsReportProgressBar = findViewById(R.id.loading_spinner);
        if (isConnected())
        {
            newsReportAdapter = new NewsReportAdapter(this, new ArrayList<NewsReport>());
            newsReportsListView.setAdapter(newsReportAdapter);
            newsReportsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
                {
                    NewsReport currentNewsReport = newsReportAdapter.getItem(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(currentNewsReport.getWebUri())));
                }
            });
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWSREPORT_LOADER_ID, null, this);
        }
        else
        {
            newsReportProgressBar.setVisibility(View.GONE);
            emptyTextView.setText(context.getString(R.string.no_network));
        }

    }

    @Override
    // Initialize the News Report menu
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.query_settings)
        {
            Intent querySettingsIntent = new Intent(this, NewsReportQuerySettingsActivity.class);
            startActivity(querySettingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected()
    {
        boolean isNetworkUp = true;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE))
        {
            isNetworkUp = false;
        }
        return isNetworkUp;
    }

    @Override
    public Loader<List<NewsReport>> onCreateLoader(int id, Bundle args)
    {
        final Context context = getApplicationContext();
        String url = createURLString(context);
        return new NewsReportLoader(context, url);
    }

    private String createURLString(Context context)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date beginDate = calendar.getTime();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String urlAPI = context.getString(R.string.guardian_api_url);
        String urlSearchTerm = sharedPreferences.getString(
                getString(R.string.query_settings_search_term_key),
                getString(R.string.query_settings_search_term_default));
        String urlOrderBy = sharedPreferences.getString(
                getString(R.string.query_settings_order_by_key),
                getString(R.string.query_settings_order_by_default));
        String urlPageSize = sharedPreferences.getString(
                getString(R.string.query_settings_page_size_key),
                getString(R.string.query_settings_page_size_default));
        String urlPages = NUM_PAGES;
        String urlFromDate = dateFormatter.format(beginDate);
        String urlToDate = dateFormatter.format(new Date());
        String urlContributor = getString(R.string.contributor);
        String urlAPIKey = BuildConfig.ApiKey;
        Uri baseUri = Uri.parse(urlAPI);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendQueryParameter(getString(R.string.query),urlSearchTerm);
        builder.appendQueryParameter(getString(R.string.order_by),urlOrderBy);
        builder.appendQueryParameter(getString(R.string.page_size), urlPageSize);
        builder.appendQueryParameter(getString(R.string.pages), urlPages);
        builder.appendQueryParameter(getString(R.string.from_date), urlFromDate);
        builder.appendQueryParameter(getString(R.string.to_date), urlToDate);
        builder.appendQueryParameter(getString(R.string.show_tag), urlContributor);
        builder.appendQueryParameter(getString(R.string.api_key), urlAPIKey);
        return builder.toString();
    }

    @Override
    public void onLoadFinished(Loader<List<NewsReport>> loader, List<NewsReport> data)
    {
        final Context context = getApplicationContext();
        newsReportProgressBar.setVisibility(View.GONE);
        newsReportAdapter.clear();
        if ( data != null && !data.isEmpty())
        {
            newsReportAdapter.addAll(data);
        }
        else if (!isConnected())
        {
            emptyTextView.setText(context.getString(R.string.no_network));
        }
        else
        {
            emptyTextView.setText(context.getString(R.string.no_articles));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsReport>> loader)
    {
        newsReportAdapter.clear();
    }
}
