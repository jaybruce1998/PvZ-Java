package pvz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public final class SnowPea extends Plant {
    private double shotCooldown = 0.6;

    public SnowPea(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 145);
    }

    @Override
    public PlantType type() {
        return PlantType.SNOW_PEA;
    }

    @Override
    public void update(GamePanel game, double dt) {
        shotCooldown -= dt;
        if (shotCooldown <= 0 && game.hasZombieAhead(row, x)) {
            shotCooldown = 1.35;
            game.addProjectile(new Projectile(row, x + width - 10, y + 35, 6.9, 18, true, new Color(141, 232, 255), new Color(198, 240, 255, 130)));
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(75, 155, 70));
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(x + width * 0.45, y + 34, x + width * 0.45, y + height - 10));

        g2.setColor(new Color(183, 234, 255));
        g2.fillOval((int) x + 18, (int) y + 18, 46, 38);
        g2.fillOval((int) x + 44, (int) y + 22, 22, 20);
        g2.fillOval((int) x + 54, (int) y + 18, 24, 16);
        g2.setColor(new Color(140, 210, 240));
        g2.drawOval((int) x + 57, (int) y + 21, 14, 9);
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
