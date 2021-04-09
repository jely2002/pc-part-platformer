package nl.hsleiden.joshuabloch.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import nl.hsleiden.joshuabloch.LevelManager;
import org.jetbrains.annotations.NotNull;

public class SceneManager extends SceneFactory {

    private final LevelManager levelManager;
    private MainMenu mainMenu;

    public SceneManager(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    @NotNull
    @Override
    public FXGLMenu newMainMenu() {
        mainMenu = new MainMenu(MenuType.MAIN_MENU, levelManager);
        return mainMenu;
    }

    @NotNull
    @Override
    public FXGLMenu newGameMenu() {
        return new GameMenu(MenuType.GAME_MENU);
    }

}
