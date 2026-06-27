package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class ScreenDoorZombie extends Zombie {
    private double doorHealth = 840;

    public ScreenDoorZombie(int row, double x, double y) {
        super(row, x, y, 64, 90, 0.17, 140, 30);
    }

    @Override
    public void damage(double amount) {
        if (doorHealth > 0) {
            doorHealth -= amount;
            return;
        }
        super.damage(amount);
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
        g2.setColor(new Color(98, 67, 52));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(66, 82, 122));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 15, (int) y + 4, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 12);
        if (doorHealth > 0) {
            g2.setColor(new Color(153, 161, 167));
            g2.fillRect((int) x, (int) y + 28, 22, 38);
            g2.setColor(new Color(111, 117, 121));
            g2.drawRect((int) x, (int) y + 28, 22, 38);
            g2.drawLine((int) x + 6, (int) y + 28, (int) x + 6, (int) y + 66);
            g2.drawLine((int) x + 12, (int) y + 28, (int) x + 12, (int) y + 66);
        }
    }
}
