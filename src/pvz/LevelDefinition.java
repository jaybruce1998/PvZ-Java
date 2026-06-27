package pvz;

import java.util.ArrayList;
import java.util.List;

public record LevelDefinition(
        int number,
        LevelTheme theme,
        int totalZombies,
        int startingSun,
        double baseSpawnInterval,
        double initialSpawnDelay,
        double coneChance,
        double bucketChance) {

    public static List<LevelDefinition> campaign() {
        List<LevelDefinition> levels = new ArrayList<>();
        for (int level = 1; level <= 50; level++) {
            LevelTheme theme = themeFor(level);
            int totalZombies = switch (level) {
                case 49 -> 175;
                case 50 -> 200;
                default -> 16 + level * 3;
            };
            int startingSun = 75;
            double baseSpawn = Math.max(1.15, 4.8 - level * 0.04);
            double initialDelay = Math.max(5.5, 9.5 - level * 0.05);
            double coneChance = Math.min(0.6, 0.12 + level * 0.012);
            double bucketChance = level >= 8 ? Math.min(0.42, 0.06 + (level - 8) * 0.012) : 0;
            levels.add(new LevelDefinition(level, theme, totalZombies, startingSun, baseSpawn, initialDelay, coneChance, bucketChance));
        }
        return levels;
    }

    private static LevelTheme themeFor(int level) {
        if (level <= 10) {
            return LevelTheme.DAY;
        }
        if (level <= 20) {
            return LevelTheme.NIGHT;
        }
        if (level <= 30) {
            return LevelTheme.POOL_DAY;
        }
        if (level <= 40) {
            return LevelTheme.POOL_NIGHT;
        }
        return LevelTheme.ROOF;
    }
}
