package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class DuckyTubeZombie extends Zombie {
    public DuckyTubeZombie(int row, double x, double y) {
        super(row, x, y, 60, 88, 0.22, 120, 28);
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
        g2.setColor(new Color(251, 215, 65));
        g2.fillOval((int) x + 10, (int) y + 40, 40, 20);
        g2.setColor(new Color(127, 174, 123));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        g2.setColor(new Color(92, 60, 50));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 26);
        g2.setColor(new Color(66, 82, 122));
        g2.fillRect((int) x + 20, (int) y + 56, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 56, 9, 20);
    }
}
