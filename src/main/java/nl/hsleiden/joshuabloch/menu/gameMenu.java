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
import com.sun.javafx.geom.BaseBounds;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.security.cert.PolicyNode;

public class gameMenu extends FXGLMenu {
    private static final int SIZE = 150;
    private Animation<?> animation;

    public gameMenu(MenuType menuType) {
        super(menuType);


        getContentRoot().setTranslateX(FXGL.getAppWidth() / 2.0 - SIZE);
        getContentRoot().setTranslateY(FXGL.getAppHeight() / 2.0 - SIZE);

        ImageView imageView = new ImageView(new Image("assets/textures/forest.png"));

        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

//        imageView.setTranslateX();
//        imageView.setTranslateY();

        // Button Resume
        var buttonResume = new Rectangle(SIZE*2, SIZE / 2);
        buttonResume.setStrokeWidth(2.5);
        buttonResume.strokeProperty().bind(Bindings.when(buttonResume.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonResume.fillProperty().bind(Bindings.when(buttonResume.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonResume.setTranslateY(-50);
        buttonResume.setOnMouseClicked(e -> FXGL.getGameController().gotoPlay());

        // Button Play
        var buttonPlay = new Rectangle(SIZE*2, SIZE / 2);
        buttonPlay.setStrokeWidth(2.5);
        buttonPlay.strokeProperty().bind(Bindings.when(buttonPlay.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonPlay.fillProperty().bind(Bindings.when(buttonPlay.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonPlay.setTranslateY(50);
        buttonPlay.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button MainMenu
        var buttonMainMenu = new Rectangle(SIZE*2, SIZE / 2);
        buttonMainMenu.setStrokeWidth(2.5);
        buttonMainMenu.strokeProperty().bind(Bindings.when(buttonMainMenu.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonMainMenu.fillProperty().bind(Bindings.when(buttonMainMenu.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonMainMenu.setTranslateY(150);
        buttonMainMenu.setOnMouseClicked(e -> FXGL.getGameController().gotoMainMenu());

        // Button Quit
        var buttonQuit = new Rectangle(SIZE*2, SIZE / 2);
        buttonQuit.setStrokeWidth(2.5);
        buttonQuit.strokeProperty().bind(Bindings.when(buttonQuit.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonQuit.fillProperty().bind(Bindings.when(buttonQuit.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonQuit.setTranslateY(250);
        buttonQuit.setOnMouseClicked(e -> FXGL.getGameController().exit());

        // Text Play
        Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.RED, FontType.GAME, 24.0);
        textResume.setTextAlignment(TextAlignment.CENTER);
        textResume.setWrappingWidth(buttonResume.getWidth());
        textResume.setTranslateY(buttonResume.getTranslateY() + (buttonResume.getHeight() / 2) + (textResume.getLayoutBounds().getHeight() / 4) );

        // Text Play
        Text textPlay = FXGL.getUIFactoryService().newText("RESTART LEVEL", Color.RED, FontType.GAME, 24.0);
        textPlay.setTextAlignment(TextAlignment.CENTER);
        textPlay.setWrappingWidth(buttonPlay.getWidth());
        textPlay.setTranslateY(buttonPlay.getTranslateY() + (buttonPlay.getHeight() / 2) + (textPlay.getLayoutBounds().getHeight() / 4) );

        // Text mainMenu
        Text textMainMenu = FXGL.getUIFactoryService().newText("BACK TO MENU", Color.RED, FontType.GAME, 24.0);
        textMainMenu.setTextAlignment(TextAlignment.CENTER);
        textMainMenu.setWrappingWidth(buttonMainMenu.getWidth());
        textMainMenu.setTranslateY(buttonMainMenu.getTranslateY() + (buttonMainMenu.getHeight() / 2) + (textMainMenu.getLayoutBounds().getHeight() / 4) );

        // Text Quit
        Text textQuit = FXGL.getUIFactoryService().newText("QUIT", Color.RED, FontType.GAME, 24.0);
        textQuit.setTextAlignment(TextAlignment.CENTER);
        textQuit.setWrappingWidth(buttonQuit.getWidth());
        textQuit.setTranslateY(buttonQuit.getTranslateY() + (buttonQuit.getHeight() / 2) + (textQuit.getLayoutBounds().getHeight() / 4) );

        textResume.setMouseTransparent(true);
        textPlay.setMouseTransparent(true);
        textMainMenu.setMouseTransparent(true);
        textQuit.setMouseTransparent(true);



        FXGL.getWindowService().getOverlayRoot().getChildren().addAll(imageView);
        getContentRoot().getChildren().addAll(buttonPlay, textPlay, buttonQuit, textQuit, buttonMainMenu, textMainMenu, buttonResume, textResume);
        FXGL.getWindowService().getOverlayRoot().setScaleX(0);
        FXGL.getWindowService().getOverlayRoot().setScaleY(0);
        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = FXGL.animationBuilder()

                .duration(Duration.seconds(1.66))

                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())

                .scale(getContentRoot())
                .scale(FXGL.getWindowService().getOverlayRoot())

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
