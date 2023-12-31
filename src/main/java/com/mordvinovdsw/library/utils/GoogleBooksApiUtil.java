package com.mordvinovdsw.library.utils;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.mordvinovdsw.library.models.BookData;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleBooksApiUtil {

    public static BookData fetchBookDataByISBN(String isbn) {
        BookData bookData = new BookData();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            String jsonResponse = makeApiRequest(isbn);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray items = jsonObject.getJSONArray("items");

            if (items.length() > 0) {
                JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");
                JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

                bookData.setTitle(volumeInfo.optString("title", "No title provided"));
                bookData.setAuthors(jsonArrayToList(volumeInfo.optJSONArray("authors")));
                bookData.setGenres(jsonArrayToList(volumeInfo.optJSONArray("categories")));

                for (int i = 0; i < industryIdentifiers.length(); i++) {
                    JSONObject identifier = industryIdentifiers.getJSONObject(i);
                    String type = identifier.optString("type");
                    String id = identifier.optString("identifier");
                    if ("ISBN_10".equals(type)) {
                        bookData.setIsbn10(id);
                    } else if ("ISBN_13".equals(type)) {
                        bookData.setIsbn13(id);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bookData;
    }

    private static String makeApiRequest(String isbn) throws Exception {
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } finally {
            connection.disconnect();
        }

        return response.toString();
    }

    private static List<String> jsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.optString(i));
            }
        }
        return list;
    }
}