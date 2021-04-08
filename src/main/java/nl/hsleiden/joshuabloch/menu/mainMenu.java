package nl.hsleiden.joshuabloch.menu;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.awt.*;
import java.security.cert.PolicyNode;

public class mainMenu extends FXGLMenu {
    private static final int SIZE = 150;
    private Animation<?> animation;

    public mainMenu(MenuType menuType) {
        super(menuType);


        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);


        // Button Play
        var buttonPlay = new Rectangle(SIZE*2, SIZE / 2);
        buttonPlay.setStrokeWidth(2.5);
        buttonPlay.strokeProperty().bind(Bindings.when(buttonPlay.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonPlay.fillProperty().bind(Bindings.when(buttonPlay.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonPlay.setTranslateY(200);
        buttonPlay.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button Quit
        var buttonQuit = new Rectangle(SIZE*2, SIZE / 2);
        buttonQuit.setStrokeWidth(2.5);
        buttonQuit.strokeProperty().bind(Bindings.when(buttonQuit.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonQuit.fillProperty().bind(Bindings.when(buttonQuit.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonQuit.setTranslateY(300);
        buttonQuit.setOnMouseClicked(e -> FXGL.getGameController().exit());



        // Text Play
        Text textPlay = FXGL.getUIFactoryService().newText("PLAY", Color.RED, FontType.GAME, 24.0);
        textPlay.setTranslateX((buttonPlay.getWidth() / 2) - (textPlay.getLayoutBounds().getWidth() / 2));
        textPlay.setTranslateY(buttonPlay.getTranslateY() + (buttonPlay.getHeight() / 2) + (textPlay.getLayoutBounds().getHeight() / 4) );

        // Text Quit
        Text textQuit = FXGL.getUIFactoryService().newText(" QUIT", Color.RED, FontType.GAME, 24.0);
        textQuit.setTranslateX((buttonQuit.getWidth() / 2) - (textQuit.getLayoutBounds().getWidth() / 2));
        textQuit.setTranslateY(buttonQuit.getTranslateY() + (buttonQuit.getHeight() / 2) + (textQuit.getLayoutBounds().getHeight() / 4) );



//        System.out.println(buttonPlay.getTranslateY());
//        System.out.println(buttonPlay.getTranslateY() + (SIZE / 2));
        textPlay.setMouseTransparent(true);
        textQuit.setMouseTransparent(true);




        getContentRoot().getChildren().addAll(buttonPlay, textPlay, buttonQuit, textQuit);

        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = FXGL.animationBuilder()

                .duration(Duration.seconds(0.66))

                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())

                .scale(getContentRoot())

                .from(new Point2D(0, 0))

                .to(new Point2D(1, 1))

                .build();

    }

    @Override
    public void onCreate() {
        animation.setOnFinished(EmptyRunnable.INSTANCE);
        animation.stop();
        animation.start();

    }


    @Override
    protected void onUpdate(double tpf) {
        animation.onUpdate(tpf);
    }
}
