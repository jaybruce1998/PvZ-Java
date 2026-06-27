package pvz;

public enum PlantType {
    SUNFLOWER("Sunflower", 50, 1, 7.5),
    PEASHOOTER("Peashooter", 100, 1, 7.5),
    WALLNUT("Wallnut", 75, 2, 25.0),
    CHERRY_BOMB("Cherry Bomb", 150, 5, 35.0),
    SNOW_PEA("Snow Pea", 175, 8, 7.5),
    PUFF_SHROOM("Puff-shroom", 0, 11, 7.5),
    REPEATER("Repeater", 200, 18, 7.5),
    LILY_PAD("Lily Pad", 25, 21, 7.5),
    FLOWER_POT("Flower Pot", 25, 41, 7.5);

    private final String label;
    private final int cost;
    private final int unlockLevel;
    private final double rechargeSeconds;

    PlantType(String label, int cost, int unlockLevel, double rechargeSeconds) {
        this.label = label;
        this.cost = cost;
        this.unlockLevel = unlockLevel;
        this.rechargeSeconds = rechargeSeconds;
    }

    public String label() {
        return label;
    }

    public int cost() {
        return cost;
    }

    public int unlockLevel() {
        return unlockLevel;
    }

    public double rechargeSeconds() {
        return rechargeSeconds;
    }

    public boolean isSupportPlant() {
        return this == LILY_PAD || this == FLOWER_POT;
    }
}
