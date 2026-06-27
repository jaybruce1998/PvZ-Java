package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class LadderZombie extends Zombie {
    public LadderZombie(int row, double x, double y) {
        super(row, x, y, 64, 92, 0.2, 300, 32);
    }

    @Override
    public int wavePoints() {
        return 4;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(103, 69, 45));
        for (int step = 0; step < 4; step++) {
            g2.drawLine((int) x + 2, (int) y + 28 + step * 8, (int) x + 18, (int) y + 28 + step * 8);
        }
        g2.drawLine((int) x + 2, (int) y + 24, (int) x + 2, (int) y + 56);
        g2.drawLine((int) x + 18, (int) y + 24, (int) x + 18, (int) y + 56);
        g2.setColor(new Color(98, 63, 50));
        g2.fillRect((int) x + 20, (int) y + 32, 24, 34);
        g2.setColor(new Color(66, 82, 122));
        g2.fillRect((int) x + 22, (int) y + 66, 9, 20);
        g2.fillRect((int) x + 33, (int) y + 66, 9, 20);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 18, (int) y + 2, 28, 28);
        drawEyes(g2, (int) x + 24, (int) x + 34, (int) y + 10);
    }
}
