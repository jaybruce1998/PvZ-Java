package pvz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public final class Peashooter extends Plant {
    private double shotCooldown = 0.6;

    public Peashooter(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 140);
    }

    @Override
    public PlantType type() {
        return PlantType.PEASHOOTER;
    }

    @Override
    public void update(GamePanel game, double dt) {
        shotCooldown -= dt;
        if (shotCooldown <= 0 && game.hasZombieAhead(row, x)) {
            shotCooldown = 1.15;
            game.addProjectile(new Projectile(row, x + width - 10, y + 35, 7.2, 20));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(73, 152, 65));
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(x + width * 0.45, y + 34, x + width * 0.45, y + height - 10));

        g2.setColor(new Color(92, 182, 74));
        g2.fillOval((int) x + 4, (int) y + 52, 30, 16);
        g2.fillOval((int) x + 26, (int) y + 60, 28, 14);

        g2.setColor(new Color(95, 190, 85));
        g2.fillOval((int) x + 18, (int) y + 18, 46, 38);
        g2.fillOval((int) x + 44, (int) y + 22, 22, 20);
        g2.fillOval((int) x + 54, (int) y + 18, 24, 16);
        g2.setColor(new Color(76, 146, 58));
        g2.drawOval((int) x + 57, (int) y + 21, 14, 9);

        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 28, (int) y + 28, 8, 10);
        g2.fillOval((int) x + 39, (int) y + 28, 8, 10);
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 31, (int) y + 31, 3, 4);
        g2.fillOval((int) x + 42, (int) y + 31, 3, 4);
    }
}
