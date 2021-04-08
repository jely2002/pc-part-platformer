package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import javafx.scene.input.KeyCode;
import nl.hsleiden.joshuabloch.game.EntityManager;
import nl.hsleiden.joshuabloch.game.EntityType;
import nl.hsleiden.joshuabloch.game.PlayerComponent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {

    private Entity player;
    private ScoreCounter scoreCounter;
    private LevelManager levelManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setAppIcon("icon.png");
        settings.setTitle("PC builder : 2021");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
        settings.setEnabledMenuItems(EnumSet.allOf(MenuItem.class));
        settings.setFullScreenAllowed(true);
        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void onPreInit() {
        scoreCounter = new ScoreCounter();
        levelManager = new LevelManager();
        /*getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(DataFile data) {
                // create a new bundle to store your data
                Bundle bundle = new Bundle("username");

                // store some data
                int progress = geti("progress");
                bundle.put("progress", progress);

                // give the bundle to data file
                data.putBundle(bundle);
            }

            @Override
            public void onLoad(DataFile data) {
                // get your previously saved bundle
                var bundle = data.getBundle("username");

                // retrieve some data
                double time = bundle.get("time");

                // update your game with saved data
                set("time", time);
            }
        });*/
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveLeft();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMovement();
            }
        }, KeyCode.A);

        getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).moveRight();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopMovement();
            }
        }, KeyCode.D);

        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).jump(250);
            }
        }, KeyCode.W);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("coin", 100);
    }

    @Override
    protected void initGame() {
        levelManager.addHandler();
        levelManager.setName("jelle");
        //TODO ask and set the player name
        levelManager.addHandler();
        levelManager.start();
        levelManager.finished(10);
        levelManager.writeSave();

        player = spawn("player", 50, 50);

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 130 * 32, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2f, getAppHeight() /2f);
        viewport.setLazy(true);
    }

    @Override
    protected void initPhysics() {
        new Physics(scoreCounter);
    }

    @Override
    protected void initUI() {
        scoreCounter.init();
    }

}
