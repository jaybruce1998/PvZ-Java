package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class PoleVaultZombie extends Zombie {
    private boolean jumped;

    public PoleVaultZombie(int row, double x, double y) {
        super(row, x, y, 64, 88, 0.34, 120, 30);
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
        return 2;
    }

    @Override
    public int rewardSun() {
        return 0;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(93, 64, 138));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(73, 79, 124));
        g2.fillRect((int) x + 20, (int) y + 64, 9, 20);
        g2.fillRect((int) x + 31, (int) y + 64, 9, 20);
        g2.setColor(new Color(129, 176, 125));
        g2.fillOval((int) x + 15, (int) y, 30, 30);
        drawEyes(g2, (int) x + 22, (int) x + 32, (int) y + 8);
        if (!jumped) {
            g2.setColor(new Color(125, 82, 39));
            g2.drawLine((int) x + 50, (int) y + 10, (int) x + 72, (int) y + 64);
        }
    }
}
