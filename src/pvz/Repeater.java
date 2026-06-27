package pvz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public final class Repeater extends Plant {
    private double shotCooldown = 0.6;
    private double followUpTimer = -1;

    public Repeater(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 150);
    }

    @Override
    public PlantType type() {
        return PlantType.REPEATER;
    }

    @Override
    public void update(GamePanel game, double dt) {
        shotCooldown -= dt;
        if (followUpTimer >= 0) {
            followUpTimer -= dt;
            if (followUpTimer <= 0 && game.hasZombieAhead(row, x)) {
                game.addProjectile(new Projectile(row, x + width - 10, y + 35, 7.4, 20));
                followUpTimer = -1;
            }
        }
        if (shotCooldown <= 0 && game.hasZombieAhead(row, x)) {
            shotCooldown = 1.3;
            followUpTimer = 0.18;
            game.addProjectile(new Projectile(row, x + width - 10, y + 35, 7.4, 20));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(73, 152, 65));
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(x + width * 0.45, y + 34, x + width * 0.45, y + height - 10));
        g2.setColor(new Color(95, 190, 85));
        g2.fillOval((int) x + 18, (int) y + 18, 46, 38);
        g2.fillOval((int) x + 42, (int) y + 12, 18, 16);
        g2.fillOval((int) x + 46, (int) y + 26, 22, 18);
        g2.fillOval((int) x + 57, (int) y + 14, 18, 14);
        g2.fillOval((int) x + 61, (int) y + 30, 18, 14);
        g2.setColor(new Color(92, 182, 74));
        g2.fillOval((int) x + 4, (int) y + 52, 30, 16);
        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 28, (int) y + 28, 8, 10);
        g2.fillOval((int) x + 39, (int) y + 28, 8, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 31, (int) y + 31, 3, 4);
        g2.fillOval((int) x + 42, (int) y + 31, 3, 4);
    }
}
