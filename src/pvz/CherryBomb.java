package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class CherryBomb extends Plant {
    private double fuse = 0.8;
    private boolean exploded;

    public CherryBomb(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 999);
    }

    @Override
    public PlantType type() {
        return PlantType.CHERRY_BOMB;
    }

    @Override
    public void update(GamePanel game, double dt) {
        fuse -= dt;
        if (!exploded && fuse <= 0) {
            exploded = true;
            game.explodeAt(row, col, 1, 1200);
            health = 0;
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(201, 40, 52));
        g2.fillOval((int) x + 16, (int) y + 26, 24, 24);
        g2.fillOval((int) x + 36, (int) y + 20, 24, 24);
        g2.setColor(new Color(80, 140, 62));
        g2.drawLine((int) x + 34, (int) y + 22, (int) x + 30, (int) y + 6);
        g2.drawLine((int) x + 34, (int) y + 22, (int) x + 50, (int) y + 8);
        g2.setColor(new Color(255, 214, 111));
        g2.fillOval((int) x + 28, (int) y + 6, 10, 10);
    }
}
