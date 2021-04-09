package nl.hsleiden.joshuabloch.game;

public interface Moveable {
    void moveLeft();
    void moveRight();
    void jump(int velocity);
    void stopMovement();
}
