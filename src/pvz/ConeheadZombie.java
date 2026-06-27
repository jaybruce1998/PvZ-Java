package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class ConeheadZombie extends Zombie {
    public ConeheadZombie(int row, double x, double y) {
        super(row, x, y, 58, 90, 0.19, 220, 32);
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
        g2.setColor(new Color(214, 121, 30));
        int[] xs = {(int) x + 18, (int) x + 44, (int) x + 31};
        int[] ys = {(int) y + 12, (int) y + 12, (int) y - 16};
        g2.fillPolygon(xs, ys, 3);
        g2.setColor(new Color(115, 163, 113));
        g2.fillOval((int) x + 15, (int) y + 6, 30, 30);
        g2.setColor(new Color(95, 61, 48));
        g2.fillRect((int) x + 18, (int) y + 34, 24, 34);
        g2.setColor(new Color(54, 69, 109));
        g2.fillRect((int) x + 20, (int) y + 68, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 68, 9, 20);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 14);
    }
}
