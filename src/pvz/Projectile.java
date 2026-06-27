package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class Projectile {
    private final int row;
    private double x;
    private final double y;
    private final double speed;
    private final double damage;
    private final boolean slows;
    private final Color coreColor;
    private final Color glowColor;
    private boolean active = true;

    public Projectile(int row, double x, double y, double speed, double damage) {
        this(row, x, y, speed, damage, false, new Color(90, 189, 60), new Color(151, 222, 120, 120));
    }

    public Projectile(int row, double x, double y, double speed, double damage, boolean slows, Color coreColor, Color glowColor) {
        this.row = row;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.damage = damage;
        this.slows = slows;
        this.coreColor = coreColor;
        this.glowColor = glowColor;
    }

    public int row() {
        return row;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double damage() {
        return damage;
    }

    public boolean isActive() {
        return active;
    }

    public boolean slows() {
        return slows;
    }

    public void deactivate() {
        active = false;
    }

    public void update(double dt) {
        x += speed * 60 * dt;
    }

    public void render(Graphics2D g2) {
        g2.setColor(coreColor);
        g2.fillOval((int) x - 7, (int) y - 7, 14, 14);
        g2.setColor(glowColor);
        g2.fillOval((int) x - 10, (int) y - 10, 20, 20);
    }
}
