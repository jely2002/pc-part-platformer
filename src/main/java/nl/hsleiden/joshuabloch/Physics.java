package nl.hsleiden.joshuabloch;

import nl.hsleiden.joshuabloch.game.EntityType;
import nl.hsleiden.joshuabloch.game.PlayerComponent;

import static com.almasb.fxgl.dsl.FXGL.onCollisionBegin;
import static com.almasb.fxgl.dsl.FXGL.onCollisionOneTimeOnly;

public class Physics {

    private final ScoreCounter scoreCounter;

    public Physics(ScoreCounter scoreCounter) {
        this.scoreCounter = scoreCounter;
        setCollisions();
    }

    private void setCollisions() {
        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.COIN, (player, coin) -> {
            coin.removeFromWorld();
            scoreCounter.increment(1);
            //TODO play coin pick up sound here
        });

        onCollisionOneTimeOnly(EntityType.PLAYER, EntityType.SACK, (player, coin) -> {
            coin.removeFromWorld();
            scoreCounter.increment(5);
            //TODO play sack pick up sound here
        });

        onCollisionBegin(EntityType.PLAYER, EntityType.SCALPER, (player, scalper) -> {
            System.out.println("register");
            if(player.getComponent(PlayerComponent.class).isInvincible()) return; //Don't register hits when the player has just respawned
            System.out.println("Hit");
            scoreCounter.decrement(1);
            //TODO play scalper hit sound here
        });
    }

}
