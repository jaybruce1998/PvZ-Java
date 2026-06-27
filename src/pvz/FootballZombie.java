package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class FootballZombie extends Zombie {
    public FootballZombie(int row, double x, double y) {
        super(row, x, y, 78, 92, 0.32, 520, 42);
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
        g2.setColor(new Color(168, 28, 30));
        g2.fillOval((int) x + 18, (int) y - 2, 38, 22);
        g2.fillRect((int) x + 16, (int) y + 22, 42, 42);
        g2.setColor(new Color(241, 241, 241));
        g2.drawLine((int) x + 37, (int) y + 26, (int) x + 37, (int) y + 60);
        g2.setColor(new Color(61, 73, 110));
        g2.fillRect((int) x + 24, (int) y + 64, 10, 20);
        g2.fillRect((int) x + 42, (int) y + 64, 10, 20);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 22, (int) y + 2, 28, 26);
        drawEyes(g2, (int) x + 28, (int) x + 38, (int) y + 8);
    }
}
