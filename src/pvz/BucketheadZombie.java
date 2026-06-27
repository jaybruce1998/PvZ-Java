package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class BucketheadZombie extends Zombie {
    public BucketheadZombie(int row, double x, double y) {
        super(row, x, y, 60, 92, 0.17, 360, 34);
    }

    @Override
    public int wavePoints() {
        return 3;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(173, 182, 189));
        g2.fillRect((int) x + 17, (int) y - 6, 28, 18);
        g2.setColor(new Color(123, 134, 141));
        g2.drawRect((int) x + 17, (int) y - 6, 28, 18);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 15, (int) y + 8, 30, 30);
        g2.setColor(new Color(92, 60, 50));
        g2.fillRect((int) x + 18, (int) y + 36, 24, 34);
        g2.setColor(new Color(66, 82, 122));
        g2.fillRect((int) x + 20, (int) y + 70, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 70, 9, 20);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 16);
    }
}
