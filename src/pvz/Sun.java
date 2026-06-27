package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class Sun {
    private double x;
    private double y;
    private final double targetY;
    private final int value;
    private final boolean skyDrop;
    private double lifetime = 9.5;
    private boolean collected;

    public Sun(double x, double y, double targetY, int value, boolean skyDrop) {
        this.x = x;
        this.y = y;
        this.targetY = targetY;
        this.value = value;
        this.skyDrop = skyDrop;
    }

    public int value() {
        return value;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public boolean isExpired() {
        return lifetime <= 0;
    }

    public void update(double dt) {
        lifetime -= dt;
        if (skyDrop && y < targetY) {
            y = Math.min(targetY, y + 90 * dt);
        }
    }

    public boolean contains(int mx, int my) {
        return mx >= x && mx <= x + 40 && my >= y && my <= y + 40;
    }

    public void render(Graphics2D g2) {
        g2.setColor(new Color(255, 220, 64));
        g2.fillOval((int) x, (int) y, 40, 40);
        g2.setColor(new Color(255, 243, 143));
        g2.fillOval((int) x + 7, (int) y + 7, 26, 26);
        g2.setColor(new Color(251, 186, 45));
        g2.drawOval((int) x, (int) y, 40, 40);
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4.0;
            int cx = (int) (x + 20 + Math.cos(angle) * 27);
            int cy = (int) (y + 20 + Math.sin(angle) * 27);
            g2.drawLine((int) x + 20, (int) y + 20, cx, cy);
        }
    }
}
