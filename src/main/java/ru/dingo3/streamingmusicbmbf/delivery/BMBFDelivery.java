package ru.dingo3.streamingmusicbmbf.delivery;

import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


//    POST /host/beatsaber/upload HTTP/1.1
//            Accept: application/json, text/plain, */*
//    Accept-Encoding: gzip, deflate
//    Accept-Language: ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6,zh-TW;q=0.5,zh;q=0.4
//    Connection: keep-alive
//    Content-Length: 1888803
//    Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryPATtSRFiUF2S1t6i
//    DNT: 1
//    Host: 192.168.1.104:50000
//    Origin: http://192.168.1.104:50000
//    Referer: http://192.168.1.104:50000/main/upload
//    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36
public class BMBFDelivery {
    private final String host;

    public BMBFDelivery(String host) {
        this.host = host;
    }

    public int deliver(BasePlaylist playlist, BaseTrack track) throws IOException {
        String url = "http://" + this.host + "/host/beatsaber/upload";
        System.out.println(url);
        String boundary = "----WebKitFormBoundaryPATtSRFiUF2S1t6i";

        StringBuilder requestBody = new StringBuilder();

        // Add audio_file part

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(track.getId()).append(".zip\"\r\n");
        requestBody.append("Content-Type: application/x-zip-compressed\r\n");
        requestBody.append("\r\n");
        System.out.println(requestBody.toString());
        File mapFile = new File(track.getMapPath());

        FileInputStream mapFileStream = new FileInputStream(mapFile);

        ByteArrayOutputStream mapBytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = mapFileStream.read(buffer)) != -1) {
            mapBytes.write(buffer, 0, bytesRead);
        }
        requestBody.append(new String(mapBytes.toByteArray(), "ISO-8859-1"));
        requestBody.append("\r\n");
        requestBody.append("--").append(boundary).append("--\r\n");

        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setDoOutput(true);
        con.setRequestProperty("Accept", "application/json, text/plain, */*");
        con.setRequestProperty("Accept-Encoding", "gzip, deflate");
        con.setRequestProperty("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6,zh-TW;q=0.5,zh;q=0.4");
        con.setRequestProperty("Connection", "keep-alive");
        con.setRequestProperty("Content-Length", String.valueOf(requestBody.length()));
        con.setRequestProperty("DNT", "1");
        con.setRequestProperty("Host", this.host);
        con.setRequestProperty("Origin", "http://" + this.host);
        con.setRequestProperty("Referer", "http://" + this.host + "/main/upload");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/" + (int) (Math.random() * 1000) + ".0.0.0 Safari/537.36");


        OutputStream outputStream = con.getOutputStream();
        outputStream.write(requestBody.toString().getBytes("ISO-8859-1"));
        outputStream.flush();
        outputStream.close();

        return con.getResponseCode();
    }
}
