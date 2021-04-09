package nl.hsleiden.joshuabloch.menu;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameMenu extends FXGLMenu {
    private static final int SIZE = 150;
    private final Animation<?> animation;

    public GameMenu(MenuType menuType) {
        super(menuType);

        getContentRoot().setTranslateX(0);
        getContentRoot().setTranslateY(0);

        ImageView imageView = new ImageView(new Image("assets/textures/menu-overlay.png"));

        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

        final int arcint = 15;

        // Button Resume
        var buttonResume = new Rectangle(SIZE*2, SIZE / 2f);
        buttonResume.setStrokeWidth(4);
        buttonResume.setArcHeight(arcint);
        buttonResume.setArcWidth(arcint);
        buttonResume.strokeProperty().bind(Bindings.when(buttonResume.hoverProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonResume.fillProperty().bind(Bindings.when(buttonResume.pressedProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonResume.setOnMouseClicked(e -> FXGL.getGameController().gotoPlay());

        // Button Restart
        var buttonRestart = new Rectangle(SIZE*2, SIZE / 2f);
        buttonRestart.setStrokeWidth(4);
        buttonRestart.setArcHeight(arcint);
        buttonRestart.setArcWidth(arcint);
        buttonRestart.strokeProperty().bind(Bindings.when(buttonRestart.hoverProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonRestart.fillProperty().bind(Bindings.when(buttonRestart.pressedProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonRestart.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button MainMenu
        var buttonMainMenu = new Rectangle(SIZE*2, SIZE / 2f);
        buttonMainMenu.setStrokeWidth(4);
        buttonMainMenu.setArcHeight(arcint);
        buttonMainMenu.setArcWidth(arcint);
        buttonMainMenu.strokeProperty().bind(Bindings.when(buttonMainMenu.hoverProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonMainMenu.fillProperty().bind(Bindings.when(buttonMainMenu.pressedProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonMainMenu.setOnMouseClicked(e -> FXGL.getGameController().gotoMainMenu());

        // Button Quit
        var buttonQuit = new Rectangle(SIZE*2, SIZE / 2f);
        buttonQuit.setStrokeWidth(4);
        buttonQuit.setArcHeight(arcint);
        buttonQuit.setArcWidth(arcint);
        buttonQuit.strokeProperty().bind(Bindings.when(buttonQuit.hoverProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonQuit.fillProperty().bind(Bindings.when(buttonQuit.pressedProperty()).then(Color.web("425622",1.0)).otherwise(Color.web("6E834C",1.0)));
        buttonQuit.setOnMouseClicked(e -> FXGL.getGameController().exit());

        // Text Resume
        Text textResume = FXGL.getUIFactoryService().newText("RESUME", Color.web("9db379",1.0), FontType.GAME, 24.0);
        textResume.setTextAlignment(TextAlignment.CENTER);

        // Text Restart
        Text textRestart = FXGL.getUIFactoryService().newText("RESTART LEVEL", Color.web("9db379",1.0), FontType.GAME, 24.0);
        textRestart.setTextAlignment(TextAlignment.CENTER);

        // Text mainMenu
        Text textMainMenu = FXGL.getUIFactoryService().newText("BACK TO MENU", Color.web("9db379",1.0), FontType.GAME, 24.0);
        textMainMenu.setTextAlignment(TextAlignment.CENTER);

        // Text Quit
        Text textQuit = FXGL.getUIFactoryService().newText("QUIT", Color.web("9db379",1.0), FontType.GAME, 24.0);
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
