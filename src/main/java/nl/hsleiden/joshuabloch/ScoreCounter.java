package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxgl.ui.FontType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;

public class ScoreCounter {

    private LocalTimer decrementTimer;

    public ScoreCounter() {
        decrementTimer = FXGL.newLocalTimer();
        decrementTimer.capture();
    }

    public void init() {
        createUIelements();
        registerListener();
    }

    public void increment(int change) {
        inc("coin", change);
    }

    public void decrement(int change) {
        if(!decrementTimer.elapsed(Duration.millis(500))) return;
        decrementTimer.capture();
        int currentCoin = geti("coin");
        float decrementValue = -FXGLMath.abs(change);
        if(currentCoin + decrementValue < 0) {
            set("coin", 0);
        } else {
            inc("coin", (int) decrementValue);
        }
    }

    private void createUIelements() {
        Font font = getUIFactoryService().newFont(FontType.GAME, 28);
        Text moneyText = new Text();
        Text moneyDesc = new Text();
        moneyDesc.setFont(font);
        moneyText.setFont(font);
        moneyDesc.setTranslateY(50);
        moneyDesc.setTranslateX(30);
        moneyText.setTranslateX(100);
        moneyText.setTranslateY(50);
        moneyDesc.textProperty().set("Coins:");
        moneyText.textProperty().bind(getWorldProperties().intProperty("coin").asString());
        getGameScene().addUINode(moneyText);
        getGameScene().addUINode(moneyDesc);
    }

    private void registerListener() {
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
                    .from(new Point2D(30, 140))
                    .to(new Point2D(30, 80))
                    .buildAndPlay();
            FXGL.animationBuilder()
                    .interpolator(Interpolators.SMOOTH.EASE_IN_OUT())
                    .delay(Duration.seconds(2))
                    .duration(Duration.seconds(2))
                    .onCycleFinished(() -> { getGameScene().removeUINode(changeText); })
                    .autoReverse(true)
                    .fadeOut(changeText)
                    .buildAndPlay();
        });
    }

}
