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
import javafx.geometry.Pos;
import javafx.scene.layout.*;
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


        getContentRoot().setTranslateX(0);
        getContentRoot().setTranslateY(0);

        System.out.println(FXGL.getAppWidth());
        System.out.println(FXGL.getAppHeight());

        ImageView imageView = new ImageView(new Image("assets/textures/forest-overlay.png"));

        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

        // Button Resume
        var buttonResume = new Rectangle(SIZE*2, SIZE / 2);
        buttonResume.setStrokeWidth(2.5);
        buttonResume.strokeProperty().bind(Bindings.when(buttonResume.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonResume.fillProperty().bind(Bindings.when(buttonResume.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonResume.setOnMouseClicked(e -> FXGL.getGameController().gotoPlay());

        // Button Restart
        var buttonRestart = new Rectangle(SIZE*2, SIZE / 2);
        buttonRestart.setStrokeWidth(2.5);
        buttonRestart.strokeProperty().bind(Bindings.when(buttonRestart.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonRestart.fillProperty().bind(Bindings.when(buttonRestart.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonRestart.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button MainMenu
        var buttonMainMenu = new Rectangle(SIZE*2, SIZE / 2);
        buttonMainMenu.setStrokeWidth(2.5);
        buttonMainMenu.strokeProperty().bind(Bindings.when(buttonMainMenu.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonMainMenu.fillProperty().bind(Bindings.when(buttonMainMenu.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonMainMenu.setOnMouseClicked(e -> FXGL.getGameController().gotoMainMenu());

        // Button Quit
        var buttonQuit = new Rectangle(SIZE*2, SIZE / 2);
        buttonQuit.setStrokeWidth(2.5);
        buttonQuit.strokeProperty().bind(Bindings.when(buttonQuit.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonQuit.fillProperty().bind(Bindings.when(buttonQuit.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonQuit.setOnMouseClicked(e -> FXGL.getGameController().exit());

        // Text Resume
        Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.RED, FontType.GAME, 24.0);
        textResume.setTextAlignment(TextAlignment.CENTER);

        // Text Restart
        Text textRestart = FXGL.getUIFactoryService().newText("RESTART LEVEL", Color.RED, FontType.GAME, 24.0);
        textRestart.setTextAlignment(TextAlignment.CENTER);

        // Text mainMenu
        Text textMainMenu = FXGL.getUIFactoryService().newText("BACK TO MENU", Color.RED, FontType.GAME, 24.0);
        textMainMenu.setTextAlignment(TextAlignment.CENTER);

        // Text Quit
        Text textQuit = FXGL.getUIFactoryService().newText("QUIT", Color.RED, FontType.GAME, 24.0);
        textQuit.setTextAlignment(TextAlignment.CENTER);

        textResume.setMouseTransparent(true);
        textRestart.setMouseTransparent(true);
        textMainMenu.setMouseTransparent(true);
        textQuit.setMouseTransparent(true);


        // StackPane Resume
        StackPane resumePane = new StackPane();
        resumePane.getChildren().addAll(buttonResume, textResume);

        // StackPane Restart
        StackPane restartPane = new StackPane();
        restartPane.getChildren().addAll(buttonRestart, textRestart);

        // StackPane Menu
        StackPane menuPane = new StackPane();
        menuPane.getChildren().addAll(buttonMainMenu, textMainMenu);

        // StackPane Quit
        StackPane quitPane = new StackPane();
        quitPane.getChildren().addAll(buttonQuit, textQuit);



        VBox vBox = new VBox();
        vBox.setPrefWidth(FXGL.getAppWidth());
        vBox.setPrefHeight(FXGL.getAppHeight());
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(resumePane, restartPane, menuPane, quitPane);
        vBox.setSpacing(30);

        getContentRoot().getChildren().addAll(imageView, vBox);
        getContentRoot().setScaleX(0);
        getContentRoot().setScaleY(0);

        animation = FXGL.animationBuilder()

                .duration(Duration.seconds(1))

                .fade(getContentRoot())
                .scale(getContentRoot())

                .from(new Point2D(1, 1))

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
