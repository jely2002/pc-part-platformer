package nl.hsleiden.joshuabloch.game;

import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.state.EntityState;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ScalperComponent extends Component implements Moveable {

    private PhysicsComponent physics;

    private LocalTimer jumpTimer;

    private LocalTimer turnTimer;

    private Entity platform;

    private Entity player;

    private EntityState GUARD_LEFT = new EntityState() {
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
            }
        }
    };

    private EntityState GUARD_RIGHT = new EntityState() {
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
    private EntityState ATTACK = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(entity.distance(getPlayer()) < 50 && jumpTimer.elapsed(Duration.seconds(3))) {
                stopMovement();
                jump(250);
                jumpTimer.capture();
            } else {
                entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
            }
        }
    };

    private EntityState POST = new EntityState() {
        @Override
        protected void onUpdate(double tpf) {
            if(entity.distance(getPlayer()) < 85 && jumpTimer.elapsed(Duration.seconds(3))) {
                stopMovement();
                jump(350);
                jumpTimer.capture();
            }
        }
    };


    private Entity getPlayer() {
        if(player != null) return player;
        return FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).get(0);
    }

    private boolean hasCollidedWithPlatform() {
        List<Entity> collidingPlatforms = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.PLATFORM).stream().collect(Collectors.toList());
        if(collidingPlatforms.size() > 0 && platform != null) {
            return collidingPlatforms.get(0) != platform && sqrDistance(collidingPlatforms.get(0).getBoundingBoxComponent(), entity.getPosition()) < 1000;
        } else {
            return false;
        }
    }

    private void avoidCollisionWithScalper() {
        List<Entity> collidingScalpers = FXGL.getGameWorld().getClosestEntity(entity, e -> e.getType() == EntityType.SCALPER).stream().collect(Collectors.toList());
        if(collidingScalpers.size() > 0 && platform != null && sqrDistance(collidingScalpers.get(0).getBoundingBoxComponent(), entity.getPosition()) < 1000 && turnTimer.elapsed(Duration.seconds(1))) {
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


    static double sqrDistance(BoundingBoxComponent rect, Point2D p) {
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
        if(entity.distance(getPlayer()) < 50 && entity.getComponent(StateComponent.class).getCurrentState() != POST) {
            entity.getComponent(StateComponent.class).changeState(ATTACK);
        }
        FXGL.getGameWorld().getCollidingEntities(entity).stream().filter(e -> e.getType() == EntityType.PLATFORM).findFirst().ifPresent(possiblePlatform -> platform = possiblePlatform);
    }

    @Override
    public void onAdded() {
        jumpTimer = FXGL.newLocalTimer();
        turnTimer = FXGL.newLocalTimer();
        jumpTimer.capture();
        turnTimer.capture();
        entity.getComponent(StateComponent.class).changeState(GUARD_LEFT);
    }

    public void moveLeft() {
        getEntity().setScaleX(-1);
        physics.setVelocityX(-100);
    }

    public void moveRight() {
        getEntity().setScaleX(1);
        physics.setVelocityX(100);
    }

    public void stopMovement() {
        physics.setVelocityX(0);
    }

    public void jump(int velocity) {
        physics.setVelocityY(-FXGLMath.abs(velocity));
    }
}
