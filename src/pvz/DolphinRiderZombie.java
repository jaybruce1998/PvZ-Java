package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class DolphinRiderZombie extends Zombie {
    private boolean jumped;

    public DolphinRiderZombie(int row, double x, double y) {
        super(row, x, y, 68, 88, 0.34, 170, 30);
    }

    public boolean canVault() {
        return !jumped;
    }

    public void vaultTo(double newX) {
        x = newX;
        jumped = true;
        setIntrinsicSpeedFactor(0.5);
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
        g2.setColor(new Color(116, 176, 224));
        g2.fillOval((int) x + 6, (int) y + 42, 44, 16);
        g2.fillOval((int) x + 34, (int) y + 44, 24, 12);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 24, (int) y + 2, 26, 26);
        drawEyes(g2, (int) x + 29, (int) x + 38, (int) y + 8);
        g2.setColor(new Color(98, 58, 48));
        g2.fillRect((int) x + 18, (int) y + 28, 24, 26);
    }
}
