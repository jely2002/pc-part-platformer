package nl.hsleiden.joshuabloch.menu;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.LoadingScene;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class MySceneFactory extends SceneFactory {

    @Override
    public FXGLMenu newMainMenu() {
        return new mainMenu(MenuType.MAIN_MENU);
    }

    @Override
    public FXGLMenu newGameMenu() {
        return new gameMenu(MenuType.GAME_MENU);
    }

}
