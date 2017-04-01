package com.github.NewsBotIRC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.apache.logging.log4j.LogManager;

/**
 * Created by Geronimo on 11/6/16.
 */
public class UrlShortener {

    public static String shortenUrl(String myUrl)
    {
        String shortenedUrl = myUrl;
        try {
            String encoded = URLEncoder.encode(myUrl, "UTF-8");

            URL url = new URL("https://tinyurl.com/api-create.php?url=" + encoded);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            shortenedUrl = reader.readLine();

        } catch (IOException e) {
            LogManager.getLogger().error(e.getMessage());
        }
        return shortenedUrl;
    }
}