package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.almasb.fxgl.ui.FontType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import nl.hsleiden.joshuabloch.game.EntityManager;
import nl.hsleiden.joshuabloch.game.EntityType;
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
        settings.setDeveloperMenuEnabled(true);
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
       getSaveLoadService().addHandler(new SaveLoadHandler() {
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
        });
    }


    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new EntityManager());

        Level level = setLevelFromMap("tmx/level_2.tmx");

        player = spawn("player", 50, 50);

        spawn("background");

        Viewport viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 130 * 32, getAppHeight());
        viewport.bindToEntity(player, getAppWidth() / 2f, getAppHeight() /2f);
        viewport.setLazy(true);
    }

    @Override
    protected void initPhysics() {
        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.COIN, (player, coin) -> {
            coin.removeFromWorld();
            inc("coin", 1);
            //TODO play coin pick up sound here
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.SACK, (player, coin) -> {
            coin.removeFromWorld();
            inc("coin", 5);
            //TODO play sack pick up sound here
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.SCALPER, (player, scalper) -> {
            System.out.println("register");
            if(player.getComponent(PlayerComponent.class).isInvincible()) return; //Don't register hits when the player has just respawned
            System.out.println("Hit");
            inc("coin", -2);
            if(geti("coin") < 0) set("coin", 0);
            //TODO play scalper hit sound here
        });
    }

    @Override
    protected void initUI() {
        Font font = getUIFactoryService().newFont(FontType.GAME, 28);
        Text moneyText = new Text();
        Text moneyDesc = new Text();
        moneyDesc.setFont(font);
        moneyText.setFont(font);
        moneyDesc.setTranslateY(100);
        moneyDesc.setTranslateX(30);
        moneyText.setTranslateX(100);
        moneyText.setTranslateY(100);
        moneyDesc.textProperty().set("Coins:");
        moneyText.textProperty().bind(getWorldProperties().intProperty("coin").asString());
        getGameScene().addUINode(moneyText);
        getGameScene().addUINode(moneyDesc);


        getGameWorld().getProperties().addListener("coin", (old, value) -> {
            int change = (int)value - (int) old;
            System.out.println(change);
            Text changeText = getUIFactoryService().newText((change > 0 ? "+" : "") + change, change > 0 ? Color.GREEN : Color.RED, 28);
            changeText.setTranslateY(180);
            changeText.setTranslateX(30);
            getGameScene().addUINode(changeText);
            FXGL.animationBuilder()
                    .interpolator(Interpolators.LINEAR.EASE_IN())
                    .duration(Duration.seconds(3))
                    .autoReverse(true)
                    .translate(changeText)
                    .from(new Point2D(30, 180))
                    .to(new Point2D(30, 140))
                    .buildAndPlay();
            FXGL.animationBuilder()
                    .interpolator(Interpolators.SMOOTH.EASE_IN_OUT())
                    .delay(Duration.seconds(3))
                    .duration(Duration.seconds(2))
                    .onCycleFinished(() -> { getGameScene().removeUINode(changeText); })
                    .autoReverse(true)
                    .fadeOut(changeText)
                    .buildAndPlay();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
