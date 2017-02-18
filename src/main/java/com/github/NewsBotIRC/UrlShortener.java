package com.github.NewsBotIRC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Geronimo on 11/6/16.
 */
public class UrlShortener {

    public static String shortenUrl(String myUrl)
    {
        String shortenedUrl = myUrl;
        try {
            String encoded = URLEncoder.encode(myUrl, "UTF-8");

            URL url = new URL("http://qurl.org/api/url?url=" + encoded);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            JSONObject json = new JSONObject( reader.readLine() );
            shortenedUrl = json.optString("url");

        } catch (IOException | JSONException e) {
            System.out.println(e.getMessage());
        }
        return shortenedUrl;
    }
}