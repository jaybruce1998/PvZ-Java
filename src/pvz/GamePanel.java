package pvz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public final class GamePanel extends JPanel {
    private static final int WIDTH = 1233;
    private static final int HEIGHT = 720;
    private static final int ROWS = 5;
    private static final int COLS = 10;
    private static final int TILE_W = 94;
    private static final int TILE_H = 102;
    private static final int GRID_X = 210;
    private static final int GRID_Y = 145;
    private static final double SUPPORT_HEALTH = 140;
    private static final double CLICK_SUN_RADIUS = 42;

    private final Random random = new Random();
    private final Plant[][] plants = new Plant[ROWS][COLS];
    private final SupportTile[][] supports = new SupportTile[ROWS][COLS];
    private final double[][] supportHealth = new double[ROWS][COLS];
    private final List<Zombie> zombies = new ArrayList<>();
    private final List<Projectile> projectiles = new ArrayList<>();
    private final List<Sun> suns = new ArrayList<>();
    private final List<LawnMower> mowers = new ArrayList<>();
    private final List<LevelDefinition> campaign = LevelDefinition.campaign();
    private final EnumMap<PlantType, Double> rechargeTimers = new EnumMap<>(PlantType.class);
    private final Timer timer;
    private final double speedMultiplier;

    private PlantType selectedPlant;
    private LevelDefinition currentLevel;
    private int currentLevelIndex;
    private int sunPoints;
    private double spawnTimer;
    private double skySunTimer;
    private double elapsedTime;
    private int zombiePointsSpawned;
    private int zombiePointsDefeated;
    private int rushCount;
    private int nextWaveBreakAt;
    private boolean spawnPausedForWaveBreak;
    private double waveBreakTimer;
    private boolean gargWaveAt100Sent;
    private boolean gargWaveAt150Sent;
    private boolean gameOver;
    private boolean levelComplete;
    private boolean campaignComplete;
    private boolean mouseDown;
    private Point mousePoint = new Point();

    public GamePanel(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
        for (PlantType type : PlantType.values()) {
            rechargeTimers.put(type, 0.0);
        }
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(new Color(112, 179, 83));
        startCampaign();

        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                mouseDown = true;
                mousePoint = event.getPoint();
                handleMousePressed(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                mouseDown = false;
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                mousePoint = event.getPoint();
                if (mouseDown) {
                    collectNearbySuns(mousePoint.x, mousePoint.y);
                }
            }

            @Override
            public void mouseMoved(MouseEvent event) {
                mousePoint = event.getPoint();
            }
        };
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_R) {
                    if (gameOver) {
                        loadLevel(currentLevelIndex);
                    } else if (campaignComplete) {
                        startCampaign();
                    }
                    return;
                }
                if (event.getKeyCode() >= KeyEvent.VK_1 && event.getKeyCode() <= KeyEvent.VK_9) {
                    selectSeedSlot(event.getKeyCode() - KeyEvent.VK_1);
                    return;
                }
                if (event.getKeyCode() >= KeyEvent.VK_NUMPAD1 && event.getKeyCode() <= KeyEvent.VK_NUMPAD9) {
                    selectSeedSlot(event.getKeyCode() - KeyEvent.VK_NUMPAD1);
                }
            }
        });

        timer = new Timer(16, event -> {
            updateGame(0.016 * speedMultiplier);
            repaint();
        });
        timer.start();
    }

    private void startCampaign() {
        currentLevelIndex = SaveData.loadNextLevelIndex();
        campaignComplete = false;
        loadLevel(currentLevelIndex);
    }

    private void loadLevel(int levelIndex) {
        currentLevelIndex = levelIndex;
        currentLevel = campaign.get(levelIndex);
        clearBoard();
        sunPoints = currentLevel.startingSun();
        spawnTimer = currentLevel.initialSpawnDelay();
        skySunTimer = currentLevel.theme().hasSkySun() ? 3.5 : Double.POSITIVE_INFINITY;
        elapsedTime = 0;
        zombiePointsSpawned = 0;
        zombiePointsDefeated = 0;
        rushCount = 0;
        nextWaveBreakAt = 20;
        spawnPausedForWaveBreak = false;
        waveBreakTimer = 0;
        gargWaveAt100Sent = false;
        gargWaveAt150Sent = false;
        selectedPlant = null;
        gameOver = false;
        levelComplete = false;
        for (PlantType type : PlantType.values()) {
            rechargeTimers.put(type, 0.0);
        }
    }

    private void clearBoard() {
        zombies.clear();
        projectiles.clear();
        suns.clear();
        mowers.clear();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                plants[row][col] = null;
                supports[row][col] = SupportTile.NONE;
                supportHealth[row][col] = 0;
            }
            mowers.add(new LawnMower(row, GRID_X - 74, GRID_Y + row * TILE_H + 22));
        }
    }

    private LevelTheme currentTheme() {
        return currentLevel == null ? LevelTheme.DAY : currentLevel.theme();
    }

    public boolean isNightLevel() {
        return currentTheme().isNight();
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void spawnSun(double x, double y, int value, boolean skyDrop) {
        double targetY = skyDrop ? y + 70 + random.nextInt(140) : y;
        suns.add(new Sun(x, y, targetY, value, skyDrop));
    }

    public boolean hasZombieAhead(int row, double plantX) {
        for (Zombie zombie : zombies) {
            if (zombie.row() == row && zombie.x() + zombie.width() > plantX + 40) {
                return true;
            }
        }
        return false;
    }

    public boolean hasZombieWithin(int row, double plantX, double range) {
        for (Zombie zombie : zombies) {
            if (zombie.row() == row && zombie.x() > plantX && zombie.x() - plantX <= range) {
                return true;
            }
        }
        return false;
    }

    public void explodeAt(int centerRow, int centerCol, int radius, double damage) {
        for (Zombie zombie : zombies) {
            int zombieCol = Math.max(0, Math.min(COLS - 1, (int) ((zombie.x() - GRID_X + 28) / TILE_W)));
            if (Math.abs(zombie.row() - centerRow) <= radius && Math.abs(zombieCol - centerCol) <= radius) {
                zombie.hitByCherryBomb(this);
            }
        }
    }

    public void explodePlantsAt(int centerRow, double centerX, int radius) {
        int centerCol = Math.max(0, Math.min(COLS - 1, (int) ((centerX - GRID_X) / TILE_W)));
        for (int row = Math.max(0, centerRow - radius); row <= Math.min(ROWS - 1, centerRow + radius); row++) {
            for (int col = Math.max(0, centerCol - radius); col <= Math.min(COLS - 1, centerCol + radius); col++) {
                plants[row][col] = null;
                supports[row][col] = SupportTile.NONE;
                supportHealth[row][col] = 0;
            }
        }
    }

    private void updateGame(double dt) {
        if (gameOver || levelComplete || campaignComplete) {
            return;
        }

        elapsedTime += dt;
        updateRechargeTimers(dt);
        if (mouseDown) {
            collectNearbySuns(mousePoint.x, mousePoint.y);
        }

        if (currentTheme().hasSkySun()) {
            skySunTimer -= dt;
            if (skySunTimer <= 0) {
                skySunTimer = 6.3 + random.nextDouble() * 3.0;
                spawnSun(tileX(random.nextInt(COLS)) + 22, 40, 25, true);
            }
        }

        updateSpawner(dt);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Plant plant = plants[row][col];
                if (plant != null) {
                    plant.update(this, dt);
                    if (plant.isDead()) {
                        plants[row][col] = null;
                    }
                }
            }
        }

        updateProjectiles(dt);
        updateSuns(dt);
        updateZombies(dt);
        updateMowers(dt);

        if (zombiePointsDefeated >= currentLevel.totalZombies() && zombies.isEmpty()) {
            if (currentLevel.number() == 50) {
                campaignComplete = true;
            } else {
                levelComplete = true;
                selectedPlant = null;
                SaveData.saveNextLevelIndex(Math.min(49, currentLevelIndex + 1));
            }
        }
    }

    private void updateSpawner(double dt) {
        if (spawnPausedForWaveBreak) {
            waveBreakTimer += dt;
            if (zombies.isEmpty() || waveBreakTimer >= 16.0) {
                spawnPausedForWaveBreak = false;
                spawnTimer = zombies.isEmpty() ? 0 : 5.6;
                waveBreakTimer = 0;
            }
            return;
        }

        if (zombiePointsSpawned >= currentLevel.totalZombies()) {
            return;
        }

        if (triggerLateGameGargWaveIfNeeded()) {
            return;
        }

        if (zombies.isEmpty()) {
            spawnTimer = 0;
        }

        spawnTimer -= dt;
        if (spawnTimer <= 0) {
            spawnRush(nextRushPoints());
            if (zombiePointsSpawned >= nextWaveBreakAt && zombiePointsSpawned < currentLevel.totalZombies()) {
                spawnPausedForWaveBreak = true;
                waveBreakTimer = 0;
                nextWaveBreakAt += 20;
            }
            double intensity = Math.min(1.8, elapsedTime / 55.0);
            spawnTimer = Math.max(2.0, (currentLevel.baseSpawnInterval() - intensity * 0.3 + random.nextDouble() * 0.7) * 2.0);
            if (rushCount <= 2) {
                spawnTimer *= 2.0;
            }
            if (currentLevel.number() >= 41) {
                spawnTimer *= 2.0;
            }
        }
    }

    private int nextRushPoints() {
        rushCount++;
        if (rushCount == 1) {
            return 1;
        }
        if (rushCount == 2) {
            return 2;
        }
        int baseRamp = 2 + currentLevel.number() / 10;
        int waveRamp = Math.min(4, (rushCount - 3) / 2);
        int variance = random.nextInt(2);
        return Math.min(9, baseRamp + waveRamp + variance);
    }

    private void spawnRush(int targetPoints) {
        int remaining = Math.min(targetPoints, currentLevel.totalZombies() - zombiePointsSpawned);
        while (remaining > 0) {
            int row = randomRowForTheme();
            Zombie zombie = createZombieForCurrentLevel(row, GRID_X + COLS * TILE_W - 18, GRID_Y + row * TILE_H + 6, remaining);
            zombies.add(zombie);
            remaining -= zombie.wavePoints();
            zombiePointsSpawned += zombie.wavePoints();
        }
    }

    private boolean triggerLateGameGargWaveIfNeeded() {
        if (currentLevel.number() < 49) {
            return false;
        }
        if (!gargWaveAt100Sent && zombiePointsDefeated >= 100) {
            gargWaveAt100Sent = true;
            spawnForcedGargWave();
            return true;
        }
        if (!gargWaveAt150Sent && zombiePointsDefeated >= 150) {
            gargWaveAt150Sent = true;
            spawnForcedGargWave();
            return true;
        }
        return false;
    }

    private void spawnForcedGargWave() {
        int row = random.nextInt(ROWS);
        Zombie garg = new GargantuarZombie(row, GRID_X + COLS * TILE_W - 18, GRID_Y + row * TILE_H + 6);
        zombies.add(garg);
        zombiePointsSpawned += garg.wavePoints();
        spawnTimer = currentLevel.number() >= 41 ? 11.2 : 5.6;
    }

    private void updateRechargeTimers(double dt) {
        for (PlantType type : PlantType.values()) {
            rechargeTimers.put(type, Math.max(0, rechargeTimers.get(type) - dt));
        }
    }

    private void updateProjectiles(double dt) {
        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectile.update(dt);
            if (projectile.x() > WIDTH - 20 || !projectile.isActive()) {
                projectileIterator.remove();
                continue;
            }

            for (Zombie zombie : zombies) {
                if (!zombie.isTargetable()) {
                    continue;
                }
                Rectangle hit = new Rectangle((int) projectile.x() - 7, (int) projectile.y() - 7, 14, 14);
                if (projectile.row() == zombie.row() && hit.intersects(zombie.bounds())) {
                    zombie.damage(projectile.damage());
                    if (projectile.slows()) {
                        zombie.slow(2.4);
                    }
                    projectile.deactivate();
                    break;
                }
            }
        }
    }

    private void updateSuns(double dt) {
        Iterator<Sun> iterator = suns.iterator();
        while (iterator.hasNext()) {
            Sun sun = iterator.next();
            sun.update(dt);
            if (sun.isCollected() || sun.isExpired()) {
                iterator.remove();
            }
        }
    }

    private void updateZombies(double dt) {
        Iterator<Zombie> zombieIterator = zombies.iterator();
        while (zombieIterator.hasNext()) {
            Zombie zombie = zombieIterator.next();
            Plant targetPlant = getPlantInBiteRange(zombie);
            int supportCol = getSupportInBiteRange(zombie);

            if (zombie instanceof ZomboniZombie) {
                if (targetPlant != null) {
                    plants[targetPlant.row()][targetPlant.col()] = null;
                    targetPlant = null;
                }
                if (supportCol >= 0) {
                    supports[zombie.row()][supportCol] = SupportTile.NONE;
                    supportHealth[zombie.row()][supportCol] = 0;
                    supportCol = -1;
                }
            }

            if (targetPlant != null && attemptJump(zombie, targetPlant.x())) {
                continue;
            }
            if (targetPlant == null && supportCol >= 0 && attemptJump(zombie, tileX(supportCol))) {
                continue;
            }

            if (targetPlant != null) {
                targetPlant.damage(zombie.biteDamagePerSecond() * dt);
            } else if (supportCol >= 0) {
                supportHealth[zombie.row()][supportCol] -= zombie.biteDamagePerSecond() * dt;
                if (supportHealth[zombie.row()][supportCol] <= 0) {
                    supports[zombie.row()][supportCol] = SupportTile.NONE;
                    supportHealth[zombie.row()][supportCol] = 0;
                }
            } else {
                zombie.move(dt);
            }

            if (zombie.isDead()) {
                zombiePointsDefeated += zombie.wavePoints();
                zombie.onDeath(this);
                zombieIterator.remove();
                continue;
            }

            if (zombie.x() < GRID_X - 10) {
                LawnMower mower = mowerForRow(zombie.row());
                if (mower != null && !mower.isTriggered() && !mower.isSpent()) {
                    mower.trigger();
                } else if (mower == null || mower.isSpent()) {
                    gameOver = true;
                }
            }
        }
    }

    private boolean attemptJump(Zombie zombie, double obstacleX) {
        double landingX = obstacleX - zombie.width() + 10;
        if (zombie instanceof PoleVaultZombie pole && pole.canVault()) {
            pole.vaultTo(landingX);
            return true;
        }
        if (zombie instanceof DolphinRiderZombie dolphin && dolphin.canVault()) {
            dolphin.vaultTo(landingX);
            return true;
        }
        if (zombie instanceof PogoZombie pogo && pogo.canVault()) {
            pogo.vaultTo(landingX);
            return true;
        }
        return false;
    }

    private void updateMowers(double dt) {
        for (LawnMower mower : mowers) {
            if (mower.isSpent()) {
                continue;
            }
            mower.update(dt);
            if (!mower.isTriggered()) {
                continue;
            }
            for (Zombie zombie : zombies) {
                if (zombie.row() == mower.row() && mower.bounds().intersects(zombie.bounds())) {
                    zombie.damage(9999);
                }
            }
            if (mower.x() > WIDTH) {
                mower.spend();
            }
        }
    }

    private Plant getPlantInBiteRange(Zombie zombie) {
        int col = Math.max(0, Math.min(COLS - 1, (int) ((zombie.x() - GRID_X + 24) / TILE_W)));
        for (int check = Math.max(0, col - 1); check <= Math.min(COLS - 1, col); check++) {
            Plant plant = plants[zombie.row()][check];
            if (plant == null) {
                continue;
            }
            Rectangle plantBounds = new Rectangle((int) plant.x() + 10, (int) plant.y() + 10, plant.width() - 20, plant.height() - 10);
            if (plantBounds.intersects(zombie.bounds())) {
                return plant;
            }
        }
        return null;
    }

    private int getSupportInBiteRange(Zombie zombie) {
        int col = Math.max(0, Math.min(COLS - 1, (int) ((zombie.x() - GRID_X + 24) / TILE_W)));
        for (int check = Math.max(0, col - 1); check <= Math.min(COLS - 1, col); check++) {
            if (supports[zombie.row()][check] == SupportTile.NONE) {
                continue;
            }
            if (supportBounds(zombie.row(), check).intersects(zombie.bounds())) {
                return check;
            }
        }
        return -1;
    }

    private Rectangle supportBounds(int row, int col) {
        return new Rectangle(tileX(col) + 12, tileY(row) + 48, 62, 26);
    }

    private LawnMower mowerForRow(int row) {
        for (LawnMower mower : mowers) {
            if (mower.row() == row) {
                return mower;
            }
        }
        return null;
    }

    private Zombie createZombieForCurrentLevel(int row, double x, double y, int maxPoints) {
        List<Zombie> pool = new ArrayList<>();
        if (currentTheme().hasPool() && isWaterLane(row)) {
            pool.add(new DuckyTubeZombie(row, x, y));
            if (currentLevel.number() >= 28) {
                pool.add(new SnorkelZombie(row, x, y));
                pool.add(new DolphinRiderZombie(row, x, y));
            }
        } else {
            pool.add(new BasicZombie(row, x, y));
            if (currentLevel.number() >= 3) {
                pool.add(new FlagZombie(row, x, y));
            }
            if (currentLevel.number() >= 5) {
                pool.add(new ConeheadZombie(row, x, y));
            }
            if (currentLevel.number() >= 8) {
                pool.add(new BucketheadZombie(row, x, y));
            }
            if (currentLevel.number() >= 10) {
                pool.add(new PoleVaultZombie(row, x, y));
            }
            if (currentLevel.number() >= 14) {
                pool.add(new NewspaperZombie(row, x, y));
            }
            if (currentLevel.number() >= 18) {
                pool.add(new ScreenDoorZombie(row, x, y));
            }
            if (currentLevel.number() >= 22) {
                pool.add(new FootballZombie(row, x, y));
            }
            if (currentLevel.number() >= 26) {
                pool.add(new JackInTheBoxZombie(row, x, y));
            }
            if (currentLevel.number() >= 34) {
                pool.add(new ZomboniZombie(row, x, y));
            }
            if (currentLevel.number() >= 42) {
                pool.add(new LadderZombie(row, x, y));
            }
            if (currentLevel.number() >= 46) {
                pool.add(new PogoZombie(row, x, y));
            }
            if (currentLevel.number() >= 48) {
                pool.add(new GargantuarZombie(row, x, y));
            }
        }

        List<Zombie> candidates = new ArrayList<>();
        for (Zombie zombie : pool) {
            if (zombie.wavePoints() <= maxPoints) {
                candidates.add(zombie);
            }
        }
        if (candidates.isEmpty()) {
            return new BasicZombie(row, x, y);
        }
        return candidates.get(random.nextInt(candidates.size()));
    }

    private int randomRowForTheme() {
        if (!currentTheme().hasPool()) {
            return random.nextInt(ROWS);
        }
        int[] weighted = {0, 1, 2, 2, 3, 3, 4};
        return weighted[random.nextInt(weighted.length)];
    }

    private void handleMousePressed(MouseEvent event) {
        Point point = event.getPoint();
        collectNearbySuns(point.x, point.y);

        if (levelComplete) {
            advanceLevel();
            return;
        }
        if (campaignComplete) {
            return;
        }

        PlantType clickedCard = cardAt(point.x, point.y);
        if (clickedCard != null) {
            if (rechargeTimers.get(clickedCard) <= 0 && sunPoints >= clickedCard.cost()) {
                selectedPlant = clickedCard;
            }
            return;
        }

        int col = (point.x - GRID_X) / TILE_W;
        int row = (point.y - GRID_Y) / TILE_H;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return;
        }

        if (event.getButton() == MouseEvent.BUTTON3) {
            if (plants[row][col] != null) {
                plants[row][col] = null;
            } else {
                supports[row][col] = SupportTile.NONE;
                supportHealth[row][col] = 0;
            }
            return;
        }

        if (selectedPlant == null || selectedPlant.cost() > sunPoints || rechargeTimers.get(selectedPlant) > 0) {
            return;
        }

        if (selectedPlant.isSupportPlant()) {
            if (!canPlaceSupport(selectedPlant, row, col)) {
                return;
            }
            supports[row][col] = selectedPlant == PlantType.LILY_PAD ? SupportTile.LILY_PAD : SupportTile.FLOWER_POT;
            supportHealth[row][col] = SUPPORT_HEALTH;
            sunPoints -= selectedPlant.cost();
            rechargeTimers.put(selectedPlant, selectedPlant.rechargeSeconds());
            selectedPlant = null;
            return;
        }

        if (plants[row][col] != null || !canPlaceMainPlant(row, col)) {
            return;
        }

        int plantX = tileX(col) + 10;
        int plantY = tileY(row) + 10;
        plants[row][col] = createPlant(selectedPlant, row, col, plantX, plantY);
        sunPoints -= selectedPlant.cost();
        rechargeTimers.put(selectedPlant, selectedPlant.rechargeSeconds());
        selectedPlant = null;
    }

    private void collectNearbySuns(int mouseX, int mouseY) {
        for (Sun sun : suns) {
            double dx = mouseX - (sun.x() + 20);
            double dy = mouseY - (sun.y() + 20);
            if (!sun.isCollected() && dx * dx + dy * dy <= CLICK_SUN_RADIUS * CLICK_SUN_RADIUS) {
                sun.collect();
                sunPoints += sun.value();
            }
        }
    }

    private boolean canPlaceSupport(PlantType type, int row, int col) {
        if (type == PlantType.LILY_PAD) {
            return isWaterLane(row) && supports[row][col] == SupportTile.NONE;
        }
        if (type == PlantType.FLOWER_POT) {
            return currentTheme().isRoof() && supports[row][col] == SupportTile.NONE;
        }
        return false;
    }

    private boolean canPlaceMainPlant(int row, int col) {
        if (currentTheme().isRoof() && supports[row][col] != SupportTile.FLOWER_POT) {
            return false;
        }
        if (isWaterLane(row) && supports[row][col] != SupportTile.LILY_PAD) {
            return false;
        }
        return true;
    }

    private boolean isWaterLane(int row) {
        return currentTheme().hasPool() && (row == 2 || row == 3);
    }

    private Plant createPlant(PlantType type, int row, int col, int plantX, int plantY) {
        return switch (type) {
            case SUNFLOWER -> new Sunflower(row, col, plantX, plantY, 74, 76);
            case PEASHOOTER -> new Peashooter(row, col, plantX, plantY, 78, 78);
            case WALLNUT -> new Wallnut(row, col, plantX, plantY, 78, 80);
            case CHERRY_BOMB -> new CherryBomb(row, col, plantX, plantY, 78, 78);
            case SNOW_PEA -> new SnowPea(row, col, plantX, plantY, 78, 78);
            case PUFF_SHROOM -> new PuffShroom(row, col, plantX, plantY + 10, 70, 62);
            case REPEATER -> new Repeater(row, col, plantX, plantY, 78, 78);
            case LILY_PAD, FLOWER_POT -> throw new IllegalArgumentException("Support plants use separate placement");
        };
    }

    private List<PlantType> availablePlants() {
        List<PlantType> unlocked = new ArrayList<>();
        for (PlantType type : PlantType.values()) {
            if (type.unlockLevel() <= currentLevel.number()) {
                unlocked.add(type);
            }
        }
        return unlocked;
    }

    private PlantType cardAt(int mouseX, int mouseY) {
        List<PlantType> unlocked = availablePlants();
        for (int i = 0; i < unlocked.size(); i++) {
            if (cardRect(i).contains(mouseX, mouseY)) {
                return unlocked.get(i);
            }
        }
        return null;
    }

    private void selectSeedSlot(int slotIndex) {
        List<PlantType> unlocked = availablePlants();
        if (slotIndex < 0 || slotIndex >= unlocked.size()) {
            return;
        }
        PlantType type = unlocked.get(slotIndex);
        if (rechargeTimers.get(type) <= 0 && sunPoints >= type.cost()) {
            selectedPlant = type;
        }
    }

    private Rectangle cardRect(int index) {
        return new Rectangle(24 + index * 82, 28, 76, 88);
    }

    private int tileX(int col) {
        return GRID_X + col * TILE_W;
    }

    private int tileY(int row) {
        return GRID_Y + row * TILE_H;
    }

    private void advanceLevel() {
        if (currentLevel.number() >= 50) {
            campaignComplete = true;
            levelComplete = false;
            return;
        }
        loadLevel(currentLevelIndex + 1);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(g2);
        paintGrid(g2);
        paintHud(g2);
        for (LawnMower mower : mowers) {
            mower.render(g2);
        }
        paintSupports(g2);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Plant plant = plants[row][col];
                if (plant != null) {
                    plant.render(g2);
                }
            }
        }
        for (Projectile projectile : projectiles) {
            projectile.render(g2);
        }
        for (Zombie zombie : zombies) {
            zombie.render(g2);
        }
        for (Sun sun : suns) {
            sun.render(g2);
        }
        paintOverlay(g2);
        g2.dispose();
    }

    private void paintBackground(Graphics2D g2) {
        if (currentTheme().isNight()) {
            g2.setPaint(new GradientPaint(0, 0, new Color(20, 28, 70), 0, HEIGHT, new Color(39, 72, 79)));
        } else if (currentTheme().isRoof()) {
            g2.setPaint(new GradientPaint(0, 0, new Color(180, 220, 255), 0, HEIGHT, new Color(250, 220, 168)));
        } else {
            g2.setPaint(new GradientPaint(0, 0, new Color(162, 227, 255), 0, HEIGHT, new Color(240, 249, 205)));
        }
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (currentTheme().isNight()) {
            g2.setColor(new Color(242, 242, 210, 110));
            g2.fillOval(890, 40, 120, 120);
        } else {
            g2.setColor(new Color(244, 244, 220));
            g2.fillOval(-80, 500, 380, 180);
            g2.fillOval(930, 520, 360, 180);
        }

        if (currentTheme().isRoof()) {
            g2.setColor(new Color(168, 93, 76));
            g2.fillPolygon(new int[]{GRID_X - 10, GRID_X + COLS * TILE_W + 10, GRID_X + COLS * TILE_W + 70, GRID_X + 50},
                    new int[]{GRID_Y + ROWS * TILE_H + 10, GRID_Y + ROWS * TILE_H + 10, GRID_Y - 50, GRID_Y - 50}, 4);
        } else {
            g2.setColor(new Color(180, 140, 96));
            g2.fillRect(0, GRID_Y - 10, GRID_X - 24, ROWS * TILE_H + 32);
            g2.setColor(new Color(135, 197, 84));
            g2.fillRoundRect(GRID_X - 4, GRID_Y - 8, COLS * TILE_W + 8, ROWS * TILE_H + 16, 24, 24);
        }
    }

    private void paintGrid(Graphics2D g2) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = tileX(col);
                int y = tileY(row);
                Color tileColor;
                if (isWaterLane(row)) {
                    tileColor = (col + row) % 2 == 0 ? new Color(71, 141, 198) : new Color(61, 160, 219);
                } else if (currentTheme().isRoof()) {
                    tileColor = (row + col) % 2 == 0 ? new Color(191, 108, 91) : new Color(177, 94, 78);
                } else {
                    tileColor = (row + col) % 2 == 0 ? new Color(115, 184, 74) : new Color(127, 198, 79);
                }
                g2.setColor(tileColor);
                g2.fillRoundRect(x, y, TILE_W - 4, TILE_H - 4, 18, 18);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(x, y, TILE_W - 4, TILE_H - 4, 18, 18);
            }
        }
    }

    private void paintHud(Graphics2D g2) {
        g2.setColor(new Color(53, 94, 37, 220));
        g2.fillRoundRect(18, 20, 770, 106, 26, 26);

        g2.setColor(new Color(255, 236, 132));
        g2.fillRoundRect(808, 26, 166, 54, 20, 20);
        g2.setColor(new Color(131, 94, 23));
        g2.setFont(new Font("Georgia", Font.BOLD, 27));
        g2.drawString("Sun: " + sunPoints, 830, 61);

        List<PlantType> unlocked = availablePlants();
        for (int i = 0; i < unlocked.size(); i++) {
            PlantType type = unlocked.get(i);
            Rectangle rect = cardRect(i);
            boolean selected = type == selectedPlant;
            boolean affordable = sunPoints >= type.cost();
            boolean charging = rechargeTimers.get(type) > 0;

            g2.setColor(selected ? new Color(255, 247, 196) : new Color(241, 226, 188));
            g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 18, 18);
            if (!affordable || charging) {
                g2.setColor(new Color(40, 40, 40, 110));
                g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 18, 18);
            }
            g2.setColor(selected ? new Color(255, 221, 64) : new Color(114, 86, 45));
            g2.setStroke(new BasicStroke(selected ? 4f : 1.8f));
            g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 18, 18);

            g2.setColor(new Color(114, 86, 45));
            g2.setFont(new Font("Georgia", Font.BOLD, 11));
            g2.drawString(shortName(type), rect.x + 7, rect.y + 18);
            g2.setFont(new Font("Georgia", Font.PLAIN, 11));
            g2.drawString(type.cost() + " sun", rect.x + 7, rect.y + 76);
            paintCardIcon(g2, type, rect.x + 44, rect.y + 18);
            if (charging) {
                g2.setColor(new Color(255, 241, 209));
                g2.setFont(new Font("Georgia", Font.BOLD, 11));
                g2.drawString(Integer.toString((int) Math.ceil(rechargeTimers.get(type))), rect.x + 27, rect.y + 48);
            }
        }

        g2.setColor(currentTheme().isNight() ? new Color(235, 239, 217) : new Color(34, 68, 24));
        g2.setFont(new Font("Georgia", Font.BOLD, 17));
        g2.drawString("Level " + currentLevel.number() + " / 50 - " + currentTheme().label(), 992, 44);
        g2.drawString("Threat: " + zombiePointsDefeated + " / " + currentLevel.totalZombies(), 992, 72);
        g2.drawString("Speed x" + String.format("%.2f", speedMultiplier), 992, 100);
    }

    private String shortName(PlantType type) {
        return switch (type) {
            case SUNFLOWER -> "Sun";
            case PEASHOOTER -> "Pea";
            case WALLNUT -> "Wall";
            case CHERRY_BOMB -> "Cherry";
            case SNOW_PEA -> "Snow";
            case PUFF_SHROOM -> "Puff";
            case REPEATER -> "Repeat";
            case FLOWER_POT -> "Pot";
            case LILY_PAD -> "Lily";
        };
    }

    private void paintSupports(Graphics2D g2) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = tileX(col) + 16;
                int y = tileY(row) + 52;
                SupportTile support = supports[row][col];
                if (support == SupportTile.LILY_PAD) {
                    g2.setColor(new Color(78, 175, 110));
                    g2.fillOval(x, y, 56, 20);
                    g2.setColor(new Color(52, 141, 84));
                    g2.drawArc(x + 14, y + 2, 18, 14, 270, 180);
                } else if (support == SupportTile.FLOWER_POT) {
                    g2.setColor(new Color(183, 115, 75));
                    g2.fillRoundRect(x + 10, y - 8, 40, 24, 10, 10);
                    g2.setColor(new Color(106, 68, 45));
                    g2.fillRect(x + 14, y + 10, 32, 8);
                }
            }
        }
    }

    private void paintCardIcon(Graphics2D g2, PlantType type, int x, int y) {
        switch (type) {
            case SUNFLOWER -> {
                g2.setColor(new Color(247, 198, 41));
                g2.fillOval(x, y, 22, 22);
                g2.setColor(new Color(121, 79, 40));
                g2.fillOval(x + 5, y + 5, 12, 12);
            }
            case PEASHOOTER, REPEATER -> {
                g2.setColor(new Color(91, 183, 78));
                g2.fillOval(x, y + 3, 24, 18);
                g2.fillOval(x + 15, y + 6, 11, 9);
                if (type == PlantType.REPEATER) {
                    g2.fillOval(x + 15, y - 1, 11, 9);
                }
            }
            case WALLNUT -> {
                g2.setColor(new Color(171, 118, 63));
                g2.fillOval(x + 3, y, 20, 26);
            }
            case CHERRY_BOMB -> {
                g2.setColor(new Color(201, 40, 52));
                g2.fillOval(x, y + 8, 11, 11);
                g2.fillOval(x + 10, y + 4, 11, 11);
            }
            case SNOW_PEA -> {
                g2.setColor(new Color(170, 231, 255));
                g2.fillOval(x, y + 3, 24, 18);
                g2.fillOval(x + 15, y + 6, 11, 9);
            }
            case PUFF_SHROOM -> {
                g2.setColor(new Color(134, 96, 182));
                g2.fillOval(x, y + 5, 24, 14);
                g2.fillRect(x + 9, y + 16, 6, 8);
            }
            case LILY_PAD -> {
                g2.setColor(new Color(78, 175, 110));
                g2.fillOval(x, y + 10, 24, 12);
            }
            case FLOWER_POT -> {
                g2.setColor(new Color(183, 115, 75));
                g2.fillRoundRect(x + 2, y + 8, 20, 14, 6, 6);
            }
        }
    }

    private void paintOverlay(Graphics2D g2) {
        if (!gameOver && !levelComplete && !campaignComplete) {
            return;
        }
        g2.setColor(new Color(21, 31, 17, 170));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.setColor(new Color(255, 243, 206));
        g2.setFont(new Font("Georgia", Font.BOLD, 46));

        String title;
        String subtitle;
        String action;
        if (campaignComplete) {
            title = "Adventure Complete!";
            subtitle = "All 50 levels are clear.";
            action = "Press R to restart the campaign.";
        } else if (levelComplete) {
            title = "Level " + currentLevel.number() + " Cleared";
            subtitle = nextUnlockText();
            action = "Click anywhere to continue.";
        } else {
            title = "The Zombies Ate Your Brains";
            subtitle = "Rebuild your defense and try again.";
            action = "Press R to retry the current level.";
        }

        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (WIDTH - titleWidth) / 2, 270);
        g2.setFont(new Font("Georgia", Font.PLAIN, 25));
        int subtitleWidth = g2.getFontMetrics().stringWidth(subtitle);
        g2.drawString(subtitle, (WIDTH - subtitleWidth) / 2, 324);
        int actionWidth = g2.getFontMetrics().stringWidth(action);
        g2.drawString(action, (WIDTH - actionWidth) / 2, 364);
    }

    private String nextUnlockText() {
        for (PlantType type : PlantType.values()) {
            if (type.unlockLevel() == currentLevel.number() + 1) {
                return "Unlocked next: " + type.label();
            }
        }
        return "Next stop: " + campaign.get(Math.min(currentLevelIndex + 1, campaign.size() - 1)).theme().label() + ".";
    }
}
