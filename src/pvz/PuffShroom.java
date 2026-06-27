package pvz;

import java.awt.Color;
import java.awt.Graphics2D;

public final class PuffShroom extends Plant {
    private static final double RANGE = 260;
    private double shotCooldown = 0.45;

    public PuffShroom(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 80);
    }

    @Override
    public PlantType type() {
        return PlantType.PUFF_SHROOM;
    }

    @Override
    public void update(GamePanel game, double dt) {
        if (!game.isNightLevel()) {
            return;
        }
        shotCooldown -= dt;
        if (shotCooldown <= 0 && game.hasZombieWithin(row, x, RANGE)) {
            shotCooldown = 0.95;
            game.addProjectile(new Projectile(row, x + width - 4, y + 32, 6.3, 14, false, new Color(215, 186, 255), new Color(223, 194, 255, 120)));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(134, 96, 182));
        g2.fillOval((int) x + 12, (int) y + 18, 52, 26);
        g2.fillOval((int) x + 22, (int) y + 10, 32, 22);
        g2.setColor(new Color(222, 210, 239));
        g2.fillRect((int) x + 32, (int) y + 36, 10, 28);
        g2.fillOval((int) x + 26, (int) y + 56, 22, 8);
        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 28, (int) y + 24, 7, 8);
        g2.fillOval((int) x + 40, (int) y + 24, 7, 8);
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 31, (int) y + 27, 2, 3);
        g2.fillOval((int) x + 43, (int) y + 27, 2, 3);
    }
}
