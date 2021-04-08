package nl.hsleiden.joshuabloch.game;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.LiftComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.state.StateComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;

public class EntityManager implements EntityFactory {

    @Spawns("coin")
    public Entity addCoin(SpawnData data) {
        Texture texture = texture("coin.png");

        LiftComponent lift = new LiftComponent();
        lift.setGoingUp(true);
        lift.yAxisDistanceDuration(6, Duration.seconds(2));

        return entityBuilder(data)
                .type(EntityType.COIN)
                .viewWithBBox(getScaledTexture(0.5, texture))
                .with(new CollidableComponent(true))
                .with(lift)
                .build();
    }

    @Spawns("sack")
    public Entity addSack(SpawnData data) {
        Texture texture = texture("coin.png"); //TODO replace for a sack image

        Entity sack = entityBuilder(data)
                .type(EntityType.SACK)
                .viewWithBBox(getScaledTexture(0.5, texture))
                .with(new CollidableComponent(true))
                .build();

        sack.getTransformComponent().setScaleOrigin(new Point2D(sack.getWidth() / 4, sack.getHeight() / 4));
        FXGL.animationBuilder()
                .interpolator(Interpolators.SMOOTH.EASE_IN_OUT())
                .duration(Duration.seconds(2))
                .repeatInfinitely()
                .autoReverse(true)
                .scale(sack)
                .from(new Point2D(1, 1))
                .to(new Point2D(1.1, 1.1))
                .buildAndPlay();

        return sack;
    }

    @Spawns("part")
    public Entity addPart(SpawnData data) {
        Texture texture = texture(data.get("part-type") + ".png");

        return entityBuilder(data)
                .type(EntityType.PART)
                .viewWithBBox(texture)
                .with(new CollidableComponent(true))
                .build();
    }

    @Spawns("background")
    public Entity addBackground(SpawnData data) {
        Image bgImage = image("forest.png");
        return entityBuilder(data)
                .view(new ScrollingBackgroundView(bgImage, getAppWidth(), getAppHeight()))
                .zIndex(-1)
                .with(new IrremovableComponent())
                .build();
    }

    @Spawns("scalper")
    public Entity addScalper(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        FixtureDef fixture = new FixtureDef();
        fixture.density(0).friction(100f);
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(fixture);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));

        return entityBuilder(data)
                .type(EntityType.SCALPER)
                .bbox(new HitBox(new Point2D(5,8), BoundingShape.circle(11)))
                .with(physics)
                .with(new StateComponent())
                .with(new ScalperComponent())
                .build();
    }

    @Spawns("platform")
    public Entity addPlatform(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setFixtureDef(new FixtureDef().friction(1.5f));
        physics.setBodyType(BodyType.STATIC);
        return entityBuilder(data)
                .type(EntityType.PLATFORM)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(physics)
                .build();
    }

    @Spawns("player")
    public Entity addPlayer(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
        FixtureDef fixture = new FixtureDef();
        fixture.density(0f).friction(0f);
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GROUND_SENSOR", new Point2D(16, 38), BoundingShape.box(6, 8)));
        physics.setFixtureDef(fixture);

        return entityBuilder(data)
                .type(EntityType.PLAYER)
                .bbox(new HitBox(new Point2D(5,5), BoundingShape.circle(11)))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .build();
    }

    private Texture getScaledTexture(double scale, Texture texture) {
        texture.setScaleX(scale);
        texture.setScaleY(scale);
        texture.setTranslateX(texture.getWidth() * -scale);
        texture.setTranslateY(texture.getHeight() * -scale);
        return texture;
    }
}
