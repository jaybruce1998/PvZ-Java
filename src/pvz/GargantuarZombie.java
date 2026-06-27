package pvz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public final class GargantuarZombie extends Zombie {
    public GargantuarZombie(int row, double x, double y) {
        super(row, x, y - 92, 100, 184, 0.12, 1700, 80);
    }

    @Override
    public int wavePoints() {
        return 8;
    }

    @Override
    public Rectangle bounds() {
        return new Rectangle((int) x + 18, (int) y + 106, 58, 72);
    }

    @Override
    public void hitByCherryBomb(GamePanel game) {
        damage(1200);
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(104, 110, 86));
        g2.fillRect((int) x + 24, (int) y + 72, 48, 64);
        g2.setColor(new Color(88, 66, 46));
        g2.fillRect((int) x + 28, (int) y + 136, 14, 38);
        g2.fillRect((int) x + 54, (int) y + 136, 14, 38);
        g2.setColor(new Color(120, 152, 113));
        g2.fillOval((int) x + 16, (int) y + 18, 56, 46);
        drawEyes(g2, (int) x + 28, (int) x + 46, (int) y + 30);
        g2.setColor(new Color(123, 92, 62));
        g2.fillRect((int) x + 2, (int) y + 54, 20, 88);
    }
}
