package com.newsapp.npmain.newsapptwo;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

final class NewsAppUtils
{
    private static final String LOG_TAG = NewsAppUtils.class.getSimpleName() + "ERROR";
    private static final String HTTP_GET = "GET";
    private static final int TIME_OUT = 15000;
    private static final String FALLBACK_STRING = "Not Available";
    private static final String EMPTY_STRING = "";

    private NewsAppUtils() {
    }

    private static URL createUrl(Context context, String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_createurl), exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(Context context, URL url) throws IOException
    {
        String jsonResponse = EMPTY_STRING;
        if (url == null )
        {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        String method = "makeHttpRequest";

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_GET);
            urlConnection.setReadTimeout(TIME_OUT /* milliseconds */);
            urlConnection.setConnectTimeout(TIME_OUT /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, context.getString(R.string.newsreport_error_httprequest, responseCode));
            }
        } catch( SecurityException se) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_securityexception, method), se);
        }
        catch (IOException e) {

            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_ioexception, method), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<NewsReport> extractNewsReports(Context context, String url)
    {
        URL urlConnect = createUrl(context,url);
        String jsonResponse = EMPTY_STRING;
        String method = "extractNewsReport";
        try
        {
            jsonResponse = makeHttpRequest(context, urlConnect);

        }
        catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.newsreport_error_ioexception, method), e);
        }
        if (jsonResponse.equals(""))
        {
            return null;
        }
        else
        {
            ArrayList<NewsReport> newsReports = new ArrayList<>();
            try {
                 final String RESULTS = context.getString(R.string.guardian_api_json_results);
                 final String SECTION_NAME = context.getString(R.string.guardian_api_json_section_name);
                 final String WEB_TITLE = context.getString(R.string.guardian_api_json_web_title);
                 final String WEB_URL = context.getString(R.string.guardian_api_json_web_url);
                 final String TAGS = context.getString(R.string.guardian_api_json_tags);
                 final String FIRST_NAME = context.getString(R.string.guardian_api_json_contributor_firstname);
                 final String LAST_NAME = context.getString(R.string.guardian_api_json_contributor_lastname);
                 final String WEB_PUBLICATION_DATE = context.getString(R.string.guardian_api_json_date_published);
                 final String RESPONSE = context.getString(R.string.guardian_api_json_response);
                JSONObject reader = new JSONObject(jsonResponse);
                JSONObject response = reader.getJSONObject(RESPONSE);
                JSONArray results = response.getJSONArray(RESULTS);

                for (int i = 0; i < results.length(); i++)
                {
                    JSONObject result = results.getJSONObject(i);
                    String sectionName = result.optString(SECTION_NAME,FALLBACK_STRING);
                    String webTitle = result.optString(WEB_TITLE, FALLBACK_STRING);
                    String webURL = result.optString(WEB_URL, FALLBACK_STRING);
                    String webPublicationDate = result.optString(WEB_PUBLICATION_DATE, FALLBACK_STRING);
                    JSONArray tags = result.getJSONArray(TAGS);
                    JSONObject tag = tags.getJSONObject(0);
                    String authorFirstName = tag.optString(FIRST_NAME, FALLBACK_STRING);
                    String authorLastName = tag.optString(LAST_NAME, FALLBACK_STRING);
                    NewsReport newsReport = new NewsReport();
                    newsReport.setAuthorName(authorFirstName, authorLastName,FALLBACK_STRING);
                    newsReport.setSectionName(sectionName);
                    newsReport.setTitle(webTitle);
                    newsReport.setWebUri(webURL);
                    newsReport.setDatePublished(webPublicationDate);
                    newsReports.add(newsReport);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, context.getString(R.string.newsreport_error_jsonexception, method), e);
            }
            return newsReports;
        }

    }
}
