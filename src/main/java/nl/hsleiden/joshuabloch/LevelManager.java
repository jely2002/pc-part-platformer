package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import nl.hsleiden.joshuabloch.game.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Function;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LevelManager {

    public int levelProgress = 0;
    public int currentLevelID = 0;
    private String name;
    private Level currentLevel;
    private HashMap<Integer, Integer> highscores = new HashMap<>();

    private final ScoreCounter scoreCounter;
    private final Main main;

    public LevelManager(ScoreCounter scoreCounter, Main main) {
        this.scoreCounter = scoreCounter;
        this.main = main;
    }

    public void addSaveHandler() {
        getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(@NotNull DataFile data) {
                Bundle bundle;
                try {
                    bundle = new Bundle(name);
                    bundle.put("progress", levelProgress);
                    bundle.put("highscores", highscores);
                    data.putBundle(bundle);
                } catch(IllegalArgumentException e) {
                    bundle = data.getBundle(name);
                    bundle.put("progress", levelProgress);
                    bundle.put("highscores", highscores);
                    data.putBundle(bundle);
                }
            }

            @Override
            public void onLoad(@NotNull DataFile data) {
                Bundle bundle;
                try {
                    bundle = data.getBundle(name);
                    levelProgress = bundle.get("progress");
                    highscores = bundle.get("highscores");
                } catch (Exception e) {
                    System.out.println("Standard values");
                    levelProgress = 0;
                    highscores = new HashMap<>();
                }
            }
        });
    }

    public void setName(String name, Function<Void, Void> callback) {
        this.name = name;
        loadSave(r -> callback.apply(null));
    }

    public String getName() {
        return name;
    }

    public Integer getHighScore(int levelID) {
        return highscores.getOrDefault(levelID, null);
    }

    public void loadSave(Function<Void, Void> callback) {
        if(getFileSystemService().exists("highscores.sav")) {
            System.out.println("exists");
            getSaveLoadService().readAndLoadTask("highscores.sav").thenWrap(n -> callback.apply(null)).run();
        } else {
            System.out.println("no exist");
            levelProgress = 0;
            highscores = new HashMap<>();
            writeSave();
            System.out.println("Standard values");
            callback.apply(null);
        }
    }

    public void writeSave() {
       getSaveLoadService().saveAndWriteTask("highscores.sav").run();
    }

    public void finished(int coins) {
        if(getHighScore(currentLevelID) != null && getHighScore(currentLevelID) < coins) {
            highscores.put(currentLevelID, coins);
        } else if(getHighScore(currentLevelID) == null) {
            highscores.put(currentLevelID, coins);
        }
        if(currentLevelID == levelProgress && levelProgress != 3) levelProgress++; //Do not increment when playing already unlocked levels
        writeSave();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void start(int levelID) {
        currentLevelID = levelID;
        FXGL.getGameController().startNewGame();
    }

    public void initGame() {
        getGameWorld().addEntityFactory(new EntityManager(this));
        currentLevel = setLevelFromMap("tmx/level_" + (currentLevelID + 1) + ".tmx");
        //currentLevel = setLevelFromMap("tmx/level_1.tmx");

        main.player = spawn("player", 50, 50);

        scoreCounter.showCost(Integer.parseInt(currentLevel.getProperties().getString("cost")));

        spawn("background");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 130 * 32, getAppHeight());
        viewport.bindToEntity(main.player, getAppWidth() / 2f, getAppHeight() /2f);
        viewport.setLazy(true);
    }

}
