package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class PogoZombie extends Zombie {
    private boolean jumped;

    public PogoZombie(int row, double x, double y) {
        super(row, x, y, 64, 90, 0.31, 190, 30);
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
        g2.setColor(new Color(130, 78, 47));
        g2.drawLine((int) x + 12, (int) y + 12, (int) x + 12, (int) y + 84);
        g2.drawLine((int) x + 4, (int) y + 84, (int) x + 20, (int) y + 84);
        g2.setColor(new Color(83, 56, 136));
        g2.fillRect((int) x + 18, (int) y + 30, 24, 34);
        g2.setColor(new Color(126, 170, 121));
        g2.fillOval((int) x + 18, (int) y + 2, 28, 28);
        drawEyes(g2, (int) x + 24, (int) x + 34, (int) y + 10);
    }
}
