package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class SnorkelZombie extends Zombie {
    public SnorkelZombie(int row, double x, double y) {
        super(row, x, y, 60, 88, 0.24, 140, 30);
    }

    @Override
    public boolean isTargetable() {
        return x < 980;
    }

    @Override
    public int wavePoints() {
        return 2;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(90, 197, 230, 120));
        g2.fillOval((int) x + 8, (int) y + 46, 40, 16);
        g2.setColor(new Color(127, 174, 123));
        g2.fillOval((int) x + 15, (int) y + 6, 30, 26);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 12);
        g2.setColor(new Color(74, 150, 193));
        g2.drawLine((int) x + 42, (int) y + 6, (int) x + 54, (int) y - 8);
        g2.fillRect((int) x + 51, (int) y - 12, 8, 8);
        g2.setColor(new Color(92, 60, 50));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 26);
    }
}
