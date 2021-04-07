package nl.hsleiden.joshuabloch.game;

public interface Moveable {
    public void moveLeft();
    public void moveRight();
    public void jump(int velocity);
    public void stopMovement();
}
