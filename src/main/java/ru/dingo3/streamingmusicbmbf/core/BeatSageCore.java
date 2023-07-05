package ru.dingo3.streamingmusicbmbf.core;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

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



    public BeatSageCore() {
        this.difficulty = new Difficulty();
        this.gameModes = new GameModes();
        this.songEvent = new SongEvent();
        this.environments = Environments.DEFAULT;
        this.modelVersion = ModelVersion.MODEL_V2;
    }

}
