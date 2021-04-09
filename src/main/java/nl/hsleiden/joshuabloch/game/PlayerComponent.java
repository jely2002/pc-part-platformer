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

import static com.almasb.fxgl.dsl.FXGL.*;

public class PlayerComponent extends Component implements Moveable {

    private PhysicsComponent physics;
    private final AnimatedTexture texture;
    private final AnimationChannel walk, still, jump;
    private LocalTimer walkTimer, jumpTimer;
    private int AVAILABLE_JUMPS = 2;
    private final int MAX_AVAILABLE_JUMPS = 2;

    public PlayerComponent() {
        Image animImage = image("player_anim.png");

        still = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(1), 0, 0);
        walk = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(0.66), 3, 6);
        jump = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(0.66), 7, 7);

        texture = new AnimatedTexture(still);
    }

    @Override
    public void onAdded() {
        walkTimer = FXGL.newLocalTimer();
        walkTimer.capture();
        jumpTimer = FXGL.newLocalTimer();
        jumpTimer.capture();

        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);
        physics.onGroundProperty().addListener((obs, old, isOnGround) -> {
            if(isOnGround) {
                if (AVAILABLE_JUMPS < MAX_AVAILABLE_JUMPS) FXGL.play("land.wav");
                AVAILABLE_JUMPS = MAX_AVAILABLE_JUMPS;
            }
        });
    }

    @Override
    public void onUpdate(double tpf) {
        if(entity.getPosition().getY() > 800) respawn(); //Respawn the player if it is knocked off a platform and falling forever
        if(!jumpTimer.elapsed(Duration.millis(700))) return;
        if(physics.isMovingX() && texture.getAnimationChannel() != walk && walkTimer.elapsed(Duration.seconds(1))) {
            walkTimer.capture();
            texture.loopAnimationChannel(walk);
        } else if(texture.getAnimationChannel() != still && (walkTimer.elapsed(Duration.seconds(1))) || !physics.isMovingX()) {
            texture.loopAnimationChannel(still);
        }
    }

    private void respawn() {
        //Entity closestPlatform = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.PLATFORM).stream().findFirst().orElse(null);
        physics.overwritePosition(new Point2D(50, 50));
        physics.setVelocityX(0);
        physics.setVelocityY(0);
        if(geti("coin") - 5 < 0) inc("coin", -5 - (geti("coin") - 5));
        else inc("coin", -5);
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
        jumpTimer.capture();
        texture.loopAnimationChannel(jump);

        // Play SoundFX when jumped once
        if(AVAILABLE_JUMPS == (MAX_AVAILABLE_JUMPS - 1)) {
            FXGL.play("jump.wav");
        }

    }

    public void stopMovement() {
        physics.setVelocityX(0);
    }
}
