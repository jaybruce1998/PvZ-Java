package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class JackInTheBoxZombie extends Zombie {
    public JackInTheBoxZombie(int row, double x, double y) {
        super(row, x, y, 62, 88, 0.22, 155, 30);
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
    public void onDeath(GamePanel game) {
        game.explodePlantsAt(row, x + width * 0.5, 1);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(46, 109, 186));
        g2.fillOval((int) x + 18, (int) y - 8, 28, 10);
        g2.setColor(new Color(239, 205, 55));
        g2.fillRect((int) x + 30, (int) y - 18, 4, 12);
        g2.fillOval((int) x + 26, (int) y - 22, 12, 10);

        g2.setColor(new Color(83, 56, 136));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(66, 82, 122));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        g2.setColor(new Color(204, 54, 54));
        g2.fillRoundRect((int) x + 1, (int) y + 34, 18, 18, 4, 4);
        g2.setColor(new Color(244, 223, 109));
        g2.drawLine((int) x + 5, (int) y + 39, (int) x + 15, (int) y + 47);
        g2.drawLine((int) x + 15, (int) y + 39, (int) x + 5, (int) y + 47);
    }
}
