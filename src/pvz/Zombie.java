package pvz;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Zombie {
    protected final int row;
    protected double x;
    protected final double y;
    protected final int width;
    protected final int height;
    protected final double baseSpeed;
    protected final double maxHealth;
    protected double health;
    protected final double baseBiteDamagePerSecond;
    protected double slowTimer;
    protected double intrinsicSpeedFactor = 1.0;

    protected Zombie(int row, double x, double y, int width, int height, double speed, double health, double biteDamagePerSecond) {
        this.row = row;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.baseSpeed = speed;
        this.maxHealth = health * 2.0;
        this.health = maxHealth;
        this.baseBiteDamagePerSecond = biteDamagePerSecond;
    }

    public int row() {
        return row;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public Rectangle bounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public void damage(double amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void move(double dt) {
        if (slowTimer > 0) {
            slowTimer = Math.max(0, slowTimer - dt);
        }
        x -= currentMoveSpeed() * 60 * dt;
    }

    public double biteDamagePerSecond() {
        return baseBiteDamagePerSecond * currentSpeedMultiplier();
    }

    public void slow(double duration) {
        slowTimer = Math.max(slowTimer, duration);
    }

    public boolean isTargetable() {
        return true;
    }

    public double currentMoveSpeed() {
        return baseSpeed * currentSpeedMultiplier();
    }

    public double currentSpeedMultiplier() {
        double slowMultiplier = slowTimer > 0 ? 0.55 : 1.0;
        return intrinsicSpeedFactor * slowMultiplier;
    }

    protected void setIntrinsicSpeedFactor(double intrinsicSpeedFactor) {
        this.intrinsicSpeedFactor = intrinsicSpeedFactor;
    }

    protected Color eyeColor() {
        return health <= maxHealth * 0.5 ? new Color(220, 40, 40) : Color.BLACK;
    }

    protected void drawEyes(Graphics2D g2, int leftX, int rightX, int eyeY) {
        g2.setColor(Color.WHITE);
        g2.fillOval(leftX, eyeY, 7, 9);
        g2.fillOval(rightX, eyeY, 7, 9);
        g2.setColor(eyeColor());
        g2.fillOval(leftX + 3, eyeY + 3, 3, 4);
        g2.fillOval(rightX + 3, eyeY + 3, 3, 4);
    }

    public void hitByCherryBomb(GamePanel game) {
        health = 0;
    }

    public void onDeath(GamePanel game) {
    }

    public abstract int wavePoints();

    public abstract int rewardSun();

    public abstract void render(Graphics2D g2);
}
