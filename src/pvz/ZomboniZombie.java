package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class ZomboniZombie extends Zombie {
    public ZomboniZombie(int row, double x, double y) {
        super(row, x, y + 16, 116, 74, 0.18, 520, 46);
    }

    @Override
    public int wavePoints() {
        return 5;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(178, 38, 41));
        g2.fillRoundRect((int) x + 16, (int) y + 12, 66, 34, 12, 12);
        g2.setColor(new Color(193, 225, 248));
        g2.fillRoundRect((int) x + 52, (int) y + 4, 20, 18, 6, 6);
        g2.setColor(new Color(57, 57, 62));
        g2.fillOval((int) x + 10, (int) y + 38, 24, 24);
        g2.fillOval((int) x + 70, (int) y + 38, 24, 24);
        g2.setColor(new Color(128, 174, 121));
        g2.fillOval((int) x + 56, (int) y + 2, 18, 18);
        drawEyes(g2, (int) x + 59, (int) x + 66, (int) y + 6);
    }
}
