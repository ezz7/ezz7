package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryData {

    public static final String LOG_TAG = QueryData.class.getName();


    //Making Private Constructor
    private QueryData() {
    }

    //Get New URL Object
    private static URL createUrl(String web_url) {
        URL url = null;
        try {
            url = new URL(web_url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //Making HTTP Request
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Converting input stream to readable string
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

    //Getting all data needed from json response
    private static List<NewsItem> extractFeatureFromJson(String gurdianJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(gurdianJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<NewsItem> newsItemList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(gurdianJSON);

            // Getting the response Object
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "results",
            // which represents a list of results (or news).
            JSONArray newsArray = responseObject.getJSONArray("results");

            // For each news item  in the news array, create an object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentNewsItem = newsArray.getJSONObject(i);

                //Extract value for section name
                String section_name = currentNewsItem.getString("sectionName");

                //Extract value for type text
                String type = currentNewsItem.getString("type");

                //Extract value for Web Title
                String web_title = currentNewsItem.getString("webTitle");

                //Extract value for pillar name text
                String pillar_name = currentNewsItem.getString("pillarName");

                //Extract value for publication date
                String publication_date = currentNewsItem.getString("webPublicationDate");

                //Extract value for URL
                String url = currentNewsItem.getString("webUrl");

                //Creating instance of newsItem
                NewsItem newsItem = new NewsItem(section_name, type, web_title, pillar_name, publication_date, url);

                // Adding it to newsItemsList
                newsItemList.add(newsItem);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the NewsItem JSON results", e);
        }

        // Return the list of newsItems
        return newsItemList;
    }


    //Collecting all results in ArrayList
    public static List<NewsItem> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of news items
        List<NewsItem> newsItemList = extractFeatureFromJson(jsonResponse);

        // Return the list of newsItems
        return newsItemList;
    }

}
