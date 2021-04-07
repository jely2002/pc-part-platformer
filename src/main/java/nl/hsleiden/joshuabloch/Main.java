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
import nl.hsleiden.joshuabloch.game.PlayerComponent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {

    private Entity player;

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
        settings.setProfilingEnabled(true);
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
        HashMap<Integer, Integer> highscores = new HashMap<>();
        //highscores.put("")
        vars.put("highscores", new HashMap<Integer, Integer>());
        vars.put("progress", 0);
        vars.put("coin", 0);
    }

    @Override
    protected void onPreInit() {
       /* getSaveLoadService().addHandler(new SaveLoadHandler() {
            @Override
            public void onSave(DataFile data) {
                // create a new bundle to store your data
                Bundle bundle = new Bundle("gameData");

                // store some data
                double time = getd("time");
                bundle.put("time", time);

                // give the bundle to data file
                data.putBundle(bundle);
            }

            @Override
            public void onLoad(DataFile data) {
                // get your previously saved bundle
                var bundle = data.getBundle("gameData");

                // retrieve some data
                double time = bundle.get("time");

                // update your game with saved data
                set("time", time);
            }
        });*/
    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new EntityManager());

        Level level = setLevelFromMap("tmx/level2.tmx");

        System.out.println(level.getProperties().getString("menuName"));

        player = spawn("player", 550, 50);

        spawn("background");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2f, getAppHeight() /2f);
        viewport.setLazy(true);
    }

    @Override
    protected void initPhysics() {
      onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.COIN,(player, coin)-> {
           coin.removeFromWorld();
           inc("coin", 1);
      });  
    }

    @Override
    protected void initUI() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
