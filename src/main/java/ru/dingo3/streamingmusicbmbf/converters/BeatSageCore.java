package ru.dingo3.streamingmusicbmbf.converters;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import ru.dingo3.streamingmusicbmbf.network.HttpRequest;

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

public class BeatSageCore {
    private Difficulty difficulty;
    private GameModes gameModes;
    private SongEvent songEvent;
    private Environments environments;
    private ModelVersion modelVersion;

    private HttpRequest request;

    public BeatSageCore() {
        this.difficulty = new Difficulty();
        this.gameModes = new GameModes();
        this.songEvent = new SongEvent();
        this.environments = Environments.DEFAULT;
        this.modelVersion = ModelVersion.MODEL_V2;
        this.request = new HttpRequest();
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

//    public void generate() {
//        try {
//            String url = "https://beatsage.com/beatsaber_custom_level_create";
//            String data = "difficulty=" + this.difficulty.isNormal() + "&difficulty=" + this.difficulty.isHard() + "&difficulty=" + this.difficulty.isExpert() + "&difficulty=" + this.difficulty.isExpertPlus() + "&gameMode=" + this.gameModes.isStandard() + "&gameMode=" + this.gameModes.isOneSaber() + "&gameMode=" + this.gameModes.isNoArrows() + "&gameMode=" + this.gameModes.isNinetyDegree() + "&gameMode=" + this.gameModes.isThreeSixtyDegree() + "&songEvent=" + this.songEvent.isBombs() + "&songEvent=" + this.songEvent.isObstacles() + "&songEvent=" + this.songEvent.isDotBlocks() + "&environment=" + this.environments.getName() + "&modelVersion=" + this.modelVersion.getDetails();
//            String response = this.request.post(url, data);
//            JSONObject json = new JSONObject(response);
//            String downloadUrl = json.getString("downloadUrl");
//            String songName = json.getString("songName");
//            String songAuthorName = json.getString("songAuthorName");
//            String songSubName = json.getString("songSubName");
//            String songCover = json.getString("songCover");
//            String songHash = json.getString("songHash");
//            String levelAuthorName = json.getString("levelAuthorName");
//            String difficulty = json.getString("difficulty");
//            String bpm = json.getString("bpm");
//            String duration = json.getString("duration");
//            String notes = json.getString("notes");
//            String obstacles = json.getString("obstacles");
//            String events = json.getString("events");
//            String version = json.getString("version");
//            String download = this.request.get(downloadUrl);
//            String fileName = songName + " - " + songAuthorName + " - " + songSubName + " (" + levelAuthorName + ") [" + difficulty + "].zip";
//            Path path = Paths.get(fileName);
//            Files.write(path, download.getBytes(StandardCharsets.UTF_8));
//            System.out.println("Downloaded " + fileName);
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
}
