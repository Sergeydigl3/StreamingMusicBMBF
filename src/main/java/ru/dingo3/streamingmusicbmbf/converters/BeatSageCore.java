package ru.dingo3.streamingmusicbmbf.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import ru.dingo3.streamingmusicbmbf.network.HttpRequest;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Setter
@Getter
class Difficulty {
    private boolean normal;
    private boolean hard;
    private boolean expert;
    private boolean expertPlus;
}

@Setter
@Getter
class GameModes {
    private boolean standard;
    private boolean oneSaber;
    private boolean noArrows;
    private boolean ninetyDegree;
    private boolean threeSixtyDegree;
}

@Setter
@Getter
class SongEvent {
    private boolean bombs;
    private boolean obstacles;
    private boolean dotBlocks;
}

@AllArgsConstructor
enum Environments {
    DEFAULT("Default"),
    ORIGINS("Origins"),
    TRIANGLE("Triangle"),
    NICE("Nice"),
    BIG_MIRROR("Big Mirror"),
    IMAGINE_DRAGONS("Imagine Dragons"),
    KDA("K/DA"),
    MONSTER_CAT("Monstercat"),
    CRAB_RAVE("Crab Rave"),
    PANIC_AT_THE_DISCO("Panic! At The Disco"),
    ROCKET_LEAGUE("Rocket League"),
    GREEN_DAY("Green Day"),
    GREEN_DAY_GRENADE("Green Day - Grenade"),
    TIMBALAND("Timbaland"),
    FITBEAT("FitBeat"),
    LINKIN_PARK("Linkin Park");

    @Getter
    private String name;
}

@AllArgsConstructor
enum ModelVersion {
    MODEL_V1("V1 (More unpredictable, fewer modes)"),
    MODEL_V2("V2"),
    MODEL_V2_FLOW("V2-Flow (Better flow, less creativity)");

    @Getter
    private String details;
}

public class BeatSageCore implements AbstractConverter {
    private String mapsPath;
    private Difficulty difficulty;
    private GameModes gameModes;
    private SongEvent songEvent;
    private Environments environments;
    private ModelVersion modelVersion;


    public BeatSageCore() {
        this.difficulty = new Difficulty();
        this.gameModes = new GameModes();
        this.songEvent = new SongEvent();
        this.environments = Environments.DEFAULT;
        this.modelVersion = ModelVersion.MODEL_V2;
    }

    public void setDifficulty(boolean normal, boolean hard, boolean expert, boolean expertPlus) {
        this.difficulty.setNormal(normal);
        this.difficulty.setHard(hard);
        this.difficulty.setExpert(expert);
        this.difficulty.setExpertPlus(expertPlus);
    }

    public void setGameModes(boolean standard, boolean oneSaber, boolean noArrows, boolean ninetyDegree, boolean threeSixtyDegree) {
        this.gameModes.setStandard(standard);
        this.gameModes.setOneSaber(oneSaber);
        this.gameModes.setNoArrows(noArrows);
        this.gameModes.setNinetyDegree(ninetyDegree);
        this.gameModes.setThreeSixtyDegree(threeSixtyDegree);
    }

    public void setSongEvent(boolean bombs, boolean obstacles, boolean dotBlocks) {
        this.songEvent.setBombs(bombs);
        this.songEvent.setObstacles(obstacles);
        this.songEvent.setDotBlocks(dotBlocks);
    }

    public void setEnvironments(Environments environments) {
        this.environments = environments;
    }

    public void setModelVersion(ModelVersion modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String generate(String file_path) throws IOException {
        String requestUrl = "https://beatsage.com/beatsaber_custom_level_create"; // Replace this with the actual URL

        // Create the boundary string
        String boundary = "----WebKitFormBoundaryoznqXU2JYvxeyPD8";

        // Prepare the request body
        StringBuilder requestBody = new StringBuilder();

        // Add audio_file part
        File audioFile = new File(file_path); // Replace with the actual audio file path
        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"audio_file\"; filename=\"")
                .append(audioFile.getName()).append("\"\r\n");
        requestBody.append("Content-Type: audio/mpeg\r\n");
        requestBody.append("\r\n");

        // Add audio_file data
        FileInputStream audioFileStream = new FileInputStream(audioFile);
        ByteArrayOutputStream audioBytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = audioFileStream.read(buffer)) != -1) {
            audioBytes.write(buffer, 0, bytesRead);
        }
        requestBody.append(new String(audioBytes.toByteArray(), "ISO-8859-1"));
        requestBody.append("\r\n");

        // Add other form data parts
        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"audio_metadata_title\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("VDV - Hello from the Sky! (Rock Version)  ВДВ - С неба привет! (Рок версия).mp3\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"audio_metadata_artist\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("VDV\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"difficulties\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("ExpertPlus,Normal,Hard,Expert\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"modes\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("Standard,NoArrows,OneSaber,90Degree\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"events\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("DotBlocks,Bombs,Obstacles\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"environment\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("DefaultEnvironment\r\n");

        requestBody.append("--").append(boundary).append("\r\n");
        requestBody.append("Content-Disposition: form-data; name=\"system_tag\"\r\n");
        requestBody.append("\r\n");
        requestBody.append("v2-flow\r\n");

        requestBody.append("--").append(boundary).append("--\r\n");

        // Prepare the connection and send the request
        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);
        // response to json
        connection.setRequestProperty("Accept", "application/json");

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.toString().getBytes("ISO-8859-1"));
        outputStream.flush();
        outputStream.close();

        // Get the response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // parse id from json
        JSONObject jsonObject = new JSONObject(response.toString());
        String id = jsonObject.getString("id");
        return id;
    }


    // Url for heartbeat https://beatsage.com/beatsaber_custom_level_heartbeat/bd4eb26ba4a74af5a2675ef97c873082
    // This is get request with json response {"status":"PENDING"} or {"status":"DONE"}
    public void waitResponse(String id) throws IOException, InterruptedException {
        String url = "https://beatsage.com/beatsaber_custom_level_heartbeat/" + id;
        while (true) {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            String status = jsonObject.getString("status");
            if (status.equals("DONE")) {
                System.out.println("DONE");
                break;
            }
            System.out.println("PENDING");
            Thread.sleep(1000);
        }
    }

    public void downloadFile(String id, String downloadPath) throws IOException {
        String url = "https://beatsage.com/beatsaber_custom_level_download/" + id;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/zip");

        InputStream inputStream = connection.getInputStream();
//        FileOutputStream fileOutputStream = new FileOutputStream("cache/" + id + ".zip");
        FileOutputStream fileOutputStream = new FileOutputStream(downloadPath);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        fileOutputStream.close();
        inputStream.close();
    }


    public static void main(String[] args) throws IOException {
        try {
//            main1();
//            waitMain2();
            main3();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main1() throws IOException {
        BeatSageCore beatSageCore = new BeatSageCore();
        beatSageCore.setDifficulty(true, true, true, true);
        beatSageCore.setGameModes(true, true, true, true, true);
        beatSageCore.setSongEvent(true, true, true);
        beatSageCore.setEnvironments(Environments.DEFAULT);
        beatSageCore.setModelVersion(ModelVersion.MODEL_V2_FLOW);
//        beatSageCore.generate("cache/yandex/music/101308852.mp3", "cache/file.zip");
    }

    public static void waitMain2() throws IOException, InterruptedException {
        BeatSageCore beatSageCore = new BeatSageCore();
        beatSageCore.waitResponse("bd4eb26ba4a74af5a2675ef97c873082");
        System.out.println("DONE");
    }

    public static void main3() {
        BeatSageCore beatSageCore = new BeatSageCore();
        try {
            String id = beatSageCore.generate("cache/yandex/music/101308852.mp3");
            System.out.println(id);
            beatSageCore.waitResponse(id);
            System.out.println("DONE");
//            beatSageCore.downloadFile(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        beatSageCore.downloadFile("bd4eb26ba4a74af5a2675ef97c873082");
    }

    @Override
    public void convertTrack(String providerId, BaseTrack track) {
        try {
            String id = generate(track.getLocalPath());
            waitResponse(id);
            String downloadPath = getMapsPath() + "/" + providerId + "-" +track.getArtist() + "-" + track.getTitle() + " (" + track.getId()+ ")" + ".zip";
            System.out.println(downloadPath);
            downloadFile(id, downloadPath);
            track.setLocalPath(downloadPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getConverterId() {
        return "beatsage";
    }

    @Override
    public String getConverterName() {
        return "BeatSage";
    }

    @Override
    public void setMapsPath(String mapsPath){
        this.mapsPath = mapsPath;
    }

    @Override
    public String getMapsPath(){
        return mapsPath;
    }
}
