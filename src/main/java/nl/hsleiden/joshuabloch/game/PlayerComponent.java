package nl.hsleiden.joshuabloch.game;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
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
    private final AnimationChannel walk, still;
    private final LocalTimer walkTimer, invincibleTimer;
    private int AVAILABLE_JUMPS = 2;
    private final int MAX_AVAILABLE_JUMPS = 2;

    public PlayerComponent() {
        Image animImage = image("player_anim.png");

        still = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(1), 3, 3);
        walk = new AnimationChannel(animImage, 8, 31, 28, Duration.seconds(0.66), 3, 6);

        texture = new AnimatedTexture(still);
        walkTimer = FXGL.newLocalTimer();
        invincibleTimer = FXGL.newLocalTimer();
    }

    @Override
    public void onAdded() {
        walkTimer.capture();
        invincibleTimer.capture();

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
        if(entity.getPosition().getY() > 800) respawn(); //Remove the entity if it is knocked off a platform and falling forever
        if(physics.isMovingX() && texture.getAnimationChannel() != walk && walkTimer.elapsed(Duration.seconds(1))) {
            walkTimer.capture();
            texture.loopAnimationChannel(walk);
        } else if(texture.getAnimationChannel() != still && (walkTimer.elapsed(Duration.seconds(1))) || !physics.isMovingX()) {
            texture.loopAnimationChannel(still);
        }
    }

    private void respawn() {
        Entity closestPlatform = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.PLATFORM).stream().findFirst().orElse(null);
        physics.overwritePosition(new Point2D(closestPlatform.getX() + 10, 50));
        physics.setVelocityX(0);
        entity.getViewComponent().setOpacity(0);
        invincibleTimer.capture();
        if(geti("coin") > 0) inc("coin", -1); //TODO Change this penalty for falling out of the map to something reasonable
        FXGL.animationBuilder()
                .duration(Duration.seconds(0.4))
                .interpolator(Interpolators.CUBIC.EASE_IN_OUT())
                .delay(Duration.seconds(0.1))
                .repeat(9)
                .autoReverse(true)
                .animate(entity.getViewComponent().opacityProperty())
                .from(0)
                .to(100)
                .buildAndPlay();
    }

    public boolean isInvincible() {
        return !invincibleTimer.elapsed(Duration.seconds(5));
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

        // Play SoundFX when jumped once
        if(AVAILABLE_JUMPS == (MAX_AVAILABLE_JUMPS - 1)) FXGL.play("jump.wav");


    }

    public void stopMovement() {
        physics.setVelocityX(0);
    }
}
