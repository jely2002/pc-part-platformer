package nl.hsleiden.joshuabloch;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class MySceneFactory extends SceneFactory {

    @Override
    public FXGLMenu newMainMenu() {
        return new mainMenu(MenuType.MAIN_MENU);
    }

    @Override
    public FXGLMenu newGameMenu() {
        return new mainMenu(MenuType.GAME_MENU);
    }
}
