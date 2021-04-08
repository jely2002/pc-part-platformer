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
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.awt.*;
import java.security.cert.PolicyNode;

public class mainMenu extends FXGLMenu {
    private static final int SIZE = 250;
    private Animation<?> animation;
    private final Rectangle buttonLevel1;
    private final Rectangle buttonLevel2;
    private final Rectangle buttonLevel3;
    private final Rectangle buttonLevel4;


    public void lockButton (int levelID) {
        if (levelID == 0 ) {
            buttonLevel1.strokeProperty().bind(Bindings.when(buttonLevel1.hoverProperty()).then(Color.BLACK).otherwise(Color.BLACK));
            buttonLevel1.fillProperty().bind(Bindings.when(buttonLevel1.pressedProperty()).then(Color.BLACK).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }

        else if (levelID == 1 ){
            buttonLevel2.strokeProperty().bind(Bindings.when(buttonLevel2.hoverProperty()).then(Color.BLACK).otherwise(Color.BLACK));
            buttonLevel2.fillProperty().bind(Bindings.when(buttonLevel2.pressedProperty()).then(Color.BLACK).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
        else if (levelID == 2 ) {
            buttonLevel3.strokeProperty().bind(Bindings.when(buttonLevel3.hoverProperty()).then(Color.BLACK).otherwise(Color.BLACK));
            buttonLevel3.fillProperty().bind(Bindings.when(buttonLevel3.pressedProperty()).then(Color.BLACK).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
        else if (levelID == 3 ){
            buttonLevel4.strokeProperty().bind(Bindings.when(buttonLevel4.hoverProperty()).then(Color.BLACK).otherwise(Color.BLACK));
            buttonLevel4.fillProperty().bind(Bindings.when(buttonLevel4.pressedProperty()).then(Color.BLACK).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
    }

    public void unlockedButton (int levelID) {
        if (levelID == 0 ) {
            buttonLevel1.strokeProperty().bind(Bindings.when(buttonLevel1.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
            buttonLevel1.fillProperty().bind(Bindings.when(buttonLevel1.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }

        else if (levelID == 1 ){
            buttonLevel2.strokeProperty().bind(Bindings.when(buttonLevel2.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
            buttonLevel2.fillProperty().bind(Bindings.when(buttonLevel2.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
        else if (levelID == 2 ){
            buttonLevel3.strokeProperty().bind(Bindings.when(buttonLevel3.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
            buttonLevel3.fillProperty().bind(Bindings.when(buttonLevel3.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
        else if (levelID == 3 ){
            buttonLevel4.strokeProperty().bind(Bindings.when(buttonLevel4.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
            buttonLevel4.fillProperty().bind(Bindings.when(buttonLevel4.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        }
    }



    public mainMenu(MenuType menuType) {
        super(menuType);


        getContentRoot().setTranslateX(0);
        getContentRoot().setTranslateY(0);


        ImageView imageView = new ImageView(new Image("assets/textures/forest-overlay.png"));
        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());


        // Button Level 1
        buttonLevel1 = new Rectangle(SIZE, 60);
        buttonLevel1.setStrokeWidth(2.5);
        buttonLevel1.strokeProperty().bind(Bindings.when(buttonLevel1.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonLevel1.fillProperty().bind(Bindings.when(buttonLevel1.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonLevel1.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());       // Level spelen

        // Button Level 2
        buttonLevel2 = new Rectangle(SIZE, 60);
        buttonLevel2.setStrokeWidth(2.5);
        buttonLevel2.strokeProperty().bind(Bindings.when(buttonLevel2.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonLevel2.fillProperty().bind(Bindings.when(buttonLevel2.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonLevel2.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button Level 3
        buttonLevel3 = new Rectangle(SIZE, 60);
        buttonLevel3.setStrokeWidth(2.5);
        buttonLevel3.strokeProperty().bind(Bindings.when(buttonLevel3.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonLevel3.fillProperty().bind(Bindings.when(buttonLevel3.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonLevel3.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button Level 4
        buttonLevel4 = new Rectangle(SIZE, 60);
        buttonLevel4.setStrokeWidth(2.5);
        buttonLevel4.strokeProperty().bind(Bindings.when(buttonLevel4.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonLevel4.fillProperty().bind(Bindings.when(buttonLevel4.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonLevel4.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());


        // Button login
        var buttonLogin = new Rectangle(SIZE, 60);
        buttonLogin.setStrokeWidth(2.5);
        buttonLogin.strokeProperty().bind(Bindings.when(buttonLogin.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonLogin.fillProperty().bind(Bindings.when(buttonLogin.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonLogin.setOnMouseClicked(e -> FXGL.getGameController().startNewGame());

        // Button Quit
        var buttonQuit = new Rectangle(SIZE, 60);
        buttonQuit.setStrokeWidth(2.5);
        buttonQuit.strokeProperty().bind(Bindings.when(buttonQuit.hoverProperty()).then(Color.YELLOW).otherwise(Color.BLACK));
        buttonQuit.fillProperty().bind(Bindings.when(buttonQuit.pressedProperty()).then(Color.YELLOW).otherwise(Color.color(0.1, 0.05, 0.0, 0.75)));
        buttonQuit.setOnMouseClicked(e -> FXGL.getGameController().exit());




        // Text Level 1
        Text textLevel1 = FXGL.getUIFactoryService().newText("LEVEL 1", Color.RED, FontType.GAME, 24.0);
        textLevel1.setTextAlignment(TextAlignment.CENTER);


        // Text Level 2
        Text textLevel2 = FXGL.getUIFactoryService().newText("LEVEL 2", Color.RED, FontType.GAME, 24.0);
        textLevel2.setTextAlignment(TextAlignment.CENTER);

        // Text Level 3
        Text textLevel3 = FXGL.getUIFactoryService().newText("LEVEL 3", Color.RED, FontType.GAME, 24.0);
        textLevel3.setTextAlignment(TextAlignment.CENTER);

        // Text Level 4
        Text textLevel4 = FXGL.getUIFactoryService().newText("LEVEL 4", Color.RED, FontType.GAME, 24.0);
        textLevel4.setTextAlignment(TextAlignment.CENTER);

        // Text Login
        Text textLogin = FXGL.getUIFactoryService().newText("LOGIN", Color.RED, FontType.GAME, 24.0);
        textLogin.setTextAlignment(TextAlignment.CENTER);
//        textLogin.setWrappingWidth(buttonLogin.getWidth());
//        textLogin.setTranslateY(buttonLogin.getTranslateY() + (buttonLogin.getHeight() / 2) + (textLogin.getLayoutBounds().getHeight() / 4) );

        // Text Quit
        Text textQuit = FXGL.getUIFactoryService().newText("QUIT", Color.RED, FontType.GAME, 24.0);
        textQuit.setTextAlignment(TextAlignment.CENTER);


        // HighScore Level 1
        Text highScoreLevel1 = FXGL.getUIFactoryService().newText("# HighscoreLevel1", Color.RED, FontType.GAME, 24.0);
        highScoreLevel1.setTextAlignment(TextAlignment.CENTER);

        // HighScore Level 2
        Text highScoreLevel2 = FXGL.getUIFactoryService().newText("# HighscoreLevel2", Color.RED, FontType.GAME, 24.0);
        highScoreLevel2.setTextAlignment(TextAlignment.CENTER);

        // HighScore Level 3
        Text highScoreLevel3 = FXGL.getUIFactoryService().newText("# HighscoreLevel3", Color.RED, FontType.GAME, 24.0);
        highScoreLevel3.setTextAlignment(TextAlignment.CENTER);

        // HighScore Level 4
        Text highScoreLevel4 = FXGL.getUIFactoryService().newText("# HighscoreLevel4", Color.RED, FontType.GAME, 24.0);
        highScoreLevel4.setTextAlignment(TextAlignment.CENTER);



        // StackPane Level1
        StackPane level1Pane = new StackPane();
        level1Pane.getChildren().addAll(buttonLevel1, textLevel1);

        // StackPane Level2
        StackPane level2Pane = new StackPane();
        level2Pane.getChildren().addAll(buttonLevel2, textLevel2);

        // StackPane Level3
        StackPane level3Pane = new StackPane();
        level3Pane.getChildren().addAll(buttonLevel3, textLevel3);

        // StackPane Level4
        StackPane level4Pane = new StackPane();
        level4Pane.getChildren().addAll(buttonLevel4, textLevel4);

        // StackPane Login
        StackPane loginPane = new StackPane();
        loginPane.getChildren().addAll(buttonLogin, textLogin);

        // StackPane Quit
        StackPane quitPane = new StackPane();
        quitPane.getChildren().addAll(buttonQuit, textQuit);


        // HBox Level1
        HBox hBoxLevel1 = new HBox();
        hBoxLevel1.getChildren().addAll(level1Pane, highScoreLevel1);
        hBoxLevel1.setSpacing(40);
        hBoxLevel1.setAlignment(Pos.CENTER_LEFT);

        // HBox Level2
        HBox hBoxLevel2 = new HBox();
        hBoxLevel2.getChildren().addAll(level2Pane, highScoreLevel2);
        hBoxLevel2.setSpacing(40);
        hBoxLevel2.setAlignment(Pos.CENTER_LEFT);


        // HBox Level3
        HBox hBoxLevel3 = new HBox();
        hBoxLevel3.getChildren().addAll(level3Pane, highScoreLevel3);
        hBoxLevel3.setSpacing(40);
        hBoxLevel3.setAlignment(Pos.CENTER_LEFT);


        // HBox Level4
        HBox hBoxLevel4 = new HBox();
        hBoxLevel4.getChildren().addAll(level4Pane, highScoreLevel4);
        hBoxLevel4.setSpacing(40);
        hBoxLevel4.setAlignment(Pos.CENTER_LEFT);


        // HBox Login
        HBox hBoxLogin = new HBox();
        hBoxLogin.getChildren().addAll(loginPane);
        hBoxLogin.setSpacing(40);

        // HBox Quit
        HBox hBoxQuit = new HBox();
        hBoxQuit.getChildren().addAll(quitPane);
        hBoxQuit.setSpacing(40);


        // Making text hover transparent
        textLevel1.setMouseTransparent(true);
        textLevel2.setMouseTransparent(true);
        textLevel3.setMouseTransparent(true);
        textLevel4.setMouseTransparent(true);
        textLogin.setMouseTransparent(true);
        textQuit.setMouseTransparent(true);

        // Creat VBoxMenu
        VBox vBoxMain = new VBox();
        vBoxMain.setPrefHeight(FXGL.getAppHeight());
        vBoxMain.setAlignment(Pos.CENTER_LEFT);
        vBoxMain.setLayoutX(70);
        vBoxMain.setMargin(hBoxLogin, new Insets(60,0,0,0));
        vBoxMain.getChildren().addAll(hBoxLevel1, hBoxLevel2, hBoxLevel3, hBoxLevel4, hBoxLogin, hBoxQuit);
        vBoxMain.setSpacing(30);


        // Creat VBoxLogo
//        VBox vBoxLogo = new VBox();
//        vBoxLogo.setPrefHeight(FXGL.getAppHeight());
//        vBoxLogo.setPrefWidth(FXGL.getAppWidth());
//        vBoxLogo.setAlignment(Pos.CENTER_RIGHT);
//        vBoxLogo.setLayoutX(300);
//        vBoxLogo.setMargin(hBoxLogin, new Insets(60,0,0,0));
//        vBoxLogo.getChildren().addAll(logo);
//        vBoxLogo.setSpacing(30);
//
//        HBox hBoxMenu = new HBox();
//        hBoxMenu.getChildren().addAll(vBoxMain, vBoxLogo);


        getContentRoot().getChildren().addAll(imageView, vBoxMain);
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
