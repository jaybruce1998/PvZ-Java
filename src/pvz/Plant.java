package pvz;

import java.awt.Graphics2D;

public abstract class Plant {
    protected final int row;
    protected final int col;
    protected final double x;
    protected final double y;
    protected final int width;
    protected final int height;
    protected double health;

    protected Plant(int row, int col, double x, double y, int width, int height, double health) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.health = health;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void damage(double amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public abstract PlantType type();

    public abstract void update(GamePanel game, double dt);

    public abstract void render(Graphics2D g2);
}
