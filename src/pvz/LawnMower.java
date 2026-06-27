package pvz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public final class LawnMower {
    private final int row;
    private double x;
    private final double y;
    private boolean triggered;
    private boolean spent;

    public LawnMower(int row, double x, double y) {
        this.row = row;
        this.x = x;
        this.y = y;
    }

    public int row() {
        return row;
    }

    public double x() {
        return x;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public boolean isSpent() {
        return spent;
    }

    public Rectangle bounds() {
        return new Rectangle((int) x, (int) y + 22, 56, 30);
    }

    public void trigger() {
        triggered = true;
    }

    public void spend() {
        spent = true;
    }

    public void update(double dt) {
        if (triggered) {
            x += 12 * 60 * dt;
        }
    }

    public void render(Graphics2D g2) {
        if (spent) {
            return;
        }
        g2.setColor(new Color(211, 51, 48));
        g2.fillRect((int) x + 14, (int) y + 18, 28, 20);
        g2.setColor(new Color(45, 45, 45));
        g2.fillOval((int) x + 10, (int) y + 34, 12, 12);
        g2.fillOval((int) x + 34, (int) y + 34, 12, 12);
        g2.setColor(new Color(200, 200, 200));
        g2.fillRect((int) x + 4, (int) y + 22, 16, 10);
        g2.drawLine((int) x + 40, (int) y + 18, (int) x + 54, (int) y + 4);
    }
}
