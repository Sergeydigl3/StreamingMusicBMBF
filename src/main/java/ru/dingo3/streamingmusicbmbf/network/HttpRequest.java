package ru.dingo3.streamingmusicbmbf.network;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequest {
    private Map<String, String> headers = new HashMap<String, String>();

    // constructor
    // public HttpRequest() {
    //     this.userAgent = "Yandex-Music-API";
    // }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public void setLanguage(String language) {
        this.setHeader("Accept-Language", language);
    }

    public void setAuthorization(String token) {
        this.setHeader("Authorization", "OAuth " + token);
    }

    public void setUserAgent(String userAgent) {
        this.setHeader("User-Agent", userAgent);
    }

    public static String convertCamelToSnake(String text) {
        String s = text.replaceAll("(.)([A-Z][a-z]+)", "$1_$2");
        return s.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }

    // HTTP GET request
    public String sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        // set headers
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }

        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        return response.toString();
    }

    // HTTP POST request
    public String sendPost(String url, String urlParameters, String postData) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // add request header
        con.setRequestMethod("POST");




        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        return response.toString();
    }
}