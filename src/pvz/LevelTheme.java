package pvz;

public enum LevelTheme {
    DAY("Day"),
    NIGHT("Night"),
    POOL_DAY("Pool Day"),
    POOL_NIGHT("Pool Night"),
    ROOF("Roof");

    private final String label;

    LevelTheme(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public boolean isNight() {
        return this == NIGHT || this == POOL_NIGHT;
    }

    public boolean hasPool() {
        return this == POOL_DAY || this == POOL_NIGHT;
    }

    public boolean isRoof() {
        return this == ROOF;
    }

    public boolean hasSkySun() {
        return this == DAY || this == POOL_DAY || this == ROOF;
    }
}
