package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class FlagZombie extends Zombie {
    public FlagZombie(int row, double x, double y) {
        super(row, x, y, 62, 88, 0.26, 105, 28);
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
        g2.setColor(new Color(152, 75, 58));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(60, 63, 104));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(130, 177, 127));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        g2.setColor(new Color(131, 78, 22));
        g2.drawLine((int) x + 48, (int) y + 20, (int) x + 48, (int) y + 74);
        g2.setColor(new Color(205, 40, 48));
        g2.fillRect((int) x + 48, (int) y + 18, 22, 16);
    }
}
