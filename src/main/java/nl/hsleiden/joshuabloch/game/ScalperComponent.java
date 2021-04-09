package nl.hsleiden.joshuabloch.game;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.almasb.fxgl.dsl.FXGL.image;

public class ScalperComponent extends Component implements Moveable {

    private PhysicsComponent physics;
    private final LocalTimer attackTimer, turnTimer, animTimer;
    private final AnimationChannel still, walk, attack, knockout;
    private final AnimatedTexture texture;
    private Entity platform, player;
    private boolean onGround;
    private double distanceFallen = 0;
    private Point2D previousPoint;
    private final int movementSpeed;
    private final int sensitivity;
    private final int attackCooldown;

    public ScalperComponent() {
        Image animImage = image("scalper_anim.png");

        movementSpeed = ThreadLocalRandom.current().nextInt(80, 150);
        sensitivity = ThreadLocalRandom.current().nextInt(50, 80);
        attackCooldown = ThreadLocalRandom.current().nextInt(1, 4);

        still = new AnimationChannel(animImage, 9, 32, 32, Duration.seconds(1), 0, 0);
        walk = new AnimationChannel(animImage, 9, 32, 32, Duration.seconds(-((movementSpeed-195f)/75f)), 1, 4);
        attack = new AnimationChannel(animImage, 9, 32, 32, Duration.seconds(1), 8, 8);
        knockout = new AnimationChannel(animImage, 9, 32, 32, Duration.seconds(1), 5, 7);

        texture = new AnimatedTexture(still);

        animTimer = FXGL.newLocalTimer();
        attackTimer = FXGL.newLocalTimer();
        turnTimer = FXGL.newLocalTimer();
        animTimer.capture();
        attackTimer.capture();
        turnTimer.capture();
    }

    private final EntityState GUARD_LEFT = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(isInWalkingArea()) {
                avoidCollisionWithScalper();
                if(hasCollidedWithPlatform()) {
                    moveRight();
                    moveRight();
                    entity.getComponent(StateComponent.class).changeState(GUARD_RIGHT);
                } else {
                    moveLeft();
                }
            } else if(turnTimer.elapsed(Duration.seconds(1))) {
                moveRight();
                turnTimer.capture();
                entity.getComponent(StateComponent.class).changeState(GUARD_RIGHT);
            } else {
                moveRight();
            }
        }
    };

    private final EntityState GUARD_RIGHT = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(isInWalkingArea()) {
                avoidCollisionWithScalper();
                if(hasCollidedWithPlatform()) {
                    moveLeft();
                    moveLeft();
                    entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
                } else {
                    moveRight();
                }
            } else if(turnTimer.elapsed(Duration.seconds(1))) {
                moveLeft();
                turnTimer.capture();
                entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
            }

        }
    };

    //TODO Attack now just means jump every 3 sec, actually follow the player.
    private final EntityState ATTACK = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(entity.distance(getPlayer()) < sensitivity && attackTimer.elapsed(Duration.seconds(attackCooldown))) {
                if (entity.getPosition().getX() - getPlayer().getPosition().getX() > 0) {
                    moveLeft();
                } else {
                    moveRight();
                }
                jump(ThreadLocalRandom.current().nextInt(100, 250));
                attackTimer.capture();
            } else {
                entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
            }
        }
    };

    private final EntityState POST = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(entity.distance(getPlayer()) < sensitivity + 30 && attackTimer.elapsed(Duration.seconds(3))) {
                stopMovement();
                jump(ThreadLocalRandom.current().nextInt(150, 250));
                attackTimer.capture();
            }
        }
    };

    private final EntityState KNOCKOUT = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {

        }
    };

    private Entity getPlayer() {
        if(player != null) return player;
        player = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
        return player;
    }

    private boolean hasCollidedWithPlatform() {
        List<Entity> collidingPlatforms = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.PLATFORM).stream().collect(Collectors.toList());
        if(collidingPlatforms.size() > 0 && platform != null) {
            return collidingPlatforms.get(0) != platform && FXGLMath.abs(sqrDistance(collidingPlatforms.get(0).getBoundingBoxComponent(), entity.getPosition())) < 1000;
        } else {
            return false;
        }
    }

    private void avoidCollisionWithScalper() {
        List<Entity> collidingScalpers = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.SCALPER).stream().collect(Collectors.toList());
        if(collidingScalpers.size() > 0 && platform != null && FXGLMath.abs(sqrDistance(collidingScalpers.get(0).getBoundingBoxComponent(), entity.getPosition())) < 1000 && turnTimer.elapsed(Duration.seconds(1))) {
            turnTimer.capture();
            if(entity.getComponent(StateComponent.class).getCurrentState() == GUARD_LEFT) {
                moveLeft();
                entity.getComponent(StateComponent.class).changeState(GUARD_RIGHT);
            } else {
                moveRight();
                entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
            }
        }
    }

    private boolean isInWalkingArea() {
        if(platform == null) return true;
        BoundingBoxComponent platformBoxComponent = platform.getBoundingBoxComponent();
        if(entity.getWidth() > platformBoxComponent.getWidth() / 4) {
            entity.getComponent(StateComponent.class).changeState(POST);
            return true;
        } else {
            Rectangle2D walkingArea = new Rectangle2D(platformBoxComponent.getMinXWorld() + entity.getWidth() * 2, platformBoxComponent.getMinYWorld() - 400, platformBoxComponent.getWidth() - entity.getWidth() * 3 - entity.getWidth(), platformBoxComponent.getHeight() + 400);
            return entity.getBoundingBoxComponent().isWithin(walkingArea);
        }
    }

    private void updateAnimation() {
        EntityState currentState = entity.getComponent(StateComponent.class).getCurrentState();
        if(currentState == ATTACK || currentState == POST) {
            if(!onGround || physics.getVelocityY() > 0) {
                texture.loopAnimationChannel(attack);
            } else {
                texture.loopAnimationChannel(still);
            }
        } else if((currentState == GUARD_LEFT || currentState == GUARD_RIGHT) && physics.isMovingX() && animTimer.elapsed(Duration.seconds(1)) && texture.getAnimationChannel() != walk) {
            animTimer.capture();
            texture.loopAnimationChannel(walk);
        } else if(currentState == KNOCKOUT && animTimer.elapsed(Duration.seconds(1))) {
            animTimer.capture();
            texture.loopAnimationChannel(knockout);
        }
    }

    private void checkAirtime() {
        if(!onGround) {
            Point2D currentPoint = entity.getPosition();
            if(previousPoint != null) distanceFallen += currentPoint.distance(previousPoint);
            previousPoint = currentPoint;
        } else {
            if(distanceFallen > ThreadLocalRandom.current().nextInt(100, 700)) entity.getComponent(StateComponent.class).changeState(KNOCKOUT);
            previousPoint = entity.getPosition();
            distanceFallen = 0;
        }
    }


    private double sqrDistance(BoundingBoxComponent rect, Point2D p) {
        double rx = (rect.getMinXWorld() + rect.getMaxXWorld()) / 2;
        double ry = (rect.getMinYWorld() + rect.getMaxYWorld()) / 2;
        double rwidth = rect.getMaxXWorld() - rect.getMinXWorld();
        double rheight = rect.getMaxYWorld() - rect.getMinYWorld();

        double dx = Math.max(Math.abs(p.getX() - rx) - rwidth / 2, 0);
        double dy = Math.max(Math.abs(p.getY() - ry) - rheight / 2, 0);
        return dx * dx + dy * dy;
    }

    @Override
    public void onUpdate(double tpf) {
        if(entity.getPosition().getY() > 1000) entity.removeFromWorld(); //Remove the entity if it is knocked off a platform and falling forever
        if(entity.distance(getPlayer()) < 50 && entity.getComponent(StateComponent.class).getCurrentState() != POST) {
            entity.getComponent(StateComponent.class).changeState(ATTACK);
        }
        FXGL.getGameWorld().getCollidingEntities(entity).stream().filter(e -> e.getType() == EntityType.PLATFORM).findFirst().ifPresent(possiblePlatform -> platform = possiblePlatform);
        checkAirtime();
        updateAnimation();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 21));
        entity.getViewComponent().addChild(texture);

        physics.onGroundProperty().addListener((obs, old, isOnGround) -> onGround = isOnGround);

        entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
    }

    public void moveLeft() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-movementSpeed);
    }

    public void moveRight() {
        getEntity().setScaleX(1);
        physics.setVelocityX(movementSpeed);
    }

    public void stopMovement() {
        physics.setVelocityX(0);
    }

    public void jump(int velocity) {
        physics.setVelocityY(-FXGLMath.abs(velocity));
    }
}
