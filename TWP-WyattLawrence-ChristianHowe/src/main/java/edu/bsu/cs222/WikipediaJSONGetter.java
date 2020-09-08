package edu.bsu.cs222;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class WikipediaJSONGetter {


    public JsonObject JSONGetter(String searchText) {
        String encoding = "UTF-8";
        searchText.replace(" ","_");
        String urlOpener = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=revisions&titles=";
        String urlCloser = "&rvprop=timestamp|user&rvlimit=24&redirects";
        String fullurl = urlOpener + searchText + urlCloser;

        try {
            URLEncoder.encode(fullurl, encoding);
        } catch (UnsupportedEncodingException e){
            return null;
        }
        try {
            JsonParser parser = new JsonParser();
            URL url = new URL(fullurl);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Revision Tracker/0.1 (welawrence@bsu.edu)");
            InputStream inputStream = connection.getInputStream();
            JsonElement rootElement = parser.parse(new InputStreamReader(inputStream));
            JsonObject pages = rootElement.getAsJsonObject().getAsJsonObject("query").getAsJsonObject("pages");
            return pages;
        } catch (IOException e) {
            System.out.println("Connection not found.");
            return null;
        }
    }
}