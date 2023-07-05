package ru.dingo3.streamingmusicbmbf.core;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import ru.dingo3.streamingmusicbmbf.models.*;
import ru.dingo3.streamingmusicbmbf.models.AccountResponse.Result.AccountInfo;

import ru.dingo3.streamingmusicbmbf.models.DownloadResponse.DownloadInfo;

import ru.dingo3.streamingmusicbmbf.models.PlaylistResponse.Track;

import ru.dingo3.streamingmusicbmbf.models.PlaylistsResponse.Playlist;


import ru.dingo3.streamingmusicbmbf.network.HttpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.security.*;


public class YandexMusicClient {
    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String language = "ru";

    @Getter
    @Setter
    private String baseUrl = "https://api.music.yandex.net";
    @Getter
    private AccountInfo me;
    private HttpRequest request;

    public YandexMusicClient(String token) {
        this.token = token;

    }

    public void init() {
        this.request = new HttpRequest();
        this.request.setAuthorization(this.token);
        this.request.setLanguage(this.language);
        String device = "os=Python; os_version=; manufacturer=Marshal; model=Yandex Music API; clid=; device_id=random; uuid=random";
        this.request.setUserAgent(device);

        this.me = this.account_status();
    }


    public AccountInfo account_status() {

        String url = this.baseUrl + "/account/status";

        try {
            String response = this.request.sendGet(url);


            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(response, AccountResponse.class).getResult().getAccount();

        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    //    public Array<Playlist> getPlaylists() {
    public List<Playlist> getPlaylists() {
        return this.getPlaylists(this.me.getUid());
    }

    public List<Playlist> getPlaylists(int userId) {
        String url = this.baseUrl + "/users/" + userId + "/playlists/list";

        try {
            String response = this.request.sendGet(url);

            JSONObject jsonObject = new JSONObject(response);

            ObjectMapper objectMapper = new ObjectMapper();

            List<Playlist> playlists = objectMapper.readValue(response, PlaylistsResponse.class).getResult();
            // print all names
//            for (Playlist playlist : playlists) {
//                System.out.println(playlist.getTitle());
//                System.out.println(playlist.getKind());
//                System.out.println(playlist.getTracks());
//                System.out.println(playlist.getOgImage());
//                System.out.println("\n");
//            }
//                System.out.println(playlist.getTags());
//                System.out.println(playlist.getOwner());

//            System.out.println(playlists);
            return playlists;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }


    public List<Track> getPlaylistTracks(int playlistId, int userId) {
        String url = this.baseUrl + "/users/" + userId + "/playlists/" + playlistId;

        try {
            String response = this.request.sendGet(url);

            ObjectMapper objectMapper = new ObjectMapper();

            // print all names
//            for (Track track : tracks) {
//                System.out.println(track.getTrack().getTitle() + " - " + track.getTrack().getArtists().get(0).getName() + " - " + track.getTrack().getId());
//            }
            return objectMapper.readValue(response, PlaylistResponse.class).getResult().getTracks();
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public List<Track> getPlaylistTracks(int playlistId) {
        return this.getPlaylistTracks(playlistId, this.me.getUid());
    }

    public void downloadTrack(int trackId, Path savePath) {
        String url = this.baseUrl + "/tracks/" + trackId + "/download-info";

        try {
            String response = this.request.sendGet(url);
//            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();
//
            List<DownloadInfo> downloadInfoList = objectMapper.readValue(response, DownloadResponse.class).getResult();
            // find one mp3 with highest bitrate
            DownloadInfo downloadInfo = downloadInfoList.stream()
                    .filter(info -> info.getCodec().equals("mp3"))
                    .max((info1, info2) -> info1.getBitrateInKbps() - info2.getBitrateInKbps())
                    .get();

            response = this.request.sendGet(downloadInfo.getDownloadInfoUrl());

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            DownloadHashResponse downloadHashResponse = xmlMapper.readValue(response, DownloadHashResponse.class);

            String host = downloadHashResponse.getHost();
            String path = downloadHashResponse.getPath();
            String ts = downloadHashResponse.getTs();
            String s = downloadHashResponse.getS();

            String sign = "XGRlBW9FXlekgbPrRHuSiA" + path.substring(1) + s;
            byte[] bytesOfMessage = sign.getBytes(StandardCharsets.UTF_8);

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            StringBuilder hexString = new StringBuilder();
            for (byte b : theMD5digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String finalDownloadUrl = "https://" + host + "/get-mp3/" + hexString + "/" + ts + path;

            try (InputStream in = new URL(finalDownloadUrl).openStream()) {
                Files.copy(in, savePath);
                System.out.println("Downloaded " + savePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        String token = System.getenv("YM_TOKEN");
        YandexMusicClient client = new YandexMusicClient(token);

        client.init();

        System.out.println(client.getMe().getDisplayName());
        client.getPlaylists();

        client.getPlaylistTracks(1020);

        client.downloadTrack(84010387, Paths.get("C:\\Projects\\suai\\StreamingMusicBMBF\\cache\\music\\84010387.mp3"));

    }
}
