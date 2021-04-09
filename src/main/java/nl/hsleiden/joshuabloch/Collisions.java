package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import nl.hsleiden.joshuabloch.game.EntityType;
import nl.hsleiden.joshuabloch.menu.SceneManager;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Collisions {

    private final ScoreCounter scoreCounter;
    private final LevelManager levelManager;
    private final SceneManager sceneManager;

    public Collisions(ScoreCounter scoreCounter, LevelManager levelManager, SceneManager sceneManager) {
        this.levelManager = levelManager;
        this.scoreCounter = scoreCounter;
        this.sceneManager = sceneManager;
        setCollisions();
    }

    private void setCollisions() {
        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.COIN, (player, coin) -> {
            coin.removeFromWorld();
            scoreCounter.increment(1);
            FXGL.play("coin.wav");
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.SACK, (player, coin) -> {
            coin.removeFromWorld();
            scoreCounter.increment(5);
            FXGL.play("coinSack.wav");
        });

        onCollisionBegin(EntityType.PLAYER, EntityType.SCALPER, (player, scalper) -> {
            System.out.println("register");
            System.out.println("Hit");
            scoreCounter.decrement(1);
            //TODO play scalper hit sound here
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.PART, (player, part) -> {
            System.out.println("part hit");
            if(geti("coin") >= Integer.parseInt(levelManager.getCurrentLevel().getProperties().getString("cost"))) {
                FXGL.animationBuilder()
                        .interpolator(Interpolators.SMOOTH.EASE_OUT())
                        .duration(Duration.seconds(1.6))
                        .scale(part)
                        .from(new Point2D(1, 1))
                        .to(new Point2D(0,0))
                        .buildAndPlay();
                animationBuilder()
                        .interpolator(Interpolators.SMOOTH.EASE_OUT())
                        .onFinished(() -> {
                            Integer highscore = levelManager.getHighScore(levelManager.currentLevelID);
                            if(levelManager.currentLevelID == 3) {
                                getDialogService().showMessageBox(highscore != null ? (highscore < geti("coin") ? "You beat the highscore, good job!" : "You didn't beat the highscore, better luck next time.") : "You got all the parts required! Congratulations!", () -> {
                                    getGameController().gotoMainMenu();
                                    levelManager.finished(geti("coin"));
                                    sceneManager.getMainMenu().lockButtons(levelManager.levelProgress);
                                    sceneManager.getMainMenu().showHighscores();
                                    part.removeFromWorld();
                                });
                            } else {
                                getDialogService().showMessageBox(highscore != null ? (highscore < geti("coin") ? "You beat the highscore, good job!" : "You didn't beat the highscore, better luck next time.") : "You got the pc part. On to the next level!", () -> {
                                    getGameController().gotoMainMenu();
                                    levelManager.finished(geti("coin"));
                                    sceneManager.getMainMenu().lockButtons(levelManager.levelProgress);
                                    sceneManager.getMainMenu().showHighscores();
                                    part.removeFromWorld();
                                });
                            }
                        })
                        .duration(Duration.seconds(1.6))
                        .rotate(part)
                        .from(0)
                        .to(180)
                        .buildAndPlay();
            } else {
                getDialogService().showMessageBox("You do not have enough coins to buy this part, try again!", getGameController()::startNewGame);
            }
        });
    }

}
