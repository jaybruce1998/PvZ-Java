package pvz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public final class Sunflower extends Plant {
    private double sunTimer = 5.0;

    public Sunflower(int row, int col, double x, double y, int width, int height) {
        super(row, col, x, y, width, height, 120);
    }

    @Override
    public PlantType type() {
        return PlantType.SUNFLOWER;
    }

    @Override
    public void update(GamePanel game, double dt) {
        sunTimer -= dt;
        if (sunTimer <= 0) {
            sunTimer = 8.0;
            game.spawnSun(x + width * 0.5 - 20, y + 12, 25, false);
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(75, 155, 70));
        g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(x + width * 0.48, y + 26, x + width * 0.48, y + height - 12));

        g2.setColor(new Color(82, 170, 74));
        g2.fillOval((int) x + 10, (int) y + 40, 24, 16);
        g2.fillOval((int) x + width - 34, (int) y + 48, 24, 16);

        g2.setColor(new Color(247, 198, 41));
        for (int i = 0; i < 12; i++) {
            double angle = i * Math.PI / 6.0;
            double px = x + width * 0.5 + Math.cos(angle) * 26 - 10;
            double py = y + 26 + Math.sin(angle) * 26 - 10;
            g2.fill(new Ellipse2D.Double(px, py, 20, 20));
        }

        g2.setColor(new Color(121, 79, 40));
        g2.fillOval((int) x + 24, (int) y + 12, 38, 38);

        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 34, (int) y + 22, 8, 11);
        g2.fillOval((int) x + 46, (int) y + 22, 8, 11);
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 37, (int) y + 26, 3, 4);
        g2.fillOval((int) x + 49, (int) y + 26, 3, 4);
        g2.drawArc((int) x + 37, (int) y + 28, 14, 10, 190, 160);
    }
}
