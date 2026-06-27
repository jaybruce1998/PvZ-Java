package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class NewspaperZombie extends Zombie {
    private double newspaperHealth = 480;
    private boolean enraged;

    public NewspaperZombie(int row, double x, double y) {
        super(row, x, y, 64, 88, 0.19, 125, 30);
    }

    @Override
    public void damage(double amount) {
        if (newspaperHealth > 0) {
            newspaperHealth -= amount;
            if (newspaperHealth <= 0 && !enraged) {
                enraged = true;
                setIntrinsicSpeedFactor(1.9);
            }
            return;
        }
        super.damage(amount);
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
        g2.setColor(enraged ? new Color(157, 48, 48) : new Color(88, 65, 56));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(68, 70, 111));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(129, 176, 125));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        if (newspaperHealth > 0) {
            g2.setColor(new Color(237, 237, 230));
            g2.fillRect((int) x + 2, (int) y + 32, 18, 24);
            g2.setColor(new Color(120, 120, 120));
            g2.drawLine((int) x + 5, (int) y + 38, (int) x + 16, (int) y + 38);
            g2.drawLine((int) x + 5, (int) y + 44, (int) x + 16, (int) y + 44);
        } else {
            g2.setColor(eyeColor());
            g2.drawLine((int) x + 20, (int) y + 6, (int) x + 28, (int) y + 10);
            g2.drawLine((int) x + 41, (int) y + 6, (int) x + 33, (int) y + 10);
            g2.drawLine((int) x + 21, (int) y + 24, (int) x + 39, (int) y + 21);
        }
    }
}
