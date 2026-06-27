package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class BasicZombie extends Zombie {
    public BasicZombie(int row, double x, double y) {
        super(row, x, y, 58, 86, 0.23, 110, 28);
    }

    @Override
    public int wavePoints() {
        return 1;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(118, 74, 58));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(76, 49, 92));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(130, 177, 127));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        g2.fillRect((int) x + 10, (int) y + 30, 10, 10);
        g2.fillRect((int) x + 40, (int) y + 30, 10, 10);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        g2.drawLine((int) x + 20, (int) y + 23, (int) x + 38, (int) y + 20);
    }
}
