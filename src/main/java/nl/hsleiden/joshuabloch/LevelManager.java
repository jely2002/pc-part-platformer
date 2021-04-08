package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import nl.hsleiden.joshuabloch.game.EntityManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static com.almasb.fxgl.dsl.FXGL.*;

public class LevelManager {

    public int levelProgress = 0;
    private String name;
    private Level currentLevel;
    private HashMap<Integer, Integer> highscores = new HashMap<>();

    public void addHandler() {
        getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(@NotNull DataFile data) {
                Bundle bundle;
                try {
                    bundle = data.getBundle(name);
                } catch(IllegalArgumentException e) {
                    bundle = new Bundle(name);
                }
                System.out.println(bundle.getName());
                bundle.put("progress", levelProgress);
                bundle.put("highscores", highscores);
                data.putBundle(bundle);
            }

            @Override
            public void onLoad(@NotNull DataFile data) {
                Bundle bundle = data.getBundle(name);
                levelProgress = bundle.get("progress");
                highscores = bundle.get("highscores");
                System.out.println((int) data.getBundle(name).get("progress"));
            }
        });
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighScore(int levelID) {
        return highscores.getOrDefault(levelID, 0);
    }

    public void loadSave() {
        getSaveLoadService().readAndLoadTask("highscores.sav").run();
    }

    public void writeSave() {
       getSaveLoadService().saveAndWriteTask("highscores.sav").run();
    }

    public void finished(int coins) {
        highscores.put(levelProgress, coins);
        levelProgress++;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void start() {
        getGameWorld().addEntityFactory(new EntityManager(this));
        currentLevel = setLevelFromMap("tmx/level_" + (levelProgress + 1) + ".tmx");
        spawn("background");
    }

}
