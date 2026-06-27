package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class Wallnut extends Plant {
    public Wallnut(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 560);
    }

    @Override
    public PlantType type() {
        return PlantType.WALLNUT;
    }

    @Override
    public void update(GamePanel game, double dt) {
    }

    @Override
    public void render(Graphics2D g2) {
        double ratio = Math.max(0, health / 560.0);
        Color shell = ratio > 0.55 ? new Color(171, 118, 63) : ratio > 0.25 ? new Color(149, 99, 57) : new Color(124, 79, 53);
        g2.setColor(shell);
        g2.fillOval((int) x + 14, (int) y + 12, width - 28, height - 10);

        g2.setColor(new Color(96, 61, 41));
        if (ratio < 0.55) {
            g2.drawArc((int) x + 24, (int) y + 34, 16, 20, 80, 160);
            g2.drawArc((int) x + 48, (int) y + 46, 14, 18, 110, 140);
        }
        if (ratio < 0.25) {
            g2.drawArc((int) x + 26, (int) y + 56, 20, 16, 90, 180);
        }

        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 32, (int) y + 34, 8, 10);
        g2.fillOval((int) x + 48, (int) y + 34, 8, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 35, (int) y + 37, 3, 4);
        g2.fillOval((int) x + 51, (int) y + 37, 3, 4);
        g2.drawArc((int) x + 35, (int) y + 48, 18, 10, 10, -160);
    }
}
