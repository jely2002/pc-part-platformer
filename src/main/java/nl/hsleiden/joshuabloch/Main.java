package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.input.KeyCode;
import nl.hsleiden.joshuabloch.game.PlayerComponent;
import nl.hsleiden.joshuabloch.menu.SceneManager;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Main extends GameApplication {

    public Entity player;
    private ScoreCounter scoreCounter;
    private LevelManager levelManager;
    private SceneManager sceneManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        scoreCounter = new ScoreCounter();
        levelManager = new LevelManager(scoreCounter, this);
        sceneManager = new SceneManager(levelManager);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setAppIcon("icon.png");
        settings.setTitle("PC builder : 2021");
        settings.setVersion("1.0");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(sceneManager);
        settings.setFullScreenAllowed(true);
        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void onPreInit() {
        levelManager.addSaveHandler();
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
        vars.put("coin", 0);
    }

    @Override
    protected void initGame() {
        levelManager.initGame();
    }

    @Override
    protected void initPhysics() {
        new Collisions(scoreCounter, levelManager, sceneManager);
    }

    @Override
    protected void initUI() {
        scoreCounter.init();
    }

}
