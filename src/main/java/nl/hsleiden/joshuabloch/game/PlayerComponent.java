package nl.hsleiden.joshuabloch.game;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class PlayerComponent extends Component implements Moveable {

    private PhysicsComponent physics;
    private AnimatedTexture texture;
    private AnimationChannel walk, still;
    private LocalTimer walkTimer;
    private int AVAILABLE_JUMPS = 4;

    public PlayerComponent() {
        Image animImage = image("player_anim.png");

        still = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(1), 3, 3);
        walk = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(0.66), 3, 6);

        texture = new AnimatedTexture(still);
        walkTimer = FXGL.newLocalTimer();
        walkTimer.capture();
        //texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if(isOnGround) AVAILABLE_JUMPS = 4;
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if(physics.isMovingX() && texture.getAnimationChannel() != walk && walkTimer.elapsed(Duration.seconds(1))) {
            walkTimer.capture();
            texture.loopAnimationChannel(walk);
        } else if(texture.getAnimationChannel() != still && (walkTimer.elapsed(Duration.seconds(1))) || !physics.isMovingX()) {
            texture.loopAnimationChannel(still);
        }
    }

    public void moveLeft() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-150);
    }

    public void moveRight() {
        getEntity().setScaleX(1);
        physics.setVelocityX(150);
    }

    public void jump(int velocity) {
        if(AVAILABLE_JUMPS == 0) return;
        physics.setVelocityY(-FXGLMath.abs(velocity));
        AVAILABLE_JUMPS--;
    }

    public void stopMovement() {
        physics.setVelocityX(0);
    }
}
